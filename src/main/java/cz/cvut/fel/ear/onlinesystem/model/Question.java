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

}
