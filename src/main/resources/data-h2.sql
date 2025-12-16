-- === COURSES ===
INSERT INTO COURSE (CODE, NAME, DESCRIPTION)
VALUES
  ('JAV101', 'Java Basics', 'Introduction to Java'),
  ('SPR201', 'Spring Boot', 'Spring Boot fundamentals'),
  ('DBS110', 'Database Systems', 'Relational DBs, SQL, indexing'),
  ('ALG200', 'Algorithms', 'Sorting, graphs, complexity');

-- === GROUPS ===
INSERT INTO GROUP_OF_STUDENT (NUMBER_OF_PARALLEL, COURSE_ID)
VALUES
  (101, 1),
  (102, 2),
  (103, 2),
  (104, 3),
  (105, 4);

-- === USERS ===
INSERT INTO EAR_USER (USERNAME, PASSWORD, EMAIL, ROLE, GROUP_ID)
VALUES
  ('admin', '$2a$10$pEBf.fUvT./PFpmsMY51qOUP2Clq9bnPZpBDqsmVY94kSAKM0otYS', 'admin@example.com', 'ADMIN', NULL),

  ('teacher1', '$2a$10$.7JVxiNew0Lr9Ht8V85XwOTmrzm5Vj3S6LVHeFq390Lvestxtp9RC', 'teacher1@example.com', 'TEACHER', 1),
  ('teacher2','$2a$10$.7JVxiNew0Lr9Ht8V85XwOTmrzm5Vj3S6LVHeFq390Lvestxtp9RC', 'teacher2@example.com','TEACHER', 2),

  ('alice', '$2a$10$JxU4ItjR/eUaq1wQjAeYMui0za6hmYgOAopcuGp2xt6iz7h/AyGxy', 'alice@example.com', 'STUDENT', 1),
  ('bob', '$2a$10$5RYY9YCuSrw6LvwHh6Y4KeuahNz1eg6Hd.6fxSBVww.L2.C2h.I0y', 'bob@example.com', 'STUDENT', 1),
  ('carol',   '$2a$10$73iRmzHVVx4EwN2XvCC1AOtZv20FZ2KaFpcMjXu6G2apPKOQ6bcB.', 'carol@example.com',   'STUDENT', 2),
  ('dave',    '$2a$10$.mi/jk52HKsam6CCjVK6vucI5cND8IIqPlmDCYiP3UkSURmSdPaBi', 'dave@example.com',    'STUDENT', 2),
  ('erin',    '$2a$10$UTmc2Sq.F3BrZQYWD7gyu.92w20qbp3jAWtX2GuSy/vE.2VR0XFJm', 'erin@example.com',    'STUDENT', 3);

-- === PASSWORDS ===
    -- admin: admin123
    -- teacher1: teacher123
    -- teacher2: teacher123
    -- alice: alice123
    -- bob: bob123
    -- carol: carol123
    -- dave: dave123
    -- erin: erin123
-- =================

-- === TEACHER <-> COURSES (M:N table) ===
INSERT INTO TEACHER_COURSES (COURSE_ID, TEACHER_ID)
VALUES
  (1, 2),
  (2, 2),
  (3, 3),
  (4, 3);

-- === TERMIN ===
INSERT INTO TERMIN (START_TIME, END_TIME)
VALUES
  ('2024-12-01T09:00:00', '2024-12-01T10:00:00'),
  ('2024-12-02T09:00:00', '2024-12-02T10:00:00'),
  ('2024-12-03T11:00:00', '2024-12-03T12:30:00'),
  ('2024-12-05T13:00:00', '2024-12-05T14:00:00');

-- === TESTS ===
INSERT INTO TEST (TITLE, DESCRIPTION, MAX_POINTS, MAX_ATTEMPTS, TEACHER_ID, COURSE_ID, GROUP_ID, TERMIN_ID)
VALUES
  ('Java Test 1', 'Basic Java knowledge', 10, 3, 2, 1, 1, 1),
  ('Java Test 2',   'OOP + Collections',                            15, 2, 2, 1, 1, 2),
  ('Spring Test 1', 'DI, Controllers, JPA basics',                  20, 2, 2, 2, 3, 3),
  ('DB Test 1',     'SQL joins, indexes, transactions',             18, 2, 3, 3, 4, 4);

-- === QUESTIONS ===
-- Test 1 (id=1)
INSERT INTO QUESTION (ID, POINT, QUESTION_TEXT, QUESTION_ORDER , TEST_ID)
VALUES
  (1, 3, 'What is JVM?', 1,1),
  (2, 4, 'Which types are primitive?', 2,1),
  (3, 3, 'Explain OOP.', 3,1);

-- Test 2 (id=2)
INSERT INTO QUESTION (ID, POINT, QUESTION_TEXT, QUESTION_ORDER , TEST_ID)
VALUES
    (4,5, 'What is polymorphism?',                1, 2),
    (5,5, 'Pick valid List implementations.',     2, 2),
    (6,5, 'Explain equals/hashCode contract.',    3, 2);

-- Spring Test 1 (id=3)
INSERT INTO QUESTION (ID, POINT, QUESTION_TEXT, QUESTION_ORDER , TEST_ID)
VALUES
    (7, 5, 'What is Dependency Injection?',        1, 3),
    (8, 5, 'What does @Transactional do?',         2, 3),
    (9, 5, 'Pick valid Spring stereotypes.',       3, 3),
    (10,5, 'Explain N+1 problem in ORM.',          4, 3);

-- DB Test 1 (id=4)
INSERT INTO QUESTION (ID, POINT, QUESTION_TEXT, QUESTION_ORDER , TEST_ID)
VALUES
    (11,6, 'Explain INNER vs LEFT JOIN.',          1, 4),
    (12,6, 'What is an index and when it helps?',  2, 4),
    (13,6, 'What is ACID?',                        3, 4);

-- === MULTIPLE CHOICE ===
INSERT INTO MULTIPLE_CHOICE (
  ID, OPTION_A, OPTION_B, OPTION_C, OPTION_D,
  CORRECT_A, CORRECT_B, CORRECT_C, CORRECT_D,
  MULTIPLE_CORRECT
)
VALUES
    -- Q2 (Test 1) Which types are primitive?
    (2, 'int', 'String', 'boolean', 'Apple', TRUE, FALSE, TRUE, FALSE, TRUE),

    -- Q5 (Test 2) Pick valid List implementations.
    (5, 'ArrayList', 'HashMap', 'LinkedList', 'StringBuilder', TRUE, FALSE, TRUE, FALSE, TRUE),

    -- Q9 (Spring Test 1) Pick valid Spring stereotypes.
    (9, '@Component', '@Service', '@Repository', '@Transactional', TRUE, TRUE, TRUE, FALSE, TRUE);

-- === OPEN QUESTIONS ===
INSERT INTO OPEN_QUESTION (ID, EXPECTED_ANSWER, MAX_LENGTH)
VALUES
    -- Q1 (Test 1) What is JVM?
    (1,  'Java Virtual Machine: runs Java bytecode, provides runtime environment, manages memory/GC.', 300),

    -- Q3 (Test 1) Explain OOP.
    (3,  'OOP principles: encapsulation, abstraction, inheritance, polymorphism; objects/classes.', 500),

    -- Q4 (Test 2) What is polymorphism?
    (4,  'Polymorphism: ability to treat objects via common interface; overriding enables runtime dispatch.', 400),

    -- Q6 (Test 2) Explain equals/hashCode contract.
    (6,  'If equals() true then hashCode must be equal; consistent; equals must be reflexive/symmetric/transitive.', 600),

    -- Q7 (Spring Test 1) What is Dependency Injection?
    (7,  'DI: dependencies are provided from outside (constructor/setter), often by IoC container (Spring).', 400),

    -- Q8 (Spring Test 1) What does @Transactional do?
    (8,  'Wraps method in transaction; commits/rolls back on exceptions; controls propagation/isolation.', 500),

    -- Q10 (Spring Test 1) Explain N+1 problem in ORM.
    (10, 'N+1: one query loads parents + N queries for children due to lazy loading; fix with fetch join/entity graph.', 650),

    -- Q11 (DB Test 1) Explain INNER vs LEFT JOIN.
    (11, 'INNER returns only matching rows; LEFT returns all left rows + NULLs for non-matching right rows.', 500),

    -- Q12 (DB Test 1) What is an index and when it helps?
    (12, 'Index is extra data structure (e.g., B-tree) that speeds lookups/sorts on selective queries; costs writes.', 550),

    -- Q13 (DB Test 1) What is ACID?
    (13, 'ACID: Atomicity, Consistency, Isolation, Durability (transaction guarantees).', 350);

-- === STUDENT TEST ===
INSERT INTO STUDENT_TEST (SCORE, TEST_ID, USER_ID, STATUS)
VALUES
    (NULL, 1, 4, 'IN_PROGRESS'),   -- alice on Java Test 1
    (7,    1, 5, 'EVALUATED'),     -- bob on Java Test 1

    (NULL, 2, 4, 'SUBMITTED'),     -- alice submitted Java Test 2 (pending open)
    (12,   2, 5, 'EVALUATED'),     -- bob evaluated Java Test 2

    (NULL, 3, 8, 'NOT_STARTED'),   -- erin on Spring Test 1
    (15,   3, 6, 'EVALUATED'),     -- carol evaluated Spring Test 1
    (NULL, 3, 7, 'SUBMITTED'),     -- dave submitted Spring Test 1

    (NULL, 4, 6, 'IN_PROGRESS'),   -- carol on DB Test 1
    (10,   4, 7, 'EVALUATED');     -- dave evaluated DB Test 1


-- === STUDENT ANSWERS ===
-- Attempt #1: (stId=1) alice on Java Test 1 -> IN_PROGRESS
INSERT INTO STUDENT_ANSWER
(SEL_A, SEL_B, SEL_C, SEL_D, AWARDED_POINTS, CORRECT, TEACHER_NOTE, STUDENT_TEST_ID, QUESTION_ID, ANSWER_ORDER )
VALUES
  (NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, 1), -- Q1 open (no grading yet)
  (TRUE, FALSE, TRUE, FALSE, NULL, NULL, NULL, 1, 2, 2); -- Q2 MCQ (chosen, not graded yet)

-- Attempt #2: (stId=2) bob on Java Test 1 -> EVALUATED, score=7
INSERT INTO STUDENT_ANSWER
(SEL_A, SEL_B, SEL_C, SEL_D, AWARDED_POINTS, CORRECT, TEACHER_NOTE, STUDENT_TEST_ID, QUESTION_ID, ANSWER_ORDER )
VALUES
  (NULL, NULL, NULL, NULL, 3, TRUE, 'Good explanation', 2, 1, 1), -- Q1 open (3/3)
  (TRUE, FALSE, TRUE, FALSE, 4, TRUE, NULL, 2, 2, 2), -- Q2 MCQ (4/4)
  (NULL, NULL, NULL, NULL, 0, FALSE, NULL, 2, 3, 3); -- Q3 open (0/3) => total 7

-- Attempt #3: (stId=3) alice on Java Test 2 -> SUBMITTED (open pending), score NULL
INSERT INTO STUDENT_ANSWER
(SEL_A, SEL_B, SEL_C, SEL_D, AWARDED_POINTS, CORRECT, TEACHER_NOTE, STUDENT_TEST_ID, QUESTION_ID, ANSWER_ORDER)
VALUES
    (NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 4, 1), -- Q4 open (pending)
    (TRUE, FALSE, TRUE, FALSE, 5, TRUE,  NULL, 3, 5, 2), -- Q5 MCQ graded (5/5)
    ( NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3, 6, 3); -- Q6 open (pending)

-- Attempt #4: (stId=4) bob on Java Test 2 -> EVALUATED, score=12 (из 15)
INSERT INTO STUDENT_ANSWER
(SEL_A, SEL_B, SEL_C, SEL_D, AWARDED_POINTS, CORRECT, TEACHER_NOTE, STUDENT_TEST_ID, QUESTION_ID, ANSWER_ORDER)
VALUES
    ( NULL, NULL, NULL, NULL, 4, TRUE,  'Good explanation.', 4, 4, 1),  -- Q4 open (4/5)
    ( TRUE, FALSE, TRUE, FALSE, 5, TRUE, NULL,               4, 5, 2),  -- Q5 MCQ (5/5)
    ( NULL, NULL, NULL, NULL, 3, TRUE,  'Missing edge cases.',4, 6, 3); -- Q6 open (3/5) => total 12

-- Attempt #6: (stId=6) carol on Spring Test 1 -> EVALUATED, score=15 (из 20)
INSERT INTO STUDENT_ANSWER
(SEL_A, SEL_B, SEL_C, SEL_D, AWARDED_POINTS, CORRECT, TEACHER_NOTE, STUDENT_TEST_ID, QUESTION_ID, ANSWER_ORDER)
VALUES
    ( NULL, NULL, NULL, NULL, 5, TRUE,  'Clear definition.',        6, 7, 1),  -- Q7 open (5/5)
    (NULL, NULL, NULL, NULL, 4, TRUE,  'Missed rollback nuances.', 6, 8, 2),  -- Q8 open (4/5)
    (TRUE, FALSE, TRUE, TRUE, 5, TRUE, NULL,                       6, 9, 3),  -- Q9 MCQ (5/5)
    ( NULL, NULL, NULL, NULL, 1, FALSE, 'Too short.',               6,10, 4);  -- Q10 open (1/5) => total 15

-- Attempt #7: (stId=7) dave on Spring Test 1 -> SUBMITTED (open pending), score NULL
INSERT INTO STUDENT_ANSWER
(SEL_A, SEL_B, SEL_C, SEL_D, AWARDED_POINTS, CORRECT, TEACHER_NOTE, STUDENT_TEST_ID, QUESTION_ID, ANSWER_ORDER)
VALUES
    (NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7, 7, 1),             -- Q7 open pending
    (NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7, 8, 2),             -- Q8 open pending
    (TRUE, FALSE, TRUE, TRUE, 5, TRUE,  NULL, 7, 9, 3),              -- Q9 MCQ graded
    (NULL, NULL, NULL, NULL, NULL, NULL, NULL, 7,10, 4);             -- Q10 open pending

-- Attempt #8: (stId=8) carol on DB Test 1 -> IN_PROGRESS
INSERT INTO STUDENT_ANSWER
(SEL_A, SEL_B, SEL_C, SEL_D, AWARDED_POINTS, CORRECT, TEACHER_NOTE, STUDENT_TEST_ID, QUESTION_ID, ANSWER_ORDER)
VALUES
    (NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8,11, 1);             -- Q11 open started, not graded

-- Attempt #9: (stId=9) dave on DB Test 1 -> EVALUATED, score=10 (из 18)
INSERT INTO STUDENT_ANSWER
(SEL_A, SEL_B, SEL_C, SEL_D, AWARDED_POINTS, CORRECT, TEACHER_NOTE, STUDENT_TEST_ID, QUESTION_ID, ANSWER_ORDER)
VALUES
    (NULL, NULL, NULL, NULL, 4, TRUE,  'Correct but brief.', 9,11, 1), -- Q11 open (4/6)
    (NULL, NULL, NULL, NULL, 6, TRUE,  'Good examples.',     9,12, 2), -- Q12 open (6/6)
    (NULL, NULL, NULL, NULL, 0, FALSE, 'Missed key points.',  9,13, 3); -- Q13 open (0/6) => total 10
