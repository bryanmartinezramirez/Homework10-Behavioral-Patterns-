package com.example;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserMessaging() {

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(charlie);

        assertEquals("Alice", alice.getName());

        Message one =
            alice.sendMessage(bob, "Hello Bob");

        Message group =
            alice.sendMessage(
                List.of(bob, charlie), "Hello everyone");

        assertEquals(
            2, alice.getHistory().getMessages().size());

        assertSame(
            one, bob.getHistory().getMessages().get(0));

        assertSame(
            group, charlie.getHistory().getMessages().get(0));
    }

    @Test
    void testUndoLastSentMessage() {

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);

        server.registerUser(alice);
        server.registerUser(bob);

        alice.sendMessage(bob, "First sent");
        alice.sendMessage(bob, "Second sent");

        bob.sendMessage(alice, "Later received");

        assertTrue(alice.undoLastMessage());

        assertEquals(
            2, alice.getHistory().getMessages().size());

        assertEquals(
            "First sent",
            alice.getHistory()
                .getLastMessageSent(alice).getContent());

        assertFalse(
            bob.getHistory().getMessages().stream()
                .anyMatch(message ->
                    message.getContent().equals("Second sent")));

        assertTrue(alice.undoLastMessage());
        assertFalse(alice.undoLastMessage());
    }

    @Test
    void testUserBlocking() {

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);

        server.registerUser(alice);
        server.registerUser(bob);

        alice.blockUser(bob);

        assertNull(
            bob.sendMessage(alice, "Blocked"));

        assertTrue(
            alice.getHistory().getMessages().isEmpty());
    }
}
