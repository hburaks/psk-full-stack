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
import com.hburak_dev.psk_full_stack.scoring.ScoringStrategyType;
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

                // Create test templates with different scoring strategies (only if they don't exist)
                if (testTemplateRepository.count() == 0) {
                        TestTemplate depressionTest = TestTemplate.builder()
                                .title("Depresyon Değerlendirmesi")
                                .subTitle("Depresyon belirtilerini değerlendir")
                                .createdBy(mockUser.getId())
                                .isActive(true)
                                .scoringStrategy(ScoringStrategyType.WEIGHTED) // Clinical assessment uses weighted scoring
                                .build();

                TestTemplate anxietyTest = TestTemplate.builder()
                                .title("Anksiyete Taraması")
                                .subTitle("Anksiyete seviyenizi kontrol edin")
                                .createdBy(mockUser.getId())
                                .isActive(true)
                                .scoringStrategy(ScoringStrategyType.PERCENTAGE) // Anxiety test uses percentage scoring
                                .build();

                // Add a third test with simple linear scoring
                TestTemplate personalityTest = TestTemplate.builder()
                                .title("Kişilik Değerlendirmesi")
                                .subTitle("Temel kişilik özelliklerinizi keşfedin")
                                .createdBy(mockUser.getId())
                                .isActive(true)
                                .scoringStrategy(ScoringStrategyType.SIMPLE_LINEAR) // Personality test uses simple linear
                                .build();

                depressionTest = testTemplateRepository.save(depressionTest);
                anxietyTest = testTemplateRepository.save(anxietyTest);
                personalityTest = testTemplateRepository.save(personalityTest);

                // Create comments for Depression Test (WEIGHTED scoring: A=0, B=1, C=3, D=6, E=10)
                // For 2 questions, max score would be 20 (2 * 10)
                List<Comment> depressionComments = List.of(
                                Comment.builder()
                                                .title("Minimal Depresyon")
                                                .score(0)
                                                .text("Sonuçlarınız minimal depresyon belirtileri gösteriyor. Ruh haliniz genel olarak olumlu görünüyor.")
                                                .testTemplateId(depressionTest.getId().longValue())
                                                .testTemplate(depressionTest)
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder()
                                                .title("Hafif Depresyon")
                                                .score(4)
                                                .text("Hafif depresyon belirtileri tespit edildi. Yaşam tarzı değişiklikleri ve destek yararlı olabilir.")
                                                .testTemplateId(depressionTest.getId().longValue())
                                                .testTemplate(depressionTest)
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder()
                                                .title("Orta Depresyon")
                                                .score(8)
                                                .text("Orta düzeyde depresyon belirtileri mevcut. Profesyonel destek almanız önerilir.")
                                                .testTemplateId(depressionTest.getId().longValue())
                                                .testTemplate(depressionTest)
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder()
                                                .title("Ciddi Depresyon")
                                                .score(15)
                                                .text("Ciddi depresyon belirtileri tespit edildi. Mutlaka bir uzmanla görüşmeniz gerekiyor.")
                                                .testTemplateId(depressionTest.getId().longValue())
                                                .testTemplate(depressionTest)
                                                .createdBy(mockUser.getId())
                                                .build());

                // Create comments for Anxiety Test (PERCENTAGE scoring: 0-100%)
                List<Comment> anxietyComments = List.of(
                                Comment.builder()
                                                .title("Düşük Anksiyete")
                                                .score(0)
                                                .text("Anksiyete seviyeniz normaldir. Stres yönetimi tekniklerini öğrenmek faydalı olabilir.")
                                                .testTemplateId(anxietyTest.getId().longValue())
                                                .testTemplate(anxietyTest)
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder()
                                                .title("Hafif Anksiyete")
                                                .score(25)
                                                .text("Hafif düzeyde anksiyete belirtileri var. Nefes egzersizleri ve meditasyon yararlı olabilir.")
                                                .testTemplateId(anxietyTest.getId().longValue())
                                                .testTemplate(anxietyTest)
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder()
                                                .title("Orta Anksiyete")
                                                .score(50)
                                                .text("Orta düzeyde anksiyete mevcut. Stres yönetimi teknikleri ve destek almanız önerilir.")
                                                .testTemplateId(anxietyTest.getId().longValue())
                                                .testTemplate(anxietyTest)
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder()
                                                .title("Yüksek Anksiyete")
                                                .score(75)
                                                .text("Yüksek anksiyete seviyesi tespit edildi. Profesyonel yardım almanız önemlidir.")
                                                .testTemplateId(anxietyTest.getId().longValue())
                                                .testTemplate(anxietyTest)
                                                .createdBy(mockUser.getId())
                                                .build());

                // Create comments for Personality Test (SIMPLE_LINEAR scoring: A=1, B=2, C=3, D=4, E=5)
                // For 2 questions, max score would be 10 (2 * 5)
                List<Comment> personalityComments = List.of(
                                Comment.builder()
                                                .title("Introvert Kişilik")
                                                .score(0)
                                                .text("Daha içe dönük bir kişilik yapısına sahipsiniz. Derin düşünme ve kaliteli ilişkileri tercih ediyorsunuz.")
                                                .testTemplateId(personalityTest.getId().longValue())
                                                .testTemplate(personalityTest)
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder()
                                                .title("Dengeli Kişilik")
                                                .score(4)
                                                .text("Dengeli bir kişiliğe sahipsiniz. Hem sosyal hem de bireysel aktiviteleri seviyorsunuz.")
                                                .testTemplateId(personalityTest.getId().longValue())
                                                .testTemplate(personalityTest)
                                                .createdBy(mockUser.getId())
                                                .build(),
                                Comment.builder()
                                                .title("Ekstrovert Kişilik")
                                                .score(7)
                                                .text("Dışa dönük bir kişiliğe sahipsiniz. Sosyal ortamları ve yeni deneyimleri seviyorsunuz.")
                                                .testTemplateId(personalityTest.getId().longValue())
                                                .testTemplate(personalityTest)
                                                .createdBy(mockUser.getId())
                                                .build());

                // Set up bidirectional relationships and save comments through test templates
                depressionTest.setComments(depressionComments);
                anxietyTest.setComments(anxietyComments);
                personalityTest.setComments(personalityComments);

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
                
                // Create questions for personality test
                List<Choice> personalityChoices1 = List.of(
                                Choice.builder().answerType(AnswerType.ANSWER_A).text("Hiç katılmıyorum")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_B).text("Katılmıyorum")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_C).text("Kararsızım")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_D).text("Katılıyorum")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_E).text("Tamamen katılıyorum")
                                                .createdBy(mockUser.getId()).build());

                List<Choice> personalityChoices2 = List.of(
                                Choice.builder().answerType(AnswerType.ANSWER_A).text("Asla")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_B).text("Nadiren")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_C).text("Bazen")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_D).text("Sıklıkla")
                                                .createdBy(mockUser.getId()).build(),
                                Choice.builder().answerType(AnswerType.ANSWER_E).text("Her zaman")
                                                .createdBy(mockUser.getId()).build());

                List<Question> personalityQuestions = new ArrayList<>();
                personalityQuestions.add(Question.builder()
                                .text("Sosyal ortamlarda enerjik ve konuşkan olurum.")
                                .createdBy(mockUser.getId())
                                .testTemplateId(personalityTest.getId().longValue())
                                .choices(personalityChoices1)
                                .build());

                personalityQuestions.add(Question.builder()
                                .text("Yeni insanlarla tanışmaktan hoşlanır mısınız?")
                                .createdBy(mockUser.getId())
                                .testTemplateId(personalityTest.getId().longValue())
                                .choices(personalityChoices2)
                                .build());

                questionRepository.saveAll(personalityQuestions);
                
                // Save all test templates with their comments (cascade will handle comments)
                testTemplateRepository.save(depressionTest);
                testTemplateRepository.save(anxietyTest);
                testTemplateRepository.save(personalityTest);
                } // End of test template creation

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