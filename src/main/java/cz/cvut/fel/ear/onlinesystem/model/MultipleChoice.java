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

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public Boolean getCorrectA() {
        return correctA;
    }

    public void setCorrectA(Boolean correctA) {
        this.correctA = correctA;
    }

    public Boolean getCorrectB() {
        return correctB;
    }

    public void setCorrectB(Boolean correctB) {
        this.correctB = correctB;
    }

    public Boolean getCorrectC() {
        return correctC;
    }

    public void setCorrectC(Boolean correctC) {
        this.correctC = correctC;
    }

    public boolean isMultipleCorrect() {
        return multipleCorrect;
    }

    public void setMultipleCorrect(boolean multipleCorrect) {
        this.multipleCorrect = multipleCorrect;
    }

    public Boolean getCorrectD() {
        return correctD;
    }

    public void setCorrectD(Boolean correctD) {
        this.correctD = correctD;
    }

    public boolean matches(Boolean selA, Boolean selB, Boolean selC, Boolean selD){
        return eq(selA, correctA) && eq(selB, correctB) && eq(selC, correctC) && eq(selD, correctD);
    }
    private static boolean eq(Boolean a, Boolean b){ return Boolean.TRUE.equals(a) == Boolean.TRUE.equals(b); }
}
