package com.example.consolecardgame.game;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.consolecardgame.cards.Attribute;
import com.example.consolecardgame.cards.AttributeType;
import com.example.consolecardgame.cards.Card;
import com.example.consolecardgame.cards.Categories;
import com.example.consolecardgame.game.Game;
import com.example.consolecardgame.players.Player;
import com.example.consolecardgame.utility.Util;

//Class implementation of Board class
public class Board {
    private final HashMap<Player, ArrayList<Card>> boardmap;
    //private final Game game;
    private final Player[] players;
    
    //constructor for Board
    public Board(Game game, Player[] players){
        boardmap = new HashMap<Player, ArrayList<Card>>();
        //timed_attributes = new ArrayList<TimedAttribute>();
        //this.game = game;
        this.players = players;
        for( Player ply : players ){
            boardmap.put(ply, new ArrayList<Card>());
        }
    }
    
    //add card to deck
    private void addCard( Player ply, Card card ){
        ArrayList<Card> ply_board =  boardmap.get(ply);
        boardmap.get(ply).add(card);
        card.setBoardId(ply_board.size()-1);
        Util.print("Card %s was added to %s's board!", card.getName(), ply.getName());
    }
    
    //remove card
    private void removeCard( Player ply, Card card ){
        boardmap.get(ply).remove(card);
        ply.getGraveyard().addCard(card);
    }
    
    //get card
    private Card getCard( Player ply, int index ){
        return boardmap.get(ply).get(index);
    }
    
   //to make sure that there is some advantage during game
    private void activateAttribute( Player ply, Card card, Attribute attribute, Player target, Card[] targets ){
        // perform attribute logic, every effect tick.
        for( Card tcard : targets ){ 
            if( tcard.inGraveyard() )
                continue;

            attribute.activate(card, tcard);
                
            if( tcard.getPower() <= 0 ){
                removeCard(target, tcard); // Avoids index out of bounds if a cards dies and gets targeted again.
                Util.print("%s's card has been destroyed - %s", target.getName(), tcard.getName());
            }
            
            if( card.getPower() <= 0 ){
                removeCard(ply, tcard);
                Util.print("%s's card has been destroyed - %s ", ply.getName(), card.getName());
                break; // if the activator of the attribute dies. Attribute stops.
            }
        }
    }
    
    //playing 
    public Categories playBoard( Player.PlayData pdata ){
        
        Util.printSeparator("Board Changes");
        // add card to board
        pdata.card_played.setActiveAttribute(pdata.at_played);
        if(pdata.at_played.getType()==AttributeType.INTELLIGENCE){
            addCard(pdata.targeted_ply,pdata.card_played); //If the card is intelligent, it willl get placed on opponents deck
            return pdata.card_played.getCategory();
        }
        else{
            addCard(pdata.player, pdata.card_played);//Otherwise on the users deck only
        }
        
        
        // Skip if no targets ( Case when we place the only card on the board! )
        if( pdata.targetids.length == 0 ){
            Util.print("No targets found for %s of %s! Card was placed on the board without attribute triggering.", pdata.card_played.getName(), pdata.at_played.getName() );
            return null;
        }
        
        // Target id translation into reference
        Card[] targets = new Card[pdata.targetids.length];
        for( int i = 0; i < pdata.targetids.length; i++ ){
            targets[i] = getCard(pdata.targeted_ply, pdata.targetids[i]);
        }
        
        activateAttribute(pdata.player, pdata.card_played, pdata.at_played, pdata.targeted_ply, targets);
        
        return null;
    }
    
    //get player power
    public int getTotalPlayerPower( Player ply ){
        int t = 0;
        for( Card c : boardmap.get(ply) ){
            t = t + c.getPower();
        }
        return t;
    }
    
    //show no of cards on board
    public int getCardsOnBoard( Player ply ){
        return boardmap.get(ply).size();
    }
    
    //display board
    public void printBoard(){
        for( Player ply : players ){
            int i = 0;
            boolean empty = true;
            Util.printSeparator("BOARD - '" + ply.getName() + "' - POWER: " + getTotalPlayerPower(ply) );
            for( Card card : boardmap.get(ply) ){
                Util.print("CARD INDEX (ON BOARD): %d", i);
                card.printCard();
                i++;
                empty = false;
            }
            if (empty) {
                Util.printEmptyMessage("NO CARDS IN THIS PLAYER'S BOARD");
            }
        }
    }
    
    //Each players board is displayed
    public void printPlayerBoard( Player ply ){
        int i = 0;
        boolean empty = true;
        Util.printSeparator("BOARD - '" + ply.getName() + "' - POWER: " + getTotalPlayerPower(ply) );
        for( Card card : boardmap.get(ply) ){
            Util.print("CARD INDEX (ON BOARD): %d", i);
            card.printCard();
            i++;
            empty = false;
        }
        if (empty) {
            Util.printEmptyMessage("NO CARDS IN THIS PLAYER'S BOARD");
        }
    }
}
