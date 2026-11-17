package com.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ChatHistory implements IterableByUser {

    private final List<Message> messages = new ArrayList<>();

    public void addMessage(Message message) {
        messages.add(message);
    }

    public Message getLastMessageSent(User sender) {

        for (int i = messages.size() - 1; i >= 0; i--) {

            if (messages.get(i).getSender() == sender) {
                return messages.get(i);
            }
        }

        return null;
    }

    public boolean removeMessage(UUID id) {
        return messages.removeIf(
            message -> message.getId().equals(id));
    }

    public List<Message> getMessages() {
        return List.copyOf(messages);
    }

    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return new searchMessagesByUser(
            getMessages(), userToSearchWith);
    }
}
