package com.mygdx.game;

import java.util.*;

public class ImpassableTerrain implements Obstruction{
    protected List<Vector2D> cornerPoints;

    public ImpassableTerrain(List<Vector2D> cornerPoints){
        this.cornerPoints = cornerPoints;
    }

    @Override
    public List<Vector2D> getBypass(Vector2D location, Vector2D target, List<Vector2D> intersectionPoints){

        //if target-point is situated inside of building
        if(intersectionPoints.size() == 1){
            if(Obstruction.isPointInPolygon(target, cornerPoints)){
                Vector2D v = Obstruction.getIntersectionPoints(location, target, cornerPoints).get(0);
                target.set(v.x, v.y);
            }
            return intersectionPoints;
        }

        //find edges which have intersection with path-line
        //this code duplicates "getIntersectionPoints" and can be optimized as described above
        List<Map.Entry<Integer, Vector2D>> intersectedEdges = new ArrayList<>();
        for(int i = 0; i < cornerPoints.size(); i++){
            int next = (i + 1 == cornerPoints.size()) ? 0 : i + 1;

            Vector2D ip = Obstruction.getIntersectionPoint(location, target, cornerPoints.get(i), cornerPoints.get(next));

            if(ip != null){
                intersectedEdges.add(new AbstractMap.SimpleEntry<>(i, ip));
            }
        }

        Vector2D firstIntersection = Collections.min(intersectionPoints, Comparator.comparingDouble(c -> Vector2D.subtract(location, c).magnitude()));
        Vector2D secondIntersection = Collections.min(intersectionPoints, Comparator.comparingDouble(c -> Vector2D.subtract(target, c).magnitude()));

        //points of bypass
        List<Vector2D> path = new ArrayList<>();
        path.add(firstIntersection);


        //if line intersects two adjacent edges
        if(Math.abs((intersectedEdges.get(0).getKey() - intersectedEdges.get(1).getKey()) % 2) == 1){
            int min = Math.min(intersectedEdges.get(0).getKey(), intersectedEdges.get(1).getKey());
            int index = Math.abs(intersectedEdges.get(0).getKey() - intersectedEdges.get(1).getKey()) % 3 + min;
            path.add(cornerPoints.get(index));
            path.add(secondIntersection);
            return path;
        }

        //if line intersects two opposite edges
        //find second bypass point
        Vector2D closestToSecond = Collections.min(cornerPoints, Comparator.comparingDouble(c -> Vector2D.subtract(secondIntersection, c).magnitude()));
        int a = Collections.min(intersectedEdges, Comparator.comparingDouble(c -> Vector2D.subtract(firstIntersection, c.getValue()).magnitude())).getKey(); //first intersected edge
        int c = cornerPoints.indexOf(closestToSecond); //index of closest corner-point for secondIntersection-point
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
        Vector2D p = cornerPoints.get(index);
        //yes, second bypass point is calculating earlier than first

        path.add(p);
        path.add(closestToSecond);
        path.add(secondIntersection);

        return path;
    }

    @Override
    public List<Vector2D> getCornerPoints(){
        return cornerPoints;
    }

    @Override
    public Vector2D getCenter(){
        return new Vector2D(0, 0);
    }

    @Override
    public void setLayer(float layer){

    }

    @Override
    public float getLayer(){
        return 0;
    }
}
