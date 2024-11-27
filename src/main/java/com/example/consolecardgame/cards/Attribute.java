package com.example.consolecardgame.cards;

import com.example.consolecardgame.utility.Util;

/*
Designed to hold information about an attribute.
Each category can have any of the types described in AttributeType.
*/
public class Attribute {
    // Instance variables
    private String name;
    private String description;
    private AttributeType type;
    private int value;
    private int num_targets;
    
    /*
    Constructor that creates a new instance of an attribute.
    name is the name of the attribute
    description is the description of the attribute
    value is the base value of the attribute
    type is the type of the attribute
    the number of targets are the number of cards the attribute can affect(set to 1 currently but can be developed in the future)
     */
    public Attribute( String name, String description, int value, int num_targets, AttributeType type ){
        this.name = name;
        this.description = description;
        this.value = value;
        this.type = type;
        this.num_targets = num_targets;
    }
    
    //Get name of the attribute
    public String getName() {
        return name;
    }
    
    //Get description of the attribute
    public String getDescription() {
        return description;
    }
    
    //Get base value of the attribute
    public int getValue() {
        return value;
    }
    
    //Get type of the attribute
    public AttributeType getType(){
        return type;
    }
    
    //Get number of targets the attribute can target
    public int getNumTargets() {
        return num_targets;
    }
    
    /*
    Activates a attribute from an activator card on a target card
    Activator is the card which the attribute belongs to
    Target is the target card
    */
    public void activate( Card activator, Card target ){
        switch( type ){
            case INFLICTING:
                int damage_after_resiliance = target.getArmour()-value;
                if( damage_after_resiliance < 0 ){
                    target.setArmour(0);
                }else{
                    target.setArmour(damage_after_resiliance);
                    Util.print("%s has inflicted %d points of damage on %s, however, resiliance protected the target's power!", activator.getName(), value, target.getName());
                    break; // No need to continue
                }
                
                target.setPower(target.getPower()+damage_after_resiliance); // left over damage subtracts on power  
                Util.print("%s has inflicted %d points of damage on %s", activator.getName(), value, target.getName());
                break;
            case BUFF:
                target.setPower(target.getPower()+value);
                Util.print("%s has buffed %s by %d", activator.getName(), target.getName(), value);
                break;
            case RESILIANCE:
                target.setArmour(target.getArmour()+value);
                Util.print("%s has inscreased %s resiliance by %d", activator.getName(), target.getName(), value);
                break;
            default:
                throw new IllegalStateException("Invalid attribute type during activation!");
        }
        
        Util.print( "Attribute '%s' from the card %s was activated targeting the card %s", name, activator.getName(), target.getName() );
    }
}
