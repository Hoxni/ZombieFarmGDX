package com.mygdx.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;

import java.util.*;

public class Zombie extends SpecialSprite{
    protected Vector2D currentTargetPoint;
    protected Vector2D target;
    protected final WhiteWave whiteWave;
    protected final ZombieActor zombieActor;
    protected TreeTexture treeTarget;
    protected boolean whiteWaveDisplayed = false;
    protected boolean cutDown = false; //"true" when zombie is going to cut down a tree
    protected boolean hasTimber = false; //"true" when zombie carries a timber
    protected boolean returnToStart = false; //"true" when zombie returns to initial point
    protected boolean wakeUp = true; //used for wake up animation when game starts
    protected boolean goDown = true; //"true" when zombie goes from top to bottom of frame
    protected final List<Vector2D> points;
    protected final List<Obstruction> obstructions;
    protected final Deque<AbstractMap.SimpleEntry<Float, Float>> layers; //layers of obstructions
    protected int pointIndex = 0; //index of current target point
    protected float layerIndex = 0; //index of current layerIndex
    protected MapLayer layer;
    protected MapObject mapObject;

    public Zombie(Vector2D location, ZombieActor actor, WhiteWave point, List<? extends Obstruction> obstructions){
        super(location);
        zombieActor = actor;
        this.whiteWave = point;
        setCenter();
        currentTargetPoint = Settings.INITIAL_POINT.copy();
        target = Settings.INITIAL_POINT.copy();
        points = new ArrayList<>();
        this.obstructions = (ArrayList<Obstruction>) obstructions;
        layers = new ArrayDeque<>();
        mapObject = new MapObject();
        mapObject.getProperties().put("actor", actor);
        layer = new MapLayer();
        setCenter();
    }

    public void update(){
        //if(wakeUp) return;

        //prohibits to change location while zombie carry a timber to initial point
        if(returnToStart){
            if(Vector2D.subtract(Settings.INITIAL_POINT, location).magnitude() < Settings.STOP_DISTANCE){
                returnToStart = false;
                hasTimber = false;
            }
        }

        if(Vector2D.subtract(location, currentTargetPoint).magnitude() < Settings.STOP_DISTANCE){
            if(target.equals(currentTargetPoint)){
                points.clear();
            }
            stop();
        } else {
            if(location.y > currentTargetPoint.y){
                if(location.x <= currentTargetPoint.x){
                    zombieActor.setFlip(true);
                } else {
                    zombieActor.setFlip(false);
                }
                if(hasTimber) zombieActor.setZombieMode(ZombieActor.WALKWOOD_UP);
                else zombieActor.setZombieMode(ZombieActor.WALK_UP);
            } else {
                if(location.x <= currentTargetPoint.x){
                    zombieActor.setFlip(true);

                } else {
                    zombieActor.setFlip(false);
                }
                if(hasTimber) zombieActor.setZombieMode(ZombieActor.WALKWOOD_DOWN);
                else zombieActor.setZombieMode(ZombieActor.WALK_DOWN);
            }
        }

        if(!layers.isEmpty()){
            if(goDown){
                if(location.y >= layers.getFirst().getValue()){
                    layerIndex = layers.getFirst().getKey() + 1;
                    layers.pop();
                }
            } else {
                if(location.y <= layers.getFirst().getValue()){
                    layerIndex = layers.getFirst().getKey();
                    layers.pop();
                }
            }
        }

        super.update(currentTargetPoint);
    }

    public void stop(){
        pointIndex++;
        if(pointIndex < points.size()){
            currentTargetPoint = points.get(pointIndex);
        } else {
            whiteWave.stop();
            whiteWaveDisplayed = false;
        }
        //if zombie will cut down a tree, cut-animation will play instead of stay-animation
        //zombie can cut down a tree if stands on special point (cutPosition) near this tree
        if(cutDown && Vector2D.subtract(location, treeTarget.getCutPosition()).magnitude() < Settings.STOP_DISTANCE){
            zombieActor.setFlip(true);
            treeTarget.chopDown(this);
        } else if(!whiteWaveDisplayed){//prohibits stand-animation if zombie is going to the target point
            zombieActor.setZombieMode(ZombieActor.STAND);
        }
    }

    public void setTreeTarget(TreeTexture tree){
        //prohibits to cut down a tree while zombie carry a timber
        if(returnToStart) return;

        cutDown = true;
        treeTarget = tree;
    }

    public TreeTexture getTreeTarget(){
        return treeTarget;
    }

    public int getZombieMode(){
        return zombieActor.getAnimationMode();
    }

    /**
     * zombie goes to the initial point with a timber
     * when he cut down a tree
     */
    public void pickTree(){
        cutDown = false;
        hasTimber = true;
        returnToStart = true;
        follow(Settings.INITIAL_POINT);
    }

    /**
     * create path to the target point
     */
    void follow(Vector2D target){
        if(returnToStart) target = Settings.INITIAL_POINT.copy();
        pointIndex = 0;
        points.clear();
        layers.clear();
        List<List<Vector2D>> paths = new ArrayList<>();
        List<AbstractMap.SimpleEntry<Float, Float>> layersList = new ArrayList<>();

        if(location.y < target.y){
            goDown = true;
        } else {
            goDown = false;
        }

        //get collections of bypass points
        for(Obstruction obstruction : obstructions){
            //if obstruction is between location and target
            //zombie need to change the layerIndex when passes near
            if(Math.abs(obstruction.getCenter().y - location.y) +
                    Math.abs(obstruction.getCenter().y - target.y) <=
                    Math.abs(location.y - target.y)){
                layersList.add(new AbstractMap.SimpleEntry<>(obstruction.getLayer(), obstruction.getCenter().y));
            }

            List<Vector2D> intersectionPoints = Obstruction.getIntersectionPoints(location, target, obstruction.getCornerPoints());
            if(!intersectionPoints.isEmpty()){
                paths.add(obstruction.getBypass(location, target, intersectionPoints));
            }
        }

        //sort collections of bypass point from closest to farthest
        //collection sorts by first bypass point
        paths.sort(Comparator.comparingDouble(c -> Vector2D.subtract(location, c.get(0)).magnitude()));

        layersList.sort(Comparator.comparingDouble(c -> Math.abs(location.y - c.getValue())));
        layers.addAll(layersList);

        //add points
        for(List<Vector2D> path : paths){
            points.addAll(path);
        }

        //add target as final point
        points.add(target);
        this.target = target;

        currentTargetPoint = points.get(pointIndex);
        whiteWave.start(target.x, target.y);
        whiteWaveDisplayed = true;
    }

    public ZombieActor getActor(){
        return zombieActor;
    }

    public void setLayer(MapLayer layer){
        removeLayer();
        this.layer = layer;
        layer.getObjects().add(mapObject);
    }

    public void removeLayer(){
        layer.getObjects().remove(mapObject);
    }

    @Override
    void setCenter(){
        this.centerX = zombieActor.getWidth() / 2;
        this.centerY = zombieActor.getHeight() / 2 + Settings.HEIGHT_OFFSET;
    }

    @Override
    void display(){
        zombieActor.setPosition(location.x - centerX, location.y - centerY);
    }
}

