package com.mygdx.game.zombie;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Settings;
import com.mygdx.game.SpecialSprite;
import com.mygdx.game.mapobjects.WhiteWave;
import com.mygdx.game.mapobjects.Obstruction;
import com.mygdx.game.mapobjects.TreeTexture;

import java.util.*;

public class Zombie extends SpecialSprite{

    private static final float HEIGHT_OFFSET = 7f;

    protected final WhiteWave whiteWave;
    protected final ZombieActor zombieActor;

    protected Vector2 currentTargetPoint;
    protected Vector2 target;

    protected TreeTexture treeTarget;

    protected boolean isCreatingPath = false; //"true" when path from location to target is creating with "follow()"
    protected boolean hasTimber = false; //"true" when zombie carries a timber
    protected boolean cutDown = false; //"true" when zombie is going to cut down a tree
    protected boolean returnToStart = false; //"true" when zombie returns to initial point
    protected boolean goDown = true; //"true" when zombie goes from top to bottom of frame

    protected final List<Obstruction> obstructions;
    protected final List<Vector2> points;
    protected final Deque<AbstractMap.SimpleEntry<Float, Integer>> layers; //layers of mapobjects

    protected int pointIndex = 0; //index of current target point
    protected int layerIndex = 0; //index of current layerIndex

    protected MapLayer layer;

    protected final MapObject mapObject;



    public Zombie(Vector2 location, ZombieActor actor, WhiteWave point, List<Obstruction> obstructions){
        super(location);
        zombieActor = actor;
        this.whiteWave = point;
        setCenter();
        currentTargetPoint = Settings.INITIAL_POINT.cpy();
        target = Settings.INITIAL_POINT.cpy();
        points = new ArrayList<>();
        this.obstructions = obstructions;
        layers = new ArrayDeque<>();
        mapObject = new MapObject();
        mapObject.getProperties().put("actor", zombieActor);
        layer = new MapLayer();
        setCenter();
        setLayerIndex();
    }

    public void update(){

        //prohibits update if path isn't created
        if(isCreatingPath) return;

        //prohibits to change location while zombie carry a timber to initial point
        if(returnToStart){
            if(location.dst2(Settings.INITIAL_POINT) < Settings.STOP_DISTANCE){
                returnToStart = false;
                hasTimber = false;
            }
        }

        if(location.dst2(currentTargetPoint) < Settings.STOP_DISTANCE){
            //stop zombie if he already on target point to predict stupid bugs with moving
            if(location.dst2(target) < Settings.STOP_DISTANCE){
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

        //change zombie-layer depending on which obstruction is nearest
        if(!layers.isEmpty()){
            if(goDown){
                if(location.y >= layers.getFirst().getKey()){
                    layerIndex = layers.getFirst().getValue() + 1;
                    layers.pop();
                }
            } else {
                if(location.y <= layers.getFirst().getKey()){
                    layerIndex = layers.getFirst().getValue();
                    layers.pop();
                }
            }
        }

        super.update(currentTargetPoint);
    }

    private void stop(){
        pointIndex++;
        if(pointIndex < points.size()){
            currentTargetPoint = points.get(pointIndex);
        } else {
            whiteWave.stop();
        }
        //if zombie will cut down a tree, cut-animation will play instead of stay-animation
        //zombie can cut down a tree if stands on special point (cutPosition) near this tree
        if(cutDown && location.dst2(treeTarget.getCutPosition()) < Settings.STOP_DISTANCE){
            zombieActor.setFlip(true);
            treeTarget.chopDown(this);
        } else {
            //if zombie is on target point
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

    public int getLayerIndex(){
        return layerIndex;
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
    public void follow(Vector2 target){
        //start creating a path
        isCreatingPath = true;

        if(returnToStart) target = Settings.INITIAL_POINT.cpy();

        pointIndex = 0;
        points.clear();
        layers.clear();

        List<List<Vector2>> paths = new ArrayList<>();
        List<AbstractMap.SimpleEntry<Float, Integer>> layersList = new ArrayList<>();

        //true if zombie moves from top to bottom
        goDown = location.y < target.y;

        //get collections of bypass points
        for(Obstruction obstruction : obstructions){
            //if obstruction is between location and target
            //zombie need to change the layerIndex when passes near
            if(Math.abs(obstruction.getCenter().y - location.y) +
                    Math.abs(obstruction.getCenter().y - target.y) <=
                    Math.abs(location.y - target.y)){
                //add location.y of and layer of obstruction in special list (read below about sorting of this list)
                layersList.add(new AbstractMap.SimpleEntry<>(obstruction.getCenter().y, obstruction.getLayer()));
            }

            //find bypass
            List<Vector2> bypass = obstruction.getBypass(location, target);
            if(bypass != null){
                paths.add(bypass);
            }

        }

        //sort collections of bypass point from closest to farthest
        //collection sorts by first bypass point
        paths.sort(Comparator.comparingDouble(c -> location.dst2(c.get(0))));

        //sort special list from farthest to closest location.y of obstruction to zombie.location.y
        //zombie alternately passes all of obstructions in special list
        //and change his layer respectively
        layersList.sort(Comparator.comparingDouble(c -> Math.abs(location.y - c.getKey())));
        layers.addAll(layersList);

        //add points
        for(List<Vector2> path : paths){
            points.addAll(path);
        }

        //add target as final point
        points.add(target);
        this.target = target;

        currentTargetPoint = points.get(pointIndex);
        whiteWave.start(target.x, target.y);

        //path is created
        isCreatingPath = false;
    }

    public ZombieActor getActor(){
        return zombieActor;
    }

    /**
     * set zombie on the new layer
     */
    public void setLayer(MapLayer layer){
        removeLayer();
        this.layer = layer;
        layer.getObjects().add(mapObject);
    }

    /**
     * set initial layer when zombie created
     */
    private void setLayerIndex(){
        //find closest obstruction
        Obstruction obstruction = Collections.min(obstructions, Comparator.comparingDouble(c->Math.abs(location.y - c.getCenter().y)));
        //if obstruction is located higher than zombie
        if(obstruction.getCenter().y <= location.y){
            layerIndex = obstruction.getLayer() + 1;
        } else {
            //if lower
            layerIndex = obstruction.getLayer();
        }
    }

    /**
     * remove zombie from the old layer
     */
    private void removeLayer(){
        layer.getObjects().remove(mapObject);
    }

    /**
     * used to correct displaying if zombie moving
     */
    private void setCenter(){
        this.centerX = zombieActor.getWidth() / 2;
        this.centerY = zombieActor.getHeight() / 2 + HEIGHT_OFFSET;
    }

    @Override
    protected void display(){
        zombieActor.setPosition(location.x - centerX, location.y - centerY);
    }
}

