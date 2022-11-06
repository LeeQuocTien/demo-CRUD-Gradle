package com.example.gradleCRUDdemo.student

import javax.persistence.*

@Entity
data class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var name: String? = "",
    var email: String? = "",
    var age: Int? = null
)
