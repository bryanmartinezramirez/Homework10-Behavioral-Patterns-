package com.example;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class User implements IterableByUser {

    private final String name;
    private final ChatServer server;
    private final ChatHistory history = new ChatHistory();

    private final Deque<MessageMemento> sentSnapshots =
        new ArrayDeque<>();

    public User(String name, ChatServer server) {

        if (name == null || name.isBlank() || server == null) {
            throw new IllegalArgumentException(
                "Name and chat server are required");
        }

        this.name = name;
        this.server = server;
    }

    public String getName() {
        return name;
    }

    public ChatHistory getHistory() {
        return history;
    }

    public Message sendMessage(User recipient, String text) {
        return sendMessage(List.of(recipient), text);
    }

    public Message sendMessage(List<User> recipients, String text) {
        return server.sendMessage(this, recipients, text);
    }

    void recordSentMessage(Message message) {
        history.addMessage(message);
        sentSnapshots.push(new MessageMemento(message));
    }

    void receiveMessage(Message message) {
        history.addMessage(message);
    }

    public boolean undoLastMessage() {

        if (sentSnapshots.isEmpty()) {
            return false;
        }

        boolean undone =
            server.undoMessage(this, sentSnapshots.peek());

        if (undone) {
            sentSnapshots.pop();
        }

        return undone;
    }

    public void blockUser(User senderToBlock) {
        server.blockUser(this, senderToBlock);
    }

    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return history.iterator(userToSearchWith);
    }
}
