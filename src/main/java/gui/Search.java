package gui;

import java.util.ArrayList;
import java.util.List;

import parser.TagAddress;
import parser.TagNode;
import parser.TagWay;
import parser.Model;
import parser.Tag;
import parser.XMLReader;
import pathfinding.Dijsktra;
import structures.Trie;
import util.TransportType;
import util.Type;

public class Search {
    private Trie trie;
    private MapView mapView;

    public Search(MapView mw){
        //mainview to be used with dijsktra
        mapView = mw;
        trie = Model.getTrie();
    }

    /**
     * A method to get the suggestions from the trie
     * @param input - The input string to search for
     * @return an ArrayList of TagAddress objects
     */
    public ArrayList<TagAddress> getSuggestions(String input){
        return trie.getAddressSuggestions(input, 5);
    }

    /**
     * A method to get the address object from the trie
     * @param input - The input string to search for
     * @param houseNumber - The house number to search for
     * @return The TagAddress object
     */
    public TagAddress getAddress(String input, String houseNumber){
        return trie.getAddressObject(input, houseNumber);
    }

    /**
     * A method to generate a dijsktra object and pathfind between the two addresses
     * @param start         The start address of pathfinding
     * @param end           The end address of pathfinding
     * @param transportType The type of transportation used to pathfind
     */
    public void pathfindBetweenTagAddresses(TagAddress start, TagAddress end, TransportType transportType){
        Dijsktra djiktra = new Dijsktra(start, end, transportType);
        ArrayList<Tag> nodes = djiktra.allVisitedPaths();
        System.out.println("Size of markedtag: " + nodes.size());
        //ArrayList<Tag> nodes = new ArrayList<>();
        TagWay way = new TagWay((long)0, "Route", djiktra.shortestPath(), (short)0, Type.PATHWAY);
        nodes.add(way);
        if (nodes.isEmpty()) return;
        mapView.getDrawingMap().setMarkedTag(nodes);
    }
}