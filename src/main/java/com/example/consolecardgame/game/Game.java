package com.example.consolecardgame.game;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringJoiner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.example.consolecardgame.game.Board;
import com.example.consolecardgame.cards.AttributeType;
import com.example.consolecardgame.cards.Hand;
import com.example.consolecardgame.players.Human;
import com.example.consolecardgame.players.Player;
import com.example.consolecardgame.utility.Util;
import com.example.consolecardgame.cards.Categories;
//Define classes below
public class Game {
    // instance constants
    private final int id; 
    private final int n_players;
    private final Scanner in;
    private final Player[] players;
    private final int max_rounds;
    private final Round[] round_data;
    private final boolean game_mode;
    
    // instance variables
    private int current_round;
    private Board board;
    private int turn;
    private boolean is_active;
    
    ///Now the game mechanics to decide the number of rounds and the specific
    private final class Round {
        public final int number;
        public final Player[] winners; 
        public final HashMap<Player, Integer> score = new HashMap<Player, Integer>();
        public final boolean draw;
        
        //constructor
        private Round(){
            this.number = current_round;
            int max = Integer.MIN_VALUE;
            int max_count = 0;
            Player winner = null;
            for( Player ply : players ){
                int tpower = board.getTotalPlayerPower(ply);
                if( max_count == 0 || tpower > max ){
                    max = tpower;
                    winner = ply;
                    max_count = 1;
                }else if ( tpower == max ){
                    max_count++;
                }
                // store round score
                score.put(ply,tpower);
            }
            
            this.winners = new Player[max_count];
            if ( max_count > 1 ) {
                draw = true;
                int i = 0;
                for( Player ply : score.keySet() ){
                    int tpower = board.getTotalPlayerPower(ply);
                    if ( tpower == max ) {
                        this.winners[i] = ply;
                        i++;
                    }       
                }
            }else{
                draw = false;
                this.winners[0] = winner;
            }
        }
    }
    
    //Game constructor
    public Game( int id, int n_players, int max_rounds, boolean game_mode, Scanner in ){
        this.id = id;
        this.n_players = n_players;
        this.max_rounds = max_rounds;
        this.in = in;
        this.game_mode = game_mode;
        this.players = new Player[n_players];
        this.round_data = new Round[max_rounds];
        
        current_round = 0;
        is_active = true;
    }
    
    //getters(id)
    public int getId() {
        return id;
    }
    
    //getters(current round)
    public int getCurrentRound() {
        return current_round;
    }
    
    //return whose turn it is
    public int getTurn() {
        return turn;
    }
    
    //Game start
    public void start(){
        if( game_mode )
            Util.clearConsole();
        
        // Introduction
        Util.printSeparator2("Welcome to 'Not A Rip-off GWENT' - A Card Game - COMP213 Assesement 1 - By Paulo Santos");
        introduction();
        if( game_mode )
            Util.clearConsole();
        
        // Player Setup
        Util.printSeparator2("A game has started! ID: " + id + " #Players: " + n_players);
        setUpPlayers();
        
        while( is_active ){ // Allows re matches with new decks for the same players!
            if( game_mode )
                Util.clearConsoleConfirm(in);
            
            current_round++;
            turn = 0;
            
            // Initialise the board and get deck
            board = new Board(this, players);
            
            
            
            if( game_mode )
                Util.clearConsoleConfirm(in);
            
            // Hand Setup and initial draw
            Util.printSeparator2("Setting up player's hands...");
            setUpHand();
            
            if( game_mode )
                Util.clearConsoleConfirm(in);
            
            // Round start
            Util.printSeparator2("Starting round " + current_round); 
            logic();
            
            // Game end
            if(current_round < max_rounds){
                Util.print("Advancing to the next round! Round number: %d/%d", current_round, max_rounds);
                is_active = true;
            }else{
                is_active = false;
            }
        }
        
        Util.printSeparator2("Game over!");
        gameOver();
        
        // close stream
        in.close();
    }
    //Game start
    private void introduction(){
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("===============[ Introduction & Game Instructions ]===============");
        joiner.add("A Round based card game with 3 factions, attribute types and categories, multiple players, ai computers, hand, deck and graveyard data structures.");
        joiner.add(" 1. Player Setup:\n   - Here the user inputs the number of players participating in the game and decides if they are a Computer or a Human.");
        joiner.add(" 2. Deck Setup:\n   - Here the user picks each player's deck.");
        joiner.add(" 3. Game Logic:\n   - The game round starts. Hand size = " + Hand.MAX_CARDS_IN_HAND +"\n   - Each turn a player plays a card.\n   - The player selects which attribute the card will be played with.\n   - There are 2 Categories of attributes TIMED and NON-TIMED. Each attribute can be on of the 3 types of attributes BUFF, INFLICT and RESILIANCE.\n   - The card selected will be played with an active attribute placed on the board\n   - Turns are repeated untill a player passes or runs out of cards\n   - Round winner is the player with the highest power on the board.\n   - Repeat until no more cards. \n   - Winner of the round is the player with most power on board.\n   - Repeat until max rounds. Game Winner is player with most round wins.");
       
        joiner.add("\n===============[ Specifics - Attributes ]===============");
        joiner.add("Attributes can be timed or not timed. Timed attributes are deployed after a certains number of turns. Non timed are deployed at play.\nEach attribute can be on of the 3 types:");  
        for( AttributeType v : AttributeType.values() )
            joiner.add( "Name: " + v.getName() + "\nDescription: " + v.getDescription());
        
        joiner.add("\n===============[ Copyright for the song ]==============="); 
        joiner.add("Copyright Disclaimer Under Section 107 of the Copyright Act 1976, allowance is made for \"fair use\" for purposes such as criticism, comment, news reporting, teaching, scholarship, and research. Fair use is a use permitted by copyright statute that might otherwise be infringing.\nNon-profit, educational or personal use tips the balance in favor of fair use.");
        joiner.add("Song: L' Arabesque Danse Toujours from Magi OST. Rights reserved to the original content creators");
        
        System.out.println(joiner.toString());
        Util.print("Done reading? Type anything to move on...");
        in.nextLine();
    }
    
    //Player Setup for each side
    private void setUpPlayers(){
        for( int i = 0; i < n_players; i++ ){
            // Setup player information       
            System.out.println("[PLAYER SETUP][ID: "+i+"]");
            Util.print("[COMPUTER SETUP] Please enter players name:");
            players[i] = new Human(i, in.nextLine());
            players[i].setGame(this);
        }
    }
    
    //setup other players hand
    private void setUpHand(){
        for( Player ply : players ){
            ply.setHand(new Hand());
            Util.printSeparator("Initial Card for '" + ply.getName() + "'");
            // draw first card, so the hand always has 2
            if( game_mode && ply instanceof Human )
                ply.drawCard().printCard();

            //computer bot can be added here
        }
    }
    
   //in game logic for implementation of game
    private void logic(){
        HashMap<Integer,Categories>state = new HashMap<>();
        while( everyoneHasNotPassed() ){
            HashMap<Integer,Categories>new_state=new HashMap<>();
            for( Player ply : players ){
                if ( !ply.hasPassed() ){
                    // Confirmation telling players the next turn is about to be computed:
                    turn++;
                    Util.print("[NEXT TURN ALERT][#%d] Type anything to move on to the next turn.. whenever you are ready!", turn);
                    in.nextLine();
                    
                    // Check if player can play this turn!(total cards left 0 or if a card is present in hand or deck )
                    if( ply.getCardsLeft() == 0 ){
                        ply.setPassed(true);
                        Util.print("Player %s has no more cards! He has passed. Total power: %d\nSkipping turn!", ply.getName(), board.getTotalPlayerPower(ply));
                        continue;
                    } 
                    
                    // Check if the player can draw a card from deck to hand
                    if( ply.getCardsDrawn() == 5 ) {
                        Util.print("Player %s has no more cards in the deck! Unable to draw!", ply.getName());
                    } else {
                        // Player draws a card from deck
                        Util.printSeparator("Card drawn by '" + ply.getName() + "'");
                        if( game_mode && ply instanceof Human )
                            //logic for random and type
                            if(state.get(ply.getId())!=null){
                                ply.drawCard(state.get(ply.getId())).printCard();

                            }
                            else{
                                ply.drawCard().printCard();
                            } 
                            
                    }
                    // Prints to console cards in hand
                    Util.printSeparator("Hand for '" + ply.getName() + "'");
                    if( game_mode && ply instanceof Human )ply.printHand();
                        
                    
                    // Prints to console board state
                    Util.print("Type anything to show board... whenever you are ready!");
                    in.nextLine();
                    board.printBoard();
                    
                    // Call abstract method that runs the logic depending on the instance of Player. Run-time Polymorphism!
                    // Store data in a ADT for easy access and use.
                    Player.PlayData pdata = ply.play(in, players, board);
                    
                    // Check if the player passed 
                    if ( ply.hasPassed() ) {
                        continue;
                    } else {
                        // Move on to board logic
                        Categories category = board.playBoard(pdata);
                        if(category!=null){
                            new_state.put(pdata.player.getId(), category);
                        }
                    }
                }else{
                    Util.print("Player %s has passed. Skipping turn!", ply.getName());
                }
                
                if( game_mode )
                    Util.clearConsoleConfirm(in);
            }
            state = new HashMap<>(new_state);
        }
        
        // Store and calculate round data
        round_data[current_round-1] = new Round();
        Util.printSeparator2("Round Winner");

        if( round_data[current_round-1].draw ){
            Util.print("There was a draw! Consiting of %d players! These players get a round win!", round_data[current_round-1].winners.length);
            for ( Player winner : round_data[current_round-1].winners ) {
                Util.print("One of the winners of this round was %s with a total power of %d", winner.getName(), board.getTotalPlayerPower(winner));
                winner.setRoundsWon(winner.getRoundsWon() + 1);
            }
        }else{
            Player winner = round_data[current_round-1].winners[0];
            Util.print("The winner of this round was %s with a total power of %d", winner.getName(), board.getTotalPlayerPower(winner));
            winner.setRoundsWon(winner.getRoundsWon() + 1);
        }
        
        // reset player states
        for( Player ply : players ) 
            ply.resetGameState();
    }
    
    //game over will show winner
    private void gameOver(){
        Util.printSeparator2("Game is over! Round(s) results:");
        for( Round round : round_data ){
            System.out.println("----------[ Round: " + round.number + " ]------------");
            for (Entry<Player, Integer> entry : round.score.entrySet()) {
                Player ply = entry.getKey();
                int value = entry.getValue();
                System.out.println("[" + ply.getName() + "]: " + value );
            }
        }
        
        Util.printSeparator2("Game winner");
        
        int max = Integer.MIN_VALUE;
        int max_count = 0;
        Player winner = null;
        for( Player ply : players ){
            int rwon = ply.getRoundsWon();
            if( max_count == 0 || ply.getRoundsWon() > max ){
                max = rwon;
                winner = ply;
                max_count = 1;
            }else if ( rwon == max ){
                max_count++;
            }
        }
        
        if ( max_count > 1 ) {
            Util.printInBox("There was a overall game draw! Consiting of %d players! No one wins the game!", round_data[current_round-1].winners.length);
        }else{
            Util.printInBox("The winner of the game is: %s with %d round wins!", winner.getName(), winner.getRoundsWon());
        }
    }
    
    //active players show info
    public void printPlayers(){
        Util.printSeparator( " PLAYERS INFO " );
        for( Player ply : players ){
            System.out.println("----------[ Player ID: " + ply.getId() + " ]-----------");
            System.out.println("Name: " + ply.getName());
            System.out.println("Cards left: " + ply.getCardsLeft());
            System.out.println("Cards hand: " + ply.getHand().getCardsInHand());
            System.out.println("Cards board: " + board.getCardsOnBoard(ply));
            System.out.println("Total Power: " + board.getTotalPlayerPower(ply));
        }
    }
    
    //to check if everyone has passed on the active round
    private boolean everyoneHasNotPassed(){ 
        for( Player ply : players )
            if( ply.hasPassed() == false )
                return true;
        
        return false;
    }
    
   //check if game mode is there or not
    public boolean isGameMode() {
        return game_mode;
    }
}