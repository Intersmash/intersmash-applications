# WildFly PostgreSQL EJB Timer Application

A WildFly application that configures EJB timers to use a PostgreSQL database as the persistent data store.

## Layers

- `cloud-default-config` (inherited from parent)
- `postgresql-datasource` (from `wildfly-datasources-galleon-pack`)

## CLI Scripts

- `scripts/configure.cli` — Configures the EJB3 timer service to use the PostgreSQL datasource as the default data store.
