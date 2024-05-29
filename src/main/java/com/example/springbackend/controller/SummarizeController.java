package com.example.springbackend.controller;

import com.example.springbackend.entity.Summary;
import com.example.springbackend.repository.SummaryRepository;
import com.example.springbackend.service.SummarizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SummarizeController {

    private final SummarizeService summarizeService;
    private final SummaryRepository summaryRepository;

    @Autowired
    public SummarizeController(SummarizeService summarizeService, SummaryRepository summaryRepository) {
        this.summarizeService = summarizeService;
        this.summaryRepository = summaryRepository;
    }

    @PostMapping("/summarize")
    public String summarize(@RequestBody String url) {
        return summarizeService.summarize(url);
    }

    @GetMapping("/history")
    public List<Summary> getHistory() {
        return summaryRepository.findAll();
    }
}
