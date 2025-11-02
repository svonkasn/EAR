package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Question extends AbstractEntity{
  @Basic(optional = false)
  @Column(name = "point", nullable = false)
  private Integer point;

  @Basic(optional = false)
  @Column(name = "questionText", nullable = false)
  private String questionText;

  @ManyToOne
  @JoinColumn(name = "test_id")
  private Test test;


  public Integer getPoint() {
    return point;
  }

  public void setPoint(Integer point) {
    this.point = point;
  }

  public String getQuestion() {
    return questionText;
  }

  public void setQuestion(String questionText) {
    this.questionText = questionText;
  }

  public Test getTest() {
    return test;
  }

  public void setTest(Test test) {
    this.test = test;
  }
}
