package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class StudentTest extends AbstractEntity {
  @Basic(optional = false)
  @Column(name = "score")
  private Integer score;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "test_id", nullable = false)
  private Test test;

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
}
