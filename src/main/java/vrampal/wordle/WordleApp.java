package vrampal.wordle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;
import vrampal.wordle.secretguesser.EntropicSecretGuesser;
import vrampal.wordle.secretmaker.RandomSecretMaker;

@Slf4j
public class WordleApp {
  
  public static final int LETTER_COUNT = 5; // 5 letters words
  
  public static void main(String[] args) throws IOException {
    new WordleApp().solveExternalGame();
  }
  
  Dictionary loadDict() throws IOException {
    Dictionary dict = new Dictionary(LETTER_COUNT);
    dict.loadFile("data/worde-answers.csv");
    dict.loadFile("data/wordle-dictionnary.csv");
    return dict;
  }

  public void playOneGame() throws IOException {
    log.info("Starting new game");
    
    Dictionary dict = loadDict();
    Board board = new Board(dict, 6); // 6 turns to guess
    
    SecretMaker secretMaker = new RandomSecretMaker();
    secretMaker.setBoard(board);
    secretMaker.selectSecret();
    
    SecretGuesser secretGuesser;
    // secretGuesser = new BruteForceSecretGuesser();
    secretGuesser = new EntropicSecretGuesser();
    secretGuesser.setBoard(board);
    
    int turnIdx = 0;
    boolean found = false;
    while ((turnIdx < board.getGameLength()) && !found) {
      secretGuesser.play(turnIdx);
      found = board.checkGuessFromSecret(turnIdx);
      turnIdx++;
    }
    
    String winner;
    if (found) {
      winner = "SecretBreaker";
    } else {
      winner = "SecretMaker";
    }
    log.info("{} win, {} turns", winner, turnIdx);
  }
  
  public void solveExternalGame() throws IOException {
    log.info("Starting new game");
    
    Dictionary dict = loadDict();
    Board board = new Board(dict, 6); // 6 turns to guess
    
    SecretGuesser secretGuesser;
    // secretGuesser = new BruteForceSecretGuesser();
    secretGuesser = new EntropicSecretGuesser();
    secretGuesser.setBoard(board);
    
    int turnIdx = 0;
    boolean found = false;
    while ((turnIdx < board.getGameLength()) && !found) {
      secretGuesser.play(turnIdx);
      Hint hint = readHintFromConsole();
      found = board.recordHint(turnIdx, hint);
      turnIdx++;
    }
    
    String winner;
    if (found) {
      winner = "SecretBreaker";
    } else {
      winner = "SecretMaker";
    }
    log.info("{} win, {} turns", winner, turnIdx);
  }
  
  Hint readHintFromConsole() throws IOException {
    Hint hint = null;
    boolean valid = false;
    do {
      System.out.println("Enter hints (GREEN = g, YELLOW = y, GRAY = a)");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String line = reader.readLine();
      String[] elem = line.split(" ");
      if (elem.length == LETTER_COUNT) {
        valid = true;
        HintColor[] colors = new HintColor[LETTER_COUNT];
        for (int index = 0; index < LETTER_COUNT; index++) {
          HintColor color = HintColor.fromLetter(elem[index]);
          if (color == null) {
            valid = false;
          }
          colors[index] = color;
        }
        hint = new Hint(colors, false);
      }
    } while(!valid);
    return hint;
  }

}
