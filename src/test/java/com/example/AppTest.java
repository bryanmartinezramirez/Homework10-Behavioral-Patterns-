package com.example;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testBothQuestionsDemonstrated() {

        PrintStream original = System.out;

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));
            App.main(new String[0]);
        } finally {
            System.setOut(original);
        }

        String result = output.toString();

        assertTrue(result.contains(
            "QUESTION 1: MEDIATOR + MEMENTO"));

        assertTrue(result.contains(
            "Alice undo successful: true"));

        assertTrue(result.contains(
            "Bob -> Charlie delivered: false"));

        assertTrue(result.contains(
            "Bob -> Alice delivered: true"));

        assertTrue(result.contains(
            "QUESTION 2: ITERATOR BY USER"));

        assertTrue(result.contains(
            "Matches for Bob: 3"));

        assertTrue(result.contains(
            "Matches for Charlie: 0"));
    }
}
