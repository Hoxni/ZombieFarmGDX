package com.mygdx.game.parser;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class TileMapHolder {

    private double defaultScale;
    private String image;
    private double maxScale;
    private double minScale;
    private int tileBorderSize;
    private int tileHeight;
    private int tileMapHeight;
    private int tileMapWidth;
    private int tileWidth;
    private int tilesPerAtlasColumn;
    private int tilesPerAtlasRow;
    private Point2D offset;

    private List<TileHolder> tiles;

    public double getDefaultScale() {
        return defaultScale;
    }

    public void setDefaultScale(double defaultScale) {
        this.defaultScale = defaultScale;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(double maxScale) {
        this.maxScale = maxScale;
    }

    public double getMinScale() {
        return minScale;
    }

    public void setMinScale(double minScale) {
        this.minScale = minScale;
    }

    public int getTileBorderSize() {
        return tileBorderSize;
    }

    public void setTileBorderSize(int tileBorderSize) {
        this.tileBorderSize = tileBorderSize;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public int getTileMapHeight() {
        return tileMapHeight;
    }

    public void setTileMapHeight(int tileMapHeight) {
        this.tileMapHeight = tileMapHeight;
    }

    public int getTileMapWidth() {
        return tileMapWidth;
    }

    public void setTileMapWidth(int tileMapWidth) {
        this.tileMapWidth = tileMapWidth;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTilesPerAtlasColumn() {
        return tilesPerAtlasColumn;
    }

    public void setTilesPerAtlasColumn(int tilesPerAtlasColumn) {
        this.tilesPerAtlasColumn = tilesPerAtlasColumn;
    }

    public int getTilesPerAtlasRow() {
        return tilesPerAtlasRow;
    }

    public void setTilesPerAtlasRow(int tilesPerAtlasRow) {
        this.tilesPerAtlasRow = tilesPerAtlasRow;
    }

    public Point2D getOffset() {
        return offset;
    }

    public void setOffset(Point2D offset) {
        this.offset = offset;
    }

    public List<TileHolder> getTiles() {
        return tiles;
    }

    public void setTiles(List<TileHolder> tiles) {
        this.tiles = tiles;
    }

    public void addTile(TileHolder tile){
        if(tiles == null){
            tiles = new ArrayList<>();
        }
        tiles.add(tile);
    }
}
