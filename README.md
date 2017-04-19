## Spring REST API Example
Simple REST API example application build with Spring Boot and secured with Spring Security.

#### Testing: 
`./gradlew test` 

#### Running: 
`./gradlew bootRun` application starts on http://localhost:8080


#### Calling API: 
You can import [Postman](https://www.getpostman.com/) collection from postman directory to se some examples.   
API endpoints are secured with spring-security. To access secured endpoints you must call /oauth/token endpoint first to get access token.
