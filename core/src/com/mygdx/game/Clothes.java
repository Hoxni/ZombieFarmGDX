package com.mygdx.game;

abstract class Clothes{
    protected final int
            NUMBER_OF_MODES = 6;
    protected final SpecialAnimation[] modes;
    protected int modeIndex = 0;

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
        modes = new SpecialAnimation[NUMBER_OF_MODES];
        modes[0] = new SpecialAnimation(stand, standXML);
        modes[1] = new SpecialAnimation(down, downXML);
        modes[2] = new SpecialAnimation(up, upXML);
        modes[3] = new SpecialAnimation(woodDown, woodDownXML);
        modes[4] = new SpecialAnimation(woodUp, woodUpXML);
        modes[5] = new SpecialAnimation(woodCut, woodCutXML);
    }

    public void setMode(int i){
        modeIndex = i;
    }

    public SpecialAnimation getHatMode(int i){
        return modes[i];
    }
}

