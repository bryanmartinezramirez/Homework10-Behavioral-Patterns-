package com.example;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatHistoryTest {

    @Test
    void testChatHistoryMethods() {

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);

        ChatHistory history = new ChatHistory();

        Message sent =
            new Message(alice, List.of(bob), "Outbox");

        Message received =
            new Message(bob, List.of(alice), "Inbox");

        history.addMessage(sent);
        history.addMessage(received);

        assertEquals(2, history.getMessages().size());

        assertSame(
            sent, history.getLastMessageSent(alice));

        assertSame(
            received, history.getLastMessageSent(bob));

        assertTrue(history.removeMessage(sent.getId()));

        assertFalse(history.removeMessage(sent.getId()));

        assertNull(history.getLastMessageSent(alice));

        assertEquals(1, history.getMessages().size());
    }
}
