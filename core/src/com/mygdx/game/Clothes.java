package com.mygdx.game;

public abstract class Clothes{

    protected final SpecialAnimation[] modes;


    public Clothes(
            String stand,
            String standXML,
            String down,
            String downXML,
            String up,
            String upXML,
            String woodDown,
            String woodDownXML,
            String woodUp,
            String woodUpXML,
            String woodCut,
            String woodCutXML){
        modes = new SpecialAnimation[ZombieActor.ZOMBIE_STAGES];
        modes[ZombieActor.STAND] = new SpecialAnimation(stand, standXML);
        modes[ZombieActor.WALK_DOWN] = new SpecialAnimation(down, downXML);
        modes[ZombieActor.WALK_UP] = new SpecialAnimation(up, upXML);
        modes[ZombieActor.WALKWOOD_DOWN] = new SpecialAnimation(woodDown, woodDownXML);
        modes[ZombieActor.WALKWOOD_UP] = new SpecialAnimation(woodUp, woodUpXML);
        modes[ZombieActor.WOODCUT] = new SpecialAnimation(woodCut, woodCutXML);
    }

    public SpecialAnimation getHatMode(int i){
        return modes[i];
    }
}

