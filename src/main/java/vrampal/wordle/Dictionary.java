package vrampal.wordle;

import java.util.HashSet;
import java.util.Set;

public class Dictionary {
  
  private final int wordLength;
  
  private final Set<String> validWords = new HashSet<>();
  
  public Dictionary(int wordLength) {
    this.wordLength = wordLength;
  }
  

}
