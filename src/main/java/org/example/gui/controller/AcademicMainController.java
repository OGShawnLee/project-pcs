package org.example.gui.controller;

import javafx.stage.Stage;

public class AcademicMainController extends Controller {

    public void handleStudentsAction() {
        ReviewStudentListController.navigateToStudentListPage(getScene());
    }

    public void handleReviewEvaluationAction() {
        ReviewEvaluationListController.navigateToEvaluationListPage(getScene());
    }

    public static void navigateToAcademicMain(Stage currentStage) {
        navigateTo(currentStage, "Página de Inicio Académico", "AcademicMainPage");
    }
}
