package com.mygdx.game;

import java.util.*;

public interface Obstruction{

    float EQUITY_TOLERANCE = 0.000000001f;

    static boolean isEqual(float d1, float d2){
        return Math.abs(d1 - d2) <= EQUITY_TOLERANCE;
    }

    static Vector2D getIntersectionPoint(Vector2D l1p1, Vector2D l1p2, Vector2D l2p1, Vector2D l2p2){
        float A1 = l1p2.y - l1p1.y;
        float B1 = l1p1.x - l1p2.x;
        float C1 = A1 * l1p1.x + B1 * l1p1.y;

        float A2 = l2p2.y - l2p1.y;
        float B2 = l2p1.x - l2p2.x;
        float C2 = A2 * l2p1.x + B2 * l2p1.y;

        //lines are parallel
        float det = A1 * B2 - A2 * B1;
        if(isEqual(det, 0)){
            return null; //parallel lines
        } else {
            float x = (B2 * C1 - B1 * C2) / det;
            float y = (A1 * C2 - A2 * C1) / det;
            boolean online1 = ((Math.min(l1p1.x, l1p2.x) < x || isEqual(Math.min(l1p1.x, l1p2.x), x))
                    && (Math.max(l1p1.x, l1p2.x) > x || isEqual(Math.max(l1p1.x, l1p2.x), x))
                    && (Math.min(l1p1.y, l1p2.y) < y || isEqual(Math.min(l1p1.y, l1p2.y), y))
                    && (Math.max(l1p1.y, l1p2.y) > y || isEqual(Math.max(l1p1.y, l1p2.y), y))
            );
            boolean online2 = ((Math.min(l2p1.x, l2p2.x) < x || isEqual(Math.min(l2p1.x, l2p2.x), x))
                    && (Math.max(l2p1.x, l2p2.x) > x || isEqual(Math.max(l2p1.x, l2p2.x), x))
                    && (Math.min(l2p1.y, l2p2.y) < y || isEqual(Math.min(l2p1.y, l2p2.y), y))
                    && (Math.max(l2p1.y, l2p2.y) > y || isEqual(Math.max(l2p1.y, l2p2.y), y))
            );

            if(online1 && online2)
                return new Vector2D(x, y);
        }
        return null; //intersection is at out of at least one segment.
    }

    static List<Vector2D> getIntersectionPoints(Vector2D l1p1, Vector2D l1p2, List<Vector2D> poly){
        //if line intersects polygon in corner point, function adds this point twice,
        //because two edges of polygon contain this point

        //Set is used to predict adding equal points
        Set<Vector2D> intersectionPoints = new TreeSet<>((o1, o2) -> {
            //check if points are equal
            if(o1.equals(o2))
                return 0;
            else return 1;
        });

        for(int i = 0; i < poly.size(); i++){

            int next = (i + 1 == poly.size()) ? 0 : i + 1;

            Vector2D ip = getIntersectionPoint(l1p1, l1p2, poly.get(i), poly.get(next));

            if(ip != null) intersectionPoints.add(ip);

        }

        return new ArrayList<>(intersectionPoints);
    }

    static boolean isPointInPolygon(Vector2D p, List<Vector2D> polygon){
        float minX = polygon.get(0).x;
        float maxX = polygon.get(0).x;
        float minY = polygon.get(0).y;
        float maxY = polygon.get(0).y;
        for(int i = 1; i < polygon.size(); i++){
            Vector2D q = polygon.get(i);
            minX = Math.min(q.x, minX);
            maxX = Math.max(q.x, maxX);
            minY = Math.min(q.y, minY);
            maxY = Math.max(q.y, maxY);
        }

        if(p.x < minX || p.x > maxX || p.y < minY || p.y > maxY){
            return false;
        }

        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        boolean inside = false;
        for(int i = 0, j = polygon.size() - 1; i < polygon.size(); j = (i++)){
            if((polygon.get(i).y > p.y) != (polygon.get(j).y > p.y) &&
                    p.x < (polygon.get(j).x - polygon.get(i).x) * (p.y - polygon.get(i).y) / (polygon.get(j).y - polygon.get(i).y) + polygon.get(i).x){
                inside = !inside;
            }
        }
        return inside;
    }

    List<Vector2D> getBypass(Vector2D first, Vector2D second, List<Vector2D> intersection);

    List<Vector2D> getCornerPoints();

    Vector2D getCenter();

    void setLayer(int layer);

    int getLayer();

    //-------------------- Normal code above ---------------------------------------------------------

    static List<List<Vector2D>> getGraph(Vector2D l1p1, Vector2D l1p2, List<Vector2D> poly){
        List<List<Vector2D>> graph = new ArrayList<>(poly.size());
        for(int i = 0; i < poly.size(); i++){
            graph.set(i, new ArrayList<>());
        }
        for(int i = 0, j = poly.size(); i < poly.size(); i++){

            int next = (i + 1 == poly.size()) ? 0 : i + 1;

            graph.get(i).add(new Vector2D(next, 1));
            graph.get(next).add(new Vector2D(i, 1));

            Vector2D ip = getIntersectionPoint(l1p1, l1p2, poly.get(i), poly.get(next));

            if(ip != null){
                graph.add(j, new ArrayList<>());
                int q;
                int v;
                if(Vector2D.subtract(ip, poly.get(i)).len2() <= Vector2D.subtract(ip, poly.get(next)).len2()){
                    q = i;
                    v = next;
                } else {
                    q = next;
                    v = i;
                }
                graph.get(j).add(new Vector2D(q, 0));
                graph.get(q).add(new Vector2D(j, 0));
                graph.get(j).add(new Vector2D(v, 1));
                graph.get(v).add(new Vector2D(j, 1));
            }
        }
        return graph;
    }
}