package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class OpenQuestion extends Question {
    @Column(name="expected_answer")
    private String expectedAnswer;

    @Column(name="max_length")
    private Integer maxLength;
}
