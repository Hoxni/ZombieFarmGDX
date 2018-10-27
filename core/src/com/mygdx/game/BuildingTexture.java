package com.mygdx.game;

import com.badlogic.gdx.maps.objects.TextureMapObject;

import java.util.*;

public class BuildingTexture extends TextureMapObject implements Obstruction{
    protected final float OFFSET = 5;
    protected final ArrayList<Vector2D> cornerPoints;
    protected float layer = 0;

    public BuildingTexture(String building, String buildingXML, float x, float y){
        SpecialAnimation animation = new SpecialAnimation(building, buildingXML);
        animation.getAnimationStage(0).flip(false, true);
        this.setTextureRegion(animation.getAnimationStage(0));
        this.setX(x);
        this.setY(y);

        cornerPoints = new ArrayList<>();
        cornerPoints.add(new Vector2D(getX() - OFFSET, getY() + 145));
        cornerPoints.add(new Vector2D(getX() + 70, getY() + animation.getHeight() + OFFSET));
        cornerPoints.add(new Vector2D(getX() + animation.getWidth() + OFFSET, getY() + 145));
        cornerPoints.add(new Vector2D(getX() + 70, getY() + 100 - OFFSET));
    }


    /*

    building is represented as a rhombus
    it has 4 corner points and 4 edges

    designation:
        |building - image of building
        |0, 1, 2, 3 - indexes of points
        |0e, 1e, 2e, 3e - indexes of edges

             scheme

              + 3 +
      3e->  +       +  <-2e
          +           +
         0   building  2
          +           +
      0e->  +       +   <-1e
              + 1 +



     */


    //can be optimized rewriting "getIntersectionPoints" in "Obstruction" interface
    //for example "getIntersectionPoints" can return intersected edges
    //or replace "getIntersectionPoints" with "isIntersected" method
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
        return new Vector2D(cornerPoints.get(1).x, cornerPoints.get(0).y);
    }

    @Override
    public void setLayer(float layer){
        this.layer = layer;
    }

    @Override
    public float getLayer(){
        return layer;
    }

}