package com.example.consolecardgame.cards;

//A enum designed to hold information about the attribute types.
public enum AttributeType {
    INFLICTING( "Inflicting", "This attribute type deals damage to the cards on the board" ),
    BUFF( "Buff", "This attribute type buffs the power of the card played on the board" ),
    RESILIANCE( "Resiliance", "This attribute type buffs the armour of the card played on the board" ),
    INTELLIGENCE("Intelligence","This attribute type guarantees that the next card will be of type higher than the current card but gets placed on the opponents board");
    private final String name;
    private final String description;
    
    /*
    Constructor for the enum.
    name is the name of the enum.
    description is the description of the enum.
    */
    AttributeType( String name, String description ){
        this.name = name;
        this.description = description;
    }
    
    //Get the name of the attribute type
    public String getName() {
        return name;
    }
    
    //Get the description of the attribute type
    public String getDescription() {
        return description;
    }
    
    /*
    Static method to translate a string into the attribute type ENUM
    str is attribute type as string
    Get Attribute type as enum
     */
    public static AttributeType getFromString( String str ){
        switch( str ){
            case "INFLICTING":
                return INFLICTING;
            case "BUFF":
                return BUFF;
            case "RESILIANCE":
                return RESILIANCE;  
            case "INTELLIGENCE":
                return INTELLIGENCE; 
            default:
                throw new Error("Invalid conversion from string " + str + " to AttributeType enum!");
        }
    }
}
