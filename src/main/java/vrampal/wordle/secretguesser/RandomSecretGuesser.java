package vrampal.wordle.secretguesser;

import java.util.Random;

import vrampal.wordle.Board;
import vrampal.wordle.Dictionary;
import vrampal.wordle.SecretGuesser;

public class RandomSecretGuesser implements SecretGuesser {

  protected final Random rand = new Random();
  
  protected Board board;
  
  protected Dictionary dict;

  @Override
  public void setBoard(Board board) {
    this.board = board;
    this.dict = board.getDict();
  }

  @Override
  public void play(int turnIdx) {
    String word = selectRandom();
    board.recordGuess(turnIdx, word);
  }

  protected final String selectRandom() {
    int index = rand.nextInt(dict.size());
    return dict.fromIndex(index);
  }

}
