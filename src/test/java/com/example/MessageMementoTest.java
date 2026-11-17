package com.example;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageMementoTest {

    @Test
    void testMessageSnapshot() {

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);

        Message message =
            new Message(alice, List.of(bob), "Remember me");

        MessageMemento snapshot =
            new MessageMemento(message);

        assertEquals(message.getId(), snapshot.getMessageId());
        assertEquals("Remember me", snapshot.getContent());
        assertEquals(message.getTimestamp(), snapshot.getTimestamp());
    }
}
