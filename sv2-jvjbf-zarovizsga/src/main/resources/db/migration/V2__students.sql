CREATE TABLE students
(
    student_id        BIGINT AUTO_INCREMENT NOT NULL,
    student_name      VARCHAR(255) NOT NULL,
    date_of_birth     date NULL,
    school_age_status VARCHAR(255) NULL,
    school_id  BIGINT NULL,
    CONSTRAINT pk_students PRIMARY KEY (student_id)
);

ALTER TABLE students
    ADD CONSTRAINT FK_STUDENTS_ON_SCHOOL FOREIGN KEY (school_id) REFERENCES schools (school_id);