package org.example.gui.controller;

public abstract class ReviewListController extends Controller {
  public void initialize() {
    loadTableColumns();
    loadDataList();
  }

  public abstract void loadTableColumns();

  public abstract void loadDataList();
}
