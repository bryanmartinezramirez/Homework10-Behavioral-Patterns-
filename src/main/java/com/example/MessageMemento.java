package com.example;

import java.time.Instant;
import java.util.UUID;

public final class MessageMemento {

    private final UUID messageId;
    private final String content;
    private final Instant timestamp;

    public MessageMemento(Message message) {
        this.messageId = message.getId();
        this.content = message.getContent();
        this.timestamp = message.getTimestamp();
    }

    public UUID getMessageId() {
        return messageId;
    }

    public String getContent() {
        return content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
