package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import java.util.*;

/**
 * used to prohibit zombies to walk on water
 */
public class GamingZone{
    protected final List<Vector2> cornerPoints;


    public GamingZone(List<Vector2> cornerPoints){
        this.cornerPoints = cornerPoints;
    }

    public void checkGamingZone(Vector2 location, Vector2 target){

        if(!Obstruction.isPointInPolygon(target, cornerPoints)){
            List<Vector2> list = Obstruction.getIntersectionPoints(location, target, cornerPoints);
            if(!list.isEmpty()){
                //set intersection point as target if target-point is not in the gaming zone
                //intersection point finds as in Building class
                Vector2 v = list.get(0);
                target.set(v.x, v.y);
            } else {
                //this part used only to predict stupid bugs
                target.set(location.x, location.y);
            }
        }
    }
}