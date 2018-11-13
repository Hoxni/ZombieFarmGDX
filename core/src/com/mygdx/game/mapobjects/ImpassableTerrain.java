package com.mygdx.game.mapobjects;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * used to prohibit zombies to walk on the flat evaluation in the middle of the map
 * (it is a big rhombus in the centre)
 * works the same as a building
 */

public class ImpassableTerrain implements Obstruction{
    protected final List<Vector2> cornerPoints;


    public ImpassableTerrain(List<Vector2> cornerPoints){
        this.cornerPoints = cornerPoints;
    }

    @Override
    public List<Vector2> getCornerPoints(){
        return cornerPoints;
    }

    @Override
    public Vector2 getCenter(){
        return new Vector2(0, 0);
    }

    @Override
    public void setLayer(int layer){

    }

    @Override
    public int getLayer(){
        return 0;
    }
}
