package com.example.consolecardgame.utility;
import java.io.IOException;
import java.util.Scanner;

/*
Utility class. 
Used through out the program. 
*/
public final class Util {
    
    /*
    Prints a message out with a prefix.
    msg is the message
    args are the arguments part of the message
    */
    public static final void print(String msg, Object... args){
        System.out.printf( "[CARD GAME] " + msg + "\n", args );
    }
    
    /*
    Prints a message out with a debug prefix.
    msg is the message
    args are the arguments part of the message
    */
    public static final void printDebug(String msg, Object... args){
        System.out.printf( "[DEBUG] " + msg + "\n", args );
    }
    
    /*
    Prints a message out with a error prefix.
    msg is the message
    args are the arguments part of the message
    */
    public static final void printError(String msg, Object... args){
        System.out.printf( "[ERROR] " + msg + "\n", args);
    }
    
    /*
    Prints a message out with a empty style.
    msg is the message
    */
    public static final void printEmptyMessage( String msg ){
        System.out.println("<<< [" + msg + "] >>>");
    }
    
    /*
    Prints a message out with a separator style.
    msg is the message
    */
    public static final void printSeparator( String msg ){
        System.out.println("==================[" + msg + "]==================");
    }
    
    /*
    Prints a message out with a second separator style.
    msg is the message
    */
    public static final void printSeparator2( String msg ){
        System.out.println(">>>>>>>>>>>>>>> [" + msg + "] <<<<<<<<<<<<<<<<");
    }
     
    /*
    Prints a message out with a box style
    msg is the message
    args are  the arguments part of the message
    */
    public static void printInBox(String msg, Object... args) {
        String border = "";
        StringBuilder sbborder = new StringBuilder(border);
        for( int i = 0; i < msg.length(); i++ )
            sbborder.append("|");
        
        for( Object ob : args ) {
            String str;
            try {
                str = (String)ob;
            }catch(ClassCastException ex) { //  I know, cheeky. In this program, it's always going to be a int.
                str = Integer.toString((Integer)ob);
            }
            for( int i = 0; i < str.length(); i++ )
                sbborder.append("|");
        }
        
        border = sbborder.toString();
        
        System.out.println("||"+border+"||");
        System.out.printf("||| " + msg + " |||\n", args);
        System.out.println("||"+border+"||");
    }
    
  
    /*
    prompts the user before clearing console
    */
    public static void clearConsoleConfirm(Scanner in){
        Util.print("Press ENTER to continue...");
        in.nextLine();
        clearConsole();
    }
    
    /*
    Clears console.
    */
    public static void clearConsole(){
        //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime();
        } catch (IOException | InterruptedException ex) {}
    }
    
    /*
    Transforms a string to a object.
    value is the string value
    Get the object
    */
    private static Object stringToObject( String value ) {
        Object parsed_value = null;
        try{
            double isNum = Double.parseDouble(value);
            if(isNum == Math.floor(isNum)) {
                parsed_value = Integer.parseInt(value);
            }else {
                parsed_value = Double.parseDouble(value);
            }
        } catch(Exception e) {
            if(value.toCharArray().length == 1) {
                parsed_value = value.charAt(0);
            }else {
                if( "true".equals(value) ){
                    parsed_value = true;
                }else if( "false".equals(value) ){
                    parsed_value = false;
                }else if( "null".equals(value) ){
                    parsed_value = null;
                }else{
                    parsed_value = value;
                }
            }
        }      
        return parsed_value;
    }
    
    /*
    Prompts the user for input validation which will be compared with the values passed in the array.
    msg is the message to show before prompt
    expected_values An array expected values that will be compared with the user input.
    Get parsed value
    */
    public static final Object promptInputValidationByValue( String msg, Scanner in, Object[] expected_values ){             
        Util.print("[PROMPT] " + msg);
        String value = in.nextLine().trim();
        Object parsed_value = stringToObject( value );
        
        boolean found = false;
        for( Object val : expected_values ){
            if( parsed_value.getClass().equals(val.getClass()) && parsed_value.equals(val) ){ // Avoid tricking java
                found = true;
                break;
            }
        }
        
        if( !found ){
            Util.printError("Error while parsing input! Please try again.");
            return promptInputValidationByValue(msg, in, expected_values);
        }
        
        return parsed_value;
    }
    
    /*
    A method used to help make things tidy. Prompts the user for input validation which will be compared with the class passed.
    msg is the message to show before prompt
    cl is the class to compare the input with.
    Get parsed value
    */
    public static final Object promptInputValidationByClass( String msg, Scanner in, Class<?> cl ){             
        Util.print("[PROMPT] " + msg);
        String value = in.nextLine().trim();
        Object parsed_value = stringToObject( value );
        
        if( !parsed_value.getClass().equals(cl) ){
            Util.printError("Error while parsing input! Please try again.");
            return promptInputValidationByClass(msg, in, cl);
        }
         
        return parsed_value;
    }
    
    /*
    A method used to help make things tidy. Prompts the user for input validation which will be compared with a integer range.
    msg is the message to show before prompt
    min is the minimum value of the range
    max is the maximum value of the range
    Get parsed value
    */
    public static final Object promptInputValidationByRange( String msg, Scanner in, int min, int max ){             
        Util.print("[PROMPT] " + msg);
        String value = in.nextLine().trim();
        int rvalue = -1;
        
        try{
            rvalue = Integer.parseInt(value);
        }catch( NumberFormatException e ){
            Util.printError("Your input was not a number!");
            return promptInputValidationByRange(msg, in, min, max);
        }
        
        if( rvalue < min || rvalue > max ){
            Util.printError("Your input: %d was not in the range of %d <= input <= %d", rvalue, min, max); 
            return promptInputValidationByRange(msg, in, min, max);
        }
        
        return rvalue;
    }
}
