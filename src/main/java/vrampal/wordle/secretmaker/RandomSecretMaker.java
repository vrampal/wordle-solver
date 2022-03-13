package vrampal.wordle.secretmaker;

import java.security.SecureRandom;
import java.util.Random;

import lombok.Setter;
import vrampal.wordle.Board;
import vrampal.wordle.Dictionary;
import vrampal.wordle.SecretMaker;

public class RandomSecretMaker implements SecretMaker {
  
  protected final Random rand = new SecureRandom();
  
  @Setter
  private Board board;

  @Override
  public void play() {
    String word = selectRandom();
    board.recordSecret(word);
  }
  
  protected final String selectRandom() {
    Dictionary dict = board.getDict();
    int index = rand.nextInt(dict.size());
    return dict.fromIndex(index);
  }

}
