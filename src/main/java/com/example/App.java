package com.example;

import java.util.Iterator;
import java.util.List;

public class App {

    public static void main(String[] args) {

        ChatServer server = new ChatServer();

        User alice = new User("Alice", server);
        User bob = new User("Bob", server);
        User charlie = new User("Charlie", server);

        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(charlie);

       

        System.out.println(
            "=== QUESTION 1: MEDIATOR + MEMENTO ===");

        System.out.println(
            "Registered users: Alice, Bob, Charlie");

        alice.sendMessage(bob, "Hi Bob!");

        System.out.println(
            "Alice sends to Bob: Hi Bob!");

        alice.sendMessage(
            List.of(bob, charlie), "Team meeting at 3 PM");

        System.out.println(
            "Alice sends to Bob and Charlie: Team meeting at 3 PM");

        bob.sendMessage(alice, "Got it, Alice");

        System.out.println(
            "Bob sends to Alice: Got it, Alice");

        System.out.println(
            "Bob history before undo: " +
            bob.getHistory().getMessages().size());

        System.out.println(
            "Alice undo successful: " +
            alice.undoLastMessage());

        System.out.println(
            "Bob history after undo: " +
            bob.getHistory().getMessages().size());

        System.out.println(
            "Charlie history after undo: " +
            charlie.getHistory().getMessages().size());


       

        charlie.blockUser(bob);

        System.out.println("Charlie blocks Bob");

        System.out.println(
            "Bob -> Charlie delivered: " +
            (bob.sendMessage(
                charlie, "Can you see this?") != null));

        System.out.println(
            "Bob -> Alice delivered: " +
            (bob.sendMessage(
                alice, "See you soon") != null));


       

        System.out.println("Alice chat history:");

        for (Message message :
                alice.getHistory().getMessages()) {

            System.out.println("  " + message);
        }


        

        System.out.println(
            "\n=== QUESTION 2: ITERATOR BY USER ===");

        System.out.println(
            "Alice history involving Bob:");

        Iterator<Message> byBob = alice.iterator(bob);

        int bobCount = 0;

        while (byBob.hasNext()) {
            System.out.println("  " + byBob.next());
            bobCount++;
        }

        System.out.println(
            "Matches for Bob: " + bobCount);


        System.out.println(
            "Alice history involving Charlie:");

        Iterator<Message> byCharlie =
            alice.iterator(charlie);

        int charlieCount = 0;

        while (byCharlie.hasNext()) {
            System.out.println("  " + byCharlie.next());
            charlieCount++;
        }

        System.out.println(
            "Matches for Charlie: " + charlieCount);
    }
}
