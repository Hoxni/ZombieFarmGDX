package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

public class OrthoCamController extends Stage {
    private final OrthographicCamera camera;
    private final Vector3 curr = new Vector3();
    private final Vector3 last = new Vector3(-1, -1, -1);
    private int mapHeight;
    private int mapWidth;
    private Zombie zombie;
    private List<TreeTexture> trees;
    private GamingZone gamingZone;

    public OrthoCamController (OrthographicCamera camera, int mapHeight, int mapWidth, Zombie zombie, List<TreeTexture> trees, GamingZone gamingZone) {
        this.camera = camera;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.zombie = zombie;
        this.trees = trees;
        this.gamingZone = gamingZone;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        Vector3 delta = new Vector3();
        camera.unproject(curr.set(x, y, 0));

        if (!(last.x == -1 && last.y == -1 && last.z == -1) && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
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
        last.set(-1, -1, -1);
        Vector3 clickCoordinates = new Vector3(x, y, 0);
        Vector3 position = camera.unproject(clickCoordinates);
        Vector2D point = new Vector2D(position.x, position.y);
        gamingZone.checkGamingZone(zombie.getLocation(), point);
        if(button == Input.Buttons.RIGHT){
            zombie.follow(point);
        }
        if(button == Input.Buttons.LEFT){
            for(TreeTexture tree : trees){
                if(tree.contains(point.x, point.y)){
                    zombie.setTreeTarget(tree);
                    point.set(tree.getCutPosition().x, tree.getCutPosition().y);
                    zombie.follow(point);
                    break;
                }
            }
        }
        return false;
    }
}