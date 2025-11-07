package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class StudentAnswer extends AbstractEntity {
    @Column(name = "awarded_points")
    private Integer awardedPoints;

    @Column(name = "correct")
    private Boolean correct;

    @Lob
    @Column(name = "teacher_note")
    private String teacherNote;

    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "student_test_id")
    private StudentTest studentTest;

    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public Integer getAwardedPoints() {
        return awardedPoints;
    }

    public void setAwardedPoints(Integer awardedPoints) {
        this.awardedPoints = awardedPoints;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public StudentTest getStudentTest() {
        return studentTest;
    }

    public void setStudentTest(StudentTest studentTest) {
        this.studentTest = studentTest;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getTeacherNote() {
        return teacherNote;
    }

    public void setTeacherNote(String teacherNote) {
        this.teacherNote = teacherNote;
    }
}
