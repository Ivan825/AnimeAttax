package com.example.consolecardgame.GameLogicJavaCallers;

public class Random {
    static {
        System.loadLibrary("RandomLib"); // Load the native library (libRandomLib.dylib)
    }

    // Declare native method
    public native String getRandomCharacter(String filename);

    public static void main(String[] args) {
        Random random = new Random();
        String result = random.getRandomCharacter("src/main/resources/com/example/consolecardgame/Cards/All.json");
        System.out.println("Random Character: " + result);
    }
}
