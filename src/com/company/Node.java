package com.company;

public class Node {
    private int value;
    private Character character = null;
    public Node left = null;
    public Node right = null;
    public Node(char letter, int val) {
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
