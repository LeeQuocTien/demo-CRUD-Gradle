package com.example.gradleCRUDdemo.student

import org.junit.jupiter.api.Test

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class StudentControllerTest @Autowired constructor (
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
)
{
    val baseUrl = "/api/students"

    @Test
    fun `should return all students`() {
        mockMvc.get(baseUrl)
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }

                jsonPath("$[0].name") { value("Jerry")}
            }
    }

    @Test
    fun `should return a student`() {
        val studentId = 1
        mockMvc.get("$baseUrl/$studentId")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }

                jsonPath("$.name") { value("Jerry")}
            }
    }

    @Test
    fun `should return NOT FOUND if a student does not exist`() {
        val studentId = 3
        mockMvc.get("$baseUrl/$studentId")
            .andDo { print() }
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun `should add new student`() {
        val newStudent = Student(name = "Mickey", email = "mickey@gmail.com", age = 22 )

        val performPost = mockMvc.post(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newStudent)
        }

        performPost.andDo { print() }
            .andExpect { status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.name") {value("Mickey")}
            }
    }

    @Test
    fun `should update a student`() {
        val updatedStudentId = 2
        val updatedStudent = Student(name = "Tom1", email = "tom1@gmail.com", age = 20)

        val performPut = mockMvc.put("$baseUrl/$updatedStudentId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updatedStudent)
        }
        performPut.andDo { print() }
            .andExpect { status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value("2")}
                jsonPath("$.name") { value("Tom1")}
                jsonPath("$.email") { value("tom1@gmail.com")}
                jsonPath("$.age") { value("20")}
            }
    }

    @Test
    fun `should delete a student`() {
        val deletedStudentId = 1
        mockMvc.delete("$baseUrl/$deletedStudentId")
            .andDo { print() }
            .andExpect { status { isOk() }
            }

        mockMvc.get("$baseUrl/$deletedStudentId")
            .andDo { print() }
            .andExpect {
                status { isNotFound() } }
    }
}