-- === COURSES ===
INSERT INTO COURSE (CODE, NAME, DESCRIPTION)
VALUES
  ('JAV101', 'Java Basics', 'Introduction to Java'),
  ('SPR201', 'Spring Boot', 'Spring Boot fundamentals');

-- === GROUPS ===
INSERT INTO GROUP_OF_STUDENT (NUMBER_OF_PARALLEL, COURSE_ID)
VALUES
  (101, 1),
  (102, 2);

-- === USERS ===
INSERT INTO EAR_USER (USERNAME, PASSWORD, EMAIL, ROLE, GROUP_ID)
VALUES
  ('admin', '$2a$10$pEBf.fUvT./PFpmsMY51qOUP2Clq9bnPZpBDqsmVY94kSAKM0otYS', 'admin@example.com', 'ADMIN', NULL),
  ('teacher1', '$2a$10$.7JVxiNew0Lr9Ht8V85XwOTmrzm5Vj3S6LVHeFq390Lvestxtp9RC', 'teacher1@example.com', 'TEACHER', 1),
  ('alice', '$2a$10$JxU4ItjR/eUaq1wQjAeYMui0za6hmYgOAopcuGp2xt6iz7h/AyGxy', 'alice@example.com', 'STUDENT', 1),
  ('bob', '$2a$10$5RYY9YCuSrw6LvwHh6Y4KeuahNz1eg6Hd.6fxSBVww.L2.C2h.I0y', 'bob@example.com', 'STUDENT', 1);

-- === PASSWORDS ===
    -- admin: admin123
    -- teacher: teacher123
    -- alice: alice123
    -- bob: bob123
-- =================

-- === TEACHER <-> COURSES (M:N table) ===
INSERT INTO TEACHER_COURSES (COURSE_ID, TEACHER_ID)
VALUES
  (1, 2),
  (2, 2);

-- === TERMIN ===
INSERT INTO TERMIN (START_TIME, END_TIME)
VALUES
  ('2024-12-01T09:00:00', '2024-12-01T10:00:00');

-- === TESTS ===
INSERT INTO TEST (TITLE, DESCRIPTION, MAX_POINTS, MAX_ATTEMPTS, TEACHER_ID, COURSE_ID, GROUP_ID, TERMIN_ID)
VALUES
  ('Java Test 1', 'Basic Java knowledge', 10, 3, 2, 1, 1, 1);

-- === QUESTIONS ===
INSERT INTO QUESTION (POINT, QUESTION_TEXT, TEST_ID)
VALUES
  (3, 'What is JVM?', 1),
  (4, 'Which types are primitive?', 1),
  (3, 'Explain OOP.', 1);

-- === MULTIPLE CHOICE ===
INSERT INTO MULTIPLE_CHOICE (
  ID, OPTION_A, OPTION_B, OPTION_C, OPTION_D,
  CORRECT_A, CORRECT_B, CORRECT_C, CORRECT_D,
  MULTIPLE_CORRECT
)
VALUES
  (2, 'int', 'String', 'boolean', 'Apple', TRUE, FALSE, TRUE, FALSE, TRUE);

-- === OPEN QUESTIONS ===
INSERT INTO OPEN_QUESTION (ID, EXPECTED_ANSWER, MAX_LENGTH)
VALUES
  (1, 'Java Virtual Machine', 300),
  (3, 'Inheritance, polymorphism, abstraction, encapsulation', 500);

-- === STUDENT TEST ===
INSERT INTO STUDENT_TEST (SCORE, TEST_ID, USER_ID, STATUS)
VALUES
  (NULL, 1, 3, 'IN_PROGRESS'),
  (7,    1, 4, 'EVALUATED');

-- === STUDENT ANSWERS ===
INSERT INTO STUDENT_ANSWER
(ID, SEL_A, SEL_B, SEL_C, SEL_D, AWARDED_POINTS, CORRECT, TEACHER_NOTE, STUDENT_TEST_ID, QUESTION_ID)
VALUES
  (1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1),
  (2, TRUE, FALSE, TRUE, FALSE, NULL, NULL, NULL, 1, 2);

INSERT INTO STUDENT_ANSWER
(ID, SEL_A, SEL_B, SEL_C, SEL_D, AWARDED_POINTS, CORRECT, TEACHER_NOTE, STUDENT_TEST_ID, QUESTION_ID)
VALUES
  (3, NULL, NULL, NULL, NULL, 3, TRUE, 'Good explanation', 2, 1),
  (4, TRUE, FALSE, TRUE, FALSE, 4, TRUE, NULL, 2, 2),
  (5, NULL, NULL, NULL, NULL, 0, FALSE, NULL, 2, 3);
