package com.company;

public class Node {
    private int value; //przechowywana wartość
    private Character character = null; //przechowywany znak
    public Node left = null; //lewe dziecko danego liścia
    public Node right = null; //prawe dziecko danego liścia
    public Node(char letter, int val) { //tworzymy konstruktor liścia dla znaków
        value = val;
        character = letter;
    }
    public Node(int val) { //tworzymy konstruktor liścia dla liści będących sumą dzieci
        value = val;
    }
    public int getValue() { //funkcja zwracająca wartość liścia
        return value;
    }
    public Character getCharacter() {//funkcja zwracająca znak liścia
        return character;
    }
    public boolean isLeaf() {
        return character != null;
    }
}
