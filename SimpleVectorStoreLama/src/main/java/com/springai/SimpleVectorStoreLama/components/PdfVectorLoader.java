package com.springai.SimpleVectorStoreLama.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;

import javax.swing.text.Document;
import java.util.List;
import java.util.Map;

@Component
public class PdfVectorLoader implements ApplicationRunner {

    private final VectorStore vectorStore;
    private final Resource pdf;

    public PdfVectorLoader(VectorStore vectorStore,
                           @Value("classpath:docker.pdf") Resource pdf) {
        this.vectorStore = vectorStore;
        this.pdf = pdf;

    }

    @Override
    public void run(ApplicationArguments args) {
        TikaDocumentReader reader = new TikaDocumentReader(pdf);
        var docs = reader.get();

        int batchSize = 3; // start very small
        for (int i = 0; i < docs.size(); i += batchSize) {
            var batch = docs.subList(i, Math.min(i + batchSize, docs.size()));
            vectorStore.add(batch);    // will call Ollama embeddings
        }
        System.out.println("--- Documents loaded into Vector Store successfully! ---");
    }
}