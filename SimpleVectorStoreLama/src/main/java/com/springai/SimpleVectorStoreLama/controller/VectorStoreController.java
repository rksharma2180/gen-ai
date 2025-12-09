package com.springai.SimpleVectorStoreLama.controller;


import com.springai.SimpleVectorStoreLama.services.DocumentProcessingService;
import com.springai.SimpleVectorStoreLama.services.VectorStoreService;
import org.springframework.ai.document.Document;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vector-store")
public class VectorStoreController {

    private final VectorStoreService vectorStoreService;
    private final DocumentProcessingService documentProcessingService;

    public VectorStoreController(VectorStoreService vectorStoreService,
                                 DocumentProcessingService documentProcessingService) {
        this.vectorStoreService = vectorStoreService;
        this.documentProcessingService = documentProcessingService;
    }

    @PostMapping("/add-text")
    public ResponseEntity<Map<String, String>> addText(@RequestBody Map<String, String> request) {
        var text = request.get("text");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "api");
        metadata.put("timestamp", System.currentTimeMillis());

        documentProcessingService.processTextDocument(text, metadata);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Text added to vector store"
        ));
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadDocument(@RequestParam("file") MultipartFile file)
            throws IOException {
        // Save temporary file
        var tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile.toFile());

        // Process with Tika
        documentProcessingService.processDocument(new FileSystemResource(tempFile.toFile()));

        // Clean up
        Files.delete(tempFile);

        /*return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", STR."Document processed: \{file.getOriginalFilename()}"
        ));*/

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Document processed: "+file.getOriginalFilename()
        ));
    }

    @PostMapping("/search")
    public ResponseEntity<List<Document>> search(@RequestBody Map<String, Object> request) {
        var query = (String) request.get("query");
        var topK = (int) request.getOrDefault("topK", 3);

        var results = vectorStoreService.search(query, topK);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> queryWithRAG(
            @RequestBody Map<String, Object> request) {
        var question = (String) request.get("question");
        var topK = (int) request.getOrDefault("topK", 3);

        var answer = vectorStoreService.queryWithRAG(question, topK);

        return ResponseEntity.ok(Map.of(
                "question", question,
                "answer", answer
        ));
    }
}