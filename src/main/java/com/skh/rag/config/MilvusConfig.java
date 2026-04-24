package com.skh.rag.config;

import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {

        return MilvusEmbeddingStore.builder()
                .host("localhost")
                .port(19530)
                .collectionName("rag_demo")
                .dimension(384)
                .build();
    }

}
