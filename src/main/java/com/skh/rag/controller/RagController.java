package com.skh.rag.controller;

import com.skh.rag.service.DocumentService;
import com.skh.rag.service.RagService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rag")
public class RagController {

    @Resource
    private RagService ragService;
    @Resource
    private DocumentService documentService;

    @PostMapping("/import")
    public String importDoc() throws Exception {
        documentService.importDocument("data/knowledge.txt");
        return "导入成功";
    }

    @GetMapping("/ask")
    public String ask(String question) {
        return ragService.ask(question);
    }

}