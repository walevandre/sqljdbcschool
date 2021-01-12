DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS courses;

CREATE TABLE groups
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    UNIQUE (name)
);

CREATE TABLE students
(
    id         SERIAL PRIMARY KEY,
    group_id   integer,
    first_name VARCHAR(64),
    last_name  VARCHAR(64)
--     CONSTRAINT "fk_group" FOREIGN KEY ("group_id") REFERENCES "groups" ("id") ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE courses
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(64) NOT NULL,
    description TEXT DEFAULT NULL,
    UNIQUE (name)
);

CREATE TABLE students_courses
(
    student_id INT REFERENCES students (id) ON DELETE CASCADE,
    course_id  INT REFERENCES courses (id) ON DELETE CASCADE,
    CONSTRAINT students_courses_pkey PRIMARY KEY (student_id, course_id)
);

INSERT INTO students (group_id, first_name, last_name)
VALUES (1, 'John', 'Williams'),
       (1, 'Natan', 'White'),
       (1, 'Ethan', 'Hamilton'),
       (2, 'Jakub', 'Bailey'),
       (2, 'Jakub', 'Day'),
       (2, 'Aadam', 'Andrews'),
       (2, 'Jakub', 'Robertson'),
       (2, 'Reece', 'Bailey'),
       (3, 'Jakub', 'George'),
       (3, 'Orion', 'Smith'),
       (4, 'Natan', 'Hamilton');


INSERT INTO groups (name)
VALUES ('XX-10'),
       ('ZZ-20'),
       ('SS-20'),
       ('DD-20'),
       ('FF-20');

INSERT INTO courses (name)
VALUES ('Algebra'),
       ('Biology'),
       ('Physics'),
       ('Science'),
       ('Psychology'),
       ('Chemistry'),
       ('Computer science');

INSERT INTO students_courses (student_id, course_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 5),
       (2, 4),
       (2, 3),
       (2, 2),
       (3, 2),
       (3, 3),
       (4, 2),
       (4, 5);

