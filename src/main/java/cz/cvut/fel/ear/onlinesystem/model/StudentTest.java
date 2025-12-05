package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class StudentTest extends AbstractEntity {
  @Column(name = "score")
  private Integer score;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "test_id", nullable = false)
  private Test test;

  @OneToMany(mappedBy = "studentTest", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<StudentAnswer> answers = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private StatusTest status;

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public StatusTest getStatus() {
    return status;
  }

  public Test getTest() { return test; }

  public void setTest(Test test) { this.test = test; }

  public void setStatus(StatusTest status) {
    this.status = status;
  }

  public void addAnswer(StudentAnswer a) {
    answers.add(a);
    a.setStudentTest(this);
  }
  public void removeAnswer(StudentAnswer a) {
    answers.remove(a);
    a.setStudentTest(null);
  }
}
