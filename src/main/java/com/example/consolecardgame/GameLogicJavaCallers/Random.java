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
        ArrayList<String> all = random.getRandomCharacter("src/main/resources/com/example/consolecardgame/Cards/All.json");
        ArrayList<String> legendary=random.getRandomCharacter("src/main/resources/com/example/consolecardgame/Cards/Cards.Legendary.json");
        ArrayList<String> epic= random.getRandomCharacter("src/main/resources/com/example/consolecardgame/Cards/Cards.Epic.json");
        ArrayList<String> rare= random.getRandomCharacter("src/main/resources/com/example/consolecardgame/Cards/Cards.Rare.json");

    }
}
