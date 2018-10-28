package com.mygdx.game;

public class Vector2D{

    public float x;
    public float y;
    static float EPS = 0.000000001f;


    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float magnitude(){
        return (float) Math.sqrt(x * x + y * y);
    }

    public void add(Vector2D v){
        x += v.x;
        y += v.y;
    }

    public void add(float x, float y){
        this.x += x;
        this.y += y;
    }

    public void multiply(float n){
        x *= n;
        y *= n;
    }

    public void div(float n){
        x /= n;
        y /= n;
    }

    public void normalize(){
        float m = magnitude();
        if(m != 0 && m != 1){
            div(m);
        }
    }

    public void limit(float max){
        if(magnitude() > max){
            normalize();
            multiply(max);
        }
    }

    @Override
    public boolean equals(Object o){
        Vector2D v = (Vector2D) o;
        return Math.abs(x - v.x) <= EPS && Math.abs(y - v.y) <= EPS;
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

