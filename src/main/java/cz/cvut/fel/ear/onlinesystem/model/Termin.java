package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Termin extends AbstractEntity{
  @Basic(optional = false)
  @Column(name = "startTime", nullable = false)
  private LocalDateTime startTime;
  @Basic(optional = false)
  @Column(name = "endTime", nullable = false)
  private LocalDateTime endTime;

  @PrePersist
  @PreUpdate
  private void validate() {
    if (startTime == null || endTime == null || !startTime.isBefore(endTime)) {
      throw new IllegalStateException("Termin: startTime must be before endTime");
    }
  }

  @OneToMany(mappedBy = "termin")
  private Set<Test> tests = new HashSet<>();

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public Set<Test> getTests() {
    return tests;
  }

  public void setTests(Set<Test> tests) {
    this.tests = tests;
  }
}
