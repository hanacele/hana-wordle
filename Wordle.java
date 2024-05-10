import java.util.*;
import java.io.*;
import java.util.Random;

public class Wordle  {
    public static final String GREEN = "🟩";
    public static final String YELLOW = "🟨";
    public static final String GRAY = "⬜";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to Wordle. Guess the 5 letter word.");

        int wordLength = 5;

        List<String> contents = loadFile(new Scanner(new File("dictionary.txt")));
        String answer = chooseWord(contents);

        List<String> guessedPatterns = new ArrayList<>();
        while (!isFinished(guessedPatterns)) {
            System.out.print("> ");
            String guess = console.next();
            String pattern = recordGuess(guess, answer, wordLength);
            guessedPatterns.add(pattern);
            System.out.println(": " + pattern);
            System.out.println();
        }
        System.out.println("Nice");
        System.out.println();
        printPatterns(guessedPatterns);
    }

    // Prints out the given list of patterns.
    // - List<String> patterns: list of patterns from the game
    public static void printPatterns(List<String> patterns) {
        for (String pattern : patterns) {
            System.out.println(pattern);
        }
    }

    // Returns true if the game is finished, meaning the user guessed the word. Returns
    // false otherwise.
    // - List<String> patterns: list of patterns from the game
    public static boolean isFinished(List<String> patterns) {
        if (patterns.isEmpty()) {
            return false;
        }
        String lastPattern = patterns.get(patterns.size() - 1);
        return !lastPattern.contains("⬜") && !lastPattern.contains("🟨");
    }

    // Loads the contents of a given file Scanner into a List<String> and returns it.
    // - Scanner dictScan: contains file contents
    public static List<String> loadFile(Scanner dictScan) {
        List<String> contents = new ArrayList<>();
        while (dictScan.hasNext()) {
            contents.add(dictScan.next());
        }
        return contents;
    }

    // Chooses a random word from dictionary
    // List<String> contents - the String list of all the words from selected dictionary 
    // int wordLength - user's desired word length
    // Returns:
    // String answer - a random 5 letter word that represents the answer
    public static String chooseWord(List<String> contents) {
        Random rand = new Random();
        int randNum = rand.nextInt(contents.size()) + 1;
        String answer = contents.get(randNum);
        return answer;
    }

    // arranges all possible patterns from guessed word into a nested collection
    // that contains the patterns as keys and the corresponding words from dictionary as values
    // & updates Set<String> words to contain largest quantity of words possible from guess 
    // String guess - user's guess from console
    // Set<String> words - current set of words after pruning
    // int wordLength - the user's specified word length
    // Returns: 
    // String bestPattern - the next best pattern from user's guess
    // Exceptions:
    // IllegalArgumentException - when the length of user's guess is not equal to specified
    //  word length
    public static String recordGuess(String guess, String answer, int wordLength) {
        if (guess.length() != wordLength) {
            throw new IllegalArgumentException();
        }

        String pattern = patternFor(answer, guess);
        return pattern;
    }

    // helper method that generates the corresponding Wordle square patterns 
    //  for a given guess
    //  String word - the current word from dictionary
    //  Stirng guess - the user's current guess
    // Return:
    //  String pattern - the Wordle pattern corresponding to user's guess 
    public static String patternFor(String word, String guess) {
        List<String> guessLetters = new ArrayList<>();
        Map<Character, Integer> answerChars = new TreeMap<>();

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (!answerChars.containsKey(letter)) {
                answerChars.put(letter, 0);
            }
            answerChars.put(letter, answerChars.get(letter) + 1);
        }

        for (int i = 0; i < guess.length(); i++) {
            String letter = "" + guess.charAt(i);
            guessLetters.add(letter);
        }

        for (int i = 0; i < word.length(); i++) {
            String answerLetter = "" + word.charAt(i);
            if (answerLetter.equals(guessLetters.get(i))) {
                guessLetters.set(i, GREEN);
                char letter = word.charAt(i);
                answerChars.put(letter, answerChars.get(letter) - 1);
            }
        }

        for (int i = 0; i < guessLetters.size(); i++) {
            String letter = guessLetters.get(i);
            char charLetter = letter.charAt(0);
            for (char curr : answerChars.keySet()) {
                if (answerChars.get(curr) != 0) {
                    if (charLetter == curr) {
                        answerChars.put(curr, answerChars.get(curr) - 1);
                        guessLetters.set(i, YELLOW);
                    }
                }
            }
        }

        for (int i = 0; i < guessLetters.size(); i++) {
            if (!guessLetters.get(i).equals(GREEN) && !guessLetters.get(i).equals
                (YELLOW)) {
                guessLetters.set(i, GRAY);
            }
        }

        String pattern = ""; 
        for (int i = 0; i < guessLetters.size(); i++) {
            pattern += guessLetters.get(i);
        }

        return pattern;
    }
}