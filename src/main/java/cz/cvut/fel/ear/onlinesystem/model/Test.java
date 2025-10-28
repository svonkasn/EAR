package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.util.List;

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

  @ManyToOne
  @JoinColumn(name = "teacher")
  private User teacher;

  @ManyToOne
  @JoinColumn(name = "course")
  private Course course;

  @OneToMany(mappedBy = "test")
  private List<Termin> termins;

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

  public List<Termin> getTermins() {
    return termins;
  }

  public void setTermins(List<Termin> termins) {
    this.termins = termins;
  }
}
