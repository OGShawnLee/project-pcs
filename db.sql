DROP DATABASE IF EXISTS Practice;

CREATE DATABASE IF NOT EXISTS Practice;

USE Practice;

# TODO: Add to the Diagrama and Dictinary
CREATE TABLE Configuration
(
    name       ENUM ('EVALUATION_ENABLED_FIRST', 'EVALUATION_ENABLED_SECOND', 'EVALUATION_ENABLED_FINAL') NOT NULL,
    is_enabled BOOLEAN                                                                                    NOT NULL DEFAULT FALSE,
    PRIMARY KEY (name)
);

INSERT INTO Configuration (name)
VALUES ('EVALUATION_ENABLED_FIRST'),
       ('EVALUATION_ENABLED_SECOND'),
       ('EVALUATION_ENABLED_FINAL');

CREATE TRIGGER before_delete_configuration
    BEFORE DELETE
    ON Configuration
    FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Configuration is READ and UPDATE only';
END;

# TODO: Add to the Diagram and Dictionary
CREATE TABLE Account
(
    email    VARCHAR(128)                                                                   NOT NULL,
    password VARCHAR(64)                                                                    NOT NULL,
    role     ENUM ('COORDINATOR', 'ACADEMIC', 'ACADEMIC_EVALUATOR', 'EVALUATOR', 'STUDENT') NOT NULL,
    PRIMARY KEY (email)
);

CREATE TABLE Student
(
    id_student         CHAR(8)                    NOT NULL,
    email              VARCHAR(128)               NOT NULL,
    name               VARCHAR(64)                NOT NULL,
    paternal_last_name VARCHAR(64)                NOT NULL,
    maternal_last_name VARCHAR(64),
    created_at         TIMESTAMP                  NOT NULL DEFAULT NOW(),
    state              ENUM ('ACTIVE', 'RETIRED') NOT NULL DEFAULT 'ACTIVE',
    phone_number       VARCHAR(16)                NOT NULL,
    final_grade        INT CHECK (final_grade >= 0 AND final_grade <= 10),
    PRIMARY KEY (id_student),
    FOREIGN KEY (email) REFERENCES Account (email) ON DELETE CASCADE
);

DROP TRIGGER IF EXISTS before_delete_student;
CREATE TRIGGER before_delete_student
    BEFORE DELETE
    ON Student
    FOR EACH ROW
BEGIN
    DELETE
    FROM Account
    WHERE email = OLD.email;
END;

CREATE TABLE Academic
(
    id_academic        VARCHAR(5)                                           NOT NULL,
    email              VARCHAR(128)                                         NOT NULL,
    name               VARCHAR(64)                                          NOT NULL,
    paternal_last_name VARCHAR(64)                                          NOT NULL,
    maternal_last_name VARCHAR(64),
    created_at         TIMESTAMP                                            NOT NULL DEFAULT NOW(),
    state              ENUM ('ACTIVE', 'RETIRED')                           NOT NULL DEFAULT 'ACTIVE',
    role               ENUM ('ACADEMIC', 'ACADEMIC_EVALUATOR', 'EVALUATOR') NOT NULL,
    phone_number       VARCHAR(16)                                          NOT NULL,
    PRIMARY KEY (id_academic),
    FOREIGN KEY (email) REFERENCES Account (email) ON DELETE CASCADE
);

DROP TRIGGER IF EXISTS before_delete_academic;
CREATE TRIGGER before_delete_academic
    BEFORE DELETE
    ON Academic
    FOR EACH ROW
BEGIN
    DELETE
    FROM Account
    WHERE email = OLD.email;
END;

DROP TRIGGER IF EXISTS after_update_academic;
CREATE TRIGGER after_update_academic
    BEFORE UPDATE
    ON Academic
    FOR EACH ROW
BEGIN
    IF OLD.role != NEW.role THEN
        UPDATE Account
        SET role = NEW.role
        WHERE email = OLD.email;
    END IF;
END;

DROP PROCEDURE IF EXISTS create_academic;
CREATE PROCEDURE create_academic(
    IN in_id_academic VARCHAR(5),
    IN in_email VARCHAR(128),
    IN in_name VARCHAR(64),
    IN in_paternal_last_name VARCHAR(64),
    IN in_maternal_last_name VARCHAR(64),
    IN in_password VARCHAR(64),
    IN in_phone_number VARCHAR(16),
    IN in_role ENUM ('ACADEMIC', 'ACADEMIC_EVALUATOR', 'EVALUATOR')
)
BEGIN
    START TRANSACTION;
    IF (SELECT EXISTS(SELECT 1 FROM Account WHERE email = in_email)) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Duplicate Email';
    END IF;

    IF (SELECT EXISTS(SELECT 1 FROM Academic WHERE id_academic = in_id_academic)) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Duplicate Academic ID';
    END IF;

    INSERT INTO Account (email, password, role)
    VALUES (in_email, in_password, in_role);
    INSERT INTO Academic (id_academic, email, name, paternal_last_name, maternal_last_name, phone_number, role)
    VALUES (in_id_academic, in_email, in_name, in_paternal_last_name, in_maternal_last_name, in_phone_number, in_role);
    COMMIT;
END;

DROP PROCEDURE IF EXISTS create_student;
CREATE PROCEDURE create_student(
    IN in_id_student VARCHAR(8),
    IN in_email VARCHAR(128),
    IN in_name VARCHAR(64),
    IN in_paternal_last_name VARCHAR(64),
    IN in_maternal_last_name VARCHAR(64),
    IN in_password VARCHAR(64),
    IN in_phone_number VARCHAR(16)
)
BEGIN
    START TRANSACTION;

    IF (SELECT EXISTS(SELECT 1 FROM Account WHERE email = in_email)) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Duplicate Email';
    END IF;

    IF (SELECT EXISTS(SELECT 1 FROM Student WHERE id_student = in_id_student)) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Duplicate Student ID';
    END IF;

    INSERT INTO Account (email, password, role)
    VALUES (in_email, in_password, 'STUDENT');
    INSERT INTO Student (id_student, email, name, paternal_last_name, maternal_last_name, phone_number)
    VALUES (in_id_student, in_email, in_name, in_paternal_last_name, in_maternal_last_name, in_phone_number);
    COMMIT;
END;

CREATE TABLE Course
(
    # TODO: Figure out the format of nrc
    nrc             VARCHAR(5)                     NOT NULL,
    id_academic     CHAR(5)                        NOT NULL,
    section         ENUM ('S1', 'S2')              NOT NULL,
    semester        ENUM ('AUG_JAN', 'FEB_JUL')    NOT NULL,
    state           ENUM ('ON_GOING', 'COMPLETED') NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMP                      NOT NULL DEFAULT NOW(),
    PRIMARY KEY (nrc),
    FOREIGN KEY (id_academic) REFERENCES Academic (id_academic) ON DELETE CASCADE
);

CREATE TABLE Enrollment
(
    id_course  CHAR(5)   NOT NULL,
    id_student CHAR(8)   NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_student, id_course),
    FOREIGN KEY (id_student) REFERENCES Student (id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_course) REFERENCES Course (nrc) ON DELETE CASCADE
);

CREATE TABLE Organization
(
    email                    VARCHAR(128)               NOT NULL,
    name                     VARCHAR(128)               NOT NULL,
    representative_full_name VARCHAR(128)               NOT NULL,
    address                  VARCHAR(256)               NOT NULL,
    state                    ENUM ('ACTIVE', 'RETIRED') NOT NULL DEFAULT 'ACTIVE',
    phone_number             VARCHAR(16)                NOT NULL,
    created_at               TIMESTAMP                  NOT NULL DEFAULT NOW(),
    PRIMARY KEY (email)
);

CREATE TABLE Project
(
    id_project       INT AUTO_INCREMENT                   NOT NULL,
    id_organization  VARCHAR(128)                         NOT NULL,
    name             VARCHAR(128)                         NOT NULL,
    description      TEXT                                 NOT NULL,
    department       VARCHAR(128)                         NOT NULL,
    available_places INT                                  NOT NULL CHECK (available_places >= 0),
    methodology      VARCHAR(128)                         NOT NULL,
    state            ENUM ('ACTIVE', 'RETIRED')           NOT NULL DEFAULT 'ACTIVE',
    # TODO: Add to the Diagram
    sector           ENUM ('PUBLIC', 'PRIVATE', 'SOCIAL') NOT NULL DEFAULT 'PRIVATE',
    created_at       TIMESTAMP                            NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_project),
    FOREIGN KEY (id_organization) REFERENCES Organization (email) ON DELETE CASCADE
);

CREATE TABLE ProjectRequest
(
    id_student      CHAR(8)                                  NOT NULL,
    id_project      INT                                      NOT NULL,
    state           ENUM ('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    # TODO: Add to the Diagram and Dictionary
    reason_of_state TEXT                                     NOT NULL,
    created_at      TIMESTAMP                                NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_student, id_project),
    FOREIGN KEY (id_student) REFERENCES Student (id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_project) REFERENCES Project (id_project) ON DELETE CASCADE
);

CREATE TABLE Practice
(
    id_student            CHAR(8),
    id_project            INT  NOT NULL,
    # TODO: Add to the Diagram and Dictionary
    reason_of_assignation TEXT NOT NULL,
    PRIMARY KEY (id_student, id_project),
    FOREIGN KEY (id_student) REFERENCES Student (id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_project) REFERENCES Project (id_project) ON DELETE CASCADE,
    UNIQUE (id_student)
);

CREATE TABLE Evaluation
(
    id_student         CHAR(8)            NOT NULL,
    id_project         INT AUTO_INCREMENT NOT NULL,
    id_academic        CHAR(5)            NOT NULL,
    skill_grade        INT                NOT NULL CHECK (skill_grade >= 0 AND skill_grade <= 10),
    content_grade      INT                NOT NULL CHECK (content_grade >= 0 AND content_grade <= 10),
    writing_grade      INT                NOT NULL CHECK (writing_grade >= 0 AND writing_grade <= 10),
    requirements_grade INT                NOT NULL CHECK (requirements_grade >= 0 AND requirements_grade <= 10),
    feedback           TEXT               NOT NULL,
    created_at         TIMESTAMP          NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_student, id_project, id_academic),
    FOREIGN KEY (id_student) REFERENCES Student (id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_project) REFERENCES Project (id_project) ON DELETE CASCADE,
    FOREIGN KEY (id_academic) REFERENCES Academic (id_academic) ON DELETE CASCADE
);

CREATE TABLE SelfEvaluation
(
    id_student                              CHAR(8)   NOT NULL,
    follow_up_grade                         INT       NOT NULL CHECK (follow_up_grade >= 0 AND follow_up_grade <= 10),
    safety_grade                            INT       NOT NULL CHECK (safety_grade >= 0 AND safety_grade <= 10),
    knowledge_application_grade             INT       NOT NULL CHECK (knowledge_application_grade >= 0 AND knowledge_application_grade <= 10),
    interesting_grade                       INT       NOT NULL CHECK (interesting_grade >= 0 AND interesting_grade <= 10),
    productivity_grade                      INT       NOT NULL CHECK (productivity_grade >= 0 AND productivity_grade <= 10),
    congruent_grade                         INT       NOT NULL CHECK (congruent_grade >= 0 AND congruent_grade <= 10),
    informed_by_organization                INT       NOT NULL CHECK (informed_by_organization >= 0 and informed_by_organization <= 10),
    regulated_by_organization               INT       NOT NULL CHECK (regulated_by_organization >= 0 and regulated_by_organization <= 10),
    importance_for_professional_development INT       NOT NULL CHECK (importance_for_professional_development >= 0 and
                                                                      importance_for_professional_development <= 10),
    created_at                              TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_student),
    FOREIGN KEY (id_student) REFERENCES Student (id_student) ON DELETE CASCADE
);

CREATE TABLE MonthlyReport
(
    id_student   CHAR(8)   NOT NULL,
    id_project   INT       NOT NULL,
    # TODO: Add to the Diagram and Dictionary
    month        INT       NOT NULL CHECK (month >= 1 AND month <= 12),
    # TODO: Add to the Diagram and Dictionary
    year         INT       NOT NULL CHECK (year >= 2025),
    worked_hours INT       NOT NULL CHECK (worked_hours >= 0),
    report       TEXT      NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_student, id_project, month, year),
    FOREIGN KEY (id_student) REFERENCES Student (id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_project) REFERENCES Project (id_project) ON DELETE CASCADE
);

CREATE OR REPLACE VIEW StudentPractice AS
SELECT Student.id_student,
       Student.email,
       Student.name,
       Student.paternal_last_name,
       Student.maternal_last_name,
       Student.created_at,
       Student.state,
       Student.phone_number,
       Student.final_grade,
       Practice.id_project,
       Practice.reason_of_assignation
FROM Student
         JOIN Practice ON Student.id_student = Practice.id_student;

SELECT *
FROM StudentPractice;

# CREATE USER practice_admin@localhost IDENTIFIED BY 'ADMIN';
# CREATE ROLE practice_admin_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON Practice.* TO practice_admin@localhost;
GRANT practice_admin_role TO practice_admin@localhost;
SET DEFAULT ROLE ALL TO practice_admin@localhost;
