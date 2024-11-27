package com.example.consolecardgame.players;
import java.util.Scanner;

import com.example.consolecardgame.cards.Attribute;
import com.example.consolecardgame.cards.Card;
import com.example.consolecardgame.cards.Graveyard;
import com.example.consolecardgame.cards.Hand;
import com.example.consolecardgame.game.Board;
import com.example.consolecardgame.game.Game;
import com.example.consolecardgame.utility.Util;

public abstract class Player {
    // instance constants/variables
    private final int id;
    private String name; 
    private int rounds_won;
    private Hand hand;
    private Graveyard graveyard;
    private boolean passed;
    private Game game;
    private int cardsDrawn;
    /*
    Constructor that creates a new player for the game.
    id is the id of the player
    name is the name of the player
    */
    public Player( int id, String name ){
        this.id = id;
        this.name = name;
        this.cardsDrawn=0;
        passed = false;
        this.graveyard=new Graveyard();
        rounds_won = 0;
    }
    
    //Get the name of the player
    public String getName() {
        return name;
    }
    
    //Get the id of the player
    public int getId() {
        return id;
    }
    
    /*
    Sets the hand of a player.
    hand is the hand ADT for the player
    */
    public void setHand( Hand hand ){
        this.hand = hand;
    }
    
    //Get the hand ADT of the player
    public Hand getHand() {
        return hand;
    }

    //Get the drawn cards
    public int getCardsDrawn(){
        return cardsDrawn;
    }

    
    //Get the Graveyard of the player
    public Graveyard getGraveyard() {
        return graveyard;
    }
    
    /*
    Sets the Graveyard for the player
    graveyard is the Graveyard of the player
    */
    public void setGraveyard(Graveyard graveyard) {
        this.graveyard = graveyard;
    }
    
    //Get the number of cards left in hand and deck of the player
    public int getCardsLeft(){
        if (this.cardsDrawn == 5 || hand == null) 
            throw new NullPointerException("This method cannot be called without first both the hand and deck to the player.");
        return hand.getCardsInHand() + 5-cardsDrawn;
    }
    
    //Get the number of rounds won by the player
    public int getRoundsWon() {
        return rounds_won;
    }
    
    /*
    Sets the number of rounds won by the player
    rounds_won is the number of rounds
    */
    public void setRoundsWon(int rounds_won) {
        this.rounds_won = rounds_won;
    }
    
    //Get the game the player is part of
    public Game getGame() {
        return game;
    }
    
    /*
    Sets the game the player will be part of
    game is the game the player is part of
    */
    public void setGame(Game game) {
        this.game = game;
    }
    
    //Get if the player has passed
    public boolean hasPassed() {
        return passed;
    }
    
    /*
    Sets if the player has passed
    passed has the player passed detail
    */
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
    
    
    //Displays the hand of the player
    public void printHand(){
        if (hand == null)
            throw new NullPointerException("This method cannot be called without first setting a hand to the player.");
        hand.printCards(); 
    }
    
    
    /*
    Makes the player draw a card from the deck and puts it in hand.
    Get the card that was drawn
    */
    public Card drawCard(){
        if (hand == null) 
            throw new NullPointerException("This method cannot be called without first both the hand and deck to the player.");
        
        if( this.cardsDrawn == 5 ){
            Util.print("Player %s has no more cards in deck!", name); 
            return null;
        }
        Card new_card = //new Card(getr.getname(), name, cardsDrawn, null, Categories.LEGENDARY ) //add function //change here to get random card from the db
        hand.addCardToHand(new_card);
        Util.print("Player %s drew a card!", name);
        
        return new_card; 
    }
    
    /*
    Makes a player play a card index. Removes it from hand and returns the card.
    index the is index of the card to play
    Get the card selected
    */
    public Card playCard( int index ){
        if (hand == null)
            throw new NullPointerException("This method cannot be called without first setting a hand to the player.");
        
        if( hand.getCardsInHand() == 0 ){
            Util.print("Player %s has no more cards in hand!", name);
            return null;
        }
            
        Util.print("Player %s removed a card from hand!", name);
        return hand.removeCardFromHand(index);
    }
    
    //Resets state of the player object
    public void resetGameState() {
        hand = null;
        cardsDrawn=0;
        graveyard = null;
        passed = false;
        Util.printDebug("Player %s's game state was reset", name); 
    }
    
    /*
    This method is dependent on player type so its abstract
    Controls the logic played per turn 
    */
    public abstract PlayData play( Scanner in, Player[] players, Board board );
    
    
    //This class is used to contain the data for a board play.
    public final static class PlayData{
        public final Player player;
        public final Card card_played;
        public final Attribute at_played;
        public final Player targeted_ply;
        public final int[] targetids;
        
        /*
        Creates an instance of PlayData
        player is the player that is playing
        card_played is the card the player selected
        at_played is the attribute the player has selected
        targeted_ply is the targeted player
        targetids are the targeted ids of the cards on the board
        */
        PlayData( Player player, Card card_played, Attribute at_played, Player targeted_ply, int[] targetids ){
            this.player = player;
            this.card_played = card_played;
            this.at_played = at_played;
            this.targeted_ply = targeted_ply;
            this.targetids = targetids;
        }
    }
}
