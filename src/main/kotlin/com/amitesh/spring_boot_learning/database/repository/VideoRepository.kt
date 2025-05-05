package com.amitesh.spring_boot_learning.database.repository

import com.amitesh.spring_boot_learning.database.model.Video
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface VideoRepository : MongoRepository<Video, ObjectId>