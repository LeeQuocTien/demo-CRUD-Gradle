package com.example.gradleCRUDdemo.student

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StudentConfig {

    @Bean
    fun databaseInitializer(repository: StudentRepository) = ApplicationRunner {
        repository.save(Student(
            name = "Jerry",
            email = "jerry@gmail.com",
            age = 22
        ))
        repository.save(Student(
            3,
            "Tom3",
            "tom3@gmail.com",
            22
        ))
    }
}

