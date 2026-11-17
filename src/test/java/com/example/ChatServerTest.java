package com.example;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatServerTest {

    @Test
    void testRegisterAndUnregister() {

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);

        assertFalse(server.isRegistered(alice));

        server.registerUser(alice);

        assertTrue(server.isRegistered(alice));

        assertThrows(IllegalArgumentException.class,
            () -> server.registerUser(alice));

        assertTrue(server.unregisterUser(alice));
        assertFalse(server.unregisterUser(alice));
        assertFalse(server.isRegistered(alice));
    }

    @Test
    void testSendMessageToMultipleUsers() {

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(charlie);

        Message first =
            server.sendMessage(alice, List.of(bob), "Hi");

        Message second =
            server.sendMessage(
                alice, List.of(bob, charlie), "Team");

        assertSame(
            first, bob.getHistory().getMessages().get(0));

        assertEquals(
            2, bob.getHistory().getMessages().size());

        assertEquals(
            1, charlie.getHistory().getMessages().size());

        assertEquals(
            List.of(bob, charlie), second.getRecipients());
    }

    @Test
    void testBlocking() {

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(charlie);

        server.blockUser(bob, alice);

        assertNull(
            server.sendMessage(alice, List.of(bob), "Blocked"));

        Message allowed = server.sendMessage(
            alice, List.of(bob, charlie), "Team");

        assertEquals(
            List.of(charlie), allowed.getRecipients());

        assertEquals(
            0, bob.getHistory().getMessages().size());

        assertEquals(
            1, charlie.getHistory().getMessages().size());
    }

    @Test
    void testUndoMessage() {

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(charlie);

        Message message = server.sendMessage(
            alice, List.of(bob, charlie), "Undo me");

        MessageMemento snapshot =
            new MessageMemento(message);

        assertTrue(
            server.undoMessage(alice, snapshot));

        assertTrue(
            alice.getHistory().getMessages().isEmpty());

        assertTrue(
            bob.getHistory().getMessages().isEmpty());

        assertTrue(
            charlie.getHistory().getMessages().isEmpty());

        assertFalse(
            server.undoMessage(alice, snapshot));
    }

    @Test
    void testUnregisteredSender() {

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);

        server.registerUser(bob);

        assertThrows(IllegalArgumentException.class,
            () -> server.sendMessage(
                alice, List.of(bob), "Hi"));
    }
}
