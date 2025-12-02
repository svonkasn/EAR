-- COURSES
INSERT INTO COURSE (ID,  	CODE,  	DESCRIPTION,  	NAME)
VALUES
  (1, 'JAV101', 'Java Basics', 'Introduction to Java'),
  (2, 'SPR201', 'Spring Boot', 'Spring Boot fundamentals');

-- GROUPS
INSERT INTO GROUP_OF_STUDENT ( COURSE_ID, ID, NUMBER_OF_PARALLEL)
VALUES
  (1, 1, 101),
  (2, 1, 102);

-- USERS (user is reserved → must be quoted)
INSERT INTO "USER" (ID, username, password, email, role, group_id)
VALUES
  (1, 'admin', 'admin123', 'admin@example.com', 'ADMIN', NULL),
  (2, 'teacher1', 'teacher123', 'teacher1@example.com', 'TEACHER', 1),
  (3, 'alice', 'alice123', 'alice@example.com', 'STUDENT', 1),
  (4, 'bob', 'bob123', 'bob@example.com', 'STUDENT', 1);

-- MANY-TO-MANY TEACHER_COURSES
INSERT INTO TEACHER_COURSES (COURSE_ID, TEACHER_ID)
VALUES
  (1, 2),
  (2, 2);

-- TERMIN
INSERT INTO TERMIN (ID, END_TIME, START_TIME)
VALUES
  (1, '2024-12-01T09:00:00', '2024-12-01T10:00:00');

-- TESTS
INSERT INTO TEST (id, title, description, maxPoints, maxAttempts,
                  teacher_id, course_id, group_id, termin_id)
VALUES
  (1, 'Java Test 1', 'Basic Java knowledge', 10, 3, 2, 1, 1, 1);

-- QUESTIONS

INSERT INTO QUESTION (ID, POINT, TEST_ID, QUESTION_TEXT)
VALUES
  (1, 3,  1, 'What is JVM?',),
  (2, 4, 1, 'Which types are primitive?' ),
  (3, 3, 1,'Explain OOP.');
--
-- -- MultipleChoice
-- INSERT INTO MULTIPLE_CHOICE (CORRECT_A, CORRECT_B, CORRECT_C ,CORRECT_D, ID, MULTIPLE_CORRECT, OPTION_A, OPTION_B, OPTION_C, OPTION_D)
-- VALUES
--   (2, 'int', 'String', 'boolean', 'Apple', TRUE, FALSE, TRUE, FALSE, TRUE);

-- OpenQuestion
INSERT INTO OPEN_QUESTION (ID, MAX_LENGTH, EXPECTED_ANSWER )
VALUES
  (1, 300, 'Java Virtual Machine'),
  (3, 500, 'Inheritance, polymorphism, abstraction, encapsulation');

-- StudentTest
INSERT INTO STUDENT_TEST (id, score, user_id, test_id, status)
VALUES
  (1, NULL, 3, 1, 'IN_PROGRESS'),
  (2, 7,    4, 1, 'EVALUATED');
