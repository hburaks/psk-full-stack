package com.hburak_dev.psk_full_stack.config.data;

import com.hburak_dev.psk_full_stack.blog.Blog;
import com.hburak_dev.psk_full_stack.blog.BlogRepository;
import com.hburak_dev.psk_full_stack.choice.Choice;
import com.hburak_dev.psk_full_stack.choice.ChoiceRepository;
import com.hburak_dev.psk_full_stack.comment.Comment;
import com.hburak_dev.psk_full_stack.comment.CommentRepository;
import com.hburak_dev.psk_full_stack.question.AnswerType;
import com.hburak_dev.psk_full_stack.question.Question;
import com.hburak_dev.psk_full_stack.question.QuestionRepository;
import com.hburak_dev.psk_full_stack.role.Role;
import com.hburak_dev.psk_full_stack.role.RoleRepository;
import com.hburak_dev.psk_full_stack.session.Session;
import com.hburak_dev.psk_full_stack.session.SessionRepository;
import com.hburak_dev.psk_full_stack.session.SessionStatusType;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplate;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplateRepository;
import com.hburak_dev.psk_full_stack.user.User;
import com.hburak_dev.psk_full_stack.user.UserRepository;
import com.hburak_dev.psk_full_stack.Util;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DataInitializer {

        private final SessionRepository sessionRepository;
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final TestTemplateRepository testTemplateRepository;
        private final CommentRepository commentRepository;
        private final BlogRepository blogRepository;
        private final PasswordEncoder passwordEncoder;
        private final QuestionRepository questionRepository;
        private final ChoiceRepository choiceRepository;

        public DataInitializer(SessionRepository sessionRepository, UserRepository userRepository,
                        RoleRepository roleRepository, TestTemplateRepository testTemplateRepository,
                        CommentRepository commentRepository, BlogRepository blogRepository,
                        PasswordEncoder passwordEncoder, QuestionRepository questionRepository,
                        ChoiceRepository choiceRepository) {
                this.sessionRepository = sessionRepository;
                this.userRepository = userRepository;
                this.roleRepository = roleRepository;
                this.testTemplateRepository = testTemplateRepository;
                this.commentRepository = commentRepository;
                this.blogRepository = blogRepository;
                this.passwordEncoder = passwordEncoder;
                this.questionRepository = questionRepository;
                this.choiceRepository = choiceRepository;
        }

        @EventListener(ApplicationReadyEvent.class)
        public void run() throws Exception {
                if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                        roleRepository.save(Role.builder().name("ROLE_USER").build());
                }
                if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                        roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
                }

                if (userRepository.findByEmail("admin@psk.com").isEmpty()) {
                        userRepository.save(User.builder()
                                        .email("admin@psk.com")
                                        .password(passwordEncoder.encode("password"))
                                        .firstname("Admin")
                                        .lastname("User")
                                        .phoneNumber("5551234567")
                                        .birthYear("1985")
                                        .enabled(true)
                                        .accountLocked(false)
                                        .roles(List.of(roleRepository.findByName("ROLE_ADMIN").get()))
                                        .build());
                }

                if (userRepository.findByEmail("user@psk.com").isEmpty()) {
                        userRepository.save(User.builder()
                                        .email("user@psk.com")
                                        .password(passwordEncoder.encode("password"))
                                        .firstname("Test")
                                        .lastname("User")
                                        .phoneNumber("5559876543")
                                        .birthYear("1995")
                                        .enabled(true)
                                        .accountLocked(false)
                                        .roles(List.of(roleRepository.findByName("ROLE_USER").get()))
                                        .build());
                }

                User mockUser = userRepository.findAll()
                                .stream()
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("No users found"));

                if (blogRepository.count() == 0) {
                        List<Blog> mockBlogs = new ArrayList<>();
                        mockBlogs.add(Blog.builder()
                                        .title("Psikolojik Sağlık ve İyi Oluş")
                                        .subTitle("Modern yaşamda psikolojik sağlığımızı korumak için önemli ipuçları")
                                        .text(
                                                        "Günümüzün hızlı tempolu yaşam tarzında, psikolojik sağlığımızı korumak her zamankinden daha önemli hale gelmiştir. Stres, kaygı ve depresyon gibi psikolojik sorunlarla başa çıkabilmek için düzenli egzersiz yapmak, sağlıklı beslenmek ve yeterli uyku almak oldukça önemlidir. Ayrıca, sosyal ilişkilerimizi güçlendirmek, hobiler edinmek ve düzenli olarak meditasyon yapmak da psikolojik sağlığımızı destekleyen önemli faktörlerdir. Bu yazıda, günlük yaşamda uygulayabileceğiniz pratik öneriler ve yaşam tarzı değişiklikleri hakkında detaylı bilgiler bulacaksınız.")
                                        .shareable(true)
                                        .createdBy(mockUser.getId())
                                        .build());
                        mockBlogs.add(Blog.builder()
                                        .title("Stres Yönetimi ve Başa Çıkma Teknikleri")
                                        .subTitle("Günlük hayatta stresi yönetmenin etkili yolları")
                                        .text(
                                                        "Stres, modern yaşamın kaçınılmaz bir parçası haline gelmiştir. İş hayatı, aile sorumlulukları ve sosyal ilişkiler gibi faktörler stres seviyemizi artırabilir. Ancak, doğru teknikler ve yaklaşımlarla stresi etkili bir şekilde yönetmek mümkündür. Nefes egzersizleri, progresif kas gevşetme teknikleri ve mindfulness uygulamaları, stres yönetiminde kullanabileceğimiz etkili araçlardır. Bu yazıda, stres yönetimi konusunda bilimsel olarak kanıtlanmış yöntemler ve günlük hayatta uygulayabileceğiniz pratik öneriler yer almaktadır.")
                                        .shareable(true)
                                        .createdBy(mockUser.getId())
                                        .build());
                        mockBlogs.add(Blog.builder()
                                        .title("İlişkilerde Duygusal Zeka")
                                        .subTitle("Sağlıklı ilişkiler kurmanın anahtarı: Duygusal zeka")
                                        .text(
                                                        "Duygusal zeka, hem kendimizin hem de başkalarının duygularını anlama ve yönetme becerisidir. İlişkilerimizde duygusal zekanın rolü büyüktür. Empati kurabilmek, etkili iletişim becerilerine sahip olmak ve duygusal farkındalık geliştirmek, sağlıklı ve uzun ömürlü ilişkiler kurmanın temel taşlarıdır. Bu makalede, duygusal zekanın beş temel bileşeni olan öz farkındalık, öz düzenleme, motivasyon, empati ve sosyal beceriler hakkında detaylı bilgiler ve bu becerileri geliştirmek için pratik öneriler bulacaksınız.")
                                        .shareable(true)
                                        .createdBy(mockUser.getId())
                                        .build());
                        blogRepository.saveAll(mockBlogs);
                }

                TestTemplate depressionTest = TestTemplate.builder()
                                .title("Depresyon Değerlendirmesi")
                                .subTitle("Depresyon belirtilerini değerlendir")
                                .createdBy(mockUser.getId())
                                .isActive(true)
                                .build();

                TestTemplate anxietyTest = TestTemplate.builder()
                                .title("Anksiyete Taraması")
                                .subTitle("Anksiyete seviyenizi kontrol edin")
                                .createdBy(mockUser.getId())
                                .isActive(true)
                                .build();

                depressionTest = testTemplateRepository.save(depressionTest);
                anxietyTest = testTemplateRepository.save(anxietyTest);

                List<Comment> depressionComments = commentRepository.saveAll(List.of(
                                Comment.builder().title("Yüksek").score(100).text("Yüksek depresyon seviyesi")
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder().title("Orta").score(50).text("Orta depresyon seviyesi")
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder().title("Düşük").score(0).text("Düşük depresyon seviyesi")
                                                .createdBy(mockUser.getId())
                                                .build()));

                List<Comment> anxietyComments = commentRepository.saveAll(List.of(
                                Comment.builder().title("Yüksek").score(100).text("Yüksek anksiyete seviyesi")
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder().title("Orta").score(50).text("Orta anksiyete seviyesi")
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder().title("Düşük").score(0).text("Düşük anksiyete seviyesi")
                                                .createdBy(mockUser.getId())
                                                .build()));

                // Assuming TestTemplate has a comments field to set
                // depressionTest.setComments(depressionComments);
                // anxietyTest.setComments(anxietyComments);

                List<Choice> depressionChoices1 = List.of(
                                Choice.builder().answerType(AnswerType.ANSWER_A).text("Hiçbir zaman")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_B).text("Bazen")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_C).text("Sık sık")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_D).text("Her zaman")
                                                .createdBy(mockUser.getId()).build());

                List<Choice> depressionChoices2 = List.of(
                                Choice.builder().answerType(AnswerType.ANSWER_A).text("Hayır")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_B).text("Ara sıra")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_C).text("Sıklıkla")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_D).text("Her gece")
                                                .createdBy(mockUser.getId()).build());

                List<Question> depressionQuestions = new ArrayList<>();
                depressionQuestions.add(Question.builder()
                                .text("Ne sıklıkla üzgün veya mutsuz hissediyorsunuz?")
                                .createdBy(mockUser.getId())
                                .testTemplateId(depressionTest.getId().longValue())
                                .choices(depressionChoices1)
                                .build());

                depressionQuestions.add(Question.builder()
                                .text("Uyku problemi yaşıyor musunuz?")
                                .createdBy(mockUser.getId())
                                .testTemplateId(depressionTest.getId().longValue())
                                .choices(depressionChoices2)
                                .build());

                questionRepository.saveAll(depressionQuestions);
                // depressionTest.setQuestions(depressionQuestions);
                testTemplateRepository.save(depressionTest);

                List<Choice> anxietyChoices1 = List.of(
                                Choice.builder().answerType(AnswerType.ANSWER_A).text("Nadiren")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_B).text("Bazen")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_C).text("Sık sık")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_D).text("Çok sık")
                                                .createdBy(mockUser.getId()).build());

                List<Choice> anxietyChoices2 = List.of(
                                Choice.builder().answerType(AnswerType.ANSWER_A).text("Hiçbir zaman")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_B).text("Nadiren")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_C).text("Bazen")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_D).text("Sıklıkla")
                                                .createdBy(mockUser.getId()).build());

                List<Question> anxietyQuestions = new ArrayList<>();
                anxietyQuestions.add(Question.builder()
                                .text("Ne sıklıkla gergin veya endişeli hissediyorsunuz?")
                                .createdBy(mockUser.getId())
                                .testTemplateId(anxietyTest.getId().longValue())
                                .choices(anxietyChoices1)
                                .build());

                anxietyQuestions.add(Question.builder()
                                .text("Panik atak yaşıyor musunuz?")
                                .createdBy(mockUser.getId())
                                .testTemplateId(anxietyTest.getId().longValue())
                                .choices(anxietyChoices2)
                                .build());

                questionRepository.saveAll(anxietyQuestions);
                // anxietyTest.setQuestions(anxietyQuestions);
                testTemplateRepository.save(anxietyTest);

                if (sessionRepository.findFirstByDateAfterAndSessionStatusNotOrderByDateAsc(LocalDateTime.now(),
                                SessionStatusType.CANCELED) == null) {
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime date1 = now.with(DayOfWeek.SATURDAY).withHour(10).withMinute(0);
                        List<Session> mockSessions = new ArrayList<>();

                        if (!sessionRepository.existsByDate(date1)) {
                                mockSessions.add(Session.builder()
                                                .createdBy(mockUser.getId())
                                                .date(date1)
                                                .user(mockUser)
                                                .sessionStatus(SessionStatusType.AWAITING_THERAPIST_APPROVAL)
                                                .isSessionPaid(true)
                                                .isMock(true)
                                                .build());
                        }

                        if (!mockSessions.isEmpty()) {
                                sessionRepository.saveAll(mockSessions);
                        }

                }
                if (sessionRepository.count() == 0) {

                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime monday = now.with(DayOfWeek.MONDAY).withHour(10).withMinute(0);

                        LocalDateTime date1 = Util.toFullHour(monday).plusHours(2);
                        LocalDateTime date2 = Util.toFullHour(monday.plusDays(1).plusHours(5));
                        LocalDateTime date3 = Util.toFullHour(monday.plusDays(3).plusHours(5));

                        List<Session> mockSessions = new ArrayList<>();

                        if (!sessionRepository.existsByDate(date1)) {
                                mockSessions.add(Session.builder()
                                                .createdBy(mockUser.getId())
                                                .date(date1)
                                                .user(mockUser)
                                                .sessionStatus(SessionStatusType.AWAITING_THERAPIST_APPROVAL)
                                                .isSessionPaid(true)
                                                .isMock(true)
                                                .build());
                        }

                        if (!sessionRepository.existsByDate(date2)) {
                                mockSessions.add(Session.builder()
                                                .createdBy(mockUser.getId())
                                                .date(date2)
                                                .user(mockUser)
                                                .sessionStatus(SessionStatusType.COMPLETED)
                                                .isSessionPaid(false)
                                                .isMock(true)
                                                .build());
                        }

                        if (!sessionRepository.existsByDate(date3)) {
                                mockSessions.add(Session.builder()
                                                .createdBy(mockUser.getId())
                                                .date(date3)
                                                .user(mockUser)
                                                .sessionStatus(SessionStatusType.CANCELED)
                                                .isSessionPaid(true)
                                                .isMock(true)
                                                .build());
                        }
                        if (!mockSessions.isEmpty()) {
                                sessionRepository.saveAll(mockSessions);
                        }
                }
        }
}