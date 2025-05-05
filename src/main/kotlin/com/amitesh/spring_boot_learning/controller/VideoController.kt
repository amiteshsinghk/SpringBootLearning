package com.amitesh.spring_boot_learning.controller


import jakarta.servlet.http.HttpServletResponse
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import org.springframework.data.mongodb.core.query.Query

@RestController
@RequestMapping("/api/videos")
class VideoController(
    private val gridFsTemplate: GridFsTemplate,
    private val gridFsOperations: GridFsOperations
) {

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadVideo(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        if (file.isEmpty) return ResponseEntity.badRequest().body("File is empty")

        val metadata = Document()
        metadata["contentType"] = file.contentType

        val fileId = gridFsTemplate.store(file.inputStream, file.originalFilename, metadata)
        return ResponseEntity.ok(fileId.toString())
    }

    @GetMapping("/stream/{id}")
    fun streamVideo(@PathVariable id: String, response: HttpServletResponse) {
        println("ObjectId:: id :: $id")
        val objectId = try {
            ObjectId(id)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid video ID format")
        }
        println("ObjectId:: $objectId")
        val file = gridFsOperations.findOne(Query(Criteria.where("_id").`is`(objectId)))
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found")

        response.contentType = file.metadata?.getString("contentType") ?: MediaType.APPLICATION_OCTET_STREAM_VALUE

        val inputStream = gridFsOperations.getResource(file).inputStream
        IOUtils.copy(inputStream, response.outputStream)
        response.flushBuffer()
    }
}

