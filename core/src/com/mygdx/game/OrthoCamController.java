package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

/**
 * allow to drag-and-drop map with mouse (dunno how it works)
 * allow to set target for main zombie with mouse (describe below)
 */

public class OrthoCamController extends Stage {

    private final OrthographicCamera camera;

    private final Vector3 curr = new Vector3();
    private final Vector3 last = new Vector3(-1, -1, -1);

    private final int mapHeight;
    private final int mapWidth;

    private final Zombie zombie;

    private final List<TreeTexture> trees;

    private final GamingZone gamingZone;


    public OrthoCamController (OrthographicCamera camera, int mapHeight, int mapWidth, Zombie zombie, List<TreeTexture> trees, GamingZone gamingZone) {
        this.camera = camera;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.zombie = zombie;
        this.trees = trees;
        this.gamingZone = gamingZone;
    }

    //dunno how it works
    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        Vector3 delta = new Vector3();
        camera.unproject(curr.set(x, y, 0));

        if (!(last.x == -1 && last.y == -1 && last.z == -1) && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {//drag map with left button
            camera.unproject(delta.set(last.x, last.y, 0));
            delta.sub(curr);

            float viewSizeY = camera.viewportHeight/2.0f;
            float viewSizeX = camera.viewportWidth/2.0f;
            float camX = camera.position.x + delta.x;
            float camY = camera.position.y + delta.y;
            boolean availableX = camX < mapWidth - viewSizeX && camX > viewSizeX;
            boolean availableY = camY < mapHeight - viewSizeY*3.0f  && camY > viewSizeY;
            if(!availableX){
                delta.x = 0;
            }
            if(!availableY){
                delta.y = 0;
            }

            camera.position.add(delta.x, delta.y, 0);
        }

        last.set(x, y, 0);
        return false;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        //dunno what it is
        last.set(-1, -1, -1);

        //used to set target on map
        Vector3 clickCoordinates = new Vector3(x, y, 0); //get coordinates
        Vector3 position = camera.unproject(clickCoordinates); //dunno what it is
        Vector2 target = new Vector2(position.x, position.y); //set target location
        gamingZone.checkGamingZone(zombie.getLocation(), target); //check is in game zone
        if(button == Input.Buttons.RIGHT){
            zombie.follow(target); //zombie just follow
        }
        if(button == Input.Buttons.LEFT){ //zombie going to cutting down a tree
            for(TreeTexture tree : trees){
                if(tree.contains(target.x, target.y)){
                    zombie.setTreeTarget(tree);
                    target.set(tree.getCutPosition().x, tree.getCutPosition().y);
                    zombie.follow(target);
                    break;
                }
            }
        }
        return false;
    }
}