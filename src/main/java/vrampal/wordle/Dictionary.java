package vrampal.wordle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
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
  
  private final List<String> content = new ArrayList<>();
  
  public boolean isValid(String word) {
    return (word.length() == wordLength) && ONLY_LETTERS.matcher(word).matches();
  }

  public boolean contains(String word) {
    return content.contains(word);
  }
  
  public void loadFile(String filename) throws IOException {
    try (Reader in = new FileReader(new File(filename))) {
      load(in);
    }
  }

  void load(Reader in) throws IOException {
    int validLines = 0;
    int invalidLines = 0;
    try (BufferedReader buff = new BufferedReader(in)) {
      String line = buff.readLine();
      while (line != null) {
        if (isValid(line)) {
          content.add(line);
          validLines++;
        } else {
          invalidLines++;
        }
        line = buff.readLine();
      }
    }
    log.info("Loaded {} lines, skipped {} lines", validLines, invalidLines);
  }

}
