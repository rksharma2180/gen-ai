package com.springai.SimpleVectorStoreLama.services;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VectorStoreService {
    
    private final VectorStore vectorStore;
    private final ChatModel chatModel;

    public VectorStoreService(VectorStore vectorStore, ChatModel chatModel) {
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
    }

    public void addDocument(Document document) {
        vectorStore.add(List.of(document));
        //System.out.println(STR."Added document: \{document.getContent().substring(0, Math.min(100, document.getContent().length()))}...");
        System.out.println("Added document: "+document.getText().substring(0, Math.min(100, document.getText().length()))+"...");
    }

    public void addDocuments(List<Document> documents) {
        vectorStore.add(documents);
        //System.out.println(STR."Added \{documents.size()} documents to vector store");
        System.out.println("Added "+documents.size()+ " documents to vector store");
    }

    public List<Document> search(String query, int topK) {
        var searchRequest = SearchRequest.builder().//SearchRequest.query(query).withTopK(topK);
        query(query).topK(topK).similarityThreshold(0.0).build();
        return vectorStore.similaritySearch(searchRequest);
    }

    public String queryWithRAG(String question, int topK) {
        var results = search(question, topK);
        
        var context = results.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        /*var prompt = STR."""
            Context:
            \{context}
            
            Question: \{question}
            
            Answer based on the context above:
            """;*/

        var prompt = """
                Context:
                %s
            
                Question: %s
            
                Answer based on the context above:
                """.formatted(context, question);


        return chatModel.call(prompt);
    }
}