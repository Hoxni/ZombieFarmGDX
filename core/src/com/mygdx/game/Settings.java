package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Settings{

    //this 2 things change zombie moving
    //all changes are unpredictable
    public static final float SPRITE_MAX_SPEED = 1.7f;//2
    public static final float SPRITE_MAX_FORCE = 1f;//0.1

    //using value*value (value^2) because distance between vectors
    //calculated with Vector2.len2()" (read on the official site about it)
    //formula: len2=(x^2 + y^2) instead of len=(x^2 + y^2)^0.5
    public static final float SPRITE_SLOW_DOWN_DISTANCE = 10*10f;//30
    public static final float STOP_DISTANCE = 10*10f;

    public static final float HEIGHT_OFFSET = 7f;

    public static final float WHITE_WAVE_DURATION = 0.13f;

    public static final float ZOMBIE_ANIMATION_SPEED = 0.03f;
    public static final float CUT_DOWN_DURATION = 0.54f;
    public static final float ZOMBIE_UPDATE_DELAY = 0.01f;

    public static final float ZOMBIE_MOVING_DELAY = 3f;

    public static final int TREES_NUMBER = 3;
    public static final int BUILDINGS_NUMBER = 3;
    public static final int ZOMBIES_NUMBER = 3;

    public static final float WINDOW_WIDTH = 900;
    public static final float WINDOW_HEIGHT = 700;

    public static final Vector2 INITIAL_POINT = new Vector2(170, 1050);
}

