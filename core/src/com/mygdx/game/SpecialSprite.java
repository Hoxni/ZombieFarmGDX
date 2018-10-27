package com.mygdx.game;

public abstract class SpecialSprite{

    final Vector2D location;
    final Vector2D velocity;
    final Vector2D acceleration;

    final float maxForce = Settings.SPRITE_MAX_FORCE;
    final float maxSpeed = Settings.SPRITE_MAX_SPEED;

    // view dimensions

    float centerX;
    float centerY;


    public SpecialSprite(Vector2D location) {

        this.location = location;
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
        //this.actor = actor;
        //setCenter();
    }

    abstract void setCenter();

    public void applyForce(Vector2D force) {
        acceleration.add(force);
    }

    public void move() {

        // set velocity depending on acceleration
        velocity.add(acceleration);

        // limit velocity to max speed
        velocity.limit(maxSpeed);

        // change target depending on velocity
        location.add(velocity);

        // angle: towards velocity (ie target)
        //angle = velocity.heading2D();

        // clear acceleration
        acceleration.multiply(0);

    }

    /**
     * Move sprite towards target
     */
    public void seek(Vector2D target) {

        Vector2D desired = Vector2D.subtract(target, location);

        // The distance is the magnitude of the vector pointing from target to target.

        float d = desired.magnitude();
        desired.normalize();

        // If we are closer than 30 pixels...
        if (d < Settings.SPRITE_SLOW_DOWN_DISTANCE) {
            //float m = Utils.map(d, 0, Settings.SPRITE_SLOW_DOWN_DISTANCE, 0, maxSpeed);
            //uncomment string above and replace 0 with m than zombie will slow down near target point
            //go to "Settings" to set mode of zombie moving
            desired.multiply(0);
        }
        // Otherwise, proceed at maximum speed.
        else {
            desired.multiply(maxSpeed);
        }

        // The usual steering = desired - velocity
        Vector2D steer = Vector2D.subtract(desired, velocity);
        steer.limit(maxForce);

        applyForce(steer);

    }

    Vector2D getNormalPoint(Vector2D p, Vector2D a, Vector2D b) {
        Vector2D ap = Vector2D.subtract(p, a);
        Vector2D ab = Vector2D.subtract(b, a);

        ab.normalize();
        ab.multiply(ap.dot(ab));
        Vector2D normalPoint = Vector2D.add(a, ab);

        return normalPoint;
    }

    /**
     * Update node position
     */
    abstract void display();

     /*   actor.setPosition((float)(location.x - centerX), (float)(location.y - centerY));
        //setRotate(Math.toDegrees( angle));*/

    public Vector2D getVelocity() {
        return velocity;
    }

    public Vector2D getLocation() {
        return location;
    }

    public void setLocation( float x, float y) {
        location.x = x;
        location.y = y;
    }

    public void setLocationOffset( float x, float y) {
        location.x += x;
        location.y += y;
    }

    public void update(Vector2D v){
        seek(v);
        move();
        display();
    }
}
