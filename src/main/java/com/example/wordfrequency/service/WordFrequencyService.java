package com.example.wordfrequency.service;

import java.io.IOException;
import java.util.Map;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

/**
 * Service interface for analyzing word frequency in a file.
 */
public interface WordFrequencyService {
	
	 // Finds the top K most frequent words in a file specified by the URL.
	 Map<String, Integer> findTopWords(String fileUrl, int k) throws IOException;
	 
	 // Downloads a file from an S3 bucket specified by the URL.
	 ResponseInputStream<GetObjectResponse> downloadFileFromS3(String fileUrl);
	 
	 // Processes a line from the file and updates the word frequency map.
	 void processLine(String line, Map<String, Integer> wordFrequencyMap);
	 
	 // Retrieves the top K words from the word frequency map.
	 Map<String, Integer> getTopKWords(Map<String, Integer> wordFrequencyMap, int k);

}
