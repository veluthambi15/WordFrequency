# Word Frequency Application

## Overview
This application calculates the top K most frequent words in a given text file. It utilizes AWS S3 for file storage and retrieval.

## Instructions to Run the Application

### Prerequisites
- Java JDK 11 or higher
- Maven
- AWS S3 Bucket

### Steps
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/veluthambi15/WordFrequency.git

Configure AWS Credentials:
Update application.properties with your AWS credentials.

## properties

   aws.access-key=<your_access_key>
   
   aws.secret-key=<your_secret_key>

   cloud.aws.region.static=<your_region>

## Run the Application:

Open the application and run it as SpringBoot App using the IDE 


## The application will be accessible at 
	
	http://localhost:8080/api/swagger-ui/

## Endpoints
Word Frequency: POST /frequencychecker/frequentwords

## Example Request Body:
   json
   {
     "url": "s3://your-bucket/your-file.txt",
     "k": 10
   }

## Run Tests:

   mvn test

## Time and Space Complexity
Time Complexity: O(N log K), where N is the number of words in the file and K is the desired number of top words.
Space Complexity: O(N), where N is the number of unique words in the file.

## Design Choices
The application uses AWS S3 for scalable storage and retrieval of text files.
The algorithm processes the file line by line, tokenizing and counting word occurrences.
The application is designed to cache results for improved performance on repeated requests.

## Technologies Used
Java Spring Boot
AWS SDK for Java
Maven for Dependency Management
