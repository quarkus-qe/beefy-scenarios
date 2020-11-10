# beefy-scenarios
Beefy scenarios for Quarkus

## Prerequisites
 - JDK 11
 - Docker

## Scenarios IDs
| Range         | Name          | Description  |
| ------------- |:-------------:| ----- |
| 000 - 099     | Quarkus | Quarkus native scenarios |
| 100 - 199     | QuarkEE | Quarkus scenarios with Java EE-like experience |
| 200 - 299     | QuarkTT | Quarkus scenarios with Thorntail-like experience |
| 300 - 399     | QuarkVert | Quarkus scenarios with Vert.x experience |
| 400 - 499     | QuarkConf | Quarkus scenarios presented at conferences |
| 500 - 599     | QuarkUser | Quarkus scenarios adopted by end users |
| 600 - 699     | QuarkSpring | Quarkus scenarios with Spring experience |

Note: It's expected that the majority of modules will go into 000 - 099 Quarkus bucket

## Tips
### LargeStaticResourceTest - testBigFileAvailability failure
On some system you may hit `LargeStaticResourceTest.testBigFileAvailability:35 » Connect Connection refuse` issue.

The root cause is that the standard JDK code can't handle properly `sun.net.www.protocol.http.HttpURLConnection` with value `http://0.0.0.0:8081/big-file`.

Use `quarkus.http.host` property to explicitly specify `127.0.0.1` as the bind address.
```
mvn clean verify -pl 201-large-static-content -Dquarkus.http.host=127.0.0.1
```