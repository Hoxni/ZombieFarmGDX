package com.mygdx.game;

public class Settings{

    //this 2 things change zombie moving
    //all changes are unpredictable
    public static final float SPRITE_MAX_SPEED = 1.7f;//2
    public static final float SPRITE_MAX_FORCE = 1f;//0.1

    public static final float SPRITE_SLOW_DOWN_DISTANCE = 10f;//30
    public static final float STOP_DISTANCE = 10f;

    public static final float HEIGHT_OFFSET = 7f;

    public static final float WHITE_WAVE_DURIATION = 0.13f;

    public static final float ZOMBIE_ANIMATION_SPEED = 0.03f;
    public static final float CUT_DOWN_DURATION = 0.54f;
    public static final float ZOMBIE_UPDATE_DELAY = 0.01f;

    public static final float WAKEUP_DURATION = 60f;
    public static final float WAKEUP_DELAY = WAKEUP_DURATION * 39f;

    public static final float ZOMBIE_MOVING_DELAY = 3f;

    public static final int TREES_NUMBER = 0;
    public static final int BUILDINGS_NUMBER = 1;
    public static final int ZOMBIES_NUMBER = 0;

    public static final float WINDOW_WIDTH = 900;
    public static final float WINDOW_HEIGHT = 700;

    public static final Vector2D INITIAL_POINT = new Vector2D(170, 1050);
}

