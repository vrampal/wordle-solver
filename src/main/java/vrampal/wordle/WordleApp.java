package vrampal.wordle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;
import vrampal.wordle.secretguesser.EntropicSecretGuesser;
import vrampal.wordle.secretmaker.RandomSecretMaker;

@Slf4j
public class WordleApp {
  
  private static final boolean EXTERNAL_SOURCE = true;
  
  public static void main(String[] args) throws IOException {
    new WordleApp().playOneGame();
  }
  
  Dictionary loadDictWordle() throws IOException {
    // From https://www.nytimes.com/games/wordle/index.html
    Dictionary dict = new Dictionary(5); // 5 letters words
    dict.loadFile("data/wordle-answers.csv");
    dict.loadFile("data/wordle-dictionnary.csv");
    return dict;
  }

  Dictionary loadDictWordle2() throws IOException {
    // From https://www.wordhurdle.in/
    Dictionary dict = new Dictionary(6); // 6 letters words
    dict.loadFile("data/wordle2-answers.csv");
    dict.loadFile("data/wordle2-dictionnary.csv");
    return dict;
  }

  Dictionary loadDictLeMot() throws IOException {
    // From https://wordle.louan.me/
    Dictionary dict = new Dictionary(5); // 5 letters words
    dict.loadFile("data/lemot-dictionnary.csv");
    dict.loadFile("data/lemot-extra50.csv");
    return dict;
  }

  Dictionary loadDictLeMotAlt5() throws IOException {
    // From https://www.solitaire-play.com/lemot/
    Dictionary dict = new Dictionary(5); // 5 letters words
    dict.loadFile("data/lemot-alt5-dictionnary.csv");
    dict.loadFile("data/lemot-alt5-answers.csv");
    return dict;
  }

  Dictionary loadDictLeMotAlt6() throws IOException {
    // From https://www.solitaire-play.com/lemot6/
    Dictionary dict = new Dictionary(6); // 6 letters words
    dict.loadFile("data/lemot-alt6-dictionnary.csv");
    dict.loadFile("data/lemot-alt6-answers.csv");
    return dict;
  }

  Dictionary loadDictAirportle() throws IOException {
    // From https://airportle.glitch.me/
    Dictionary dict = new Dictionary(3); // 3 letters words
    dict.loadFile("data/airport_codes.txt");
    dict.loadFile("data/airport_answers.txt");
    return dict;
  }
  
  Dictionary loadDictPrimel() throws IOException {
    // From https://converged.yt/primel/
    Dictionary dict = new Dictionary(5); // 5 letters words
    dict.loadFile("data/primel-primes.csv");
    return dict;
  }
  

  public void playOneGame() throws IOException {
    log.info("Starting new game");
    
    Dictionary dict;
    dict = loadDictWordle();
    // dict = loadDictWordle2();
    // dict = loadDictLeMot();
    // dict = loadDictLeMotAlt5();
    // dict = loadDictLeMotAlt6();
    // dict = loadDictAirportle();
    // dict = loadDictPrimel();
    Board board = new Board(dict, 6); // 6 turns to guess
    
    if (!EXTERNAL_SOURCE) {
      SecretMaker secretMaker = new RandomSecretMaker();
      secretMaker.setBoard(board);
      secretMaker.selectSecret();
    }
    
    SecretGuesser secretGuesser;
    // secretGuesser = new BruteForceSecretGuesser();
    secretGuesser = new EntropicSecretGuesser();
    secretGuesser.setBoard(board);
    
    int wordLength = board.getDict().getWordLength();
    int turnIdx = 0;
    boolean found = false;
    while ((turnIdx < board.getGameLength()) && !found) {
      secretGuesser.play(turnIdx);
      if (!EXTERNAL_SOURCE) {
        found = board.checkGuessFromSecret(turnIdx);
      } else {
        Hint hint = readHintFromConsole(wordLength);
        found = board.recordHint(turnIdx, hint);
      }
      turnIdx++;
    }
    
    String winner;
    if (found) {
      winner = "SecretBreaker";
    } else {
      winner = "SecretMaker";
    }
    log.info("{} win, {} turns", winner, turnIdx);
  }
  
  Hint readHintFromConsole(int wordLength) throws IOException {
    Hint hint = null;
    boolean valid = false;
    do {
      System.out.println("Enter hints (GREEN = g, YELLOW = y, GRAY = a)");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String line = reader.readLine();
      String[] elem = line.split(" ");
      if (elem.length == wordLength) {
        valid = true;
        HintColor[] colors = new HintColor[wordLength];
        for (int index = 0; index < wordLength; index++) {
          HintColor color = HintColor.fromLetter(elem[index]);
          if (color == null) {
            valid = false;
          }
          colors[index] = color;
        }
        hint = new Hint(colors, false);
      }
    } while(!valid);
    return hint;
  }

}
