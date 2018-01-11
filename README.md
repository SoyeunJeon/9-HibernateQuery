Hibernate query
Querying in Hibernate is quite complicated topic. Hibernate is powerful enough to offer flexible query mechanisms to fit our need. We use use a variety of methods to query data from our database. 

Given the following classes:

        @Entity
        public class Answer {

           @Id
           @Column
           @GeneratedValue(strategy = GenerationType.IDENTITY)
           private int id;

           @Column
           private String answer;

           @ManyToOne
           private Question question;

           @Column
           private boolean isCorrectAnswer;
        }


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

Criteria interface
This is the simplest method to query data using Hibernate.  For example, to query all questions with a specific content:

                public List<Question> findQuestions(String search){
                   return sessionFactory.getCurrentSession().createCriteria(Question.class)
                           .add(Restrictions.like("question", search, MatchMode.ANYWHERE))
                           .list();
                }

We create a criteria and add to the criteria different Restriction. We can restrict result with like, eq, gt, lt etc. 

The limitation of this method is that we can only query, not update data. There are also some problems with performance. 

Direct SQL language (SQL)

This method does not leverage anything from ORM. Basically, you must write tedious SQL statements. 

                public List<Question> findQuestions3(String search){
                   return sessionFactory.getCurrentSession().createSQLQuery("select * from question where question like '%"+search+"%'")
                           .list();
                }

There is also problem with SQL Injection when using this method. 

Hibernate query language (HQL)

The recommended method is HQL. It is flexible and powerful. The following example illustrates a join between Question and Answer class. We try to find all questions that have content and answers that match a search condition. HQL allows us to join a class with a collection without having to specify the join condition. 

                public List<Question> findQuestions2(String search){
                   return sessionFactory.getCurrentSession()
                           .createQuery("from Question as q inner join q.answerList as a where q.question like:question or a.answer like:answer ")
                           .setString("question", "%"+search+"%")
                           .setString("answer", "%"+search+"%")
                           .list();
                }
For querying the other end, it is even easy as we do not need any join:

                public List<Answer> findAnswers(String search){
                   return sessionFactory.getCurrentSession()
                           .createQuery("from Answer as a where a.question.question like:question or a.answer like:answer ")
                           .setString("question", "%"+search+"%")
                           .setString("answer", "%"+search+"%")
                           .list();
                }

The difference between the two examples is the first returns an question object that contains a list of answer while the second returns a collection of answers. (And each answer has a property that links to a question).

How to call the method in main:

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
"# 9-HibernateQuery" 
