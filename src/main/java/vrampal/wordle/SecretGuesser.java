package vrampal.wordle;

public interface SecretGuesser {

  void setBoard(Board board);

  void play(int turnIdx);

}
