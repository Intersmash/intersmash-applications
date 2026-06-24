# Intersmash Applications - WildFly Distributed Timers with Infinispan

A WildFly/JBoss EAP 8.x application that demonstrates distributed EJB timer management using a remote
Infinispan server for timer persistence. Timer execution events are recorded by a separate
`timer-expiration-store` service backed by PostgreSQL.

The whole application is basically a wrapper around `jakarta.ejb.TimerService` to create timers which are cached in a 
remote Infinispan server; timers are set in motion and expected to expire at determined points in time; timers are also
expected to survive cluster failures;

## Architecture

The application involves three infrastructure components:

```
                                                     
  Client          WildFly App Server          Infinispan Server    WildFly timer-expiration-store    PostgreSQL
    |                    |                            |                         |                        |
    |--- REST call ----->|                            |                         |                        |
    |                    |--- store timer state ----->|                         |                        |
    |                    |        (HotRod)            |                         |                        |
    |                    |                            |                         |                        |
    |                    |<--- timer fires ---------->|                         |                        |
    |                    |--- record expiration ------------------------------>|--- persist ----------->|
    |                    |        (Remote EJB)        |                         |       (JPA)            |
                                                     
```

- **WildFly App Server**: runs this application; manages EJB timers whose state is persisted to a
  remote Infinispan server via the HotRod protocol instead of the default local JDBC store.
- **Infinispan Server (Red Hat Data Grid)**: provides distributed, highly-available timer state
  storage. Communication is secured with SCRAM-SHA-512 authentication over TLS.
- **WildFly timer-expiration-store**: a separate WildFly application that exposes a `@Remote` EJB interface
  (`TimerExpirationStore`). Each time a timer fires, this application records the event (executor
  hostname, timer name, timestamp) via a remote EJB call to the store, which persists it to
  PostgreSQL via JPA.

## Timer execution flow

1. A client creates a timer via the REST API.
2. The EJB `TimerService` creates a persistent interval timer; its state is stored in the remote
   Infinispan cache (`hotrod-persistent`).
3. After the initial delay, the timer fires and invokes
   `TransactionalRecurringTimerService.doExecute()` in a new transaction (`REQUIRES_NEW`).
4. The timeout method builds a `TimerExpiration` record (executor hostname, timer name, application
   info, timestamp) and sends it to the `timer-expiration-store` service via a remote EJB call.
5. The store persists the record to PostgreSQL.
6. Steps 3-5 repeat at every expiration interval until the timer is cancelled.

Because timer state lives in the remote Infinispan cluster rather than in a local database, the
timer survives application server restarts and can be managed across a WildFly cluster.

## Configuration

All settings support a system property, an environment variable, and a default value (in that
priority order):

| Setting                    | System property                            | Environment variable                          | Default              |
|----------------------------|--------------------------------------------|-----------------------------------------------|----------------------|
| Timer store URL            | `timer.expiration.api.base.url`            | `TIMER_EXPIRATION_API_BASE_URL`               | `http://localhost:8080` |
| Timer initial delay (ms)   | `recurring.timer.execution.initial.delay`  | `RECURRING_TIMER_EXECUTION_INITIAL_DELAY`     | `10000`              |
| Timer interval (ms)        | `recurring.timer.expiration.interval`      | `RECURRING_TIMER_EXPIRATION_INTERVAL`         | `1000`               |

The remote Infinispan connection is configured via environment variables consumed by the
`scripts/remote-infinispan.cli` provisioning script:

| Environment variable     | Description                                     |
|--------------------------|-------------------------------------------------|
| `JDG_HOST`               | Infinispan server hostname                      |
| `JDG_PORT`               | Infinispan server HotRod port                   |
| `CACHE_USERNAME`          | SASL authentication username                   |
| `CACHE_PASSWORD`          | SASL authentication password                   |
| `TRUST_STORE_PASSWORD`    | Password for the TLS trust store                |

## Build and provisioning

The application is built as `ROOT.war` and provisioned with a trimmed WildFly server using the
`wildfly-maven-plugin`.

Galleon layers:
- `ejb` -- EJB 3 support
- `ejb-dist-cache` -- distributed EJB timer/session caching
- `ejb-local-cache` is **excluded** (replaced by the remote Infinispan store)

During provisioning, `scripts/remote-infinispan.cli` configures the server to:

1. Connect to a remote Infinispan server via HotRod with SCRAM-SHA-512 authentication over TLS.
2. Create a local invalidation cache (`hotrod-persistent`) backed by the remote HotRod store.
3. Register an `infinispan-timer-management` resource that uses this cache.
4. Replace the default JDBC-based timer persistence with the Infinispan-based one.