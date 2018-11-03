package com.mygdx.game;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class BuildingTexture extends TextureMapObject implements Obstruction{

    protected final float OFFSET = 20;
    protected final float VERTICAL_OFFSET_1 = 100;
    protected final float VERTICAL_OFFSET_2 = 145;

    protected final List<Vector2> cornerPoints;

    protected int layer = 0;


    public BuildingTexture(String building, String buildingXML, float x, float y){

        SpecialAnimation animation = new SpecialAnimation(building, buildingXML);
        animation.getAnimationStage(0).flip(false, true);
        this.setTextureRegion(animation.getAnimationStage(0));
        this.setX(x);
        this.setY(y);

        float width = animation.getWidth();
        float height = animation.getHeight();
        cornerPoints = new ArrayList<>();
        cornerPoints.add(new Vector2(getX() - OFFSET, getY() + VERTICAL_OFFSET_2));
        cornerPoints.add(new Vector2(getX() + width / 2, getY() + height + OFFSET));
        cornerPoints.add(new Vector2(getX() + width + OFFSET, getY() + VERTICAL_OFFSET_2));
        cornerPoints.add(new Vector2(getX() + width / 2, getY() + VERTICAL_OFFSET_1 - OFFSET));

    }

    @Override
    public List<Vector2> getCornerPoints(){
        return cornerPoints;
    }

    @Override
    public Vector2 getCenter(){
        return new Vector2(cornerPoints.get(1).x, cornerPoints.get(0).y);
    }

    @Override
    public void setLayer(int layer){
        this.layer = layer;
    }

    @Override
    public int getLayer(){
        return layer;
    }

}