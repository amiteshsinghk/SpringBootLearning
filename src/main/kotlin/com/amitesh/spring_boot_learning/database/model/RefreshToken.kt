package com.amitesh.spring_boot_learning.database.model

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

import java.time.Instant

@Document("refresh_token")
data class RefreshToken(
    val userId: ObjectId,
    @Indexed(expireAfter ="0s")
    val expiresAt: Instant,
    val hashedToken: String,
    val createdAt: Instant = Instant.now()
)