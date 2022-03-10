package fr.coyotejeje;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            StudentRepository studentRepository,
            StudentIdCardRepository studentIdCardRepository) {
        return args -> {
            Faker faker = new Faker();

            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@coyotejeje.fr", firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55));

            student.addBook(
                    new Book("Clean code", LocalDateTime.now().minusDays(4)));
            student.addBook(
                    new Book("Spring Boot", LocalDateTime.now()));
            student.addBook(
                    new Book("Java 17", LocalDateTime.now().minusYears(1)));

            StudentIdCard studentIdCard = new StudentIdCard("123456789", student);

            student.setStudentIdCard(studentIdCard);

            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L, 1L),
                    student,
                    new Course("Java", "IT"),
                    LocalDateTime.now()
            ));

            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L, 2L),
                    student,
                    new Course("Ruby on Rails", "IT"),
                    LocalDateTime.now().minusDays(15)
            ));

            studentRepository.save(student);

            studentRepository.findById(1L)
                    .ifPresent(s -> {
                        System.out.println("Fetch book lazy...");
                        List<Book> books = student.getBooks();
                        books.forEach(book -> {
                            System.out.println(
                                    s.getFirstName() + " borrowed " + book.getBookName());
                        });
                    });
//
//            studentIdCardRepository.findById(1L)
//                    .ifPresent(System.out::println);

            //studentRepository.deleteById(1L);

            //generateRandomStudents(studentRepository);

            /*
            PageRequest pageRequest = PageRequest.of(
                    0,
                    5,
                    Sort.by("firstName").ascending());

            Page<Student> page = studentRepository.findAll(pageRequest);
            System.out.println(page);

            */
            //sorting(studentRepository);

            /*
            Student john = new Student(
                    "John",
                    "Doe",
                    "john@yahoo.fr",
                    30
            );

            Student john2 = new Student(
                    "John",
                    "Doe",
                    "john2@yahoo.fr",
                    21
            );

            Student emilie = new Student(
                    "Emilie",
                    "Win",
                    "emilie@gmail.com",
                    25
            );

            //System.out.println("Adding John and Emilie");
            studentRepository.saveAll(List.of(john, john2, emilie));

            studentRepository
                    .findStudentByEmail("emilie@gmail.com")
                    .ifPresentOrElse(
                            System.out::println,
                            ()-> System.out.println("Student with email emilie@gmail.com not found"));

            studentRepository
                    .SelectStudentWhereFirstNameAndAgeGreaterOrEqual(
                    "John",
                    18)
                    .forEach(System.out::println);

            studentRepository
                    .SelectStudentWhereFirstNameAndAgeGreaterOrEqualNative(
                            "John",
                            18)
                    .forEach(System.out::println);

            System.out.println("Deleting John 2");
            System.out.println(studentRepository.deleteStudentById(2L));
            */
            /*
            System.out.print("Number of students: ");
            System.out.println(studentRepository.count());

            studentRepository
                    .findById(2L)
                    .ifPresentOrElse(
                            System.out::println,
                            ()-> System.out.println("Student with ID 2 not found"));

            studentRepository
                    .findById(3L)
                    .ifPresentOrElse(
                            System.out::println,
                            ()-> System.out.println("Student with ID 3 not found"));

            System.out.println("Select all students");
            List<Student> students = studentRepository.findAll();
            students.forEach(System.out::println);

            System.out.println("Delete John");
            studentRepository.deleteById(1L);

            System.out.print("Number of students: ");
            System.out.println(studentRepository.count());
             */
        };
    }

    private void sorting(StudentRepository studentRepository) {
        //Sort sort = Sort.by(Sort.Direction.DESC, "firstName");
        Sort sort = Sort.by("firstName").ascending()
                .and(Sort.by("age").descending());

        studentRepository.findAll(sort)
                .forEach(student -> {
                    System.out.println(student.getFirstName() + " " + student.getAge());
                });
    }

    private void generateRandomStudents(StudentRepository studentRepository) {
        Faker faker = new Faker();
        for (int i = 0; i < 20 ; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@coyotejeje.fr", firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55));
            studentRepository.save(student);
        }
    }

}

