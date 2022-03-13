package vrampal.wordle;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import vrampal.wordle.secretguesser.EntropicSecretGuesser;
import vrampal.wordle.secretmaker.RandomSecretMaker;

@Slf4j
public class WordleApp {
  
  public static void main(String[] args) throws IOException {
    new WordleApp().playOneGame();
  }

  public void playOneGame() throws IOException {
    log.info("Starting new game");
    
    Dictionary dict = new Dictionary(5); // 5 letters words
    dict.loadFile("data/wordle-dictionnary.csv");
    
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
      found = board.checkGuess(turnIdx);
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

}
