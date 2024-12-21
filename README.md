# Slideshow API

## Overview

The Slideshow API allows users to manage a list of image URLs and play a slideshow with specified transitions. Users can add, remove, and search images, as well as create slideshows that play images. The application also includes features for tracking and recording image lifecyle events (add, delete) and their transition in the slideshow.

## Core Features

- **Add a new image URL**: Store image URLs with specific durations for slideshow transitions.
- **Delete an image**: Remove an image from the system by its ID.
- **Add a new slideshow**: Create a slideshow by specifying a list of images.
- **Delete a slideshow**: Remove a slideshow by its ID.
- **Search for images**: Find images by keywords in their URL or by the duration associated with the image.
- **Retrieve ordered images for a slideshow**: Get images in a DESC order based on their addition date.
- **Proof-of-play notification**: Track when an image in the slideshow has been played.

---

## Technologies Used

- **Java 17**: For the backend application development.
- **Spring Boot 3.4.x**: For building RESTful APIs.
- **MySQL 8.0**: For persistent data storage.
- **JUnit 5**: For unit testing.
- **Maven**: For build and dependency management.
- **Docker**: For containerization.
- **OpenAPI**: For the API schema documentation.

---

## API Endpoints

### 1. `POST /addImage`

Add a new image URL with a specified duration.

#### Request Body:
```json
{
  "url": "https://example.com/image.jpg",
  "name": "example",
  "duration": 500
}
```
* **url** : *Required*. The URL of the image (must be validated to ensure it points to an actual image file such as JPEG, PNG, WEBP, etc.).
* **duration** : *Required*. The duration in milliseconds that the image will be displayed in the slideshow.


#### Response Body:
```json
{
  "id": 1,
  "url": "https://example.com/image.jpg",
  "name": "example",
  "duration": 500,
  "addedAt": "2024-12-21T16:35:00.130338"
}
```

### 2. `DELETE /deleteImage/{id}`
Remove an image by its ID.
#### Path Parameter:
* **id** :  The unique identifier of the image to delete


### 3. `POST /addSlideshow`
Create a new slideshow with the specified list of images
#### Request Body:
```json
{
  "name": "Slideshow",
  "description": "Slideshow",
  "imagesIds": [
    1,2
  ]
}
```
* **name** : *Optional*. Name of the slideshow.
* **description** : *Optional*. Description of the slideshow.
* **imagesIds** : *Required*: List of images IDs.

### 4. `DELETE /deleteSlideshow/{id}`
Remove a slideshow by its ID.
#### Path Parameter:
* **id** :  The unique identifier of the slideshow to delete

### 5. `GET /slideShow/{id}/slideshowOrder`
Retrieve images in a slideshow ordered by the image addition date
#### Path Parameter:
* **id** :  The unique identifier of the slideshow to get images for
#### Query Parameter:
* **sortDirection** : Sort direction, value must be ASC or DESC

### 6. `GET /images/search`
Search for images and their associated slideshows by keywords in the image URL or by the duration. At least one of the search parameters ("keywords" or "duration") should be specified. 
Returns paginated result of all images that have specified *duration* or URL containing at least one of the *keywords* 
#### Query Parameter:
* **keywords** : A list of keywords to search for in the image URL.
* **duration** : Duration in milliseconds to filter images by their duration
* **pageIndex** : Index of the page to return from paginated result. Default value=0
* **pageSize** : Number of elements per page
#### Response Body:
```json
{
  "search": {
    "duration": 1500,
    "keywords": [
      "castle"
    ]
  },
  "data": {
    "totalPages": 1,
    "totalElements": 3,
    "pageSize": 10,
    "pageIndex": 0,
    "results": [
      {
        "id": 2,
        "url": "https://upload.wikimedia.org/wikipedia/commons/castle.jpg",
        "name": "castle",
        "duration": 500,
        "slideshows": [
          {
            "id": 1,
            "name": "arch"
          }
        ]
      },
      {
        "id": 4,
        "url": "https://upload.wikimedia.org/wikipedia/commons/7/7e/cat.jpg",
        "name": "cat",
        "duration": 1500,
        "slideshows": []
      }
    ]
  }
}
```
### 7. `POST slideShow/{id}/proof-of-play/{imageId}`
Record an event when an image is replaced by the next one in the slideshow
#### Path Parameter:
* **id** :  The unique identifier of the slideshow where the image is being played
* **imageId** :  The unique identifier of the image being played

---
## Error Handling
The API returns standardized error responses with the following HTTP codes
* **400 Bad Request**: When input validation fails (e.g., invalid URL).
* **404 Not Found**: When an entity (e.g., image, slideshow) with the provided ID is not found.
* **500 Internal Server Error**: For unexpected server errors.

#### Error Message Body:
```json
{
  "message": "Resource not found",
  "details": "Image [7] doesn't exist."
}
```
---
## Event
The API returns standardized error responses with the following HTTP codes
* **400 Bad Request**: When input validation fails (e.g., invalid URL).
* **404 Not Found**: When an entity (e.g., image, slideshow) with the provided ID is not found.
* **500 Internal Server Error**: For unexpected server errors.

#### Error Message Body:
```json
{
  "message": "Resource not found",
  "details": "Image [7] doesn't exist."
}
```
## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yupolinkevich/slideshow.git
   cd slideshow
2. Build the project
    ```bash
   mvn clean package
   
3. Create MySQL database
    ```bash
   CREATE DATABASE slideshow;
4. Update application.properties if needed, e.g.
    ```bash
    spring:
       datasource:
          url:      jdbc:mysql://localhost:3306/slideshow
          username: root
          password:
5. Run the application:
    ```bash
    java -jar target/slideshow.jar
6. Access the service:

   * API: http://localhost:8080
   * Swagger UI: http://localhost:8080/swagger-ui.html

**Docker Deployment**

Run the following to command to build images and start containers from the root app directory
    
   ```bash
    docker compose up --build

