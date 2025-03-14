# Intersmash Deployments - WildFly

Application server deployments that support latest WildFly, EAP 8.0.z, EAP 8.1.x and EAP XP 6.x deliverables 

## Profiles

- `eap-xp6`
Adding `-Peap-xp6` to the build will let Maven use the specifics for building an EAP XP 6 deployment, rather than a 
WildFly one - which is the default behavior.

**IMPORTANT**:
_So far, no EAP XP 6 bits have been released, so the configuration is still using EAP XP 5 bits._
