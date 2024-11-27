package com.example.consolecardgame.GameLogicJavaCallers;

import java.util.ArrayList;

import com.example.consolecardgame.cards.AttributeType;
import com.example.consolecardgame.cards.Card;
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
    public Card getRandomCardFromAll(){
        return getRandom(random.getRandomCharacter("src/main/resources/com/example/consolecardgame/Cards/All.json"));
    }
    public Card getRandomLegendaryCard(){
        return getRandom(random.getRandomCharacter("src/main/resources/com/example/consolecardgame/Cards/Cards.Legendary.json"));
    }
    public Card getRandomEpicCard(){
        return getRandom(random.getRandomCharacter("src/main/resources/com/example/consolecardgame/Cards/Cards.Epic.json"));
    }
    public Card getRandomRareCard(){
        return getRandom(random.getRandomCharacter("src/main/resources/com/example/consolecardgame/Cards/Cards.Rare.json"));
    }

    private Card getRandom(ArrayList<String>card){  
        String isIntelligent = card.get(4);
        String attack = card.get(2);
        String name = card.get(0);
        String description = card.get(1);
        String power = card.get(6);
        String resilience = card.get(3);
        String buff = card.get(5);
        String type = card.get(8);
        if(isIntelligent.equals("0")){
            Card player_card = new Card(name,description,Integer.parseInt(power), 0,new Attribute[]{new Attribute(Integer.parseInt(attack), AttributeType.INFLICTING),new Attribute(Integer.parseInt(buff),AttributeType.BUFF),new Attribute(Integer.parseInt(resilience), AttributeType.RESILIANCE)},type);
            return player_card;
        }
        else{
            Card player_card = new Card(name,description,Integer.parseInt(power), 1,new Attribute[]{new Attribute(1, AttributeType.INTELLIGENCE),new Attribute(Integer.parseInt(buff),AttributeType.BUFF),new Attribute(Integer.parseInt(resilience), AttributeType.RESILIANCE)},type);
            return player_card;
        }
    }
}
