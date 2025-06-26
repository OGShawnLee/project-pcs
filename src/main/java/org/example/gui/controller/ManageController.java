package org.example.gui.controller;

public abstract class ManageController<T> extends ContextController<T> {
  protected abstract void handleUpdateCurrentDataObject();
}
