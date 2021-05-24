package com.company;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

    private static HashMap < Character, Integer > occurences = new HashMap < Character, Integer > ();
    private static HashMap < Character, Float > prob = new HashMap < Character, Float > ();
    private static HashMap < Character, String > encodedValues = new HashMap < Character, String > ();
    private static float entropy = 0;
    private static float avgWordLength = 0;
    private static String output = "";

    private static void frequency(String text) {
        for (char c : text.toCharArray()) {
            if (occurences.containsKey(c)) {
                occurences.put(c, occurences.get(c) + 1);
            } else {
                occurences.put(c, 1);
            }
        }
    }

    private static void probabilities() {
        int length = 0;
        float p = 0;

        for(Map.Entry<Character, Integer> entry : occurences.entrySet()) {
            length += entry.getValue();
        }
        for(Map.Entry<Character, Integer> entry : occurences.entrySet()) {
            p = entry.getValue().floatValue()/length;
            prob.put(entry.getKey(), p);
        }
    }

    private static Node createHuffmanTree() {

        NodeComparator nc = new NodeComparator();
        PriorityQueue<Node> nodes = new PriorityQueue<Node>(occurences.size(), nc);
        for (Map.Entry<Character, Integer> entry : occurences.entrySet()) {
            Node n = new Node(entry.getKey(), entry.getValue());
            nodes.add(n);
        }
        Node rootNode = null;
        while (nodes.size() > 1) {

            Node n1 = nodes.peek();
            nodes.poll();
            Node n2 = nodes.peek();
            nodes.poll();
            Node parent = new Node(n1.getValue() + n2.getValue());
            if (n1.getValue() == n2.getValue() && !n1.isLeaf()) {
                Node pom = n1;
                n1 = n2;
                n2 = pom;
            }
            rootNode = parent;
            parent.left = n1;
            parent.right = n2;
            nodes.add(parent);
        }
        return rootNode;
    }

    private static void encodeValues(Node node, String txt, HashMap < Character, String > encodedValues) {
        if (node == null) {
            return;
        }
        if (node.getCharacter() != null) {
            encodedValues.put(node.getCharacter(), txt);
        }
        encodeValues(node.left, txt + "0", encodedValues);
        encodeValues(node.right,txt + "1", encodedValues);
    }

    public static float log2(float n) {
        float result = (float)(Math.log(n) / Math.log(2));

        return result;
    }

    private static void getEntropy() {
        for(Map.Entry<Character, Float> entry : prob.entrySet()) {
            entropy += entry.getValue() * log2(1/entry.getValue());
        }
    }

    private static String decode(Node root, String encoded){
        String decoded = "";
        Node currentNode = root;
        for(char c : encoded.toCharArray()){
            if(c == '0'){
                if(currentNode.left.isLeaf()){
                    decoded += currentNode.left.getCharacter();
                    currentNode = root;
                }
                else{
                    currentNode = currentNode.left;
                }

            }
            else{
                if(currentNode.right.isLeaf()){
                    decoded += currentNode.right.getCharacter();
                    currentNode = root;
                }
                else{
                    currentNode = currentNode.right;
                }

            }

        }
        return decoded;
    }

    private static void getAvgWordLength() {
        for(Map.Entry<Character, Float> i : prob.entrySet()) {
            avgWordLength += i.getValue() * encodedValues.get(i.getKey()).length();
        }
    }

    private static void createTable() {
        for(Map.Entry<Character, Float> entry : prob.entrySet()) {
            System.out.println(entry.getKey() + "\t\t" + entry.getValue() + "\t\t\t" + occurences.get(entry.getKey()) + "\t\t\t\t" +encodedValues.get(entry.getKey()));
        }
    }

    private static void coding(String text) {
        for (char c: text.toCharArray()) {
            output += encodedValues.get(c);
        }
    }

    public static void main(String[] args) {
        System.out.println("Kodowanie Huffmana\nPodaj tekst do zakodowania: ");
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        sc.close();
        frequency(text);
        probabilities();
        Node rootNode = createHuffmanTree();
        encodeValues(rootNode, "", encodedValues);
        System.out.println("\nStatystki:\n");
        getEntropy();
        System.out.println("Wartość entropii: " + entropy + "\n");
        getAvgWordLength();
        System.out.println("Srednia dlugość słowa kodowego " + avgWordLength + "\n");
        System.out.println("Znak\tPrawdopodobieństwo\tIlość wystąpień\tKod");
        createTable();
        coding(text);
        System.out.println("\nOto twój tekst zakodowany metodą Huffmana: " + output + "\n");
        System.out.println("Oto odkodowany tekst: " + decode(rootNode, output));
    }
}
