package org.example.gui.controller;

public abstract class ManageController<T> extends Controller {
  private T currentDataObject;

  public T getCurrentDataObject() {
    return currentDataObject;
  }

  protected void initialize(T dataObject) {
    this.currentDataObject = dataObject;
  }

  protected abstract void handleUpdateCurrentDataObject();
}
