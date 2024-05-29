package com.example.springbackend.service;

import com.example.springbackend.entity.Summary;
import com.example.springbackend.repository.SummaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class SummarizeService {

    private static final Logger logger = LoggerFactory.getLogger(SummarizeService.class);

    private final RestTemplate restTemplate;
    private final SummaryRepository summaryRepository;

    @Autowired
    public SummarizeService(RestTemplate restTemplate, SummaryRepository summaryRepository) {
        this.restTemplate = restTemplate;
        this.summaryRepository = summaryRepository;
    }

    public String summarize(String url) {
        String apiUrl = "http://localhost:8000/summarize"; // Update this URL if needed
        SummarizeRequest request = new SummarizeRequest(url);
        try {
            SummarizeResponse response = restTemplate.postForObject(apiUrl, request, SummarizeResponse.class);
            if (response == null || response.getSummary() == null) {
                throw new RuntimeException("Failed to get a response or summary text from the summarization API");
            }

            String summaryText = response.getSummary();

            // Save the summary to the database
            Summary summary = new Summary(url, summaryText);
            summaryRepository.save(summary);

            return summaryText;
        } catch (RestClientException e) {
            logger.error("Error while communicating with the summarization API", e);
            throw new RuntimeException("Error while communicating with the summarization API", e);
        }
    }

    private static class SummarizeRequest {
        private String url;

        public SummarizeRequest(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    private static class SummarizeResponse {
        private String summary;

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }
}
