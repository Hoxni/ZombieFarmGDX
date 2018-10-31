package com.mygdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;

public class TreeTexture extends TextureMapObject implements Obstruction{
    protected final float OFFSET = 12;
    protected final SpecialAnimation stumpView;
    protected boolean isCutDown = false;
    protected final List<Vector2> cornerPoints;
    protected final TreeTexture thisTree;
    protected final float WIDTH, HEIGHT;
    protected int layer = 0;
    protected final Vector2 center;

    public TreeTexture(
            String palm,
            String palmXML,
            String shadow,
            String shadowXML,
            String stump,
            String stumpXML, float x, float y){
        SpecialAnimation treeView = new SpecialAnimation(palm, palmXML);
        WIDTH = treeView.getWidth();
        HEIGHT = treeView.getHeight();
        SpecialAnimation shadowView = new SpecialAnimation(shadow, shadowXML);
        treeView.getAnimationStage(0).flip(false, true);
        shadowView.getAnimationStage(0).flip(false, true);

        //combine shadow and tree images in one texture
        Texture textureShadow = shadowView.getAnimationStage(0).getTexture();
        textureShadow.getTextureData().prepare();
        Pixmap pixmapShadow = textureShadow.getTextureData().consumePixmap();
        Texture textureTree = treeView.getAnimationStage(0).getTexture();
        textureTree.getTextureData().prepare();
        Pixmap pixmapTree = textureTree.getTextureData().consumePixmap();
        Pixmap pixmap = new Pixmap(textureTree.getWidth(), textureTree.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.drawPixmap(pixmapShadow, 0, (int) (HEIGHT - shadowView.getHeight()));
        pixmap.drawPixmap(pixmapTree, 0, 0);
        Texture texture = new Texture(pixmap);
        pixmapShadow.dispose();
        pixmapTree.dispose();
        textureShadow.dispose();
        textureTree.dispose();

        TextureRegion treeWithShadow = new TextureRegion(texture);
        treeWithShadow.flip(false, true);
        this.setTextureRegion(treeWithShadow);
        this.setX(x);
        this.setY(y);
        thisTree = this;

        stumpView = new SpecialAnimation(stump, stumpXML);
        stumpView.getAnimationStage(0).flip(false, true);

        cornerPoints = new ArrayList<>();
        cornerPoints.add(new Vector2(getPosX() - OFFSET * 3, getPosY()));
        cornerPoints.add(new Vector2(getPosX(), getPosY() + OFFSET));
        cornerPoints.add(new Vector2(getPosX() + OFFSET * 3, getPosY()));
        cornerPoints.add(new Vector2(getPosX() + OFFSET, getPosY() - OFFSET * 3));
        center = new Vector2(getPosX(), getPosY());
    }

    public void chopDown(Zombie zombie){
        if(!isCutDown){
            zombie.getActor().setZombieMode(ZombieActor.WOODCUT);
        }

        Timer cutProcess = new Timer();
        cutProcess.scheduleTask(new Timer.Task(){
            @Override
            public void run(){
                //if zombie stood near tree all necessary time, tree replaced by stump
                if(zombie.getZombieMode() == ZombieActor.WOODCUT && zombie.getTreeTarget() == thisTree && !isCutDown){
                    thisTree.setTextureRegion(stumpView.getAnimationStage(0));
                    float x = getX() + WIDTH / 2 - stumpView.getWidth() / 2;
                    float y = getY() + HEIGHT - stumpView.getHeight();
                    thisTree.setX(x);
                    thisTree.setY(y);
                    zombie.pickTree();
                    isCutDown = true;
                }
            }
        }, Settings.CUT_DOWN_DURATION);
        cutProcess.start();
    }

    public float getPosX(){
        return getX() + WIDTH / 2;
    }

    public float getPosY(){
        return getY() + HEIGHT - OFFSET;
    }

    public Vector2 getCutPosition(){
        return new Vector2(cornerPoints.get(0).x + OFFSET, getPosY() + OFFSET);
    }

    public boolean contains(double x, double y){
        if(isCutDown) return false;
        double leftBound = this.getX() + WIDTH / 2.0 - stumpView.getWidth() / 2.0;
        double rightBound = this.getX() + WIDTH / 2.0 + stumpView.getWidth() / 2.0;
        double top = this.getY();
        double bottom = this.getY() + HEIGHT;
        if(x >= leftBound && x <= rightBound){
            return y <= bottom && y >= top;
        }
        return false;
    }

    @Override
    public List<Vector2> getCornerPoints(){
        return cornerPoints;
    }

    @Override
    public Vector2 getCenter(){
        return center;
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

