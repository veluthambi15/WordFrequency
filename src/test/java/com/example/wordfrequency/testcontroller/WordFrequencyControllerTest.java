package com.example.wordfrequency.testcontroller;

import com.example.wordfrequency.controller.WordFrequencyController;
import com.example.wordfrequency.model.FileRequest;
import com.example.wordfrequency.service.WordFrequencyService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WordFrequencyControllerTest {

    @Mock
    private WordFrequencyService wordFrequencyService;

    @InjectMocks
    private WordFrequencyController wordFrequencyController;

    @Test
    @DisplayName("Test Get The Top Words - Positive Case")
    void getTopWords_PositiveCase() throws IOException {
    	
        // Mocking the service response
        Map<String, Integer> expectedResponse = new HashMap<>();
        expectedResponse.put("word1", 5);
        expectedResponse.put("word2", 3);

        when(wordFrequencyService.findTopWords(any(String.class), any(Integer.class))).thenReturn(expectedResponse);

        // Making a request
        FileRequest fileRequest = new FileRequest();
        fileRequest.setUrl("s3://bucket-name/file-path");
        fileRequest.setK(2);

        // Calling the controller method
        ResponseEntity<Map<String, Integer>> responseEntity = wordFrequencyController.getTopWords(fileRequest);

        // Verifying the service method is called
        verify(wordFrequencyService, times(1)).findTopWords(fileRequest.getUrl(), fileRequest.getK());

        // Verifying the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test Get The Top Words - Negative Case")
    void getTopWords_NegativeCase() throws IOException {
        // Mocking the service to throw an exception
        when(wordFrequencyService.findTopWords(any(String.class), any(Integer.class))).thenThrow(new IOException("File not found"));

        // Making a request
        FileRequest fileRequest = new FileRequest();
        fileRequest.setUrl("s3://bucket-name/non-existent-file");
        fileRequest.setK(2);

        // Calling the controller method
        ResponseEntity<Map<String, Integer>> responseEntity = wordFrequencyController.getTopWords(fileRequest);

        // Verifying the service method is called
        verify(wordFrequencyService, times(1)).findTopWords(fileRequest.getUrl(), fileRequest.getK());

        // Verifying the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}

