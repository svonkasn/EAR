package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class Test extends AbstractEntity {
  @Basic(optional = false)
  @Column(name = "title", nullable = false)
  private String title;

  @Basic(optional = false)
  @Column(name = "description", nullable = false)
  private String description;

  @Basic(optional = false)
  @Column(name = "maxPoints", nullable = false)
  private Integer maxPoints;

  @Basic(optional = false)
  @Column(name = "maxAttempts", nullable = false)
  private Integer maxAttempts;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "teacher_id")
  private User teacher;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "course_id")
  private Course course;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "group_id", nullable = false)
  private GroupOfStudent group;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "termin_id")
  private Termin termin;

  @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Question> questions = new ArrayList<>();

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getMaxPoints() {
    return maxPoints;
  }

  public void setMaxPoints(Integer maxPoints) {
    this.maxPoints = maxPoints;
  }

  public Integer getMaxAttempts() {
    return maxAttempts;
  }

  public void setMaxAttempts(Integer maxAttempts) {
    this.maxAttempts = maxAttempts;
  }

  public User getTeacher() {
    return teacher;
  }

  public void setTeacher(User teacher) {
    this.teacher = teacher;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public Termin getTermin() {
    return termin;
  }

  public void setTermin(Termin termin) {
    this.termin = termin;
  }

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  public GroupOfStudent getGroup() { return group; }

  public void setGroup(GroupOfStudent group) { this.group = group; }
}
