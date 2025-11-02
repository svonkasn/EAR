package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class OpenQuestion extends Question {
    @Column(name="expected_answer")
    private String expectedAnswer;

    @Column(name="max_length")
    private Integer maxLength;

    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    public void setExpectedAnswer(String expectedAnswer) {
        this.expectedAnswer = expectedAnswer;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
