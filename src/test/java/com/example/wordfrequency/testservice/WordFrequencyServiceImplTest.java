package com.example.wordfrequency.testservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.wordfrequency.model.AwsProperties;
import com.example.wordfrequency.serviceImpl.WordFrequencyServiceImpl;

import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
class WordFrequencyServiceImplTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private AwsProperties awsProperties;

    @InjectMocks
    private WordFrequencyServiceImpl wordFrequencyService;

    @Test
    @DisplayName("Test Process Line - Positive Case")
    void processLine_PositiveCase() {
        // Mocking the word frequency map
        Map<String, Integer> wordFrequencyMap = new HashMap<>();

        // Making a request
        String line = "word1 word2 word1";

        // Calling the service method
        wordFrequencyService.processLine(line, wordFrequencyMap);

        // Verifying the result
        assertNotNull(wordFrequencyMap);
        assertEquals(2, wordFrequencyMap.size());
        assertEquals(2, wordFrequencyMap.get("word1"));
        assertEquals(1, wordFrequencyMap.get("word2"));
    }

    @Test
    @DisplayName("Test Get The Top Words - Positive Case")
    void getTopKWords_PositiveCase() {
        // Mocking the word frequency map
        Map<String, Integer> wordFrequencyMap = new HashMap<>();
        wordFrequencyMap.put("word1", 3);
        wordFrequencyMap.put("word2", 5);

        // Making a request
        int k = 2;

        // Calling the service method
        Map<String, Integer> result = wordFrequencyService.getTopKWords(wordFrequencyMap, k);

        // Verifying the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(5, result.get("word2"));
        assertEquals(3, result.get("word1"));
    }
}
