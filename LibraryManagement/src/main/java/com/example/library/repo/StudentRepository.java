package com.example.library.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
