package org.example.gui;

public class Session {
    private static Object currentUser;

    public static void startSession(Object user) {
        currentUser = user;
    }

    public static Object getCurrentUser() {
        return currentUser;
    }

    public static void closeSession() {
        currentUser = null;
    }
}

