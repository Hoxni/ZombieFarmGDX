package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ZombieActor extends Actor{
    static final int
            ZOMBIE_STAGES = 7;

    static final int
            STAND = 0,
            WALK_DOWN = 1,
            WALK_UP = 2,
            WALKWOOD_DOWN = 3,
            WALKWOOD_UP = 4,
            WOODCUT = 5,
            WAKEUP = 6;

    protected int mode = 0;
    protected boolean flip = true;
    protected float duration = 0;

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

    public void setZombieMode(int mode){
        if(this.mode == mode) return;
        this.mode = mode;
        duration = 0;
        zombie = getAnimationMode(mode);
    }

    public void setFlip(boolean flag){
        flip = flag;
    }

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
        zombieAnimations[6] = new SpecialAnimation(Paths.WOODCUTTER_WAKEUP, Paths.WOODCUTTER_WAKEUP_XML);
        zombie = zombieAnimations[0];
    }

    public SpecialAnimation getAnimationMode(int i){
        return zombieAnimations[i];
    }

    public void setHat(Hat hat){
        this.hat = hat;
        hat.setMode(mode);
        hasHat = true;
    }

    public void setCloth(Cloth cloth){
        this.cloth = cloth;
        cloth.setMode(mode);
        hasCloth = true;
    }
}
