package com.skh.rag.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class DocumentService {

    @Resource
    private EmbeddingModel embeddingModel;
    @Resource
    private EmbeddingStore<TextSegment> store;

    public void importDocument(String file) throws Exception {

        String text = Files.readString(Path.of(file));

        Document document = Document.from(text);

        DocumentSplitter splitter = DocumentSplitters.recursive(300, 50);

        List<TextSegment> segments = splitter.split(document);

        for (TextSegment segment : segments) {
            Embedding embedding = embeddingModel.embed(segment).content();
            store.add(embedding, segment);
        }

    }

}