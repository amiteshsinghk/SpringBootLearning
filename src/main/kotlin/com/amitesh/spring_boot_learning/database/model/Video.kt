package com.amitesh.spring_boot_learning.database.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "videos")
data class Video(
    @Id val id: String? = null,
    val title: String,
    val description: String,
    val filePath: String
)
