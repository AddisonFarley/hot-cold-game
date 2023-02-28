//Author: Addison Farley
//Class: SDEV 219
//Program: Hot and Cold Game

import java.util.*;

public class Main
{
    //class fields
    private static final Scanner input = new Scanner(System.in);
    private static boolean gameOver = false;
    private static int totalMatches;
    private static int totalGuesses;
    private static int lowestGuesses;
    private static int highestGuesses;
    private static int matchGuesses;

    public static void main(String[] args)
    {
        printRules();
        showMenu();
    }

    private static void printRules()
    {
        //prints the rules of the game
        System.out.println("""

                Welcome to the Hot/Cold game!
                *****************************
                Game Rules: a match starts by picking a random number\s
                between a low and high value. The player then tries to\s
                guess the number. The game will respond with adjectives\s
                such as: ice cold, cold, warm, hot, very hot! The hotter the\s
                adjective the closer the guess was to the right answer,
                according to the following table:

                *****************************
                extremely hot - 1 away from the target number
                very hot - 2 away from the target number
                hot - 3-5 away from the target number
                warm - 6-10 away from the target number
                cold - 11-30 away from the target number
                ice cold - more than 30 away from the target number
                *****************************
                """);
    }

    private static void showMenu()
    {
        //this method acts as the "backbone" of the game
        //since this is in a do while loop, a choice will run completely through when called on
        //selecting the game over choice will set the boolean gameOver to true and terminate the program
        int menuChoice;

        do
        {
            menuChoice = getMenuChoice();

            switch (menuChoice) {
                case 1 -> playMatch();
                case 2 -> showStatistics();
                case 3 -> gameOver = true;
            }
        }
        while(!gameOver);
    }

    private static int getMenuChoice()
    {
        //prints out the menu choices and asks user for input
        //catches user input that is not an integer and will ask for an int
        System.out.print("""
                Use the menu below to start the game.
                1. Start a new match of Hot/Cold game
                2. Print match statistics
                3. Exit""");

        boolean validChoice = false;
        int userMenuNum = 0;

        do
        {
            System.out.print("\nMake a choice: ");

            try // https://stackoverflow.com/questions/16816250/java-inputmismatchexception
            //I tried a few different ways to get hasNextInt() to work, but it kept throwing an InputMismatchException
            //stack overflow was used to get an idea for how others would handle an InputMismatchException
            {
                int userMenuChoice = input.nextInt();

                if(userMenuChoice == 1 || userMenuChoice == 2 || userMenuChoice == 3)
                {
                    validChoice = true;
                    userMenuNum = userMenuChoice;
                }
                else
                {
                    System.out.println("\nNot a valid option!");
                }
            }

            catch(InputMismatchException e)
            {
                System.out.println("\nNot a valid option!");
                input.nextLine();
            }

        }
        while(!validChoice);

        return userMenuNum;
    }

    private static void playMatch()
    {
        //this method is the game portion of this program
        //variables are declared up top to be used throughout the different loops
        boolean answerFound = false;
        boolean validInputs = false;
        int lowVal;
        int highVal;
        int val1 = 0;
        int val2 = 0;

        //this loop asks the user for the low and high numbers and will generate a random target number
        //the program will inform you to use integers if there is a non-integer input
        //for this do-while loop to break, both val1 and val2 must have integer inputs
        do
        {
            try
            {
                System.out.print("\nEnter a low value for the match: ");
                val1 = input.nextInt();
                System.out.print("\nEnter a high value for the match: ");
                val2 = input.nextInt();
                validInputs = true;
            }
            catch(InputMismatchException e)
            {
                System.out.println("\nPlease enter a number");
                input.nextLine();
            }
        }
        while(!validInputs);

        //determines which value is lowest in case of user input error
        if(val1 < val2)
        {
            System.out.println("\nGenerating a number between: " + val1 + "-" + val2);
            lowVal = val1;
            highVal = val2;
        }
        else if (val1 > val2)
        {
            System.out.println("\nGenerating a number between: " + val2 + "-" + val1);
            lowVal = val2;
            highVal = val1;
        }
        else
        {
            System.out.println("\nGenerating a number between: " + val1 + "-" + val2);
            lowVal = val1;
            highVal = val2;
        }

        //sets the target number for the match
        int target = randomBetween(lowVal, highVal);

        //this loop applies the rules for the game
        //it listens for user input and will tell you how far you are from the target number
        //it also catches non-integer input
        //matchGuesses and totalGuesses are tracked for game statistics
        do
        {
            try
            {
                System.out.print("\nGuess: ");
                int guess = input.nextInt();

                if(guess > target + 30 || guess < target - 30)
                {
                    System.out.println("Ice cold!");
                }
                else if((guess >= target + 11 && guess <= target + 30) || (guess <= target - 11 && guess >= target - 30))
                {
                    System.out.println("Cold!");
                }
                else if((guess >= target + 6 && guess <= target + 10) || (guess <= target - 6 && guess >= target - 10))
                {
                    System.out.println("Warm!");
                }
                else if((guess >= target + 3 && guess <= target + 5) || (guess <= target - 3 && guess >= target - 5))
                {
                    System.out.println("Hot!");
                }
                else if(guess == target + 2 || guess == target - 2)
                {
                    System.out.println("Very hot!");
                }
                else if(guess == target + 1 || guess == target - 1)
                {
                    System.out.println("Extremely hot!");
                }
                else
                {
                    System.out.println("\nCongratulations, you found the number: " + target + "!\n");
                    answerFound = true;
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println("\nPlease enter a number!");
                input.nextLine();
            }

            matchGuesses += 1;
            totalGuesses += 1;
        }
        while(!answerFound);

        //statistics are tallied here
        //because totalGuesses are a class object, its value will not reset once this method finishes like matchGuesses
        totalMatches += 1;

        if(lowestGuesses == 0)
        {
            lowestGuesses = matchGuesses;
            highestGuesses = matchGuesses;
        }
        else if(matchGuesses < lowestGuesses)
        {
            lowestGuesses = matchGuesses;
        }
        else if(matchGuesses > highestGuesses)
        {
            highestGuesses = matchGuesses;
        }
    }

    private static void showStatistics()
    {
        //prints out game statistics if there is at least 1 match played
        if(totalMatches > 0)
        {
            System.out.println("\nTotal matches: " + totalMatches + "\nTotal guesses across matches: " + totalGuesses +
                    "\nLowest guesses in a match: " + lowestGuesses + "\nHighest guesses in a match: " +
                    highestGuesses + "\n");
        }
        else
        {
            System.out.println("\nYou must play a game before statistics are shown!\n");
        }
    }

    private static int randomBetween(int low, int high)
    {
        //generates a random number to be used as the target number
        Random num = new Random();
        return num.nextInt(high - low +1) + low;
    }
}