# Quarkus - Kamelet

Quarkus-kamelet provide you support to interacting with Camel routes templates.
The aim of this module is to cover the following Kamelet scenarios:
* Camel producers, those scenarios where your service produces events and are consumed by a camel route
* Camel consumers, those scenarios where your service consumes a camel route
* Chain routes multiples routes
* Load application properties as routes bodies
* Validate Kamelet resources as ocp/k8s kamelet yamls(routes-temapltes, routes-bindings...)

Project folder structure

* `/resources/kamelets` contains kamelets resources as templates or KameletBindings. Also, there are groovy scripts 
in order to instantiate these templates by your self (as an example). 

* `io.quarkus.qe.kamelet.KameletRoutes` contains templates that could be invoked (tested) directly by code. So is not 
need it to be deployed into ocp or some other platform. 

### Recommended Readings
[Kamelet introduction](https://camel.apache.org/camel-k/latest/kamelets/kamelets-user.html)

[Kamelets developer guide](https://camel.apache.org/camel-k/latest/kamelets/kamelets-dev.html)

[Camel-Quarkus first steps](https://camel.apache.org/camel-quarkus/latest/user-guide/first-steps.html)
