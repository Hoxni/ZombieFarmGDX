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

public class CustomOrthogonalTiledMapRenderer extends OrthogonalTiledMapRenderer{
    private List<Zombie> zombies;

    public CustomOrthogonalTiledMapRenderer(TiledMap map){
        super(map);
        //list of all zombies on map
        //used to correct displaying zombies
        zombies = new ArrayList<>();
    }

    //add zombie in list
    public void addZombie(Zombie sprite){
        zombies.add(sprite);
    }

    @Override
    public void render(){
        beginRender();
        for(MapLayer layer : map.getLayers()){
            if(layer.isVisible()){
                //dunno what it is
                if(layer instanceof TiledMapTileLayer){
                    renderTileLayer((TiledMapTileLayer) layer);
                }
                //display objects on map like zombies, trees and buildings
                for(MapObject object : layer.getObjects()){
                    renderObject(object);
                }
            }
        }
        //display zombies on the correct layers
        for(Zombie zombie : zombies){
            zombie.setLayer(map.getLayers().get(zombie.getLayerIndex()));
        }
        endRender();

    }

    @Override
    public void renderObject(MapObject object){
        Object actor = object.getProperties().get("actor");
        if(actor instanceof ZombieActor){
            ZombieActor zombie = (ZombieActor) actor;
            //change stage of animation
            zombie.act(Gdx.graphics.getDeltaTime());
            zombie.draw(batch, 0);
        } else if(object instanceof TextureMapObject){
            TextureMapObject mapObject = (TextureMapObject) object;
            //isVisible used only to check if whiteWave must be displayed
            //trees and buildings are always visible
            if(mapObject.isVisible()){
                //draw trees, building and whiteWave
                batch.draw(mapObject.getTextureRegion(), mapObject.getX(), mapObject.getY());
            }
        }

    }
}