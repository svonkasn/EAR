package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Question extends AbstractEntity{
  @Basic(optional = false)
  @Column(name = "point", nullable = false)
  private Integer point;

  @Basic(optional = false)
  @Column(name = "questionText", nullable = false)
  private String questionText;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "test_id")
  private Test test;

  @OneToMany(mappedBy = "question")
  private List<StudentAnswer> answers = new ArrayList<>();

  public Integer getPoint() {
    return point;
  }

  public void setPoint(Integer point) {
    this.point = point;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public Test getTest() {
    return test;
  }

  public void setTest(Test test) {
    this.test = test;
  }
}
