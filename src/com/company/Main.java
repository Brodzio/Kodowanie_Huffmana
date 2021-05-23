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
         //tworzymy mapę, która przechowa ilość wystąpień dla każdego znaku

        NodeComparator nc = new NodeComparator(); //tworzymy instancję pomocniczej klasy
        PriorityQueue<Node> nodes = new PriorityQueue<Node>(occurences.size(), nc); //korzystamy z PriorityQueue - w uproszczeniu jest to lista, która zadba, aby nasze elementy były zawsze posortowane; korzysta ona z naszego Comparatora
        for (Map.Entry<Character, Integer> entry : occurences.entrySet()) { //zamieniamy znaki i ich ilość wystąpień na liście, a następnie dodajemy je do utworzonej listy
            Node n = new Node(entry.getKey(), entry.getValue());
            nodes.add(n);
        }
        Node rootNode = null; //zmienna pomocnicza przechowująca korzeń drzewa - finalnie ma być on liściem przechowującym wartość równą długości wpisanego wyrazu
        while (nodes.size() > 1) { //dopóki na liście nie pozostał jeden element(korzeń)

            Node n1 = nodes.peek(); //pobieramy najmniejszy element z listy
            nodes.poll(); //a następnie go usuwamy
            Node n2 = nodes.peek(); //ponownie pobieramy najmniejszy element z listy
            nodes.poll(); //i ponownie go usuwamy; w ten sposób pobraliśmy dwa najmniejsze elementy z listy
            Node parent = new Node(n1.getValue() + n2.getValue()); //tworzymy liść, który będzie przechowywał powyższe elementy
            if (n1.getValue() == n2.getValue() && !n1.isLeaf()) {
                Node pom = n1;
                n1 = n2;
                n2 = pom;
            }
            rootNode = parent; // ustawiamy go tymczasowo jako korzeń
            parent.left = n1; // mniejszy element ustawamy jako jego lewe dziecko
            parent.right = n2;// większy jako jego prawe dziecko
            nodes.add(parent); //następnie dodajemy rodzica jako samodzielny element do listy
        }
        return rootNode;
    }

    private static void encodeValues(Node node, String txt, HashMap < Character, String > encodedValues) { //tworzymy rekurencyjną funkcję, która nada liścią odpowiednie wartości
        if (node == null) {
            return;
        }
        if (node.getCharacter() != null) { //jeżeli liść posiada swój znak
            //System.out.println(node.getCharacter() + ":" + txt);
            encodedValues.put(node.getCharacter(), txt); //dodajemy jego zakodowaną wartość do tablicy
        }
        encodeValues(node.left, txt + "0", encodedValues); //wywołujemy funkcję rekurencyjnie
        encodeValues(node.right,txt + "1", encodedValues); //dla obu dzieci; w ten sposób całemu drzewu zostanie przypisana wartość
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
        String decoded = ""; //zmienna pomocnicza
        Node currentNode = root; //aktualnie sprawdzany liść
        for(char c : encoded.toCharArray()){ //iterujemy poprzez zakodowany tekst
            if(c == '0'){ //jeżeli iterowany znak jest zerem, to oznacza, że musimy iść w lewo
                if(currentNode.left.isLeaf()){ //jeżeli dziecko po lewo jest liściem przechowującym znak
                    decoded += currentNode.left.getCharacter(); //dodajemy ten znak do zmiennej pomocniczej
                    currentNode = root; //a następnie wracamy na początek drzewa
                }
                else{
                    currentNode = currentNode.left; //jeżeli trafiliśmy na kontener, ustawiamy go jako aktualny
                }

            }
            else{ //jeżeli jest to inny znak (1), to przechodzimy na prawą stronę
                if(currentNode.right.isLeaf()){ //jeżeli dziecko po prawo jest liściem przechowującym znak
                    decoded += currentNode.right.getCharacter();//dodajemy ten znak do zmiennej pomocniczej
                    currentNode = root;//a następnie wracamy na początek drzewa
                }
                else{
                    currentNode = currentNode.right;//jeżeli trafiliśmy na kontener, ustawiamy go jako aktualny
                }

            }

        }
        return decoded; //na koniec zwracamy odkodowany tekst
    }

    private static void getAvgWordLength() {
        for(Map.Entry<Character, Float> i : prob.entrySet()) {
            avgWordLength += i.getValue() * encodedValues.get(i.getKey()).length();
        }
    }

    private static void createTable() {
        for(Map.Entry<Character, Float> entry : prob.entrySet()) {
            System.out.println(entry.getKey() + "\t\t" + entry.getValue() + "\t\t\t" + encodedValues.get(entry.getKey()));
        }
    }

    public static void main(String[] args) {
        System.out.println("Kodowanie Huffmana\nPodaj tekst do zakodowania: ");
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine(); //pobieramy wiersz od użytkownika
        sc.close(); //zwalnianie zasobów
        frequency(text);
        probabilities();
        Node rootNode = createHuffmanTree();
        encodeValues(rootNode, "", encodedValues);
        System.out.println("Statystki:");
        getEntropy();
        System.out.println("Wartość entropii: " + entropy);
        getAvgWordLength();
        System.out.println("Srednia dlugość słowa kodowego " + avgWordLength);
        System.out.println("Znak\tPrawdopodobieństwo\tKod");
        createTable();
        String output = "";
        for (char c: text.toCharArray()) {
            output += encodedValues.get(c); //następnie kodujemy wyraz, korzystając z utworzonej tablicy kodowania
        }
        System.out.println();
        System.out.println("Oto twój tekst zakodowany metodą Huffmana: " + output);
        System.out.println();
        System.out.println("Oto odkodowany tekst: " + decode(rootNode, output));
    }
}
