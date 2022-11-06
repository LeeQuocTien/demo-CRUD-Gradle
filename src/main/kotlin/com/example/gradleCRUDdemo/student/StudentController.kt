package com.example.gradleCRUDdemo.student

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@CrossOrigin( maxAge = 3600)
@RestController
@RequestMapping("/api/students")
class StudentController(val studentService: StudentService) {

    @GetMapping
    fun getStudents(): List<Student> {
        return studentService.getStudents()
    }

    @GetMapping("/{studentId}")
    fun getStudent(
        @PathVariable(value = "studentId") studentId: Int
    ): ResponseEntity<Student> {
        val studentData : Optional<Student> = studentService.getStudent(studentId)
        return if (studentData.isPresent) {
            ResponseEntity<Student>(studentData.get(), HttpStatus.OK)
        } else {
            ResponseEntity<Student>(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private fun createNewStudent(@RequestBody student: Student): Student {
        studentService.addNewStudent(student)
        return student
    }

    @DeleteMapping("/{studentId}")
    fun deleteStudent(
        @PathVariable(value = "studentId") studentId: Int
    ) {
        studentService.deleteStudent(studentId);
    }

    @PutMapping("/{studentId}")
    fun updateStudent(
        @PathVariable(value = "studentId") studentId: Int,
        @RequestBody newStudent: Student
    ): Student {
        return studentService.updateStudent(studentId, newStudent.name, newStudent.email, newStudent.age)
    }
}