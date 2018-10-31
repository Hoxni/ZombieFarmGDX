package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public abstract class SpecialSprite{

    final Vector2 location;
    final Vector2 velocity;
    final Vector2 acceleration;

    final float maxForce = Settings.SPRITE_MAX_FORCE;
    final float maxSpeed = Settings.SPRITE_MAX_SPEED;

    float centerX;
    float centerY;


    public SpecialSprite(Vector2 location) {

        this.location = location;
        this.velocity = new Vector2(0, 0);
        this.acceleration = new Vector2(0, 0);
    }

    public void applyForce(Vector2 force) {
        acceleration.add(force);
    }

    public void move(){

        // set velocity depending on acceleration
        velocity.add(acceleration);

        // limit velocity to max speed
        velocity.limit(maxSpeed);

        // change target depending on velocity
        location.add(velocity);

        // angle: towards velocity (ie target)
        //angle = velocity.heading2D();

        // clear acceleration
        acceleration.scl(0);

    }

    /**
     * Move sprite towards target
     */
    public void seek(Vector2 target) {

        //Vector2.subtract(target, location);
        Vector2 desired = target.cpy();
        desired.sub(location);

        // The distance is the len2 of the vector pointing from target to target.

        float d = desired.len2();
        desired.nor();

        // If we are closer than 30 pixels...
        if (d < Settings.SPRITE_SLOW_DOWN_DISTANCE) {
            //float m = Utils.map(d, 0, Settings.SPRITE_SLOW_DOWN_DISTANCE, 0, maxSpeed);
            //uncomment string above and replace 0 with m than zombie will slow down near target point
            //go to "Settings" to set mode of zombie moving
            desired.scl(0);
        }
        // Otherwise, proceed at maximum speed.
        else {
            desired.scl(maxSpeed);
        }

        // The usual steering = desired - velocity
        //Vector2.subtract(desired, velocity);
        Vector2 steer = desired.cpy();
        steer.sub(velocity);
        steer.limit(maxForce);

        applyForce(steer);

    }

    /**
     * Update node position
     */
    abstract void display();

    public Vector2 getLocation() {
        return location;
    }

    public void update(Vector2 v){
        seek(v);
        move();
        display();
    }
}
