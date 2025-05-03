package org.example.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.example.business.dto.EvaluationDTO;

public class VisualizeEvaluationController {

    @FXML
    private Label projectLabel;
    @FXML
    private Label idStudentLabel;
    @FXML
    private Label skillGradeLabel;
    @FXML
    private Label contentGradeLabel;
    @FXML
    private Label writingGradeLabel;
    @FXML
    private Label requirementsGradeLabel;
    @FXML
    private TextArea feedbackTextArea;

    public void setEvaluation(EvaluationDTO evaluation) {
        projectLabel.setText("ID Proyecto: " + evaluation.getIDProject());
        idStudentLabel.setText("ID Estudiante: " + evaluation.getIDStudent());
        skillGradeLabel.setText("Calificación Habilidad: " + evaluation.getSkillGrade());
        contentGradeLabel.setText("Calificación Contenido: " + evaluation.getContentGrade());
        writingGradeLabel.setText("Calificación Redacción: " + evaluation.getWritingGrade());
        requirementsGradeLabel.setText("Calificación Requisitos: " + evaluation.getRequirementsGrade());
        feedbackTextArea.setText(evaluation.getFeedback());
    }
}