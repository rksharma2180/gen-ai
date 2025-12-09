package com.springai.SimpleVectorStoreLama.services;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DocumentProcessingService {

    private final VectorStoreService vectorStoreService;

    public DocumentProcessingService(VectorStoreService vectorStoreService) {
        this.vectorStoreService = vectorStoreService;
    }

    public void processDocument(Resource resource) {
        // Read document using Tika
        var tikaReader = new TikaDocumentReader(resource);
        var documents = tikaReader.get();

        // Split documents into chunks
        var splitter = new TokenTextSplitter();
        var chunks = splitter.apply(documents);

        // Add to vector store
        vectorStoreService.addDocuments(chunks);
        
        /*System.out.println(STR."Processed document: \{resource.getFilename()}");
        System.out.println(STR."Created \{chunks.size()} chunks");*/
        System.out.println("Processed document: "+resource.getFilename());
        System.out.println("Created "+chunks.size() +" Chunks");
    }

    public void processTextDocument(String text, Map<String, Object> metadata) {
        var document = new Document(text, metadata);
        
        // Split into chunks
        /*var splitter = new TokenTextSplitter(500, 50);*/
        var splitter = new TokenTextSplitter();
        var chunks = splitter.apply(List.of(document));
        
        vectorStoreService.addDocuments(chunks);
        //System.out.println(STR."Processed text with \{chunks.size()} chunks");
        System.out.println("Processed text with "+chunks.size()+" chunks");
    }
}