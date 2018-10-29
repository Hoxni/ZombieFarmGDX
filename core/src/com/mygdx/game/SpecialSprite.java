package com.mygdx.game;

public abstract class SpecialSprite{

    final Vector2D location;
    final Vector2D velocity;
    final Vector2D acceleration;

    final float maxForce = Settings.SPRITE_MAX_FORCE;
    final float maxSpeed = Settings.SPRITE_MAX_SPEED;

    float centerX;
    float centerY;


    public SpecialSprite(Vector2D location) {

        this.location = location;
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
    }

    abstract void setCenter();

    public void applyForce(Vector2D force) {
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
    public void seek(Vector2D target) {

        Vector2D desired = Vector2D.subtract(target, location);

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
        Vector2D steer = Vector2D.subtract(desired, velocity);
        steer.limit(maxForce);

        applyForce(steer);

    }

    /**
     * Update node position
     */
    abstract void display();

    public Vector2D getLocation() {
        return location;
    }

    public void setLocation( float x, float y) {
        location.x = x;
        location.y = y;
    }

    public void update(Vector2D v){
        seek(v);
        move();
        display();
    }
}
