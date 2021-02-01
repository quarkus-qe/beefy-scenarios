# Quarkus  / Vert.x SQL

<img src="https://media4.giphy.com/media/l0MYs84E42CL9I9JC/giphy.gif?cid=ecf05e474m9in4t30rbqkspwiylltm8i34is1jvk8cmzshty&rid=giphy.gif" title="Find your flight!" >

## Requirements

To compile and run this demo you will need:

- JDK 11+
- Docker
- PostgreSQL
- MySQL
- DB2

## Scope of the testing

Quarkus / Vertx SQL exploratory testing

* Quarkus Vertx Reactive Routes (annotations)
* OpenAPI from annotations
* Reactive SQL / PostgreSQL / MySQL / DB2
    * Select / insert statements
    * Transaction statement between several tables 
* Error mapping through routes

## Description

A flight search engine in order to test Quarkus Reactive SQL extensions.
End user-facing website allows customers to compare flight prices of different airlines based on various business rules. 
The end phase is to book the flight in a transactional way

## Pricing rules

### Days to departure date
| days prior to the departure date | % of the base price |
|----------------------------------|---------------------|
| more than 30                     | 80%                 |
| 30 - 16                          | 100%                |
| 15 - 3                           | 120%                |
| less that 3                      | 150%                |

### Passenger type
| passenger type | price                                                                                          |
|----------------|------------------------------------------------------------------------------------------------|
| adult          | full price (i.e. price resulting from the *days to departure date* rule)                       |
| child          | 33% discount of the price calculated according to the *days to departure date* rule            |
| infant         | fixed price depending on the airline. Rule *days to departure date* is not applied for infants |

### Airports
| IATA code | city       |
|-----------|------------|
| MAD       | Madrid     |
| BCN       | Barcelona  |
| LHR       | London     |
| CDG       | Paris      |
| FRA       | Frakfurt   |
| IST       | Istanbul   |
| AMS       | Amsterdam  |
| FCO       | Rome       |
| CPH       | Copenhagen |

### Airlines with infant prices
| IATA code | name             | infant price |
|-----------|------------------|--------------|
| IB        | Iberia           | 10 €         |
| BA        | British Airways  | 15 €         |
| LH        | Lufthansa        | 7 €          |
| FR        | Ryanair          | 20 €         |
| VY        | Vueling          | 10 €         |
| TK        | Turkish Airlines | 5 €          |
| U2        | Easyjet          | 19.90 €      |

### Flights

You could have a look [on this link](src/main/resources/db/migration/common/V1.0.1__load_default_data.sql). All discounts are applied over, flight's base price.

## Examples:

* 1 adult, 31 days to the departure date, flying AMS -> FRA

  flights:

    * TK2372, 157.6 €
    * TK2659, 198.4 €
    * LH5909, 90.4 €

* 2 adults, 1 child, 1 infant, 15 days to the departure date, flying LHR -> IST

  flights:

    * TK8891, 806 € (2 * (120% of 250) + 67% of (120% of 250) + 5)
    * LH1085, 481.19 € (2 * (120% of 148) + 67% of (120% of 148) + 7)

* 1 adult, 2 children, 2 days to the departure date, flying BCN -> MAD

  flights:

    * IB2171, 909.09 € (150% of 259 + 2 * 67% of (150% of 259))
    * LH5496, 1028.43 € (150% of 293 + 2 * 67% of (150% of 293))


## Recommended Readings

[Reactive SQL](https://quarkus.io/guides/reactive-sql-clients)

[Reactive Routes](https://quarkus.io/guides/reactive-routes)

[Domain Driven Design](https://www.amazon.es/Domain-Driven-Design-Tackling-Complexity-Software/dp/0321125215/ref=sr_1_1?adgrpid=67721313493&dchild=1&gclid=CjwKCAiAl4WABhAJEiwATUnEF7tdh_PoSQPXNWWntUHxb2pCj0DG_iM6V79-txOlzynCiL94p-Og_BoCzVgQAvD_BwE&hvadid=338561691742&hvdev=c&hvlocphy=1005417&hvnetw=g&hvqmt=e&hvrand=3068697347785968801&hvtargid=kwd-301893551508&hydadcr=16512_1804452&keywords=domain+driven+design+eric+evans&qid=1610717134&sr=8-1&tag=hydes-21)


## Useful material

Docker compose: `src/test/resources/docker-compose.yaml`
Swagger: `http://localhost:8082/q/swagger-ui/`

Example: 

Search flight from AMS to FRA 1 adult, 31 days before departure time:
 
```
curl -X PUT "http://localhost:8082/flights/search" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"adult\":1,\"child\":0,\"infant\":0,\"daysToDeparture\":31,\"from\":\"AMS\",\"to\":\"FRA\"}"
```

Search flight from LHR to IST 2 adult 1 child 1 infant, 15 days before departure time:
 ```
curl -X PUT "http://localhost:8082/flights/search" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"adult\":2,\"child\":1,\"infant\":1,\"daysToDeparture\":15,\"from\":\"LHR\",\"to\":\"IST\"}"
```

Search flight from BCN to MAD 1 adult 2 child , 2 days before departure time:

```
curl -X PUT "http://localhost:8082/flights/search" -H  "accept: application/json" -H  "Content-Type: application/json" -d "{\"adult\":1,\"child\":2,\"daysToDeparture\":2,\"from\":\"BCN\",\"to\":\"MAD\"}"
```