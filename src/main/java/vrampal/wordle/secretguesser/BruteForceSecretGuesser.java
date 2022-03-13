package vrampal.wordle.secretguesser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import vrampal.wordle.Hint;

@Slf4j
public class BruteForceSecretGuesser extends RandomSecretGuesser {

  private int bruteForceIndex;

  protected List<String> possibleSecrets;

  @Override
  public void play(int turnIdx) {
    String hypothesis;
    if (turnIdx == 0) {
      hypothesis = selectRandom();
    } else {
      hypothesis = compute3(turnIdx);
    }
    board.recordGuess(turnIdx, hypothesis);
  }

  private String compute1(int turnIdx) {
    String hypothesis = selectRandom();
    boolean possible = checkHypothesisPossible(turnIdx, hypothesis);

    while (!possible) {
      hypothesis = selectRandom();
      possible = checkHypothesisPossible(turnIdx, hypothesis);
    }

    return hypothesis;
  }

  private String compute2(int turnIdx) {
    String hypothesis = dict.fromIndex(bruteForceIndex);
    boolean possible = checkHypothesisPossible(turnIdx, hypothesis);

    while (!possible) {
      bruteForceIndex++;
      hypothesis = dict.fromIndex(bruteForceIndex);
      possible = checkHypothesisPossible(turnIdx, hypothesis);
    }

    return hypothesis;
  }
  
  private String compute3(int turnIdx) {
    updatePossibleSecrets(turnIdx);
    
    // Take a random guess from the valid ones
    int index = rand.nextInt(possibleSecrets.size());
    String hypothesis = possibleSecrets.get(index);

    return hypothesis;
  }

  protected final boolean checkHypothesisPossible(int turnIdx, String hypothesis) {
    for (int prevTurnIdx = 0; prevTurnIdx < turnIdx; prevTurnIdx++) {
      String prevGuess = board.getGuess(prevTurnIdx);
      Hint hint = new Hint(hypothesis, prevGuess);
      Hint prevHint = board.getHint(prevTurnIdx);
      if (!prevHint.equals(hint)) {
        return false;
      }
    }
    return true;
  }

  private void generatePossibleSecrets(int turnIdx) {
    possibleSecrets = new ArrayList<>();

    // Keep only valid words from all words possible
    for (int wordIdx = 0; wordIdx < dict.size(); wordIdx++) {
      String hypothesis = dict.fromIndex(wordIdx);
      if ((turnIdx == 0) || checkHypothesisPossible(turnIdx, hypothesis)) {
        possibleSecrets.add(hypothesis);
      }
    }
  }

  private void filterPossibleSecrets(int turnIdx) {
    Iterator<String> iter = possibleSecrets.iterator();
    while (iter.hasNext()) {
      String hypothesis = iter.next();
      if (!checkHypothesisPossible(turnIdx, hypothesis)) {
        iter.remove();
      }
    }
  }

  protected final void updatePossibleSecrets(int turnIdx) {
    if (possibleSecrets == null) {
      generatePossibleSecrets(turnIdx);
    } else {
      filterPossibleSecrets(turnIdx);
    }
    log.debug("Possible secret: {}", possibleSecrets.size());
  }

}
