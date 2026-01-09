package com.benson.azure_rag_file_ingestion;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.application.name=azure-rag-file-ingestion-test",
		"spring.cloud.azure.storage.blob.account-name=test-account",
		"spring.cloud.azure.storage.blob.endpoint=https://test-account.blob.core.windows.net",
		"spring.cloud.azure.storage.blob.account-key=test-key",
		"azure.storage.container-name=test-knowledge-base",
		"azure.search.endpoint=https://test-search.local/",
		"azure.search.api-key=test-search-key",
		"azure.search.indexer-name=test-indexer",
		"api_key=test-api-key",
		"spring.servlet.multipart.max-file-size=10MB",
		"spring.servlet.multipart.max-request-size=10MB"
})
class AzureRagFileIngestionApplicationTests {

	@Test
	void contextLoads() {
	}

}
