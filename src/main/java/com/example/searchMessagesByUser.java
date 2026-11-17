package com.example;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class searchMessagesByUser implements Iterator<Message> {

    private final List<Message> messages;
    private final User userToSearchWith;

    private int index = 0;

    public searchMessagesByUser(
            List<Message> messages, User userToSearchWith) {

        if (userToSearchWith == null) {
            throw new IllegalArgumentException(
                "A user is required for searching");
        }

        this.messages = List.copyOf(messages);
        this.userToSearchWith = userToSearchWith;
    }

    @Override
    public boolean hasNext() {

        while (index < messages.size()) {

            Message message = messages.get(index);

            if (message.getSender() == userToSearchWith ||
                message.getRecipients().contains(userToSearchWith)) {

                return true;
            }

            index++;
        }

        return false;
    }

    @Override
    public Message next() {

        if (!hasNext()) {
            throw new NoSuchElementException(
                "No more messages for this user");
        }

        return messages.get(index++);
    }
}
