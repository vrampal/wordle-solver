package vrampal.wordle.secretmaker;

import java.util.Random;

import vrampal.wordle.Board;
import vrampal.wordle.Dictionary;
import vrampal.wordle.SecretMaker;

public class RandomSecretMaker implements SecretMaker {
  
  protected final Random rand = new Random();
  
  protected Board board;
  
  protected Dictionary dict;

  @Override
  public void setBoard(Board board) {
    this.board = board;
    this.dict = board.getDict();
  }

  @Override
  public void play() {
    String word = selectRandom();
    board.recordSecret(word);
  }
  
  protected final String selectRandom() {
    int index = rand.nextInt(dict.size());
    return dict.fromIndex(index);
  }

}
