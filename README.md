
# Gamelist Web Application - Backend

This repository serves as the backend for the "Gamelist" project, a web application designed to facilitate game management, list creation, and game reviews. The backend is developed using Java and incorporates various technologies for seamless functionality.




## Tech Stack

**Client:** Angular,Material, TailwindCSS

**Server:** Java, Spring boot, Maven, MySQL

**Deploy(not running now):** AWS EC2,S3 and RDS


## Features

- Game Management: Users can efficiently manage their game collection, create custom lists, and leave reviews for each game.
- IGDB API Integration: Utilizes the IGDB API to fetch comprehensive data for each game, enhancing the user experience with detailed game information.
- Search Functionality: Enables users to easily search for games within the application.
- Technologies Used:
    - Persistence: JPA for efficient data persistence.
    - Authentication: Firebase Auth ensures secure user authentication.
    - Database: MySQL for reliable data storage.
    - Authorization: JWT with Firebase for secure authorization.
    - Package Structure: Follows the package-for-feature organizational pattern for        improved  code structure.
    - Testing: Unit and integration testing conducted using JUnit and Mockito.
    - Image Handling: Cloudinary integration for effective image management.

- Deployment : Amazon Web Services : 
    - EC2 instance
    - RDS MySQL 


## Run Locally

Clone the project

```bash
  git clone https://github.com/lauchilus/backendProject
```

Create a application.properties in resource folder and add

```bash
  spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://www.googleapis.com/service_accounts/v1/jwk/securetoken%40system.gserviceaccount.com
```

Configure Firebase

```bash
  Set up a Firebase project and configure the Firebase Auth credentials.
```

Get your Twitch Secrets(This for igdb api)

```bash
  https://dev.twitch.tv/docs/authentication/register-app/
```

Get your Cloudinary account

```bash
 https://cloudinary.com/
```

Create a class "SecretsStore" in com.gamelist.main.helpers and create the following Variables

```bash
 //Twitch Secrets
	public static final String API_CLIENT_ID =  "YOUR_CLIENT_ID";
	public static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
	
	//Cloudinary Secrets
	public static final String CLOUD_NAME = "YOUR_CLOUD_NAME";
	public static final String API_KEY = "YOUR_API_KEY";
	public static final String API_SECRET = "YOUR_API_SECRET";
```


## Run the project

Run the project from your favorite IDE

Go to you web browser or postman and test if is working

```bash
  http:localhost:8080/games?offset=0
```


## Running Tests


First create a application-test.properties in src/test/resources



To run tests, run the following command

```bash
  mvn test
```


## Others

#### CrossOrigin - CORS

The controllers are configured with the @CrossOrigin annotation applied to all endpoints. Feel free to make changes to this configuration as needed.



## Feedback

If you have any feedback, please feel free to reach out to me at lautarofalfan@gmail.com. This is my first real project, so any input would be highly appreciated!


## Documentation

[Documentation](https://linktodocumentation) (In progress sorry >.<)

