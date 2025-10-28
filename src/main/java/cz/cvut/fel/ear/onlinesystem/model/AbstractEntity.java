package cz.cvut.fel.ear.onlinesystem.model;
import jakarta.persistence.*;

import java.io.Serializable;
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
  @Id
  @GeneratedValue
  private Integer id;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
