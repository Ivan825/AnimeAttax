package com.example.consolecardgame.cards;

import com.example.consolecardgame.players.Player;
import com.example.consolecardgame.utility.Util;

/*
Designed to hold information about a card that is part of the game.
A card has a set of 3 attributes.
*/
public class Card {
    public static final int MAX_CARD_ATTRIBUTE = 3; 
    private String name;
    private String description;
    private int power;
    private int armour;
    //type
    private Categories category;
    private boolean in_graveyard;
    private Attribute[] attributes;
    private Player owner;
    
    // board data
    private Attribute active_attribute;
    private int board_id;
    private int is_Intelligent;
    
    /*
    Constructor that creates a new card instance.
    name is the name of the card
    description is the description of the card
    power is the power of the card
    attributes is the attributes the card has
    */
    public Card( String name, String description, int power, int isIntelligent,Attribute[] attributes,String category ){ 
        if ( attributes.length != MAX_CARD_ATTRIBUTE )
            throw new IllegalArgumentException("Attributes array is not within the specified length!");

        this.name = name;
        this.description = description;
        this.power = power;
        this.attributes = attributes;
        this.is_Intelligent=isIntelligent;
        switch (category) {
            case "Common":
                this.category=Categories.COMMON;
                break;
            case "Rare":
                this.category=Categories.RARE;
                break;
            case "Epic":
                this.category=Categories.EPIC;
                break;
            case "Legendary":
                this.category=Categories.LEGENDARY;
                break;
            default:
                break;
        }
        this.in_graveyard = false;
        armour = 0; 
        // not necessary but here to mean that these are set once a card gets ownership.
        this.owner = null;
        this.active_attribute = null;
        this.board_id = -1;
    }
    
    //Get the name of the card
    public String getName(){
        return name;
    }

    //Get category of card
    public Categories getCategory(){
        return this.category;
    }
    
    //Get is the card is Intelligent or Inflicting
    public int isIntelligent(){
        return this.is_Intelligent;
    }
    //Get the power of the card
    public int getPower() {
        return power;
    }
    
    //Get the description of the card
    public String getDescription(){
        return description;
    }

    /*
    Sets the base power of the card
    power is the value of the base power of the card
    */
    public void setPower(int power) {
        this.power = power;
    }
    
    //Get the resiliance of the card
    public int getArmour() {
        return this.armour;
    }
    
    /*
    Sets the resiliance of the card
    resiliance is the resiliance value of the card
    */
    public void setArmour(int armour) {
        this.armour = armour;
    }

    //Get gets the card attributes
    public Attribute[] getAttributes() {
        return attributes.clone();
    }

    //Get gets the card active attribute (the one it is played with)
    public Attribute getActiveAttribute() {
        return active_attribute;
    }
    
    /*
    Sets the active atribute of the card
    active_attribute is active attribute of the card (the one it is played with)
    */
    public void setActiveAttribute(Attribute active_attribute) {
        this.active_attribute = active_attribute;
    }
    
    //Get gets the owner of the card
    public Player getOwner() {
        return owner;
    }
    
    /**
     * Sets the owner of the card
     * @param owner the player that owns the card
     * //Get the card instance for programming convinence
     */
    public Card setOwner(Player owner) {
        this.owner = owner;
        return this; // Why? Eases the loading of decks. To much writing otherwise. Why not have it in the constructor? Because there may be cards without owners - someday :D
    }
    
    //Get if the card is in the graveyard
    public boolean inGraveyard() {
        return in_graveyard;
    }
    
    //Sets if the card is in the graveyard
    public void setInGraveyard(boolean in_graveyard) {
        this.in_graveyard = in_graveyard;
    }
    

    //Get the id of the board the card is in 
    public int getBoardId() {
        return board_id;
    }
    
    /*
    Sets the board id for this card
    board_id is the id of the board
    */
    public void setBoardId(int board_id) {
        this.board_id = board_id;
    }
    
    //Displays the card in console
    public void printCard() {
        final int NAME_SPACE = 24;
        final int POWER_SPACE = 23;
        final int RESILIANCE_SPACE = 22;
        final int TYPE_SPACE = 24; // Space allocated for Type
        final int ATTRIBUTE_SPACE = 30;
        final String over = "(...)";
    
        // Format name
        String name = this.name;
        if (name.length() >= NAME_SPACE) {
            name = name.substring(0, NAME_SPACE - over.length()) + over;
        }
        name = String.format("%-" + NAME_SPACE + "s", name);
    
        // Format power
        String power = Integer.toString(this.power);
        power = String.format("%-" + POWER_SPACE + "s", power);
    
        // Format resiliance
        String resiliance = Integer.toString(this.armour);
        resiliance = String.format("%-" + RESILIANCE_SPACE + "s", resiliance);
    
        // Format type
        int isIntelligent = this.is_Intelligent;
        String type;
        if (isIntelligent == 1 ) {
            type = "Intelligent"; // Fallback if `type` is not set
        }
        else{
            type="Inflicting";
        }
        type = String.format("%-" + TYPE_SPACE + "s", type);
    
        // Print card
        System.out.printf("|-------------------------------|%n");
        System.out.printf("| Name: %s|%n", name);
        System.out.printf("| Power: %s|%n", power);
        System.out.printf("| Armour: %s|%n", resiliance);
        System.out.printf("| Type: %s|%n", type);
        System.out.printf("|                               |%n");
    
        // Print attributes with their values
        for (Attribute v : attributes) {
            String at = v.getName();
            String value = Integer.toString(v.getValue());

            // Combine attribute name and value
            String attributeDisplay = at + " (" + value + ")";

            // Truncate if too long
            if (attributeDisplay.length() >= ATTRIBUTE_SPACE) {
                attributeDisplay = attributeDisplay.substring(0, ATTRIBUTE_SPACE - over.length()) + over;
            }

            // Format the attribute display
            attributeDisplay = String.format("%-" + ATTRIBUTE_SPACE + "s", attributeDisplay);

            // Print the formatted attribute
            System.out.printf("| %s|%n", attributeDisplay);
        }
    
        System.out.printf("|-------------------------------|%n");
    }
    
    
    
    //Displays the attributes of the card in console
    public void printAttributes(){
        Util.printSeparator( name + " - CARD ATTRIBUTES");
        int i = 0;
        for( Attribute at : attributes ){
            System.out.println("--------[ INDEX " + i + " ]---------" );
            System.out.println("Name: " + at.getName());
            System.out.println("Value: " + at.getValue());
            System.out.println("#Targets: " + at.getNumTargets());;
            i++;
        }
    }
}
