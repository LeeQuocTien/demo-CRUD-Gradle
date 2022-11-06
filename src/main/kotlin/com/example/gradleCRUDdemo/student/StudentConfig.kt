package com.example.gradleCRUDdemo.student

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StudentConfig(val repository: StudentRepository) {

    @Bean
    fun databaseInitializer() = ApplicationRunner {
        repository.save(Student(
            name = "Jerry",
            email = "jerry@gmail.com",
            age = 22
        ))
        repository.save(Student(
            3,
            "Tom",
            "tom@gmail.com",
            22
        ))
    }
}

