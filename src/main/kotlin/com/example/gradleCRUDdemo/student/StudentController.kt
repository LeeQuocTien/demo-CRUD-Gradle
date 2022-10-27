package com.example.gradleCRUDdemo.student

import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/students")
class StudentController(val studentService: StudentService) {

    @GetMapping
    fun getStudents(): List<Student> {
        return studentService.getStudents()
    }

    @PostMapping
    private fun createNewStudent(@RequestBody student: Student): Int {
        studentService.addNewStudent(student)
        return student.id!!
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
    ): Int {
        studentService.updateStudent(studentId, newStudent.name, newStudent.email, newStudent.age)
        return newStudent.id!!
    }
}