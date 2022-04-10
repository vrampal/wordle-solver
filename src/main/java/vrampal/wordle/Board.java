package vrampal.wordle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class Board {

  private static final boolean SAFETY_CHECK = true;

  @Getter
  private final Dictionary dict;

  @Getter
  private final int gameLength;

  private final String[] guesses;

  private final Hint[] hints;

  @Getter
  private int currentTurnIdx = 0;

  private String secretWord = null;

  public Board(Dictionary dict, int gameLength) {
    this.dict = dict;
    this.gameLength = gameLength;
    guesses = new String[gameLength];
    hints = new Hint[gameLength];
  }
  
  public String getGuess(int turnIdx) {
    return guesses[turnIdx];
  }
  
  public Hint getHint(int turnIdx) {
    return hints[turnIdx];
  }

  public void recordSecret(String secretWord) {
    if (SAFETY_CHECK && (currentTurnIdx != 0)) {
      throw new WordleException("Secret cannot be changed after start");
    }
    if (SAFETY_CHECK && !dict.contains(secretWord)) {
      throw new WordleException("Secret word is not in dictionary");
    }
    this.secretWord = secretWord;
    log.info("SecretMaker choose {}", secretWord);
  }

  public void recordGuess(int turnIdx, String guess) {
    if (SAFETY_CHECK && (turnIdx != currentTurnIdx)) {
      throw new WordleException("Invalid turnIdx");
    }
    if (SAFETY_CHECK && !dict.contains(guess)) {
      throw new WordleException("Guess is not in dictionary");
    }
    guesses[turnIdx] = guess;
    log.info("SecretGuesser proposed {}", guess);
  }
  
  boolean recordHint(int turnIdx, Hint hint) {
    hints[turnIdx] = hint;
    currentTurnIdx++;
    log.info("Get {}", hint);
    return hint.isVictory();
  }

  boolean checkGuessFromSecret(int turnIdx) {
    String guess = guesses[turnIdx];
    Hint hint = new Hint(secretWord, guess);
    return recordHint(turnIdx, hint);
  }

}
