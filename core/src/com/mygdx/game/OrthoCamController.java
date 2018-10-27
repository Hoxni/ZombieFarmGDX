package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.move.DraggedListener;
import com.mygdx.game.move.TouchListener;

import java.util.ArrayList;
import java.util.List;

public class OrthoCamController extends Stage {
    private final OrthographicCamera camera;
    private final Vector3 curr = new Vector3();
    private final Vector3 last = new Vector3(-1, -1, -1);
    private int mapHeight;
    private int mapWidth;

    private List<DraggedListener> listeners;
    private List<TouchListener> touchListeners;

    public OrthoCamController (OrthographicCamera camera, int mapHeight,int mapWidth) {
        this.camera = camera;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        listeners = new ArrayList<>();
        touchListeners = new ArrayList<>();
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        Vector3 delta = new Vector3();
        camera.unproject(curr.set(x, y, 0));

        if (!(last.x == -1 && last.y == -1 && last.z == -1) && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
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
            for(DraggedListener listener : listeners){

                listener.onDrag(delta.x,delta.y);
            }
        }

        last.set(x, y, 0);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for(TouchListener listener : touchListeners){
            listener.onTouch(screenX,screenY);
        }
        return false;
    }


    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        last.set(-1, -1, -1);
        return false;
    }

    public void addDraggedListener(DraggedListener listener){
        listeners.add(listener);

    }

    public void addTouchListener(TouchListener listener){
        touchListeners.add(listener);

    }




}