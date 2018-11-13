package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Settings{

    //this 2 things change zombie moving
    //all changes are unpredictable
    public static final float SPRITE_MAX_SPEED = 1.7f;//2
    public static final float SPRITE_MAX_FORCE = 1f;//0.1

    //using value*value (value^2) because distance between vectors
    //calculated with Vector2.dst2()" (read on the official site about it)
    //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/math/Vector2.html#dst2-com.badlogic.gdx.math.Vector2-
    //   formula: dst2=(x2 - x1)^2 + (y2 - y1)^2
    //instead of  dst=((x2 - x1)^2 + (y2 - y1)^2)^0.5
    public static final float SPRITE_SLOW_DOWN_DISTANCE = 10*10f;//30
    public static final float STOP_DISTANCE = 10*10f;

    public static final float WHITE_WAVE_DURATION = 0.13f;

    public static final float ZOMBIE_ANIMATION_SPEED = 0.03f;
    public static final float CUT_DOWN_DURATION = 0.54f;
    public static final float ZOMBIE_UPDATE_DELAY = 0.01f;

    public static final float ZOMBIE_MOB_MOVING_DELAY = 3f;

    public static final int TREES_NUMBER = 7;
    public static final int BUILDINGS_NUMBER = 7;
    public static final int ZOMBIES_NUMBER = 5;

    public static final float WINDOW_WIDTH = 900;
    public static final float WINDOW_HEIGHT = 700;

    public static final Vector2 INITIAL_POINT = new Vector2(170, 1050);

}

