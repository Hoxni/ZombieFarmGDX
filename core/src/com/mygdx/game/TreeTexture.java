package com.mygdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.utils.Timer;

import java.util.*;

public class TreeTexture extends TextureMapObject implements Obstruction{
    protected final float OFFSET = 12;
    protected final SpecialAnimation stumpView;
    protected boolean isCutDown = false;
    protected final List<Vector2D> cornerPoints;
    protected final TreeTexture thisTree;
    protected final float WIDTH, HEIGHT;
    protected int layer = 0;
    protected Vector2D center;

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
        cornerPoints.add(new Vector2D(getPosX() - OFFSET * 3, getPosY()));
        cornerPoints.add(new Vector2D(getPosX(), getPosY() + OFFSET));
        cornerPoints.add(new Vector2D(getPosX() + OFFSET * 3, getPosY()));
        cornerPoints.add(new Vector2D(getPosX() + OFFSET, getPosY() - OFFSET * 3));
        center = new Vector2D(getPosX(), getPosY());
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
                    return;
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

    public Vector2D getCutPosition(){
        return new Vector2D(cornerPoints.get(0).x + OFFSET, getPosY() + OFFSET);
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

    //the same as in "Building"
    @Override
    public List<Vector2D> getBypass(Vector2D location, Vector2D target, List<Vector2D> intersectionPoints){

        //if target-point is situated inside of building
        if(intersectionPoints.size() == 1){
            if(Obstruction.isPointInPolygon(target, cornerPoints)){
                Vector2D v = Obstruction.getIntersectionPoints(location, target, cornerPoints).get(0);
                target.set(v.x, v.y);
            }
            return intersectionPoints;
        }

        //find edges which have intersection with path-line
        //this code duplicates "getIntersectionPoints" and can be optimized as described above
        List<Map.Entry<Integer, Vector2D>> intersectedEdges = new ArrayList<>();
        for(int i = 0; i < cornerPoints.size(); i++){
            int next = (i + 1 == cornerPoints.size()) ? 0 : i + 1;

            Vector2D ip = Obstruction.getIntersectionPoint(location, target, cornerPoints.get(i), cornerPoints.get(next));

            if(ip != null){
                intersectedEdges.add(new AbstractMap.SimpleEntry<>(i, ip));
            }
        }

        Vector2D firstIntersection = Collections.min(intersectionPoints, Comparator.comparingDouble(c -> Vector2D.subtract(location, c).magnitude()));
        Vector2D secondIntersection = Collections.min(intersectionPoints, Comparator.comparingDouble(c -> Vector2D.subtract(target, c).magnitude()));

        //points of bypass
        List<Vector2D> path = new ArrayList<>();
        path.add(firstIntersection);


        //if line intersects two adjacent edges
        if(Math.abs((intersectedEdges.get(0).getKey() - intersectedEdges.get(1).getKey()) % 2) == 1){
            int min = Math.min(intersectedEdges.get(0).getKey(), intersectedEdges.get(1).getKey());
            int index = Math.abs(intersectedEdges.get(0).getKey() - intersectedEdges.get(1).getKey()) % 3 + min;
            path.add(cornerPoints.get(index));
            path.add(secondIntersection);
            return path;
        }

        //if line intersects two opposite edges
        //find second bypass point
        Vector2D closestToSecond = Collections.min(cornerPoints, Comparator.comparingDouble(c -> Vector2D.subtract(secondIntersection, c).magnitude()));
        int a = Collections.min(intersectedEdges, Comparator.comparingDouble(c -> Vector2D.subtract(firstIntersection, c.getValue()).magnitude())).getKey(); //first intersected edge
        int c = cornerPoints.indexOf(closestToSecond); //index of closest corner-point for secondIntersection-point
        int index = -1;

        //some hard-to-explain checks to find first bypass point
        switch(c){
            case 0:
                if(a == 1) index = 1;
                else index = 3;
                break;
            case 1:
                if(a == 2) index = 2;
                else index = 0;
                break;
            case 2:
                if(a == 0) index = 1;
                else index = 3;
                break;
            case 3:
                if(a == 0) index = 0;
                else index = 2;
                break;
        }

        //get first bypass point
        Vector2D p = cornerPoints.get(index);
        //yes, second bypass point is calculating earlier than first

        path.add(p);
        path.add(closestToSecond);
        path.add(secondIntersection);

        return path;
    }

    @Override
    public List<Vector2D> getCornerPoints(){
        return cornerPoints;
    }

    @Override
    public Vector2D getCenter(){
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

