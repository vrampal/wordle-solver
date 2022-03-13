package vrampal.wordle.secretguesser;

import java.security.SecureRandom;
import java.util.Random;

import lombok.Setter;
import vrampal.wordle.Board;
import vrampal.wordle.Dictionary;
import vrampal.wordle.SecretGuesser;

public class RandomSecretGuesser implements SecretGuesser {

  protected final Random rand = new SecureRandom();
  
  @Setter
  private Board board;

  @Override
  public void play(int turnIdx) {
    String word = selectRandom();
    board.recordGuess(turnIdx, word);
  }

  protected final String selectRandom() {
    Dictionary dict = board.getDict();
    int index = rand.nextInt(dict.size());
    return dict.fromIndex(index);
  }

}
