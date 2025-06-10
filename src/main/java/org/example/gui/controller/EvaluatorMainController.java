package org.example.gui.controller;

import javafx.stage.Stage;


public class EvaluatorMainController extends Controller{

    public void handleRegisterEvaluationAction() {
        RegisterEvaluationController.navigateToRegisterEvaluationPage(getScene());
    }

    public void handleReviewEvaluationAction() {
        ReviewEvaluationListController.navigateToEvaluationListPage(getScene());
    }

    public static void navigateToEvaluatorMain(Stage currentStage) {
        navigateTo(currentStage, "Página del evaluador", "EvaluatorMainPage");
    }
}
