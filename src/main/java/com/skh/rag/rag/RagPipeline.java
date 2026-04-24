package com.skh.rag.rag;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.data.segment.TextSegment;

import java.util.List;

public class RagPipeline {

    private final EmbeddingStoreContentRetriever retriever;
    private final ChatLanguageModel chatModel;

    public RagPipeline(EmbeddingStore<TextSegment> store,
                       EmbeddingModel embeddingModel,
                       ChatLanguageModel chatModel) {

        this.retriever =
                EmbeddingStoreContentRetriever.builder()
                        .embeddingStore(store)
                        .embeddingModel(embeddingModel)
                        .maxResults(3)
                        .build();

        this.chatModel = chatModel;
    }

    public String ask(String question) {

        List<Content> contents = retriever.retrieve(Query.from(question));

        StringBuilder context = new StringBuilder();

        for (Content c : contents) {
            context.append(c.textSegment().text()).append("\n");
        }

        String prompt = """
                根据以下知识回答问题：
                %s
                问题：%s
                """.formatted(context, question);

        return chatModel.generate(prompt);
    }

}