package vrampal.wordle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Dictionary {
  
  private static final Pattern ONLY_LETTERS = Pattern.compile("[a-zA-Z]+");
  
  @Getter
  private final int wordLength;
  
  private final Set<String> contentAsSet = new HashSet<>();
  
  private final List<String> contentAsList = new ArrayList<>();
  
  public int size() {
    return contentAsSet.size();
  }
  
  public boolean contains(String word) {
    return contentAsSet.contains(word);
  }
  
  public String fromIndex(int index) {
    return contentAsList.get(index);
  }
  
  boolean isValid(String word) {
    return (word.length() == wordLength) && ONLY_LETTERS.matcher(word).matches();
  }

  public void loadFile(String filename) throws IOException {
    try (Reader in = new FileReader(new File(filename))) {
      load(in);
    }
  }

  void load(Reader in) throws IOException {
    int loadedWords = 0;
    int skippedWords = 0;
    try (BufferedReader buff = new BufferedReader(in)) {
      String line = buff.readLine();
      while (line != null) {
        if (isValid(line)) {
          boolean modified = contentAsSet.add(line);
          if (modified) {
            contentAsList.add(line);
            loadedWords++;
          } else {
            skippedWords++;
          }
        } else {
          skippedWords++;
        }
        line = buff.readLine();
      }
    }
    log.info("Loaded {} words, skipped {} words", loadedWords, skippedWords);
  }

}
