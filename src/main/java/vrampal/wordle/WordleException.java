package vrampal.wordle;

public class WordleException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  WordleException(String message) {
    super(message);
  }

  WordleException(String message, Throwable cause) {
    super(message, cause);
  }

}
