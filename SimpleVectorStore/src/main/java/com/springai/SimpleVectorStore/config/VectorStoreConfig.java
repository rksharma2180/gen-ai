package com.springai.SimpleVectorStore.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.ai.vectorstore.SimpleVectorStore;

import java.io.File;
import java.util.List;

@Configuration
public class VectorStoreConfig {

    @Value("classpath:docker.pdf")
    private Resource pdfResource;

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel){
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();

        if (!pdfResource.exists()) {
            System.err.println("PDF document not found: " + pdfResource.getFilename());
            throw new NullPointerException("PDF document not found");
        }

        try {

            TikaDocumentReader tikaReader = new TikaDocumentReader(pdfResource);

            // 3. Read the Document(s)
            List<Document> documents = tikaReader.get();

            // 1. Load the PDF document
            // Configure the reader: e.g., to only include a title for each page.
            /*PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder()
                            .build())
                    .build();

            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource, config);
            List<Document> documents = pdfReader.get();*/

            // 2. Split the Text (Chunking)
            // TokenTextSplitter breaks the documents into smaller chunks for better RAG performance.
            TokenTextSplitter textSplitter = new TokenTextSplitter();
            List<Document> chunks = textSplitter.split(documents);

            // 3. and 4. Generate Embeddings and Store Vectors
            // The SimpleVectorStore (using the configured EmbeddingModel)
            // will automatically generate embeddings for the text chunks upon addition.
            System.out.println("Adding " + chunks.size() + " document chunks to the SimpleVectorStore...");
            vectorStore.add(chunks);
            System.out.println("Ingestion complete.");

            // Optional: Save the SimpleVectorStore to a file for persistence
            // Assuming your SimpleVectorStore bean is configured for persistence or you manually save it.
             File saveFile = new File("classpath:./vectorstore.json");
             vectorStore.save(saveFile);

        } catch (Exception e) {
            System.err.println("Error during PDF ingestion: " + e.getMessage());
            e.printStackTrace();
        }
        return vectorStore;
    }
}
