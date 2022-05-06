package vrampal.wordle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(of = {"colors"})
public final class Hint {

  @Getter
  private final HintColor[] colors;

  @Getter
  private final boolean victory;

  public Hint(HintColor[] colors) {
    this.colors = colors;
    boolean vict = true;
    for (HintColor col : colors) {
      if (col != HintColor.GREEN) {
        vict = false;
      }
    }
    victory = vict;
  }

  public Hint(String secret, String candidate) {
    int length = secret.length();
    colors = new HintColor[length];
    boolean[] secretLetterUsed = new boolean[length];
    boolean[] candidateLetterUsed = new boolean[length];

    boolean vict = true;
    for (int idxCand = 0; idxCand < length; idxCand++) {
      if (candidate.charAt(idxCand) == secret.charAt(idxCand)) {
        colors[idxCand] = HintColor.GREEN;
        secretLetterUsed[idxCand] = true;
        candidateLetterUsed[idxCand] = true;
      } else {
        vict = false;
      }
    }
    victory = vict;

    for (int idxCand = 0; idxCand < length; idxCand++) {
      if (!candidateLetterUsed[idxCand]) {
        for (int idxSecr = 0; idxSecr < length; idxSecr++) {
          if (!secretLetterUsed[idxSecr]) {
            if (candidate.charAt(idxCand) == secret.charAt(idxSecr)) {
              colors[idxCand] = HintColor.YELLOW;
              secretLetterUsed[idxSecr] = true;
              candidateLetterUsed[idxCand] = true;
            }
          }
        }
      }
    }

    for (int idxCand = 0; idxCand < length; idxCand++) {
      if (!candidateLetterUsed[idxCand]) {
        colors[idxCand] = HintColor.GRAY;
      }
    }
  }

}
