package com.hburak_dev.psk_full_stack.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Integer> {

    List<Test> findAllByIsActiveTrue();

}
