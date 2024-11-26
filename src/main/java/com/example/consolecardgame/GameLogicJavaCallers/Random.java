package com.example.consolecardgame.GameLogicJavaCallers;

import java.util.ArrayList;

public class Random {
    static {
        System.loadLibrary("RandomLib"); // Load the native library (libRandomLib.dylib)
    }

    // Declare native method
    public native ArrayList<String> getRandomCharacter(String filename);

    public static void main(String[] args) {
        Random random = new Random();

        // Call the native method
        ArrayList<String> result = random.getRandomCharacter("src/main/resources/com/example/consolecardgame/Cards/All.json");

        // Print the result
        System.out.println("Random Character Details: " + result);
    }
}
