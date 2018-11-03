package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.parser.ConfigurationParser;
import com.mygdx.game.parser.TileHolder;
import com.mygdx.game.parser.TileMapHolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class MyGdxGame extends ApplicationAdapter{

    protected TiledMap map;
    protected TiledMapRenderer renderer;
    protected GamingZone gamingZone;
    protected SpriteBatch batch;

    protected List<TreeTexture> trees;
    protected List<BuildingTexture> buildings;
    protected List<Obstruction> obstructions;

    protected List<Zombie> zombies;
    protected Zombie zombie;

    protected List<Cloth> cloths;
    protected List<Hat> hats;

    protected OrthographicCamera camera;
    protected OrthoCamController cameraController;
    protected Vector3 cameraPosition;


    @Override
    public void create(){

        createMap();
        setGamingZone();

        createTrees();
        createBuildings();
        setObstructions();

        createMainZombie();
        createMobZombies();
        createZombiesUpdater();

    }

    /**
     * create map, renderer, batch and camera
     */
    public void createMap(){
        TileMapHolder tileMapHolder = ConfigurationParser.readConfigurationFromXml(Paths.MAIN_ISLAND_XML);

        int mapHeight = tileMapHolder.getTileMapHeight();
        int mapWidth = tileMapHolder.getTileMapWidth();
        int tileHeight = tileMapHolder.getTileHeight();
        int tileWidth = tileMapHolder.getTileWidth();

        camera = new OrthographicCamera();
        camera.setToOrtho(true, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
        camera.update();

        batch = new SpriteBatch();

        map = new TiledMap();
        MapLayers layers = map.getLayers();
        Texture wholeImage = new Texture(Paths.MAIN_ISLAND);

        int columns = tileMapHolder.getTilesPerAtlasColumn();
        int rows = tileMapHolder.getTilesPerAtlasRow();
        int xOffset = (int) tileMapHolder.getOffset().getX();
        int yOffset = (int) tileMapHolder.getOffset().getY();


        TiledMapTileLayer layer = new TiledMapTileLayer(mapWidth, mapHeight, tileWidth, tileHeight);
        List<TileHolder> tiles = tileMapHolder.getTiles();
        for(TileHolder tile : tiles){
            int i = (tile.getIndex() - 1) / columns;
            int j = (tile.getIndex() - 1) % rows;

            TextureRegion region = new TextureRegion(wholeImage, j * 104, i * 104, tileWidth + 4, tileHeight + 4);
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            region.flip(tile.isFlipHorizontal(), !tile.isFlipVertical());
            cell.setTile(new StaticTiledMapTile(region));
            layer.setCell((tile.getX() - xOffset) / tileWidth, (tile.getY() - yOffset) / tileHeight, cell);
        }
        layers.add(layer);

        renderer = new CustomOrthogonalTiledMapRenderer(map);
        cameraPosition = new Vector3(camera.position);
    }

    public void createTrees(){
        trees = new ArrayList<>();
        for(int i = 0; i < Settings.TREES_NUMBER; i++){
            float y = 500 + i * 150;
            float x = (float) (200 + Math.random() * 3200);
            TreeTexture tree = new TreeTexture(
                    Paths.TROPIC_PALM,
                    Paths.TROPIC_PALM_XML,
                    Paths.TROPIC_PALM_SHADOW,
                    Paths.TROPIC_PALM_SHADOW_XML,
                    Paths.TROPIC_PALM_STUMP,
                    Paths.TROPIC_PALM_STUMP_XML,
                    x,
                    y);
            trees.add(tree);
        }
    }

    public void createBuildings(){
        buildings = new ArrayList<>();
        for(int i = 0; i < Settings.BUILDINGS_NUMBER; i++){
            float y = 700 + i * 150;
            float x = (float) (200 + Math.random() * 500);
            BuildingTexture building = new BuildingTexture(
                    Paths.TOWER,
                    Paths.TOWER_XML,
                    x, y);
            buildings.add(building);
        }
    }

    public void createClothes(){
        cloths = new ArrayList<>();
        cloths.add(new Cloth(
                Paths.CLOTH_STAND,
                Paths.CLOTH_STAND_XML,
                Paths.CLOTH_WALK_DOWN,
                Paths.CLOTH_WALK_DOWN_XML,
                Paths.CLOTH_WALK_UP,
                Paths.CLOTH_WALK_UP_XML,
                Paths.CLOTH_WALKWOOD_DOWN,
                Paths.CLOTH_WALKWOOD_DOWN_XML,
                Paths.CLOTH_WALKWOOD_UP,
                Paths.CLOTH_WALKWOOD_UP_XML,
                Paths.CLOTH_WOODCUT,
                Paths.CLOTH_WOODCUT_XML));
        cloths.add(new Cloth(
                Paths.DOUBLE_CLOTH_STAND,
                Paths.DOUBLE_CLOTH_STAND_XML,
                Paths.DOUBLE_CLOTH_WALK_DOWN,
                Paths.DOUBLE_CLOTH_WALK_DOWN_XML,
                Paths.DOUBLE_CLOTH_WALK_UP,
                Paths.DOUBLE_CLOTH_WALK_UP_XML,
                Paths.DOUBLE_CLOTH_WALKWOOD_DOWN,
                Paths.DOUBLE_CLOTH_WALKWOOD_DOWN_XML,
                Paths.DOUBLE_CLOTH_WALKWOOD_UP,
                Paths.DOUBLE_CLOTH_WALKWOOD_UP_XML,
                Paths.DOUBLE_CLOTH_WOODCUT,
                Paths.DOUBLE_CLOTH_WOODCUT_XML));

        hats = new ArrayList<>();
        hats.add(new Hat(
                Paths.HAT_STAND,
                Paths.HAT_STAND_XML,
                Paths.HAT_WALK_DOWN,
                Paths.HAT_WALK_DOWN_XML,
                Paths.HAT_WALK_UP,
                Paths.HAT_WALK_UP_XML,
                Paths.HAT_WALKWOOD_DOWN,
                Paths.HAT_WALKWOOD_DOWN_XML,
                Paths.HAT_WALKWOOD_UP,
                Paths.HAT_WALKWOOD_UP_XML,
                Paths.HAT_WOODCUT,
                Paths.HAT_WOODCUT_XML));
        hats.add(new Hat(
                Paths.DOUBLE_HAT_STAND,
                Paths.DOUBLE_HAT_STAND_XML,
                Paths.DOUBLE_HAT_WALK_DOWN,
                Paths.DOUBLE_HAT_WALK_DOWN_XML,
                Paths.DOUBLE_HAT_WALK_UP,
                Paths.DOUBLE_HAT_WALK_UP_XML,
                Paths.DOUBLE_HAT_WALKWOOD_DOWN,
                Paths.DOUBLE_HAT_WALKWOOD_DOWN_XML,
                Paths.DOUBLE_HAT_WALKWOOD_UP,
                Paths.DOUBLE_HAT_WALKWOOD_UP_XML,
                Paths.DOUBLE_HAT_WOODCUT,
                Paths.DOUBLE_HAT_WOODCUT_XML));
    }

    /**
     * create main-zombie, whiteWave for him
     * and cameraController (description inside)
     */
    public void createMainZombie(){
        MapLayer whiteWaveLayer = new MapLayer();
        WhiteWave whiteWave = new WhiteWave();
        whiteWaveLayer.getObjects().add(whiteWave);
        whiteWave.setVisible(false);
        map.getLayers().add(whiteWaveLayer);

        createClothes();

        ZombieActor zombieActor = new ZombieActor();
        zombieActor.setCloth(cloths.get(1));
        zombieActor.setHat(hats.get(1));
        zombieActor.setPosition(Settings.INITIAL_POINT.x, Settings.INITIAL_POINT.y);
        zombie = new Zombie(Settings.INITIAL_POINT.cpy(), zombieActor, whiteWave, obstructions);

        ((CustomOrthogonalTiledMapRenderer)renderer).addZombie(zombie);

        //cameraController allow to drag-and-drop map
        //and set targets for main-zombie on map with mouse
        cameraController = new OrthoCamController(camera, 2700, 3400, zombie, trees, gamingZone);
        Gdx.input.setInputProcessor(cameraController);
    }

    public void createMobZombies(){
        zombies = new ArrayList<>();
        WhiteWave whiteWave = new WhiteWave();
        for(int i = 0; i < Settings.ZOMBIES_NUMBER; i++){
            ZombieActor zombieActor = new ZombieActor();
            zombieActor.setPosition(Settings.INITIAL_POINT.x, Settings.INITIAL_POINT.y);
            Zombie zombie = new Zombie(Settings.INITIAL_POINT.cpy(), zombieActor, whiteWave, obstructions);
            zombies.add(zombie);
            ((CustomOrthogonalTiledMapRenderer)renderer).addZombie(zombie);
        }

        //change target for mobs every (Setting.ZOMBIE_MOVING_DELAY) seconds
        Random random = new Random();
        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task(){
            @Override
            public void run(){
                for(Zombie mob : zombies){
                    Vector2 target = new Vector2(random.nextInt(3400), random.nextInt(2700));
                    gamingZone.checkGamingZone(mob.getLocation(), target);
                    mob.follow(target);
                    for(TreeTexture tree : trees){
                        if(tree.contains(target.x, target.y)){
                            mob.setTreeTarget(tree);
                            target.set(tree.getCutPosition().x, tree.getCutPosition().y);
                            mob.follow(target);
                            break;
                        }
                    }
                }
            }
        }, 0, Settings.ZOMBIE_MOVING_DELAY);
        timer.start();
    }

    /**
     * update displaying of all zombies
     */
    public void createZombiesUpdater(){
        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task(){
            @Override
            public void run(){
                zombie.update();
                for(Zombie mob : zombies){
                    mob.update();
                }
            }
        }, 0, Settings.ZOMBIE_UPDATE_DELAY);
        timer.start();
    }

    /**
     * set impassable terrain
     * set layers for all obstructions depending on the location
     * ascending from top to bottom (first layerIndex = 1, last = number of obstructions - 1)
     * zero layer used to display zombie behind the topmost building
     */
    public void setObstructions(){
        obstructions = new ArrayList<>();
        obstructions.addAll(trees);
        obstructions.addAll(buildings);
        //sort obstruction ascending location.y from top to bottom
        obstructions.sort(Comparator.comparingDouble(o -> o.getCenter().y));

        //add zero level layer
        map.getLayers().add(new MapLayer());

        //set layers and add objects on the map
        for(int i = 0; i < obstructions.size(); i++){
            MapLayer mapLayer = new MapLayer();
            Obstruction obstruction = obstructions.get(i);
            mapLayer.getObjects().add((TextureMapObject) obstruction);
            obstruction.setLayer(i + 1);
            map.getLayers().add(mapLayer);
        }

        //set impassable terrain
        List<Vector2> points = new ArrayList<>();
        points.add(new Vector2(638, 780));
        points.add(new Vector2(1300, 1105));
        points.add(new Vector2(2375, 585));
        points.add(new Vector2(1700, 80));

        ImpassableTerrain elevation = new ImpassableTerrain(points);
        obstructions.add(elevation);
    }

    public void setGamingZone(){
        List<Vector2> points = new ArrayList<>();
        points.add(new Vector2(150, 1050));
        points.add(new Vector2(1710, 1845));
        points.add(new Vector2(3270, 1065));
        points.add(new Vector2(1710, 265));

        gamingZone = new GamingZone(points);
    }

    /**
     * prevent image stretching when resizing a window
     */
    @Override
    public void resize(int width, int height){
        super.resize(width, height);
        camera.setToOrtho(true, width, height);
        camera.translate(cameraPosition);
        camera.update();
    }

    //dunno what it is
    @Override
    public void render(){

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();
        batch.setProjectionMatrix(camera.combined);
        cameraController.act(Gdx.graphics.getDeltaTime());
        cameraController.draw();
        camera.update();

    }

    //dunno what it is
    @Override
    public void dispose(){
        batch.dispose();
        map.dispose();
        cameraController.dispose();
    }
}