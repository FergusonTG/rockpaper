/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rockpaper;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author tim
 */
public class RockPaper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        
        ScoreBoard.Answer a = ScoreBoard.getAnswer(0,0);
        
        String prompt = 
            IntStream.range(0, ScoreBoard.NAMES.length)
                    .mapToObj( i -> String.valueOf(i+1) + " " + ScoreBoard.NAMES[i])
                    .collect(Collectors.joining("; ", "", "\nEnter a number: "));

        while (a.whoWon == 0) {
            System.out.print(prompt);

            int yourGuessInt = s.nextInt() - 1;
            
            long timestamp = java.time.Instant.now().toEpochMilli();
            int myGuessInt = (int) (timestamp & 0xFFFFL) % 5;
            
            if (yourGuessInt < 0 || yourGuessInt > ScoreBoard.NAMES.length)
                userError();

            a = ScoreBoard.getAnswer(myGuessInt, yourGuessInt);

            System.out.println(a.outputString());
        
            }
        // exit status 0 if user won
        System.exit(a.whoWon == 1 ? 0 : 1);
    }

    private static void userError() {
        System.err.println("Please enter a number in the correct range");
        System.exit(1);
    }
    

    static class ScoreBoard {

        static final String[] NAMES
                = {"Rock", "Paper", "Scissors", "Lizard", "Spock"};

        /**
         * value 1 indicates row wins; 0 indicates column wins
         */
        static final int[] OUTCOMES = {
            1,
            0, 1,
            0, 1, 0,
            1, 0, 1, 0
        };

        static final String[] STRINGS = {
            "Paper wraps Rock",
            "Rock blunts Scissors",
            "Scissors cuts Paper",
            "Rock crushes Lizard",
            "Lizard eats Paper",
            "Scissors chops Lizard",
            "Spock throws Rock",
            "Paper disproves Spock",
            "Spock bends Scissors",
            "Lizard poisons Spock"
        };

        /**
         * getAnswer: turn a pair of guesses into a result
         *
         * @param a computer guess
         * @param b user guess
         *
         * return -1 if computer wins, 0 draw, _1 user win
         */
        static Answer getAnswer(int a, int b) {
            Answer ans = null;
            int index;
            if (a < b) {
                index = ((b - 1) * b) / 2 + a;
                // convert 0 to -1, 1 to 1
                int outcome = 2 * OUTCOMES[index] - 1;
                return new Answer(outcome, STRINGS[index]);
            } else if (a > b) {
                index = ((a - 1) * a) / 2 + b;
                // convert 0 to 1, 1 to -1
                int outcome = 1 - 2 * OUTCOMES[index];
                return new Answer(outcome, STRINGS[index]);
            } else {
                return new Answer(0, "Go round again...");
            }
        }

        static class Answer {

            int whoWon;
            String userPrompt;

            public Answer(int whoWon, String userPrompt) {
                this.whoWon = whoWon;
                this.userPrompt = userPrompt;
            }

            public String outputString() {
                String[] s = {"Computer wins", "Draw", "User wins"};
                return s[this.whoWon + 1] + " : " + this.userPrompt;
            }

        }
    }
}
