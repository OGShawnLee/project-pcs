package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.business.dao.SelfEvaluationDAO;
import org.example.business.dto.SelfEvaluationDTO;
import org.example.business.dto.StudentDTO;
import org.example.gui.Modal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.IntStream;

public class RegisterSelfEvaluationController {
    @FXML
    private ComboBox<Integer> followUpGrade;
    @FXML
    private ComboBox<Integer> safetyGrade;
    @FXML
    private ComboBox<Integer> knowledgeApplicationGrade;
    @FXML
    private ComboBox<Integer> interestingGrade;
    @FXML
    private ComboBox<Integer> productivityGrade;
    @FXML
    private ComboBox<Integer> congruentGrade;
    @FXML
    private ComboBox<Integer> informedByOrganization;
    @FXML
    private ComboBox<Integer> regulatedByOrganization;
    @FXML
    private ComboBox<Integer> importanceForProfessionalDevelopment;

    SelfEvaluationDAO SELF_EVALUATION_DAO;

    @FXML
    public void initialize() {
        SELF_EVALUATION_DAO = new SelfEvaluationDAO();
        var numbers = FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 10).boxed().toList()
        );

        followUpGrade.setItems(numbers);
        safetyGrade.setItems(numbers);
        knowledgeApplicationGrade.setItems(numbers);
        interestingGrade.setItems(numbers);
        productivityGrade.setItems(numbers);
        congruentGrade.setItems(numbers);
        informedByOrganization.setItems(numbers);
        regulatedByOrganization.setItems(numbers);
        importanceForProfessionalDevelopment.setItems(numbers);
    }

    @FXML
    public void handleRegisterSelfEvaluation(ActionEvent event) {
        Object user = Session.getCurrentUser();
        if (!(user instanceof StudentDTO student)) {
            Modal.displayError("Solo un estudiante puede registrar su autoevaluación.");
            return;
        }

        try {
            SelfEvaluationDTO dataObjectSelfEvaluation = new SelfEvaluationDTO.SelfEvaluationBuilder()
                    .setIDStudent(student.getID())
                    .setFollowUpGrade(followUpGrade.getValue())
                    .setSafetyGrade(safetyGrade.getValue())
                    .setKnowledgeApplicationGrade(knowledgeApplicationGrade.getValue())
                    .setInterestingGrade(interestingGrade.getValue())
                    .setProductivityGrade(productivityGrade.getValue())
                    .setCongruentGrade(congruentGrade.getValue())
                    .setInformedByOrganization(informedByOrganization.getValue())
                    .setRegulatedByOrganization(regulatedByOrganization.getValue())
                    .setImportanceForProfessionalDevelopment(importanceForProfessionalDevelopment.getValue())
                    .build();

            SelfEvaluationDTO dataObjectExistingSelfEvaluation = SELF_EVALUATION_DAO.getOne(dataObjectSelfEvaluation.getIDStudent());

            if (dataObjectExistingSelfEvaluation != null) {
                Modal.displayError(
                        "No ha sido posible registrar la autoevaluacion debido a que ya existe una organización registrada con ese correo electrónico."
                );
                return;
            }

            SELF_EVALUATION_DAO.createOne(dataObjectSelfEvaluation);
            Modal.displaySuccess("La autoevaluacion ha sido registrada exitosamente.");
            returnToMainPage(event);
        } catch (IllegalArgumentException e) {
            Modal.displayError(e.getMessage());
        } catch (SQLException e) {
            Modal.displayError("No ha sido posible registrar la organización debido a un error de sistema.");
        } catch (IOException e) {
            Modal.displayError("Ha ocurrido un error al cargar la pagina");
        }
    }

    @FXML
    public void returnToMainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/StudentMainPage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Menu Principal");
        stage.show();
    }
}
