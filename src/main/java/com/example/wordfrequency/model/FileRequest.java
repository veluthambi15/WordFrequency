package com.example.wordfrequency.model;

/**
 * Model class representing a request for word frequency analysis from a file.
 * It contains information such as the file URL and the value of 'k' for determining the top K frequent words.
 */
public class FileRequest {
	
    private String url;  // File URL to be analyzed
    private int k;       // Value of 'k' for top K frequent words

    /**
     * Getter method for retrieving the file URL.
     * @return String - File URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter method for setting the file URL.
     * @param url String - File URL to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter method for retrieving the value of 'k'.
     * @return int - Value of 'k'
     */
    public int getK() {
        return k;
    }

    /**
     * Setter method for setting the value of 'k'.
     * @param k int - Value of 'k' to set
     */
    public void setK(int k) {
        this.k = k;
    }
}