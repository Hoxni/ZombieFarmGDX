package com.mygdx.game;

import java.util.*;

/**
 * used to prohibit zombies to walk on water
 */
public class GamingZone{
    protected List<Vector2D> cornerPoints;

    public GamingZone(List<Vector2D> cornerPoints){
        this.cornerPoints = cornerPoints;
    }

    public void checkGamingZone(Vector2D location, Vector2D target){

        if(!Obstruction.isPointInPolygon(target, cornerPoints)){
            List<Vector2D> list = Obstruction.getIntersectionPoints(location, target, cornerPoints);
            if(!list.isEmpty()){
                //set intersection point as target if target-point is not in the gaming zone
                //intersection point finds as in Building class
                Vector2D v = list.get(0);
                target.set(v.x, v.y);
            } else {
                //this part used only to predict stupid bugs
                target.set(location.x, location.y);
            }
        }
    }
}