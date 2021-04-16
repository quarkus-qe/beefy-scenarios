Module that covers the RestClient integration with OIDC extension using Keycloak container.

Applications:
- Ping application that will invoke the Pong application using the RestClient and will return the expected "ping pong" output.
- Pong application that will return the "pong" output.
- Secured endpoints to output the claims data
- Token generator endpoint to generate tokens using the OIDC Client

Test cases:
- When calling `/ping` or `/pong` endpoints without bearer token, then it should return 401 Unauthorized. 
- When calling `/ping` or `/pong` endpoints with incorrect bearer token, then it should return 401 Unauthorized.
- When calling `/ping` endpoint with valid bearer token, then it should return 200 OK and "ping pong" as response.
- When calling `/pong` endpoint with valid bearer token, then it should return 200 OK and "pong" as response.

Variants:
- Using REST endpoints (quarkus-resteasy extension)
- Using Reactive endpoints (quarkus-resteasy-mutiny extension)
- Using Lookup authorization via `@ClientHeaderParam` annotation 
- Using `OIDC Client Filter` extension to automatically acquire the access token from Keycloak when calling to the RestClient.
- Using `OIDC Token Propagation` extension to propagate the tokens from the source REST call to the target RestClient. 
