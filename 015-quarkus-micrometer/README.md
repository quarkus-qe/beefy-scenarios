Module that covers the Micrometer extension usage via `MeterRegistry` and `MicroProfile API` approaches. Both will use a counter metrics for testing purposes. 

The `MeterRegistry` approach includes three scenarios: 
- `simple`: single call will increment the counter.
- `forloop`: will increment the counter a number of times.
- `forloop parallel`: will increment the counter a number of times using a parallel flow.

The `MicroProfile API` approach will include only the `simple` scenario.   