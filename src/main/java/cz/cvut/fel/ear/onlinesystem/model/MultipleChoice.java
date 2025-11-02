package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class MultipleChoice extends Question{
    @Column(name = "option_a")
    private String optionA;

    @Column(name = "option_b")
    private String optionB;

    @Column(name = "option_c")
    private String optionC;

    @Column(name = "option_d")
    private String optionD;

    @Column(name = "correct_a")
    private Boolean correctA;

    @Column(name = "correct_b")
    private Boolean correctB;

    @Column(name = "correct_c")
    private Boolean correctC;

    @Column(name = "correct_d")
    private Boolean correctD;

    @Column(name = "multiple_correct", nullable = false)
    private boolean multipleCorrect;
}
