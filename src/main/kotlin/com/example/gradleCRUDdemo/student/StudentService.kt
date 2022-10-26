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

    fun addNewStudent(student: Student) {
        repository.save(student)
    }

    fun deleteStudent(studentId: Int) {
        val exist : Boolean = repository.existsById(studentId);
        check(exist) { "student with id " + studentId + "does not exist" }
        repository.deleteById(studentId)
    }

    @Transactional
    fun updateStudent(studentId: Int, name: String?, email: String?, age: Int?) {
        val student : Student = repository.findById(studentId).orElseThrow {
            IllegalStateException(
                "student with id $studentId" + "does not exist"
            )
        }

        if (!name.isNullOrEmpty() && !Objects.equals(student.name, name)) {
            student.name = name;
        }

        if (!email.isNullOrEmpty() && !Objects.equals(student.email, email)
        ) {
            val studentOptional: Optional<Student> = repository.findStudentByEmail(email)
            check(!studentOptional.isPresent) { "email taken" }
            student.email = email
        }

        if (age != null && age > 0 && !Objects.equals(student.age, age)) {
            student.age = age;
        }
    }


}