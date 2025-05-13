package org.example.gui.controller;

public class Session {
    private static Object actualUser;

    public static void startSession(Object user) {
        actualUser = user;
    }

    public static Object getActualUser() {
        return actualUser;
    }

    public static void closeSession() {
        actualUser = null;
    }
}

