package com.skh.rag.service;

import com.skh.rag.rag.RagPipeline;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Service;
import dev.langchain4j.model.embedding.EmbeddingModel;

@Service
public class RagService {

    private final RagPipeline rag;

    public RagService(EmbeddingStore<TextSegment> store, EmbeddingModel embeddingModel, ChatLanguageModel chatModel) {

        this.rag = new RagPipeline(store, embeddingModel, chatModel);
    }

    public String ask(String question) {
        return rag.ask(question);
    }

}
