package com.example.gradleCRUDdemo.student

import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional
import kotlin.collections.ArrayList


@Service
class StudentService(val repository: StudentRepository) {

    fun getStudents(): List<Student> {
        val students: MutableList<Student> = ArrayList()
        repository.findAll().forEach { student -> students.add(student) }
        return students
    }

    fun getStudent(studentId: Int): Optional<Student> {
        return repository.findById(studentId)
    }

    fun addNewStudent(student: Student) {
        repository.save(student)
    }

    fun deleteStudent(studentId: Int) {
        repository.deleteById(studentId)
    }

    @Transactional
    fun updateStudent(studentId: Int, name: String?, email: String?, age: Int?): Student {
        val student : Student = repository.findById(studentId).orElseThrow {
            IllegalStateException(
                "student with id $studentId does not exist"
            )
        }

        if (name!!.isNotEmpty() && !Objects.equals(student.name, name)) {
            student.name = name;
        }

        if (email!!.isNotEmpty() && !Objects.equals(student.email, email)
        ) {
            val studentOptional: Optional<Student> = repository.findStudentByEmail(email)
            check(!studentOptional.isPresent) { "email taken" }
            student.email = email
        }

        if (age!! > 0 && !Objects.equals(student.age, age)) {
            student.age = age;
        }

        return student
    }


}