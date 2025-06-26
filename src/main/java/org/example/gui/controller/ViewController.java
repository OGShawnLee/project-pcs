package org.example.gui.controller;

public class ViewController<T> extends Controller {
  private T currentDataObject;

  public T getCurrentDataObject() {
    return currentDataObject;
  }

  public void initialize(T dataObject) {
    this.currentDataObject = dataObject;
  }
}
