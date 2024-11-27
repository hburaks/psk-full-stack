package com.hburak_dev.psk_full_stack;

import com.hburak_dev.psk_full_stack.blog.Blog;
import com.hburak_dev.psk_full_stack.blog.BlogRepository;
import com.hburak_dev.psk_full_stack.choice.Choice;
import com.hburak_dev.psk_full_stack.question.AnswerType;
import com.hburak_dev.psk_full_stack.question.Question;
import com.hburak_dev.psk_full_stack.role.Role;
import com.hburak_dev.psk_full_stack.role.RoleRepository;
import com.hburak_dev.psk_full_stack.session.Session;
import com.hburak_dev.psk_full_stack.session.SessionRepository;
import com.hburak_dev.psk_full_stack.session.SessionStatusType;
import com.hburak_dev.psk_full_stack.test.Test;
import com.hburak_dev.psk_full_stack.test.TestRepository;
import com.hburak_dev.psk_full_stack.user.User;
import com.hburak_dev.psk_full_stack.user.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing
@EnableAsync
@SpringBootApplication
public class PskFullStackApplication {

	public static void main(String[] args) {
		SpringApplication.run(PskFullStackApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataInitializer(
			SessionRepository sessionRepository,
			UserRepository userRepository,
			RoleRepository roleRepository,
			TestRepository testRepository,
			BlogRepository blogRepository) {
		return args -> {
			if (roleRepository.findByName("ROLE_USER").isEmpty()) {
				roleRepository.save(Role.builder().name("ROLE_USER").build());
			}
			if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
				roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
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
					.text("Günümüzün hızlı tempolu yaşam tarzında, psikolojik sağlığımızı korumak her zamankinden daha önemli hale gelmiştir. Stres, kaygı ve depresyon gibi psikolojik sorunlarla başa çıkabilmek için düzenli egzersiz yapmak, sağlıklı beslenmek ve yeterli uyku almak oldukça önemlidir. Ayrıca, sosyal ilişkilerimizi güçlendirmek, hobiler edinmek ve düzenli olarak meditasyon yapmak da psikolojik sağlığımızı destekleyen önemli faktörlerdir. Bu yazıda, günlük yaşamda uygulayabileceğiniz pratik öneriler ve yaşam tarzı değişiklikleri hakkında detaylı bilgiler bulacaksınız.")
					.shareable(true)
					.createdBy(mockUser.getId())
					.build());
				mockBlogs.add(Blog.builder()
					.title("Stres Yönetimi ve Başa Çıkma Teknikleri")
					.subTitle("Günlük hayatta stresi yönetmenin etkili yolları")
					.text("Stres, modern yaşamın kaçınılmaz bir parçası haline gelmiştir. İş hayatı, aile sorumlulukları ve sosyal ilişkiler gibi faktörler stres seviyemizi artırabilir. Ancak, doğru teknikler ve yaklaşımlarla stresi etkili bir şekilde yönetmek mümkündür. Nefes egzersizleri, progresif kas gevşetme teknikleri ve mindfulness uygulamaları, stres yönetiminde kullanabileceğimiz etkili araçlardır. Bu yazıda, stres yönetimi konusunda bilimsel olarak kanıtlanmış yöntemler ve günlük hayatta uygulayabileceğiniz pratik öneriler yer almaktadır.")
					.shareable(true)
					.createdBy(mockUser.getId())
					.build());
				mockBlogs.add(Blog.builder()
					.title("İlişkilerde Duygusal Zeka")
					.subTitle("Sağlıklı ilişkiler kurmanın anahtarı: Duygusal zeka")
					.text("Duygusal zeka, hem kendimizin hem de başkalarının duygularını anlama ve yönetme becerisidir. İlişkilerimizde duygusal zekanın rolü büyüktür. Empati kurabilmek, etkili iletişim becerilerine sahip olmak ve duygusal farkındalık geliştirmek, sağlıklı ve uzun ömürlü ilişkiler kurmanın temel taşlarıdır. Bu makalede, duygusal zekanın beş temel bileşeni olan öz farkındalık, öz düzenleme, motivasyon, empati ve sosyal beceriler hakkında detaylı bilgiler ve bu becerileri geliştirmek için pratik öneriler bulacaksınız.")
					.shareable(true)
					.createdBy(mockUser.getId())
					.build());
				blogRepository.saveAll(mockBlogs);
			}

			if (testRepository.count() == 0) {
				List<Test> mockTests = new ArrayList<>();

				mockTests.add(Test.builder()
						.title("Depresyon Değerlendirmesi")
						.subTitle("Depresyon belirtilerini değerlendir")
						.createdBy(mockUser.getId())
						.isActive(true)
						.questions(List.of(
								Question.builder()
										.text("Ne sıklıkla üzgün veya mutsuz hissediyorsunuz?")
										.createdBy(mockUser.getId())
										.choices(List.of(
												Choice.builder().answerType(AnswerType.ANSWER_A).text("Hiçbir zaman").createdBy(mockUser.getId())
														.build(),
												Choice.builder().answerType(AnswerType.ANSWER_B).text("Bazen").createdBy(mockUser.getId())
														.build(),
												Choice.builder().answerType(AnswerType.ANSWER_C).text("Sık sık").createdBy(mockUser.getId())
														.build(),
												Choice.builder().answerType(AnswerType.ANSWER_D).text("Her zaman").createdBy(mockUser.getId())
														.build()))
										.build(),
								Question.builder()
										.text("Uyku problemi yaşıyor musunuz?")
										.createdBy(mockUser.getId())
										.choices(List.of(
												Choice.builder().answerType(AnswerType.ANSWER_A).text("Hayır").createdBy(mockUser.getId()).build(),
												Choice.builder().answerType(AnswerType.ANSWER_B).text("Ara sıra")
														.createdBy(mockUser.getId()).build(),
												Choice.builder().answerType(AnswerType.ANSWER_C).text("Sıklıkla").createdBy(mockUser.getId())
														.build(),
												Choice.builder().answerType(AnswerType.ANSWER_D).text("Her gece").createdBy(mockUser.getId())
														.build()))
										.build()))
						.build());

				mockTests.add(Test.builder()
						.title("Anksiyete Taraması")
						.subTitle("Anksiyete seviyenizi kontrol edin")
						.createdBy(mockUser.getId())
						.isActive(true)
						.questions(List.of(
								Question.builder()
										.createdBy(mockUser.getId())
										.text("Ne sıklıkla gergin veya endişeli hissediyorsunuz?")
										.choices(List.of(
												Choice.builder().answerType(AnswerType.ANSWER_A).text("Nadiren").createdBy(mockUser.getId())
														.build(),
												Choice.builder().answerType(AnswerType.ANSWER_B).text("Bazen").createdBy(mockUser.getId())
														.build(),
												Choice.builder().answerType(AnswerType.ANSWER_C).text("Sık sık").createdBy(mockUser.getId())
														.build(),
												Choice.builder().answerType(AnswerType.ANSWER_D).text("Çok sık").createdBy(mockUser.getId())
														.build()))
										.build(),
								Question.builder()
										.createdBy(mockUser.getId())
										.text("Panik atak yaşıyor musunuz?")
										.choices(List.of(
												Choice.builder().answerType(AnswerType.ANSWER_A).text("Hiçbir zaman").createdBy(mockUser.getId())
														.build(),
												Choice.builder().answerType(AnswerType.ANSWER_B).text("Nadiren").createdBy(mockUser.getId())
														.build(),
												Choice.builder().answerType(AnswerType.ANSWER_C).text("Bazen").createdBy(mockUser.getId())
														.build(),
												Choice.builder().answerType(AnswerType.ANSWER_D).text("Sıklıkla").createdBy(mockUser.getId())
														.build()))
										.build()))
						.build());

				testRepository.saveAll(mockTests);
			}

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
						.sessionStatus(SessionStatusType.AWAITING_PSYCHOLOGIST_APPROVAL)
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
		};
	}
}
