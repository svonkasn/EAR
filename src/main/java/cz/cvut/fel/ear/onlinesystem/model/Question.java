package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Question extends AbstractEntity{
  @Basic(optional = false)
  @Column(name = "point", nullable = false)
  private Integer point;

  @Basic(optional = false)
  @Column(name = "question", nullable = false)
  private String question;

  @ManyToOne
  @JoinColumn(name = "test")
  private Test test;


  public Integer getPoint() {
    return point;
  }

  public void setPoint(Integer point) {
    this.point = point;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public Test getTest() {
    return test;
  }

  public void setTest(Test test) {
    this.test = test;
  }
}
