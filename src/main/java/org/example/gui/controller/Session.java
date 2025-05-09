package org.example.gui.controller;

public class Session {
    private static Object usuarioActual;

    public static void startSession(Object usuario) {
        usuarioActual = usuario;
    }

    public static Object getActualUser() {
        return usuarioActual;
    }

    public static void closeSession() {
        usuarioActual = null;
    }
}

