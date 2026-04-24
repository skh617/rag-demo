package com.skh.rag.demo;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;

import dev.langchain4j.data.segment.TextSegment;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import dev.langchain4j.model.embedding.EmbeddingModel;

import dev.langchain4j.data.embedding.Embedding;

import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

public class RagDemo {

//    @Value("${deepseek.apiKey}")
//    private static String apiKey;

    public static void main(String[] args) throws Exception {

        // 1 初始化 DeepSeek
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey("sk-a7615b0a2d05444a87bafc02a767b5d7")
                .baseUrl("https://api.deepseek.com")
                .modelName("deepseek-chat")
                .build();

        // 2 embedding模型
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        // 3 向量存储
        EmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();

        // 4 加载文档
        String text = Files.readString(Path.of("data/knowledge.txt"));
        Document document = Document.from(text);

        // 5 文档切分
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 50);
        List<TextSegment> segments = splitter.split(document);

        // 6 向量化并存储
        for (TextSegment segment : segments) {
            Embedding embedding = embeddingModel.embed(segment).content();
            store.add(embedding, segment);
        }

        // 7 创建检索器
        EmbeddingStoreContentRetriever retriever =EmbeddingStoreContentRetriever
                .builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                .build();

        // 8 用户问题
        String question = "什么是Transformer模型？";

        // 9 检索相关内容
        List<Content> contents = retriever.retrieve(Query.from(question));

        // 10 构建上下文
        StringBuilder context = new StringBuilder();
        for (Content c : contents) {
            context.append(c.textSegment().text()).append("\n");
        }

        // 11 构建Prompt
        String prompt = "根据以下知识回答问题：\n" + context + "\n问题：" + question;

        // 12 调用 DeepSeek
        String answer = model.generate(prompt);

        // 13 输出答案
        System.err.println(answer);

    }
}