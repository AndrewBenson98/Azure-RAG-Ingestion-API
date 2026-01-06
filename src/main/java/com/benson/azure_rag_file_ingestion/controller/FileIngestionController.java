package com.benson.azure_rag_file_ingestion.controller;

import com.benson.azure_rag_file_ingestion.service.FileIngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ingest")
public class FileIngestionController {

    private final FileIngestionService fileIngestionService;

    private final String VALID_API_KEY;

    public FileIngestionController(@Autowired FileIngestionService fileIngestionService, @Value("${api.key}") String VALID_API_KEY) {
        this.fileIngestionService = fileIngestionService;
        this.VALID_API_KEY= VALID_API_KEY;
    }

    /**
     * Upload PDF file to Azure Blob Storage
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPDF(@RequestParam("file")MultipartFile file, @RequestHeader("api_key") String apiKey) {

        if (!VALID_API_KEY.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        try {
            String fileUrl = fileIngestionService.uploadFile(file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }

    }
}
