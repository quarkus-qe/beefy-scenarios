# scenario-201-static

Query static resources.

Based on https://github.com/thorntail/thorntail-examples/tree/master/static/static-war.

Also worth checking out: https://quarkus.io/guides/http-reference

Test:
- Query the root path (index.html).

Big file scenario:
- Generate a large file (invoked by Maven build).
- Query for the file.
