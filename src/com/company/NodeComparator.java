package com.company;

import java.util.Comparator;

public class NodeComparator implements Comparator < Node > {

    @Override
    public int compare(Node n1, Node n2) {
        if(n1.getValue() != n2.getValue())
            return n1.getValue() - n2.getValue();
        if(n1.isLeaf() && !n2.isLeaf())
            return 1;
        if(n2.isLeaf() && !n1.isLeaf())
            return -1;
        if(n1.isLeaf() && n2.isLeaf())
            return n1.getCharacter() - n2.getCharacter();
        return -1;
    }
}
