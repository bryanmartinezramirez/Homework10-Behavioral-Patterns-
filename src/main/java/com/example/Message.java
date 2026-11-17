package com.example;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Message {

    private final UUID id;
    private final User sender;
    private final List<User> recipients;
    private final Instant timestamp;
    private final String content;

    public Message(User sender, List<User> recipients, String content) {

        if (sender == null || recipients == null || recipients.isEmpty()) {
            throw new IllegalArgumentException(
                "Sender and recipients are required");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException(
                "Message content cannot be empty");
        }

        this.id = UUID.randomUUID();
        this.sender = sender;
        this.recipients = List.copyOf(recipients);
        this.timestamp = Instant.now();
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public List<User> getRecipients() {
        return recipients;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        String names = recipients.stream()
            .map(User::getName)
            .collect(Collectors.joining(", "));

        return sender.getName() + " -> " + names + ": " + content;
    }
}
