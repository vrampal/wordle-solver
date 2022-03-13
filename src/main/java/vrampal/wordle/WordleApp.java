package vrampal.wordle;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import vrampal.wordle.secretguesser.RandomSecretGuesser;
import vrampal.wordle.secretmaker.RandomSecretMaker;

@Slf4j
public class WordleApp {
  
  public static void main(String[] args) throws IOException {
    new WordleApp().playOneGame();
  }

  public void playOneGame() throws IOException {
    log.info("Starting new game");
    
    Dictionary dict = new Dictionary(5);
    dict.loadFile("data/wordle-dictionnary.csv");
    
    Board board = new Board(dict, 6);
    
    SecretMaker secretMaker = new RandomSecretMaker();
    secretMaker.setBoard(board);
    secretMaker.play();
    
    SecretGuesser secretGuesser = new RandomSecretGuesser();
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
    log.info("{} win", winner);
  }

}
