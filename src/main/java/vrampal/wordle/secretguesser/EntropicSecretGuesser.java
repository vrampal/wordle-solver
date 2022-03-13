package vrampal.wordle.secretguesser;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import vrampal.wordle.Hint;

@Slf4j
public class EntropicSecretGuesser extends BruteForceSecretGuesser {

  static final class MutableCounter {
    int value;
  }
  
  private static final double LOG2 = Math.log(2.0d);
  
  @Override
  public void play(int turnIdx) {
    String hypothesis;
    if (turnIdx == 0) {
      hypothesis = selectRandom();
    } else {
      hypothesis = compute(turnIdx);
    }
    board.recordGuess(turnIdx, hypothesis);
  }
  
  String compute(int turnIdx) {
    updatePossibleSecrets(turnIdx);
    
    int possibleSecretsSize = possibleSecrets.size();
    if (possibleSecretsSize == 1) {
      return possibleSecrets.get(0);
    }
    
    double remainingEntropy = 0.0d;
    if (log.isDebugEnabled()) {
      remainingEntropy = log2(possibleSecretsSize);
      log.debug("Remaining entropy: {}", remainingEntropy);
    }

    // Find the hypothesis with highest entropy
    double bestEntropy = -1.0d;
    String bestHypothesis = null;
    for(String hypothesis : possibleSecrets) {
      double entropy = computeEntropy(hypothesis);
      if (bestEntropy < entropy) {
        bestEntropy = entropy;
        bestHypothesis = hypothesis;
      }
    }
    if (log.isDebugEnabled()) {
      log.debug("Best entropy: {}", bestEntropy);
      double expectedEntropy = remainingEntropy - bestEntropy;
      log.debug("Expected entropy: {}", expectedEntropy);
    }
    return bestHypothesis;
  }
  
  private double computeEntropy(String secret) {
    Map<Hint, MutableCounter> possibleOutcomes = new HashMap<>();
    
    for(String hypothesis : possibleSecrets) {
      Hint hint = new Hint(secret, hypothesis);
      MutableCounter count;
      if (!possibleOutcomes.containsKey(hint)) {
        count = new MutableCounter();
        possibleOutcomes.put(hint, count);
      } else {
        count = possibleOutcomes.get(hint);
      }
      count.value++;
    }
    
    double entropy = 0.0d;
    double possibleSecretsSize = possibleSecrets.size();
    for(MutableCounter counter : possibleOutcomes.values()) {
      double proba = counter.value / possibleSecretsSize;
      entropy += proba * log2(1.0d / proba);
    }
    
    return entropy;
  }

  private static double log2(double value) {
    return Math.log(value) / LOG2;
  }
  
}
