package com.example.gradleCRUDdemo.student

import javax.persistence.*

@Entity
@Table
data class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var name: String? = null,
    var email: String? = null,
    var age: Int? = null
)
