package com.benson.azure_rag_file_ingestion.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileIngestionService {
    String uploadFile(MultipartFile file) throws IOException;
}
