DROP TABLE IF EXISTS ENROLLMENTS;
DROP TABLE IF EXISTS COURSES;
DROP TABLE IF EXISTS STUDENTS;
DROP TABLE IF EXISTS INSTRUCTORS;

CREATE TABLE INSTRUCTORS (
    FiD TEXT PRIMARY KEY,
    first_name TEXT,
    last_name TEXT,
    email TEXT,
    department TEXT
);

CREATE TABLE STUDENTS (
    id INTEGER,
    reg_no TEXT PRIMARY KEY,
    first_name TEXT,
    last_name TEXT,
    email TEXT,
    status TEXT,
    registration_date TEXT
);

CREATE TABLE COURSES (
    code TEXT PRIMARY KEY,
    title TEXT,
    credits INTEGER,
    department TEXT,
    instructor_id TEXT,
    semester TEXT,
    CONSTRAINT fk_instructor FOREIGN KEY (instructor_id) REFERENCES INSTRUCTORS(FiD)
);

CREATE TABLE ENROLLMENTS (
    student_reg_no TEXT,
    course_code TEXT,
    grade TEXT,
    PRIMARY KEY (student_reg_no, course_code),
    CONSTRAINT fk_student FOREIGN KEY (student_reg_no) REFERENCES STUDENTS(reg_no),
    CONSTRAINT fk_course FOREIGN KEY (course_code) REFERENCES COURSES(code)
);