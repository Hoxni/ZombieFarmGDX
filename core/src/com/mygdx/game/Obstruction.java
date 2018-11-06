package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import java.util.*;

import static com.badlogic.gdx.math.MathUtils.isEqual;

public interface Obstruction{

    float EPS = 1f;

    static Vector2 getIntersectionPoint(Vector2 l1p1, Vector2 l1p2, Vector2 l2p1, Vector2 l2p2){
        float A1 = l1p2.y - l1p1.y;
        float B1 = l1p1.x - l1p2.x;
        float C1 = A1 * l1p1.x + B1 * l1p1.y;

        float A2 = l2p2.y - l2p1.y;
        float B2 = l2p1.x - l2p2.x;
        float C2 = A2 * l2p1.x + B2 * l2p1.y;

        //lines are parallel
        float det = A1 * B2 - A2 * B1;
        if(isEqual(det, 0)){ //MathUtils.isEqual()
            return null; //parallel lines
        } else {
            float x = (B2 * C1 - B1 * C2) / det;
            float y = (A1 * C2 - A2 * C1) / det;
            boolean online1 = ((Math.min(l1p1.x, l1p2.x) < x || isEqual(Math.min(l1p1.x, l1p2.x), x))
                    && (Math.max(l1p1.x, l1p2.x) > x || isEqual(Math.max(l1p1.x, l1p2.x), x))
                    && (Math.min(l1p1.y, l1p2.y) < y || isEqual(Math.min(l1p1.y, l1p2.y), y))
                    && (Math.max(l1p1.y, l1p2.y) > y || isEqual(Math.max(l1p1.y, l1p2.y), y))
            );
            boolean online2 = ((Math.min(l2p1.x, l2p2.x) <= x || isEqual(Math.min(l2p1.x, l2p2.x), x))
                    && (Math.max(l2p1.x, l2p2.x) > x || isEqual(Math.max(l2p1.x, l2p2.x), x))
                    && (Math.min(l2p1.y, l2p2.y) < y || isEqual(Math.min(l2p1.y, l2p2.y), y))
                    && (Math.max(l2p1.y, l2p2.y) > y || isEqual(Math.max(l2p1.y, l2p2.y), y))
            );

            if(online1 && online2)
                return new Vector2(x, y);
        }
        return null; //intersection is at out of at least one segment.
    }

    //intersection of line (not segments) and segment
    static Vector2 getLineIntersection(Vector2 l1p1, Vector2 l1p2, Vector2 l2p1, Vector2 l2p2){
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

            boolean online = ((Math.min(l2p1.x, l2p2.x) < x || isEqual(Math.min(l2p1.x, l2p2.x), x))
                    && (Math.max(l2p1.x, l2p2.x) > x || isEqual(Math.max(l2p1.x, l2p2.x), x))
                    && (Math.min(l2p1.y, l2p2.y) < y || isEqual(Math.min(l2p1.y, l2p2.y), y))
                    && (Math.max(l2p1.y, l2p2.y) > y || isEqual(Math.max(l2p1.y, l2p2.y), y))
            );

            if(online)
                return new Vector2(x, y);
        }
        return null; //intersection is at out of at least one segment.
    }

    static List<Vector2> getIntersectionPoints(Vector2 p1, Vector2 p2, List<Vector2> poly){
        //if line intersects polygon in corner point, function adds this point twice,
        //because two edges of polygon contain this point

        //Set is used to predict adding equal points
        Set<Vector2> intersectionPoints = new TreeSet<>((o1, o2) -> {
            //check if points are equal
            if(o1.epsilonEquals(o2, EPS))
                return 0;
            else return 1;
        });

        for(int i = 0; i < poly.size(); i++){

            int next = (i + 1 == poly.size()) ? 0 : i + 1;

            Vector2 ip = getIntersectionPoint(p1, p2, poly.get(i), poly.get(next));

            if(ip != null) intersectionPoints.add(ip);

        }

        return new ArrayList<>(intersectionPoints);
    }

    //intersections of line (not segment) and polygon (consist of segments)
    static List<Vector2> getLineIntersections(Vector2 p1, Vector2 p2, List<Vector2> poly){
        //if line intersects polygon in corner point, function adds this point twice,
        //because two edges of polygon contain this point

        //Set is used to predict adding equal points
        Set<Vector2> intersectionPoints = new TreeSet<>((o1, o2) -> {
            //check if points are equal
            if(o1.epsilonEquals(o2, EPS))
                return 0;
            else return 1;
        });

        for(int i = 0; i < poly.size(); i++){

            int next = (i + 1 == poly.size()) ? 0 : i + 1;

            Vector2 ip = getLineIntersection(p1, p2, poly.get(i), poly.get(next));

            if(ip != null) intersectionPoints.add(ip);

        }

        return new ArrayList<>(intersectionPoints);
    }

    static boolean isPointInPolygon(Vector2 p, List<Vector2> polygon){
        float minX = polygon.get(0).x;
        float maxX = polygon.get(0).x;
        float minY = polygon.get(0).y;
        float maxY = polygon.get(0).y;
        for(int i = 1; i < polygon.size(); i++){
            Vector2 q = polygon.get(i);
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

    //"true" if segment intersects polygon
    static boolean isPolygonIntersected(Vector2 p1, Vector2 p2, List<Vector2> poly){

        for(int i = 0; i < poly.size(); i++){

            int next = (i + 1 == poly.size()) ? 0 : i + 1;

            Vector2 ip = getIntersectionPoint(p1, p2, poly.get(i), poly.get(next));

            if(ip != null) return true;

        }

        return false;
    }

    /**

    obstruction is represented as a rhombus
    it has 4 corner points and 4 edges

    designation:
        |image - image of obstruction
        |0, 1, 2, 3 - indexes of points
        |0e, 1e, 2e, 3e - indexes of edges

             scheme

              + 3 +
      3e->  +       +  <-2e
          +           +
         0    image    2
          +           +
      0e->  +       +   <-1e
              + 1 +


     */
    //can be optimized rewriting "getIntersectionPoints" in "Obstruction" interface
    //for example "getIntersectionPoints" can return intersected edges
    //or replace "getIntersectionPoints" with "isIntersected" method
    default List<Vector2> getBypass(Vector2 location, Vector2 target){
        
        //used to predict stupid bugs
        try{
        //if target-point is situated inside of building
        if(Obstruction.isPointInPolygon(target, getCornerPoints())){
            List<Vector2> points = Obstruction.getLineIntersections(location, target, getCornerPoints());

                //set target as closest to location intersection point
                target.set(Collections.min(points, Comparator.comparingDouble(location::dst2))); //o -> Vector2.subtract(location, o).len2()

            //return list of one element
            return Collections.singletonList(target);
        }

        //return null if zombie has not to bypass this obstruction
        if(!Obstruction.isPolygonIntersected(location, target, getCornerPoints()) && !Obstruction.isPointInPolygon(location, getCornerPoints())){
            return null;
        }

        //set of intersection points and edges that contain these points
        TreeSet<Map.Entry<Vector2, Integer>> intersectedEdges = new TreeSet<>((o1, o2) -> {
            if(o1.getKey().epsilonEquals(o2.getKey(), EPS))
                return 0;
            else return 1;
        });


        //if it doesn't need to fix stupid bugs
        //find edges which have intersection with path-line
        //this code duplicates "getIntersectionPoints" and can be optimized as described above
        for(int i = 0; i < getCornerPoints().size(); i++){
            int next = (i + 1 == getCornerPoints().size()) ? 0 : i + 1;

            Vector2 ip = Obstruction.getLineIntersection(location, target, getCornerPoints().get(i), getCornerPoints().get(next));

            if(ip != null){
                intersectedEdges.add(new AbstractMap.SimpleEntry<>(ip, i));
            }
        }

        //adds correct intersection points
        List<Vector2> intersectionPoints = new ArrayList<>();
        for(Map.Entry<Vector2, Integer> o : intersectedEdges){
            intersectionPoints.add(o.getKey());
        }

        //points of bypass
        List<Vector2> path = new ArrayList<>();

            Vector2 firstIntersection = Collections.min(intersectionPoints, Comparator.comparingDouble(location::dst2)); //c -> Vector2.subtract(location, c).len2()
            Vector2 secondIntersection = Collections.min(intersectionPoints, Comparator.comparingDouble(target::dst2)); //c -> Vector2.subtract(target, c).len2()

            path.add(firstIntersection);

            //if line intersects two adjacent edges
            if(Math.abs((intersectedEdges.first().getValue() - intersectedEdges.last().getValue()) % 2) == 1){
                int min = Math.min(intersectedEdges.first().getValue(), intersectedEdges.last().getValue());
                int index = Math.abs(intersectedEdges.first().getValue() - intersectedEdges.last().getValue()) % 3 + min;
                path.add(getCornerPoints().get(index));
                path.add(secondIntersection);
                return path;
            }

            //if line intersects two opposite edges
            //find second bypass point
            Vector2 closestToSecond = Collections.min(getCornerPoints(), Comparator.comparingDouble(secondIntersection::dst2)); //c -> Vector2.subtract(secondIntersection, c).len2()
            //first intersected edge
            int a = Collections.min(intersectedEdges, Comparator.comparingDouble(c -> firstIntersection.dst2(c.getKey()))).getValue(); //c -> Vector2.subtract(firstIntersection, c.getValue()).len2())).getKey()
            int c = getCornerPoints().indexOf(closestToSecond); //index of closest corner-point for secondIntersection-point
            int index = -1;

            //some hard-to-explain checks to find first bypass point
            switch(c){
                case 0:
                    if(a == 1) index = 1;
                    else index = 3;
                    break;
                case 1:
                    if(a == 2) index = 2;
                    else index = 0;
                    break;
                case 2:
                    if(a == 0) index = 1;
                    else index = 3;
                    break;
                case 3:
                    if(a == 0) index = 0;
                    else index = 2;
                    break;
            }

            //get first bypass point
            Vector2 p = getCornerPoints().get(index);
            //yes, second bypass point is calculating earlier than first

            path.add(p);
            path.add(closestToSecond);
            path.add(secondIntersection);

            return path;

        }catch(NoSuchElementException e){
            //predict stupid bugs
            target.set(location);
            return Collections.singletonList(location);
        }
    }

    List<Vector2> getCornerPoints();

    Vector2 getCenter();

    void setLayer(int layer);

    int getLayer();

}
