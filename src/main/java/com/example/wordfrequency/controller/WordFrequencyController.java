package com.example.wordfrequency.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wordfrequency.model.FileRequest;
import com.example.wordfrequency.service.WordFrequencyService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/frequencychecker")
public class WordFrequencyController {

	@Autowired
	private WordFrequencyService wordFrequencyService;

	/**
     * Endpoint to retrieve the top K most frequent words from a AWS file.
     * @param fileRequest Request object containing the file URL and K value
     * @return ResponseEntity<Map<String, Integer>> - Response with the top words and their frequencies
     */
	@PostMapping("/frequentwords")
	@ApiOperation(value = "Get the top K most frequent words", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the top words"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
	public ResponseEntity<Map<String, Integer>> getTopWords(@RequestBody FileRequest fileRequest) {
		try {
			Map<String, Integer> topWords = wordFrequencyService.findTopWords(fileRequest.getUrl(), fileRequest.getK());
			return ResponseEntity.ok(topWords);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

 }
