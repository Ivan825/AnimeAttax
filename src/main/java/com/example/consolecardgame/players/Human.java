package com.example.consolecardgame.players;
import java.util.Scanner;

import com.example.consolecardgame.cards.Attribute;
import com.example.consolecardgame.cards.AttributeType;
import com.example.consolecardgame.cards.Card;
import com.example.consolecardgame.game.Board;
import com.example.consolecardgame.utility.Util;


public class Human extends Player {
    
    /*
    Constructor for the Human class.
    id is the id of the human.
    name is the name of the human.
    */
    public Human(int id, String name) {
        super(id, name);
    }

    @Override
    public PlayData play(Scanner in, Player[] players, Board board) {
        // Pass turn?
        Util.print("%s, do you wish to pass this turn? (type 'pass' to pass anything else to ignore)", getName());
        if( in.nextLine().equalsIgnoreCase("pass") ){
            Util.print("Player %s has passed his turn - total power: %d\nSkipping turn!", getName(), board.getTotalPlayerPower(this));
            setPassed(true);
            return null;
        }else{
            Util.print("Player %s has chosen to continue his turn!", getName());
        }
        
        // Check Graveyard?
        Util.print("%s, do you wish to check your graveyard before the turn? (type 'grave' to check anything else to ignore)", getName());
        if( in.nextLine().equalsIgnoreCase("grave") ){
            Util.print("Player %s has checked his graveyard!", getName());
            getGraveyard().printGraveyard();
        }else{
            Util.print("Player %s did not check his graveyard.", getName());
        }
        
        Util.print("Now %s's turn!", getName());
        
        // Selection of cards and back selection logic
        int cindex = -1;
        int aindex = -1;
        Card card_played = null;
        Attribute at_played = null;
        while( true ){
            // Prompt card select 
            cindex = (Integer) Util.promptInputValidationByRange("[PROMPT MOVE]["+getName()+"][ID: "+getId()+"] Please select a card by index to play!", in, 0, getHand().getCardsInHand()-1);

            card_played = getHand().getCardFromHand(cindex);
            card_played.printAttributes();
            // Prompt to go back?
            Util.print("[PROMPT MOVE][%s][ID: %d] If you wish to undo your card selection type 'BACK' else press ENTER!", getName(), getId() );
            if( in.nextLine().equalsIgnoreCase("back") )
                continue;
            
            // Prompt attribute select
            Util.print("[PROMPT MOVE][%s][ID: %d] Please select an the attribute to play with the card %s!", getName(), getId(), card_played.getName() );
            aindex = (Integer) Util.promptInputValidationByRange("Please input the index of the attribute:", in, 0, Card.MAX_CARD_ATTRIBUTE-1 );           
            
            at_played = card_played.getAttributes()[aindex];
            break;
        }
        
        // remove card from hand
        playCard(cindex);       
        int pindex = -1;
        int at_played_num_targets = at_played.getNumTargets();
        Player targeted_ply;
        if(at_played.getType()!=AttributeType.INTELLIGENCE ){
            Util.print("Player %s selected attribute %s for %s card.", getName(), at_played.getName(), card_played.getName() );
            
            // Prompt attacking board selection
            getGame().printPlayers();      
            pindex = (Integer) Util.promptInputValidationByRange("[PROMPT MOVE]["+getName()+"][ID: "+getId()+"] Please select the player you wish to target the board of!", in, 0, players.length-1);
            targeted_ply=players[pindex];
            Util.print("Player %s selected player's %s board", getName(), targeted_ply.getName() );
        }
        else{
            for(Player player:players){
                if(player.getId()!=getId()){
                    pindex=player.getId();
                }
            }
            targeted_ply=players[pindex];
            Util.print("Player %s selected player's %s board", getName(), targeted_ply.getName() );
            return new PlayData(this, card_played, at_played, targeted_ply, null);
        }
        
        // Show target board and prompt target selection
        board.printPlayerBoard(targeted_ply);
        int targetids[];
        int cards_on_selected_board = board.getCardsOnBoard(targeted_ply);
        if(  cards_on_selected_board == 0 ){
            Util.print("There are no cards on %s's board! Placing your card on your board with base power!", targeted_ply.getName());
            targetids = new int[0];
        }else{
            targetids = new int[at_played_num_targets];
            Util.print("[PROMPT MOVE][%s][ID: %d] Please select the targets you want to use %s on", getName(), getId(), at_played.getName());
            for( int i = 0; i < at_played_num_targets; i++ ){
                int tindex = 1;
                tindex = (Integer) Util.promptInputValidationByRange("[TARGET SELECTION] Target "+i+"/"+at_played_num_targets+"!", in, 0, cards_on_selected_board-1);
                targetids[i] = tindex;
                Util.print("%s selected as targed id!", tindex);
            }
        }
        
        return new PlayData(this, card_played, at_played, targeted_ply, targetids);
    }
}
