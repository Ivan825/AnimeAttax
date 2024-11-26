package utility;
import java.util.ArrayList;

import cards.Attribute;
import cards.AttributeType;
import cards.Card;

public class AnimeCards {
    // Main 2D ArrayLists for different card types
    private static ArrayList<ArrayList<String>> legendaryCards = new ArrayList<>();
    private static ArrayList<ArrayList<String>> epicCards = new ArrayList<>();
    private static ArrayList<ArrayList<String>> rareCards = new ArrayList<>();
    private static ArrayList<ArrayList<String>> commonCards = new ArrayList<>();
    private static ArrayList<ArrayList<String>> allCards = new ArrayList<>();

    private static void populateData() {
        // Add sample Legendary cards
        legendaryCards.add(createCard("Hori, Kyouko", "Horimiya", "70", "75", "0", "98", "75", "0", "Legendary"));
        legendaryCards.add(createCard("Himejima, Akeno", "High School DxD", "70", "75", "0", "95", "75", "0", "Legendary"));
        legendaryCards.add(createCard("Yuuki Asuna", "Sword Art Online", "60", "70", "0", "95", "70", "0", "Legendary"));
        legendaryCards.add(createCard("Gremory Rias", "High School DxD", "75", "50", "0", "95", "50", "0", "Legendary"));
        legendaryCards.add(createCard("Esdeath", "Akame ga Kill!", "75", "75", "0", "94", "75", "0", "Legendary"));

        // Add sample Epic cards
        epicCards.add(createCard("Forger, Anya", "Spy x Family", "0", "80", "1", "90", "65", "0", "Epic"));
        epicCards.add(createCard("Osaki, Nana", "Nana", "0", "70", "1", "88", "60", "0", "Epic"));
        epicCards.add(createCard("Shiro", "No Game No Life", "0", "88", "1", "80", "50", "0", "Epic"));
        epicCards.add(createCard("Aizen, Sousuke", "Bleach", "0", "95", "1", "80", "80", "0", "Epic"));
        epicCards.add(createCard("Nakano Miku", "The Quintessential Quintuplets", "50", "70", "0", "90", "60", "0", "Epic"));

        // Add sample Rare cards
        rareCards.add(createCard("Tony Tony, Chopper", "One Piece", "0", "60", "1", "90", "65", "0", "Rare"));
        rareCards.add(createCard("Nico Robin", "One Piece", "0", "65", "1", "84", "75", "0", "Rare"));
        rareCards.add(createCard("Makise Kurisu", "Steins;Gate", "0", "70", "1", "88", "60", "0", "Rare"));
        rareCards.add(createCard("Brando, Dio", "JoJo's Bizarre Adventure", "0", "80", "1", "85", "75", "0", "Rare"));
        rareCards.add(createCard("Arlert Armin", "Attack on Titan", "0", "85", "1", "75", "75", "0", "Rare"));
        // Add sample Common cards
        commonCards.add(createCard("Lucy", "Elfen Lied", "0", "60", "1", "85", "45", "0", "Common"));
        commonCards.add(createCard("Revy", "Black Lagoon", "0", "70", "1", "85", "55", "0", "Common"));
        commonCards.add(createCard("Scarlet, Erza", "Fairy Tail", "0", "70", "1", "85", "55", "0", "Common"));
        commonCards.add(createCard("Ginko", "Mushishi", "0", "70", "1", "75", "55", "0", "Common"));
        commonCards.add(createCard("Okabe Rintarou", "Steins;Gate", "0", "60", "1", "65", "45", "0", "Common"));
    }

    private static void combineAllCards() {
        allCards.addAll(legendaryCards);
        allCards.addAll(epicCards);
        allCards.addAll(rareCards);
        allCards.addAll(commonCards);
    }

    private static ArrayList<String> createCard(String character, String anime, String attack, String resilience, String intelligence, String buff, String power, String armor, String type) {
        ArrayList<String> card = new ArrayList<>();
        card.add(character);
        card.add(anime);
        card.add(attack);
        card.add(resilience);
        card.add(intelligence);
        card.add(buff);
        card.add(power);
        card.add(armor);
        card.add(type);
        return card;
    }

    // Function to get a random Legendary card
    public Card getRandomLegendaryCard() {
        return getRandomCard(legendaryCards);
    }

    // Function to get a random Epic card
    public Card getRandomEpicCard() {
        return getRandomCard(epicCards);
    }

    // Function to get a random Rare card
    public Card getRandomRareCard() {
        return getRandomCard(rareCards);
    }

    // Function to get a random card from all collections
    public Card getRandomCardFromAll() {
        return getRandomCard(allCards);
    }

    // Helper function to get a random card from a given list
    private static Card getRandomCard(ArrayList<ArrayList<String>> cards) {
        // Populate data
        populateData();

        // Combine all cards into the master list
        combineAllCards();
        if (cards.isEmpty()) {
            return null; // Return empty if no cards available
        }
        int randomIndex = (int) (Math.random() * cards.size());
        ArrayList<String>card = cards.get(randomIndex);
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
