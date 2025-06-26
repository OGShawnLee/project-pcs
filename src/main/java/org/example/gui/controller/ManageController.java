package org.example.gui.controller;

public abstract class ManageController<T> extends ViewController<T> {
  protected abstract void handleUpdateCurrentDataObject();
}
