package rmit.service;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import rmit.entity.Answer;
import rmit.entity.Question;

import java.util.List;

/**
 * Created by CoT on 11/9/17.
 */
@Transactional
public class CourseService {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addQuestion(Question question){
        sessionFactory.getCurrentSession().save(question);
    }

    public List<Question> findQuestions(String search){
        return sessionFactory.getCurrentSession().createCriteria(Question.class)
                .add(Restrictions.like("question", search, MatchMode.ANYWHERE))
                .list();
    }

    public List<Question> findQuestions2(String search){
        return sessionFactory.getCurrentSession()
                .createQuery("from Question as q inner join q.answerList as a where q.question like:question or a.answer like:answer ")
                .setString("question", "%"+search+"%")
                .setString("answer", "%"+search+"%")
                .list();
    }

    public List<Answer> findAnswers(String search){
        return sessionFactory.getCurrentSession()
                .createQuery("from Answer as a where a.question.question like:question or a.answer like:answer ")
                .setString("question", "%"+search+"%")
                .setString("answer", "%"+search+"%")
                .list();
    }


    public List<Question> findQuestions3(String search){
        return sessionFactory.getCurrentSession().createSQLQuery("select * from question where question like '%"+search+"%'")
                .list();
    }

}
