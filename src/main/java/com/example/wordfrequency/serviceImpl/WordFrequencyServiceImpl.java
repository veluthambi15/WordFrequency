package com.example.wordfrequency.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.wordfrequency.model.AwsProperties;
import com.example.wordfrequency.service.WordFrequencyService;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

/**
 * Service implementation for analyzing word frequency in a file stored in Amazon S3.
 */
@Service
public class WordFrequencyServiceImpl implements WordFrequencyService {

    @Autowired
    private S3Client s3Client;
    
    @Autowired
    private AwsProperties awsProperties;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;
    
    // Cache to store word frequency results for different files and k values
    private Map<String, Map<Integer, Map<String, Integer>>> cache = new HashMap<>();
    
    // Method executed after bean creation to configure AWS SDK with credentials
    @PostConstruct
    public void configureAwsSdk() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(awsProperties.getAccessKey(), awsProperties.getSecretKey());
        s3Client = S3Client.builder()
        		.region(Region.of(awsRegion))  // Set the AWS region
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
    
    /**
     * Finds the top K most frequent words in a file stored in Amazon S3.
     * @param fileUrl String - S3 URL of the file to analyze
     * @param k int - Number of top words to retrieve
     * @return Map<String, Integer> - Map containing the top K words and their frequencies
     * @throws IOException if an I/O error occurs during file processing
     */
    public Map<String, Integer> findTopWords(String fileUrl, int k) throws IOException {
        // Check if result is cached
        if (cache.containsKey(fileUrl) && cache.get(fileUrl).containsKey(k)) {
            return cache.get(fileUrl).get(k);
        }

        Map<String, Integer> wordFrequencyMap = new HashMap<>();

        try (ResponseInputStream<GetObjectResponse> s3ObjectInputStream = downloadFileFromS3(fileUrl)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(s3ObjectInputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                processLine(line, wordFrequencyMap);
            }
        }

        Map<String, Integer> result = getTopKWords(wordFrequencyMap, k);

        // Cache the result
        cache.computeIfAbsent(fileUrl, key -> new HashMap<>()).put(k, result);

        return result;
    }

    
    /**
     * Downloads a file from an S3 bucket specified by the URL.
     * @param fileUrl String - S3 URL of the file to download
     * @return ResponseInputStream<GetObjectResponse> - Response input stream containing the file content
     */
    public ResponseInputStream<GetObjectResponse> downloadFileFromS3(String fileUrl) {
        // Assuming the URL is in the format: s3://bucket-name/file-path
        String[] parts = fileUrl.split("/", 4);
        String bucketName = parts[2];
        String key = parts[3];

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Client.getObject(getObjectRequest);
    }
    
    /**
     * Processes a line from the file and updates the word frequency map.
     * @param line String - Line of text from the file
     * @param wordFrequencyMap Map<String, Integer> - Map to update with word frequencies
     */
    public void processLine(String line, Map<String, Integer> wordFrequencyMap) {
        StringTokenizer tokenizer = new StringTokenizer(line, " ,.!?;':\"()");
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken().toLowerCase();
            wordFrequencyMap.merge(word, 1, Integer::sum);
        }
    }
    
    /**
     * Retrieves the top K words from the word frequency map.
     * @param wordFrequencyMap Map<String, Integer> - Word frequency map to retrieve top words 
     * @param k int - Number of top words to retrieve
     * @return Map<String, Integer> - Map containing the top K words and their frequencies in descending order
     */
    public Map<String, Integer> getTopKWords(Map<String, Integer> wordFrequencyMap, int k) {
        return wordFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(k)
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
    }
}
