DROP DATABASE IF EXISTS Practice;

CREATE DATABASE IF NOT EXISTS Practice;

USE Practice;

# TODO: Add to the Diagram and Dictionary
CREATE TABLE Account (
    email VARCHAR(128) NOT NULL,
    password VARCHAR(32) NOT NULL,
    PRIMARY KEY (email)
);

CREATE TABLE Student (
    id_student CHAR(8) NOT NULL,
    email VARCHAR(128) NOT NULL,
    name VARCHAR(64) NOT NULL,
    paternal_last_name VARCHAR(64) NOT NULL,
    maternal_last_name VARCHAR(64) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    state ENUM('ACTIVE', 'RETIRED') NOT NULL DEFAULT 'ACTIVE',
    final_grade INT CHECK (final_grade >= 0 AND final_grade <= 10),
    PRIMARY KEY (id_student),
    FOREIGN KEY (email) REFERENCES Account(email) ON DELETE CASCADE
);

CREATE TABLE Academic (
    id_academic CHAR(5) NOT NULL,
    email VARCHAR(128) NOT NULL,
    name VARCHAR(64) NOT NULL,
    paternal_last_name VARCHAR(64) NOT NULL,
    maternal_last_name VARCHAR(64) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    state ENUM('ACTIVE', 'RETIRED') NOT NULL DEFAULT 'ACTIVE',
    role ENUM('PROFESSOR', 'EVALUATOR', 'EVALUATOR-PROFESSOR') NOT NULL,
    PRIMARY KEY (id_academic),
    FOREIGN KEY (email) REFERENCES Account(email) ON DELETE CASCADE
);

CREATE TABLE Course (
    # TODO: Figure out the format of nrc
    nrc VARCHAR(5) NOT NULL,
    id_academic CHAR(5) NOT NULL,
    section VARCHAR(16) NOT NULL,
    started_at DATETIME NOT NULL,
    ended_at DATETIME NOT NULL,
    PRIMARY KEY (nrc),
    FOREIGN KEY (id_academic) REFERENCES Academic(id_academic) ON DELETE CASCADE
);

CREATE TABLE Enrollment (
    id_course CHAR(5) NOT NULL,
    id_student CHAR(8) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_student, id_course),
    FOREIGN KEY (id_student) REFERENCES Student(id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_course) REFERENCES Course(nrc) ON DELETE CASCADE
);

CREATE TABLE Organization (
    email VARCHAR(128) NOT NULL,
    name VARCHAR(128) NOT NULL,
    representative_full_name VARCHAR(128) NOT NULL,
    colony VARCHAR(128) NOT NULL,
    street VARCHAR(128) NOT NULL,
    state ENUM ('ACTIVE', 'RETIRED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (email)
);

CREATE TABLE Project (
    id_project INT AUTO_INCREMENT NOT NULL,
    id_organization VARCHAR(128) NOT NULL,
    name VARCHAR(128) NOT NULL,
    methodology VARCHAR(128) NOT NULL,
    state ENUM ('ACTIVE', 'RETIRED') NOT NULL DEFAULT 'ACTIVE',
    # TODO: Add to the Diagram
    sector ENUM ('PUBLIC', 'PRIVATE', 'SOCIAL') NOT NULL DEFAULT 'PRIVATE',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_project),
    FOREIGN KEY (id_organization) REFERENCES Organization(email) ON DELETE CASCADE
);

CREATE TABLE ProjectRequest (
    id_student CHAR(8) NOT NULL,
    id_project INT NOT NULL,
    state ENUM ('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    # TODO: Add to the Diagram and Dictionary
    reason_of_state TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_student, id_project),
    FOREIGN KEY (id_student) REFERENCES Student(id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_project) REFERENCES Project(id_project) ON DELETE CASCADE
);

CREATE TABLE Practice (
    id_student CHAR(8),
    id_project INT NOT NULL,
    # TODO: Add to the Diagram and Dictionary
    reason_of_assignation TEXT NOT NULL,
    PRIMARY KEY (id_student, id_project),
    FOREIGN KEY (id_student) REFERENCES Student(id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_project) REFERENCES Project(id_project) ON DELETE CASCADE,
    UNIQUE (id_student)
);

CREATE TABLE Evaluation (
    id_student CHAR(8) NOT NULL,
    id_project INT AUTO_INCREMENT NOT NULL,
    id_academic CHAR(5) NOT NULL,
    skill_grade INT NOT NULL CHECK (skill_grade >= 0 AND skill_grade <= 10),
    content_grade INT NOT NULL CHECK (content_grade >= 0 AND content_grade <= 10),
    writing_grade INT NOT NULL CHECK (writing_grade >= 0 AND writing_grade <= 10),
    requirements_grade INT NOT NULL CHECK (requirements_grade >= 0 AND requirements_grade <= 10),
    feedback TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_student, id_project, id_academic),
    FOREIGN KEY (id_student) REFERENCES Student(id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_project) REFERENCES Project(id_project) ON DELETE CASCADE,
    FOREIGN KEY (id_academic) REFERENCES Academic(id_academic) ON DELETE CASCADE
);

CREATE TABLE SelfEvaluation (
    id_student CHAR(8) NOT NULL,
    follow_up_grade INT NOT NULL CHECK (follow_up_grade >= 0 AND follow_up_grade <= 10),
    safety_grade INT NOT NULL CHECK (safety_grade >= 0 AND safety_grade <= 10),
    knowledge_application_grade INT NOT NULL CHECK (knowledge_application_grade >= 0 AND knowledge_application_grade <= 10),
    interesting_grade INT NOT NULL CHECK (interesting_grade >= 0 AND interesting_grade <= 10),
    productivity_grade INT NOT NULL CHECK (productivity_grade >= 0 AND productivity_grade <= 10),
    congruent_grade INT NOT NULL CHECK (congruent_grade >= 0 AND congruent_grade <= 10),
    informed_by_organization INT NOT NULL CHECK (informed_by_organization >= 0 and informed_by_organization <= 10),
    regulated_by_organization INT NOT NULL CHECK (regulated_by_organization >= 0 and regulated_by_organization <= 10),
    importance_for_professional_development INT NOT NULL CHECK (importance_for_professional_development >= 0 and importance_for_professional_development <= 10),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_student),
    FOREIGN KEY (id_student) REFERENCES Student(id_student) ON DELETE CASCADE
);

CREATE TABLE MonthlyReport (
    id_student CHAR(8) NOT NULL,
    id_project INT NOT NULL,
    # TODO: Add to the Diagram and Dictionary
    month INT NOT NULL CHECK (month >= 1 AND month <= 12),
    # TODO: Add to the Diagram and Dictionary
    year INT NOT NULL CHECK (year >= 2025),
    worked_hours INT NOT NULL CHECK (worked_hours >= 0),
    report TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id_student, id_project, month, year),
    FOREIGN KEY (id_student) REFERENCES Student(id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_project) REFERENCES Project(id_project) ON DELETE CASCADE
);

CREATE USER practice_admin@localhost IDENTIFIED BY 'ADMIN';
CREATE ROLE practice_admin_role;
GRANT SELECT, INSERT, UPDATE, DELETE ON Practice.* TO practice_admin@localhost;
GRANT practice_admin_role TO practice_admin@localhost;
SET DEFAULT ROLE ALL TO practice_admin@localhost;