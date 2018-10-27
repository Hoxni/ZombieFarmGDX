package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demelyanov on 20.08.2018.
 */
public class CustomOrthogonalTiledMapRenderer extends OrthogonalTiledMapRenderer {
    private Sprite sprite;
    private List<Zombie> sprites;

    public CustomOrthogonalTiledMapRenderer(TiledMap map) {
        super(map);
        sprites = new ArrayList<>();
    }


    public void addSprite(Zombie sprite){
        sprites.add(sprite);
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
        for(Zombie zombie : sprites){
            zombie.setLayer(map.getLayers().get((int) zombie.layerIndex));
        }
        endRender();

    }

    @Override
    public void renderObject(MapObject object) {
        Object oActor = object.getProperties().get("actor");
        if(oActor instanceof Actor){
            Actor actor = (Actor) oActor;
            actor.act(Gdx.graphics.getDeltaTime());
            actor.draw(batch,0);
        }
        if(object instanceof TextureMapObject){
            TextureMapObject mapObject = (TextureMapObject)object;
            if(mapObject.isVisible()){
                batch.draw(mapObject.getTextureRegion(), mapObject.getX(), mapObject.getY());
            }
        }

    }


}
