package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

@Entity
public class StudentTest extends AbstractEntity {
  @Basic(optional = false)
  @Column(name = "status",nullable = false)
  private String status;

  @Basic(optional = false)
  @Column(name = "score", nullable = false)
  private Integer score;

  @ManyToOne
  @JoinColumn(name = "username")
  private User user;

  @ManyToOne
  @JoinColumn(name = "termin")
  private Termin termin;
}
