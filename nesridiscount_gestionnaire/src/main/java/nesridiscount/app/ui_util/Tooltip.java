package nesridiscount.app.ui_util;

import java.util.ArrayList;

import javafx.scene.Node;

/**
 * gestion des tooltips placés sur les élements
 */
public class Tooltip {
    public static ArrayList<Tooltip> pageTooltipList = new ArrayList<>();

    private String textToShow;

    private Node linkedNode;

    public Tooltip(String textToShow,Node linkedNode){
        this.textToShow = textToShow;
        this.linkedNode = linkedNode;
    }

    
}
