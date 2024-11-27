package com.example.consolecardgame.cards;

import com.example.consolecardgame.utility.Util;

/*
A data structure to control the hand of the user.
Designed to be a static array of size = MAX_CARDS_IN_HAND
A Player has a hand.
A Hand contains Cards that the user can play to the Board.
*/
public class Hand {
    // Data structure variables
    public static final int MAX_CARDS_IN_HAND = 2; // this should remain as 2 - some logic depends on it :b (slightly lazy)
    private final Card array[];
    private int cards_in_hand;
    
    /*
    Constructor
    Creates a new hand instance with size MAX_CARDS_IN_HAND
    */
    public Hand(){
        array = new Card[MAX_CARDS_IN_HAND];
        cards_in_hand = 0;
    }
    
    //Adds a card to the Hand
    public void addCardToHand( Card card ){
        if ( cards_in_hand == MAX_CARDS_IN_HAND ) {
            throw new IllegalStateException("Hand is full!");
        }
        
        for( int i = 0; i < array.length; i++ ){
            if( array[i] == null ){ // find first free space and break
                array[i] = card;
                Util.printDebug("Added card %s to a hand at pos %d!", array[i].getName(), i);
                break;
            }
        }
       
        cards_in_hand++;
    }
    
    /*
    Removes the card from hand
     */
    public Card removeCardFromHand( int index ){
        if ( cards_in_hand == 0 ) {
            throw new IllegalStateException("Hand is empty");
        } 
        
        if ( !hasCardOnIndex(index) ) {
            throw new IllegalStateException("There is no card on the hand at this index!");
        }
        
        Card cardremoved = array[index];
        array[index] = null;
        cards_in_hand--;
        
        return cardremoved;
    }
    
    /*
    Check if there is card on hand given the index
     */
    public boolean hasCardOnIndex( int index ){
        return array[index] != null;
    }
    
    /*
    Gets the number of cards in the hand datastructure.
     */
    public int getCardsInHand(){
        return cards_in_hand;
    }
    
    /*
    Given an index, get the card from hand
     */
    public Card getCardFromHand( int index ){
        if ( !hasCardOnIndex(index) ) {
            throw new IllegalStateException("There is no card on the hand at this index!");
        }
        
        return array[index];
    }
    
    /*
    Gets the index of the first card in hand
     */
    public int getFirstCardIndexFromHand() {
        return ( array[0] == null ) ? 1 : 0;
    }
    
    /*
    Prints the cards in a game format in this data structure.
     */
    public void printCards() {
        int i = 0;
        for( Card c : array ){
            if( c != null ){
                Util.print("Card Index on hand: %d", i);
                c.printCard();
                i++;
            }
        }
        if( i == 0 ){
            Util.printEmptyMessage("HAND IS EMPTY");
        }
    }
    
    
}
