package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ZombieActor extends Actor{

    public static final int
            ZOMBIE_STAGES = 6,
            STAND = 0,
            WALK_DOWN = 1,
            WALK_UP = 2,
            WALKWOOD_DOWN = 3,
            WALKWOOD_UP = 4,
            WOODCUT = 5;

    protected int mode = 0;
    protected float duration = 0;
    protected boolean flip = true;


    protected SpecialAnimation[] zombieAnimations;
    protected SpecialAnimation zombie;

    protected Hat hat;
    protected Cloth cloth;
    protected boolean
            hasHat,
            hasCloth;


    public ZombieActor(){
        createZombieAnimations();
        hasHat = false;
        hasCloth = false;
        hat = null;
        cloth = null;
        float width = getAnimationMode(ZombieActor.STAND).getWidth();
        float height = getAnimationMode(ZombieActor.STAND).getHeight();
        zombie = getAnimationMode(0);
        setBounds(getX(), getY(), width, height);
    }

    public int getAnimationMode(){
        return mode;
    }

    //set mode of animation (mode types above in constants)
    public void setZombieMode(int mode){
        //mode does not change if new mode the same as the current mode
        if(this.mode == mode) return;
        this.mode = mode;
        duration = 0;
        zombie = getAnimationMode(mode);
    }

    //reflect texture depending on where zombie goes
    //true if to the right side
    public void setFlip(boolean flag){
        flip = flag;
    }

    //draw texture
    @Override
    public void draw(Batch batch, float alpha){
        Sprite sprite = new Sprite(zombie.getAnimation().getKeyFrame(duration, true));
        sprite.flip(flip, true);
        batch.draw(sprite, getX(), getY());
        if(hasHat){
            Sprite hatTexture = new Sprite(hat.getHatMode(mode).getAnimation().getKeyFrame(duration, true));
            hatTexture.flip(flip, true);
            batch.draw(hatTexture, getX(), getY());
        }
        if(hasCloth){
            Sprite clothTexture = new Sprite(cloth.getHatMode(mode).getAnimation().getKeyFrame(duration, true));
            clothTexture.flip(flip, true);
            batch.draw(clothTexture, getX(), getY());
        }
    }

    //change animation depending on time
    @Override
    public void act(float delta){
        super.act(delta);
        duration += delta;
    }

    private void createZombieAnimations(){
        zombieAnimations = new SpecialAnimation[ZOMBIE_STAGES];
        zombieAnimations[0] = new SpecialAnimation(Paths.WOODCUTTER_STAND, Paths.WOODCUTTER_STAND_XML);
        zombieAnimations[1] = new SpecialAnimation(Paths.WOODCUTTER_WALK_DOWN, Paths.WOODCUTTER_WALK_DOWN_XML);
        zombieAnimations[2] = new SpecialAnimation(Paths.WOODCUTTER_WALK_UP, Paths.WOODCUTTER_WALK_UP_XML);
        zombieAnimations[3] = new SpecialAnimation(Paths.WOODCUTTER_WALKWOOD_DOWN, Paths.WOODCUTTER_WALKWOOD_DOWN_XML);
        zombieAnimations[4] = new SpecialAnimation(Paths.WOODCUTTER_WALKWOOD_UP, Paths.WOODCUTTER_WALKWOOD_UP_XML);
        zombieAnimations[5] = new SpecialAnimation(Paths.WOODCUTTER_WOODCUT, Paths.WOODCUTTER_WOODCUT_XML);
        zombie = zombieAnimations[0];
    }

    public SpecialAnimation getAnimationMode(int i){
        return zombieAnimations[i];
    }

    public void setHat(Hat hat){
        this.hat = hat;
        hasHat = true;
    }

    public void setCloth(Cloth cloth){
        this.cloth = cloth;
        hasCloth = true;
    }
}
