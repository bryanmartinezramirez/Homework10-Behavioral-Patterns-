package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ChatServer {

    private final Map<String, User> registered =
        new LinkedHashMap<>();

    private final Map<User, Set<User>> blockedSenders =
        new HashMap<>();

    private final Map<UUID, Message> sentMessages =
        new HashMap<>();

    public void registerUser(User user) {

        if (user == null || registered.containsKey(user.getName())) {
            throw new IllegalArgumentException(
                "User is null or already registered");
        }

        registered.put(user.getName(), user);
    }

    public boolean unregisterUser(User user) {

        if (!isRegistered(user)) {
            return false;
        }

        registered.remove(user.getName());
        blockedSenders.remove(user);

        for (Set<User> blocked : blockedSenders.values()) {
            blocked.remove(user);
        }

        return true;
    }

    public void blockUser(User recipient, User senderToBlock) {

        if (!isRegistered(recipient) || !isRegistered(senderToBlock)) {
            throw new IllegalArgumentException(
                "Both users must be registered");
        }

        blockedSenders
            .computeIfAbsent(recipient, key -> new HashSet<>())
            .add(senderToBlock);
    }

    public Message sendMessage(
            User sender, List<User> recipients, String text) {

        if (!isRegistered(sender) ||
            recipients == null || recipients.isEmpty()) {
            throw new IllegalArgumentException(
                "Registered sender and recipients required");
        }

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                "Message content cannot be empty");
        }

        Set<User> uniqueRecipients =
            new LinkedHashSet<>(recipients);

        List<User> deliveredTo = new ArrayList<>();

        for (User recipient : uniqueRecipients) {

            if (!isRegistered(recipient)) {
                throw new IllegalArgumentException(
                    "Recipient is not registered");
            }

            if (recipient != sender &&
                !blockedSenders.getOrDefault(
                    recipient, Set.of()).contains(sender)) {

                deliveredTo.add(recipient);
            }
        }

        if (deliveredTo.isEmpty()) {
            return null;
        }

        Message message =
            new Message(sender, deliveredTo, text);

        sentMessages.put(message.getId(), message);

        sender.recordSentMessage(message);

        for (User recipient : deliveredTo) {
            recipient.receiveMessage(message);
        }

        return message;
    }

    public boolean undoMessage(
            User sender, MessageMemento snapshot) {

        if (!isRegistered(sender) || snapshot == null) {
            return false;
        }

        Message message =
            sentMessages.get(snapshot.getMessageId());

        if (message == null ||
            message.getSender() != sender ||
            !message.getTimestamp().equals(snapshot.getTimestamp()) ||
            !message.getContent().equals(snapshot.getContent())) {
            return false;
        }

        sender.getHistory().removeMessage(message.getId());

        for (User recipient : message.getRecipients()) {
            recipient.getHistory().removeMessage(message.getId());
        }

        sentMessages.remove(message.getId());

        return true;
    }

    public boolean isRegistered(User user) {
        return user != null &&
            registered.get(user.getName()) == user;
    }
}
