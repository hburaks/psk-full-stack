package com.hburak_dev.psk_full_stack;

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
			TestRepository testRepository) {
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
