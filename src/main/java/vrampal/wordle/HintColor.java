package vrampal.wordle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum HintColor {
  
  GRAY("a"),
  YELLOW("y"),
  GREEN("g");

  public final String letter;

  static HintColor fromLetter(String letter) {
    for (HintColor val : values()) {
      if (val.letter.equals(letter)) {
        return val;
      }
    }
    return null;
  }

}
