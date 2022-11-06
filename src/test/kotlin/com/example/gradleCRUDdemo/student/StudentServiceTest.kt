package com.example.gradleCRUDdemo.student

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*


internal class StudentServiceTest {

    private val mockStudentRepository = Mockito.mock(StudentRepository::class.java)
    private val mockStudentService = StudentService(mockStudentRepository)

    @BeforeEach
    fun setUp() {
        val student1 = Student()
        student1.id = 1
        student1.name = "Jerry"
        student1.age = 22
        student1.email = "jerry@gmail.com"
        val student2 = Student()
        student2.id = 2
        student2.name = "Tom"
        student2.email = "tom@gmail.com"
        student2.age = 22
        val students: MutableList<Student> = ArrayList<Student>()
        students.add(student1)
        students.add(student2)
        val updatedStudent = Student()
        updatedStudent.id = 1
        updatedStudent.name = "Tom3"
        updatedStudent.email = "tom@gmail.com"
        updatedStudent.age = 20
        Mockito.`when`(mockStudentRepository.findById(student1.id!!)).thenReturn(Optional.of(student1))
        Mockito.`when`(mockStudentRepository.findAll()).thenReturn(students)
        Mockito.`when`(mockStudentRepository.findStudentByEmail(updatedStudent.email!!)).thenReturn(Optional.of(student2))
    }

    @Test
    fun getStudents() {
        val students: List<Student> = mockStudentService.getStudents()
        assertEquals(2, students.size)
    }

    @Test
    fun getStudent() {
        val studentId = 1
        val student: Optional<Student> = mockStudentService.getStudent(studentId)
        assertEquals(studentId, student.get().id)
    }

    @Test
    fun addNewStudent() {
        val student = Student()
        student.name = "Mickey"
        student.email = "mickey@gmail.com"
        student.age = 20
        mockStudentService.addNewStudent(student)
        verify(mockStudentRepository, times(1)).save(student)
    }

    @Test
    fun deleteStudent() {
        val studentId = 1
        mockStudentService.deleteStudent(studentId)
        verify(mockStudentRepository, times(1)).deleteById(studentId)
    }

    @Test
    fun updateStudent() {
        val studentId = 1
        val student = Student()
        student.name = "Tom3"
        student.email = "tom3@gmail.com"
        student.age = 18

        val willUpdateStudent: Optional<Student> = mockStudentRepository.findById(studentId)
        assertTrue(willUpdateStudent.isPresent)
        assertTrue(student.name!!.isNotEmpty())
        assertNotEquals(student.name, willUpdateStudent.get().name)
        assertTrue(student.email!!.isNotEmpty())
        assertNotEquals(student.email, willUpdateStudent.get().email)
        assertTrue(student.age!! > 0)
        assertNotEquals(student.age, willUpdateStudent.get().age)

        val studentOptional : Optional<Student> = mockStudentRepository.findStudentByEmail(student.email!!)
        assertFalse(studentOptional.isPresent)

        val updatedStudent : Student = mockStudentService.updateStudent(studentId, student.name, student.email, student.age)

        assertEquals(studentId, updatedStudent.id)
        assertEquals(student.name, updatedStudent.name)
        assertEquals(student.email, updatedStudent.email)
        assertEquals(student.age, updatedStudent.age)
    }

    @Test
    fun `updateStudent() with studentId not exist`() {
        val studentId = 4
        val student = Student()
        student.name = "Tom3"
        student.email = "tom3@gmail.com"
        student.age = 18

        val willUpdateStudent: Optional<Student> = mockStudentRepository.findById(studentId)
        assertFalse(willUpdateStudent.isPresent)

        val thrown: IllegalStateException = assertThrows(IllegalStateException::class.java)
        {mockStudentService.updateStudent(studentId, student.name, student.email, student.age) }

        assertEquals("student with id $studentId does not exist", thrown.message)
    }

    @Test
    fun `updateStudent() with input name is empty`(){
        val studentId = 1
        val student = Student()
        student.name = ""
        student.email = "tom3@gmail.com"
        student.age = 18

        val willUpdateStudent: Optional<Student> = mockStudentRepository.findById(studentId)
        assertTrue(willUpdateStudent.isPresent)

        assertTrue(student.name!!.isEmpty())

        val studentOptional : Optional<Student> = mockStudentRepository.findStudentByEmail(student.email!!)
        assertFalse(studentOptional.isPresent)

        val updatedStudent : Student = mockStudentService.updateStudent(studentId, student.name, student.email, student.age)

        assertEquals(studentId, updatedStudent.id)
        assertNotEquals(student.name, updatedStudent.name)
        assertEquals(student.email, updatedStudent.email)
        assertEquals(student.age, updatedStudent.age)
    }

    @Test
    fun `updateStudent() with the same input name`(){
        val studentId = 1
        val student = Student()
        student.name = "Jerry"
        student.email = "tom3@gmail.com"
        student.age = 18

        val willUpdateStudent: Optional<Student> = mockStudentRepository.findById(studentId)
        assertTrue(willUpdateStudent.isPresent)
        assertTrue(student.name!!.isNotEmpty())
        assertEquals(willUpdateStudent.get().name, student.name)

        val studentOptional : Optional<Student> = mockStudentRepository.findStudentByEmail(student.email!!)
        assertFalse(studentOptional.isPresent)

        val updatedStudent : Student = mockStudentService.updateStudent(studentId, student.name, student.email, student.age)

        assertEquals(studentId, updatedStudent.id)
        assertEquals(student.name, updatedStudent.name)
        assertEquals(student.email, updatedStudent.email)
        assertEquals(student.age, updatedStudent.age)
    }

    @Test
    fun `updateStudent() with input email is empty`(){
        val studentId = 1
        val student = Student()
        student.name = "Tom3"
        student.email = ""
        student.age = 18

        val willUpdateStudent: Optional<Student> = mockStudentRepository.findById(studentId)
        assertTrue(willUpdateStudent.isPresent)
        assertTrue(student.email!!.isEmpty())

        val updatedStudent : Student = mockStudentService.updateStudent(studentId, student.name, student.email, student.age)
        verify(mockStudentRepository, times(0)).findStudentByEmail(student.email!!)

        assertEquals(studentId, updatedStudent.id)
        assertEquals(student.name, updatedStudent.name)
        assertNotEquals(student.email, updatedStudent.email)
        assertEquals(student.age, updatedStudent.age)
    }

    @Test
    fun `updateStudent() with the same input email`(){
        val studentId = 1
        val student = Student()
        student.name = "Tom3"
        student.email = "jerry@gmail.com"
        student.age = 18

        val willUpdateStudent: Optional<Student> = mockStudentRepository.findById(studentId)
        assertTrue(willUpdateStudent.isPresent)
        assertTrue(student.email!!.isNotEmpty())
        assertEquals(willUpdateStudent.get().email, student.email)

        val updatedStudent : Student = mockStudentService.updateStudent(studentId, student.name, student.email, student.age)
        verify(mockStudentRepository, times(0)).findStudentByEmail(student.email!!)

        assertEquals(studentId, updatedStudent.id)
        assertEquals(student.name, updatedStudent.name)
        assertEquals(student.email, updatedStudent.email)
        assertEquals(student.age, updatedStudent.age)
    }

    @Test
    fun `updateStudent() with email is taken`() {
        val studentId = 1
        val student = Student()
        student.name = "Tom3"
        student.email = "tom@gmail.com"
        student.age = 18

        val willUpdateStudent: Optional<Student> = mockStudentRepository.findById(studentId)
        assertTrue(willUpdateStudent.isPresent)

        val studentOptional : Optional<Student> = mockStudentRepository.findStudentByEmail(student.email!!)
        assertTrue(studentOptional.isPresent)

        val thrown : IllegalStateException = assertThrows(IllegalStateException::class.java)
        {mockStudentService.updateStudent(studentId, student.name, student.email, student.age) }

        assertEquals("email taken", thrown.message)
    }

    @Test
    fun `updateStudent() with input age is 0`(){
        val studentId = 1
        val student = Student()
        student.name = "Tom3"
        student.email = "tom3@gmail.com"
        student.age = 0

        val willUpdateStudent: Optional<Student> = mockStudentRepository.findById(studentId)
        assertTrue(willUpdateStudent.isPresent)

        assertFalse(student.age!! > 0)

        val studentOptional : Optional<Student> = mockStudentRepository.findStudentByEmail(student.email!!)
        assertFalse(studentOptional.isPresent)

        val updatedStudent : Student = mockStudentService.updateStudent(studentId, student.name, student.email, student.age)

        assertEquals(studentId, updatedStudent.id)
        assertEquals(student.name, updatedStudent.name)
        assertEquals(student.email, updatedStudent.email)
        assertNotEquals(student.age, updatedStudent.age)
    }

    @Test
    fun `updateStudent() with the same input age`(){
        val studentId = 1
        val student = Student()
        student.name = "Tom3"
        student.email = "tom3@gmail.com"
        student.age = 22

        val willUpdateStudent: Optional<Student> = mockStudentRepository.findById(studentId)
        assertTrue(willUpdateStudent.isPresent)

        assertTrue(student.age!! > 0)
        assertEquals(willUpdateStudent.get().age, student.age)

        val studentOptional : Optional<Student> = mockStudentRepository.findStudentByEmail(student.email!!)
        assertFalse(studentOptional.isPresent)

        val updatedStudent : Student = mockStudentService.updateStudent(studentId, student.name, student.email, student.age)

        assertEquals(studentId, updatedStudent.id)
        assertEquals(student.name, updatedStudent.name)
        assertEquals(student.email, updatedStudent.email)
        assertEquals(student.age, updatedStudent.age)
    }
}




