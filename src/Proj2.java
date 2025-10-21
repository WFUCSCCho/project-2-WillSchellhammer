/************************************************************************
 * @file: Proj2.java
 * @description: Reads a dataset of steam games (path defined in args) and tests runtimes for different binary search tree operations.
 * @author: Will S
 * @date: October 20, 2025
 ************************************************************************/

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Proj2 {
    public static void main(String[] args) throws IOException {
        // Use command line arguments to specify the input file
        if (args.length != 2) {
            System.err.println("Usage: java TestAvl <input file> <number of lines>");
            System.exit(1);
        }

        String inputFileName = args[0];
        int numLines = Integer.parseInt(args[1]);

        PrintWriter writer;

        if (new File("output.csv").isFile()) {
            FileOutputStream output = new FileOutputStream("output.csv", true);
            writer = new PrintWriter(output);
            //Used if file exists
            writer.println();
        }
        else {
            FileOutputStream output = new FileOutputStream("output.csv", true);
            writer = new PrintWriter(output);
            //Only used for the 1st time running the file
            writer.println("Sorted BST Insert,Time,Lines,,Sorted AVL Tree Insert,Time,Lines,,Shuffled BST Insert,Time,Lines,,Shuffled AVL Tree Insert,Time,Lines,,Sorted BST Search,Time,Lines,,Sorted AVL Tree Search,Time,Lines,,Shuffled BST Search,Time,Lines,,Shuffled AVL Tree Search,Time,Lines");
        }

        //Unused code below (I copied my code from Project 1 over instead)
        /*
        // For file input
        FileInputStream inputFileNameStream = null;
        Scanner inputFileNameScanner = null;

        // Open the input file
        inputFileNameStream = new FileInputStream(inputFileName);
        inputFileNameScanner = new Scanner(inputFileNameStream);

        // ignore first line
        inputFileNameScanner.nextLine();
        */

        long start;
        long end;

        ArrayList<String> gameList = createGameList(inputFileName, numLines);
        Collections.sort(gameList);

        start = System.nanoTime();
        BST<Game> sortedBST = listToBST(gameList.toArray(new String[0]));
        end = System.nanoTime();
        System.out.println("Sorted BST Insert, Time (ns): " + (end-start) + ", Lines: " + (gameList.size()-1));
        writer.print("Sorted BST Insert," + (end-start) + "," + (gameList.size()-1) + ",,");

        start = System.nanoTime();
        AVLTree<Game> sortedAVLTree = listToAVLTree(gameList.toArray(new String[0]));
        end = System.nanoTime();
        System.out.println("Sorted AVL Tree Insert, Time (ns): " + (end-start) + ", Lines: " + (gameList.size()-1));
        writer.print("Sorted AVL Tree Insert," + (end-start) + "," + (gameList.size()-1) + ",,");

        Collections.shuffle(gameList);

        start = System.nanoTime();
        BST<Game> shuffledBST = listToBST(gameList.toArray(new String[0]));
        end = System.nanoTime();
        System.out.println("Shuffled BST Insert, Time (ns): " + (end-start) + ", Lines: " + (gameList.size()-1));
        writer.print("Shuffled BST Insert," + (end-start) + "," + (gameList.size()-1) + ",,");

        start = System.nanoTime();
        AVLTree<Game> shuffledAVLTree = listToAVLTree(gameList.toArray(new String[0]));
        end = System.nanoTime();
        System.out.println("Shuffled AVL Tree Insert, Time (ns): " + (end-start) + ", Lines: " + (gameList.size()-1));
        writer.print("Shuffled AVL Tree Insert," + (end-start) + "," + (gameList.size()-1) + ",,");

        //Choose a random game to search for
        Collections.shuffle(gameList);
        Game toSearch = null;
        while (toSearch == null) toSearch = processLine(gameList.getFirst());

        start = System.nanoTime();
        System.out.println("\n" + (sortedBST.search(toSearch) != null));
        end = System.nanoTime();
        System.out.println("Sorted BST Search, Time (ns): " + (end-start) + ", Lines: " + (gameList.size()-1));
        writer.print("Sorted BST Search," + (end-start) + "," + (gameList.size()-1) + ",,");

        start = System.nanoTime();
        System.out.println("\n" + sortedAVLTree.contains(toSearch));
        end = System.nanoTime();
        System.out.println("Sorted AVL Tree Search, Time (ns): " + (end-start) + ", Lines: " + (gameList.size()-1));
        writer.print("Sorted AVL Tree Search," + (end-start) + "," + (gameList.size()-1) + ",,");

        start = System.nanoTime();
        System.out.println("\n" + (shuffledBST.search(toSearch) != null));
        end = System.nanoTime();
        System.out.println("Shuffled BST Search, Time (ns): " + (end-start) + ", Lines: " + (gameList.size()-1));
        writer.print("Shuffled BST Search," + (end-start) + "," + (gameList.size()-1) + ",,");

        start = System.nanoTime();
        System.out.println("\n" + shuffledAVLTree.contains(toSearch));
        end = System.nanoTime();
        System.out.println("Shuffled AVL Tree Search, Time (ns): " + (end-start) + ", Lines: " + (gameList.size()-1));
        writer.print("Shuffled AVL Tree Search," + (end-start) + "," + (gameList.size()-1) + ",,");

        writer.flush();
        writer.close();
    }

    //inserts a list of steam games into the AVLTree
    //Arguments: File name, number of lines to read, sort or shuffle the list
    private static ArrayList<String> createGameList(String filename, int numLines) throws FileNotFoundException {
        Scanner csvReader = new Scanner(new File(filename));
        csvReader.nextLine(); //skip the header line
        ArrayList<String> lines = new ArrayList<>();
        while (csvReader.hasNextLine() && numLines >= 0) {
            String line = csvReader.nextLine();
            if (!line.isEmpty()) {
                lines.add(line);
                numLines--;
            }
        }
        return lines;
    }

    //takes the array of Strings, processes them as games, and inserts into the AVLTree
    private static BST<Game> listToBST(String[] steam_games) {
        BST<Game> myBST = new BST<>();
        for (String game : steam_games) {
            Game toInsert = processLine(game);
            if (toInsert != null) myBST.insert(toInsert);
        }
        System.out.println("\nBinary Search Tree populated with " + myBST.size() + " nodes.");
        return myBST;
    }

    //takes the array of Strings, processes them as games, and inserts into the AVLTree
    private static AVLTree<Game> listToAVLTree(String[] steam_games) {
        AVLTree<Game> myAVLTree = new AVLTree<>();
        for (String game : steam_games) {
            Game toInsert = processLine(game);
            if (toInsert != null) myAVLTree.insert(toInsert);
        }
        System.out.println("\nAVL Tree populated with " + myAVLTree.size() + " nodes.");
        return myAVLTree;
    }

    private static Game processLine(String line) {
        String[] data = line.split(",");
        Game processed = null;
        try {
            processed = (new Game(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]), data[3], Integer.parseInt(data[4]),
                    Integer.parseInt(data[5]), Integer.parseInt(data[6]), Integer.parseInt(data[7]), Integer.parseInt(data[8]), Double.parseDouble(data[9])));
        } catch (NumberFormatException e) {
            System.out.println("Error with parsing line: " + line);
        }
        return processed;
    }
}
