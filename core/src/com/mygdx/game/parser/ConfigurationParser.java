package com.mygdx.game.parser;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigurationParser {

    public static TileMapHolder readConfigurationFromXml(String filename) {
        TileMapHolder tileMap = new TileMapHolder();
        SAXBuilder saxBuilder = new SAXBuilder();
        File inputFile = new File(filename);
        try {
            Document document = saxBuilder.build(inputFile);
            Element mapElement = document.getRootElement();
            readMap(tileMap, mapElement);
            List<Element> tiles = mapElement.getChild("items").getChild("list").getChildren();
            for (Element tile : tiles) {
                tileMap.addTile(readTile(tile));
            }
            Element offset = mapElement.getChild("offset");
            Element pointElement = offset.getChild("Point");
            Point point = new Point();
            int x = pointElement.getAttribute("x").getIntValue();
            int y = pointElement.getAttribute("y").getIntValue();
            point.setLocation(x, y);
            tileMap.setOffset(point);

        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return tileMap;
    }

    private static void readMap(TileMapHolder tileMap, Element classElement) throws DataConversionException {
        tileMap.setDefaultScale(classElement.getAttribute("defaultScale").getDoubleValue());
        tileMap.setImage(classElement.getAttribute("image").getValue());
        tileMap.setMaxScale(classElement.getAttribute("maxScale").getDoubleValue());
        tileMap.setMinScale(classElement.getAttribute("minScale").getDoubleValue());
        tileMap.setTileBorderSize(classElement.getAttribute("tileBorderSize").getIntValue());
        tileMap.setTileHeight(classElement.getAttribute("tileHeight").getIntValue());
        tileMap.setTileMapHeight(classElement.getAttribute("tileMapHeight").getIntValue());
        tileMap.setTileMapWidth(classElement.getAttribute("tileMapWidth").getIntValue());
        tileMap.setTileWidth(classElement.getAttribute("tileWidth").getIntValue());
        tileMap.setTilesPerAtlasColumn(classElement.getAttribute("tilesPerAtlasColumn").getIntValue());
        tileMap.setTilesPerAtlasRow(classElement.getAttribute("tilesPerAtlasRow").getIntValue());
    }

    private static TileHolder readTile(Element classElement) throws DataConversionException {
        TileHolder tile = new TileHolder();
        tile.setFlipHorizontal(classElement.getAttribute("flipHorizontal").getBooleanValue());
        tile.setFlipVertical(classElement.getAttribute("flipVertical").getBooleanValue());
        tile.setHeight(classElement.getAttribute("height").getIntValue());
        tile.setWidth(classElement.getAttribute("width").getIntValue());
        tile.setIndex(classElement.getAttribute("index").getIntValue());
        tile.setX(classElement.getAttribute("x").getIntValue());
        tile.setY(classElement.getAttribute("y").getIntValue());
        return tile;
    }


}
