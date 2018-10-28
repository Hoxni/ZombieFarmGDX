package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demelyanov on 20.08.2018.
 */
public class CustomOrthogonalTiledMapRenderer extends OrthogonalTiledMapRenderer{
    private List<Zombie> zombies;

    public CustomOrthogonalTiledMapRenderer(TiledMap map){
        super(map);
        zombies = new ArrayList<>();
    }


    public void addZombie(Zombie sprite){
        zombies.add(sprite);
    }

    @Override
    public void render(){
        beginRender();
        for(MapLayer layer : map.getLayers()){
            if(layer.isVisible()){
                if(layer instanceof TiledMapTileLayer){
                    renderTileLayer((TiledMapTileLayer) layer);
                }
                for(MapObject object : layer.getObjects()){
                    renderObject(object);
                }
            }
        }
        for(Zombie zombie : zombies){
            zombie.setLayer(map.getLayers().get(zombie.layerIndex));
        }
        endRender();

    }

    @Override
    public void renderObject(MapObject object){
        Object actor = object.getProperties().get("actor");
        if(actor instanceof ZombieActor){
            ZombieActor zombie = (ZombieActor) actor;
            zombie.act(Gdx.graphics.getDeltaTime());
            zombie.draw(batch, 0);
        } else if(object instanceof TextureMapObject){
            TextureMapObject mapObject = (TextureMapObject) object;
            if(mapObject.isVisible()){
                batch.draw(mapObject.getTextureRegion(), mapObject.getX(), mapObject.getY());
            }
        }

    }
}