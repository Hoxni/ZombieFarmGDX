package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Vector2D extends Vector2{

    public Vector2D(float x, float y){
        super(x, y);
    }

    @Override
    public boolean equals(Object o){
        Vector2D v = (Vector2D) o;
        return this.epsilonEquals(v);
    }

    static public Vector2D subtract(Vector2D v1, Vector2D v2){
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }

    static public Vector2D add(Vector2D v1, Vector2D v2){
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }

    public Vector2D copy(){
        return new Vector2D(x, y);
    }
}

