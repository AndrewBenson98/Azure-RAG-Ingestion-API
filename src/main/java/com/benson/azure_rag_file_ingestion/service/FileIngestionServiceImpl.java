package com.benson.azure_rag_file_ingestion.service;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.search.documents.indexes.SearchIndexerClient;
import com.azure.search.documents.indexes.SearchIndexerClientBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileIngestionServiceImpl implements FileIngestionService{

    private final BlobServiceClient blobServiceClient;

    private final SearchIndexerClient searchIndexerClient;

    private final String containerName;

    private final String indexerName; // Replace with your actual indexer name

    /**
     * Constructor
     * @param blobServiceClient The client to interact with Azure Blob Storage
     * @param containerName The name of the Blob container
     * @param searchEndpoint   The Azure Search endpoint
     * @param searchApiKey  The Azure Search API key
     * @param indexerName The name of the Azure Search indexer
     */
    public FileIngestionServiceImpl(@Autowired BlobServiceClient blobServiceClient ,
                                    @Value("${azure.storage.container-name}") String containerName,
                                    @Value("${azure.search.endpoint}") String searchEndpoint,
                                    @Value("${azure.search.api-key}") String searchApiKey,
                                    @Value("${azure.search.indexer-name}") String indexerName) {
        this.blobServiceClient = blobServiceClient;
        this.containerName = containerName;
        this.indexerName =indexerName;
        this.searchIndexerClient = new SearchIndexerClientBuilder()
                .endpoint(searchEndpoint)
                .credential(new AzureKeyCredential(searchApiKey))
                .buildClient();
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        //Upload File

        // Get client for the specific container
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        // Use original filename or generate a unique ID
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        // Upload the stream directly to Azure
        blobClient.upload(file.getInputStream(), file.getSize(), true);

        //Trigger Indexer to index new file
        searchIndexerClient.runIndexer(indexerName);

        return blobClient.getBlobUrl();
    }
}
