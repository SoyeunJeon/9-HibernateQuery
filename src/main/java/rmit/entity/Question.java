package rmit.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String question;


    @OneToMany(mappedBy = "question")
    @Cascade(CascadeType.ALL)
    private List<Answer> answerList = new ArrayList<Answer>();

    public void addAnswer(String answer, boolean isCorrect){

        Answer answer1 = new Answer();
        answer1.setAnswer(answer);
        answer1.setCorrectAnswer(isCorrect);
        answer1.setQuestion(this);

        answerList.add(answer1);
    }

    public Question() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }
}
