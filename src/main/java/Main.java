
import rmit.config.AppConfig;
import rmit.entity.Question;
import rmit.service.CourseService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * Created by CoT on 10/13/17.
 */
public class Main {


    public static void main(String[] args){

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        CourseService courseService = (CourseService) context.getBean("courseService");

        Question question = new Question();
        question.setQuestion("How are you?");
        question.addAnswer("I am fine", false);
        question.addAnswer("I am bad", false);
        question.addAnswer("I am ok", true);

        courseService.addQuestion(question);

        System.out.println(courseService.findAnswers("I").size());
    }

}
