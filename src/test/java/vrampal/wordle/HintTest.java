package vrampal.wordle;

import static org.junit.Assert.*;

import org.junit.Test;

public class HintTest {

  @Test
  public void testHint1() {
    Hint hint = new Hint("abcde", "abcde");
    for (HintColor color : hint.getColors()) {
      assertEquals(HintColor.GREEN, color);
    }
    assertTrue(hint.isVictory());
  }

  @Test
  public void testHint2() {
    Hint hint = new Hint("abcde", "eabcd");
    for (HintColor color : hint.getColors()) {
      assertEquals(HintColor.YELLOW, color);
    }
    assertFalse(hint.isVictory());
  }

  @Test
  public void testHint3() {
    Hint hint = new Hint("abcde", "fghij");
    for (HintColor color : hint.getColors()) {
      assertEquals(HintColor.GRAY, color);
    }
    assertFalse(hint.isVictory());
  }

  @Test
  public void testHint4() {
    Hint hint = new Hint("abcde", "abicj");
    assertEquals(HintColor.GREEN, hint.getColors()[0]);
    assertEquals(HintColor.GREEN, hint.getColors()[1]);
    assertEquals(HintColor.GRAY, hint.getColors()[2]);
    assertEquals(HintColor.YELLOW, hint.getColors()[3]);
    assertEquals(HintColor.GRAY, hint.getColors()[4]);
    assertFalse(hint.isVictory());
  }

  @Test
  public void testHint5() {
    Hint hint = new Hint("abcde", "iaaaa");
    assertEquals(HintColor.GRAY, hint.getColors()[0]);
    assertEquals(HintColor.YELLOW, hint.getColors()[1]);
    assertEquals(HintColor.GRAY, hint.getColors()[2]);
    assertEquals(HintColor.GRAY, hint.getColors()[3]);
    assertEquals(HintColor.GRAY, hint.getColors()[4]);
    assertFalse(hint.isVictory());
  }

}
