package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

@Entity
public class StudentTest extends AbstractEntity {
  @Basic(optional = false)
  @Column(name = "score", nullable = false)
  private Integer score;

  @ManyToOne
  @JoinColumn(name = "username")
  private User user;

  @ManyToOne
  @JoinColumn(name = "termin")
  private Termin termin;

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

  public Termin getTermin() {
    return termin;
  }

  public void setTermin(Termin termin) {
    this.termin = termin;
  }

  public StatusTest getStatus() {
    return status;
  }

  public void setStatus(StatusTest status) {
    this.status = status;
  }
}
