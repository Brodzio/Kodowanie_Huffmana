package com.company;

import java.util.Comparator;

public class NodeComparator implements Comparator < Node > {

    @Override
    public int compare(Node n1, Node n2) {
        if(n1.getValue() != n2.getValue()) //jeżeli liście są różnej wartości
            return n1.getValue() - n2.getValue(); //wykonaj zwykłe porównanie
        if(n1.isLeaf() && !n2.isLeaf()) //jeżeli są równe i drugi jest kontenerem, to zawsze będzie on uznawany jako większy
            return 1;
        if(n2.isLeaf() && !n1.isLeaf()) //jeżeli są równe i drugi jest kontenerem, to zawsze będzie on uznawany jako większy
            return -1;
        if(n1.isLeaf() && n2.isLeaf()) //jeżeli oba są liściami
            return n1.getCharacter() - n2.getCharacter(); //to decyduje kolejność alfabetyczna
        return -1; // jeżeli żaden z powyższych warunków nie zostanie spełniony, zwracamy -1 - mniejszość
    }
}
