package com.mygdx.game;

import java.util.*;

public class GamingZone{
    protected List<Vector2D> cornerPoints;

    public GamingZone(List<Vector2D> cornerPoints){
        this.cornerPoints = cornerPoints;
    }

    public void checkGamingZone(Vector2D location, Vector2D target){
        List<Vector2D> list = Obstruction.getIntersectionPoints(location, target, cornerPoints);
        if(!list.isEmpty()){
            Vector2D v = list.get(0);
            target.set(v.x, v.y);
        }
    }
}
