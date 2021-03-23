# Table of Contents
1. [Quarkus http non-application endpoints](#Quarkus-http-non-application-endpoints)
2. [Quarkus - OpenShift extension](#Quarkus-openshift-extension)

## Quarkus http non-application endpoints

We have covered the following non-application endpoints:

```
- /metrics
    - /metrics/vendor
    - /metrics/application
    - /metrics/base
- /health
  - /health/live
  - /health/ready
  - /health/well
  - /health/group
- /openapi
- /swagger-ui
```

Scenarios:
* Relative paths: 

```
# Application.properties example
quarkus.http.root-path=/api
quarkus.http.non-application-root-path=q
quarkus.http.redirect-to-non-application-root-path=false
```

Valid request example: `http://localhost:8080/api/q/health`

* Empty non-application root path:

```
# Application.properties example
quarkus.http.root-path=/api
quarkus.http.non-application-root-path=/
quarkus.http.redirect-to-non-application-root-path=false
```
All request will return an HTTP status `404`

* Non-application root path with `slash` base path:
```
# Application.properties example
%emptyRootPath.quarkus.http.root-path=/
%emptyRootPath.quarkus.http.non-application-root-path=/q
%emptyRootPath.quarkus.http.redirect-to-non-application-root-path=false
```

Valid request example: `http://localhost:8080/q/health`

* Defined application base path and non-application root path:

```
# Application.properties example
quarkus.http.root-path=/api
quarkus.http.non-application-root-path=/q
quarkus.http.redirect-to-non-application-root-path=false
```

Valid request example: `http://localhost:8080/q/health`

* Backward compatibility (allow redirections):

```
quarkus.http.root-path=/api
quarkus.http.non-application-root-path=/q
quarkus.http.redirect-to-non-application-root-path=true
```

Valid request example: `http://localhost:8080/api/health`, will be redirected to `http://localhost:8080/q/health`

## Quarkus OpenShift extension 
By adding *quarkus-openshift* extension, maven build creates */kubernetes* directory under the */target*
which includes *.yml* files for deployment

### Test goal
The goal of the test is to ensure that *kubernetes.yml* file has been created
under mentioned */kubernetes* directory and parse that file to see required fields are present.
