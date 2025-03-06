# Intersmash Applications - WildFly

Applications that support latest WildFly, EAP 8.0.z, EAP 8.1.x and EAP XP 6.x deliverables 

## Profiles

- `build-distribution.eapxp`
Adding `-Pbuild-distribution.eapxp` to the build will let Maven use the specifics for building an EAP XP 6 application, 
 rather than a WildFly one, which is the default behavior.

- `build-stream.eapxp6`
Adding `-Pbuild-stream.eapxp6` to the build will let Maven use the EAP XP 6 bits, rather than the community (WildFly) 
 ones, which is the default behavior.

**IMPORTANT**:
_So far, no EAP XP 6 bits have been released, so the configuration is still using EAP XP 5 bits._
