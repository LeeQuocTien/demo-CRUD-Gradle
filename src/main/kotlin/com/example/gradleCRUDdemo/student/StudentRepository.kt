package com.example.gradleCRUDdemo.student

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*


interface StudentRepository : CrudRepository <Student, Int> {

    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    fun findStudentByEmail(email: String): Optional<Student>
}