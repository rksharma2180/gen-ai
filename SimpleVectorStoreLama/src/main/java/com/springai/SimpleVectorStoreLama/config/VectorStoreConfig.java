package com.springai.SimpleVectorStoreLama.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.ai.embedding.Embedding;

@Configuration
public class VectorStoreConfig {
    @Value("classpath:docker.pdf") // Change this to your actual document path
    private Resource resource;

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        // 1. Create the VectorStore (in-memory for simple usage)
        return SimpleVectorStore.builder(embeddingModel).build();
    }
}
