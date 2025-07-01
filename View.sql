CREATE OR REPLACE VIEW EvaluationPreview AS
SELECT Evaluation.id_student,
       Evaluation.id_project,
       Evaluation.id_academic,
       Evaluation.adequate_use_grade,
       Evaluation.content_congruence_grade,
       Evaluation.writing_grade,
       Evaluation.methodological_rigor_grade,
       Evaluation.feedback,
       Evaluation.kind,
       Evaluation.created_at,
       get_person_full_name(Student.name, Student.paternal_last_name, Student.maternal_last_name) AS full_name_student,
       get_person_full_name(Academic.name, Academic.paternal_last_name, Academic.maternal_last_name) AS full_name_academic
FROM Evaluation
         JOIN Student ON Evaluation.id_student = Student.id_student
         JOIN Academic ON Evaluation.id_academic = Academic.id_academic;