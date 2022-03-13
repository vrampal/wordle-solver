package vrampal.wordle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Board {

  private static final boolean SAFETY_CHECK = true;

  @Getter
  private final Dictionary dict;

  @Getter
  private final int gameLength;

  @Getter
  private final String[] guesses;

  @Getter
  private final Hint[] hints;

  private int currentTurnIdx = 0;

  private String secretWord = null;

  public Board(Dictionary dict, int gameLength) {
    this.dict = dict;
    this.gameLength = gameLength;
    guesses = new String[gameLength];
    hints = new Hint[gameLength];
  }

  public void recordSecret(String secretWord) {
    if (SAFETY_CHECK && !dict.contains(secretWord)) {
      throw new WordleException("Secret word is not in dictionary");
    }
    if (SAFETY_CHECK && (this.secretWord != null)) {
      throw new WordleException("Secret word already set");
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
    if (SAFETY_CHECK && (this.secretWord == null)) {
      throw new WordleException("Secret has not been set yet");
    }
    guesses[turnIdx] = guess;
    currentTurnIdx++;
    log.info("SecretGuesser proposed {}", guess);
  }

  boolean checkGuess(int turnIdx) {
    String guess = guesses[turnIdx];
    Hint hint = new Hint(secretWord, guess);
    hints[turnIdx] = hint;
    log.info("Get {}", hint);
    return hint.isVictory();
  }

}
