# Intersmash Applications - WildFly

Applications that support latest WildFly, JBoss EAP 8.1.x and EAP XP 6.x deliverables 

## Profiles

### `wildfly-build-stream.jboss-eap.81`

Adding `-Pwildfly-build-stream.jboss-eap.81` to the build will let Maven use the JBoss EAP 8.1.x bits, rather than 
the community (WildFly) ones, which is the default behavior.

### `wildfly-target-distribution.jboss-eap`

Adding `-Pwildfly-target-distribution.jboss-eap` to the build will let Maven use the specifics for building a 
JBoss EAP 8.x application, rather than a WildFly one, which is the default behavior.

### `wildfly-build-stream.jboss-eap-xp.6`

Adding `-Pwildfly-build-stream.jboss-eap-xp.6` to the build will let Maven use the JBoss EAP XP 6 bits, rather than the community (WildFly)
ones, which is the default behavior.

### `wildfly-target-distribution.jboss-eap-xp`

Adding `-Pwildfly-target-distribution.jboss-eap-xp` to the build will let Maven use the specifics for building an JBoss EAP XP 6 application, 
 rather than a WildFly one, which is the default behavior.

**IMPORTANT**:
- The above order is important when enabling the aforementioned profiles, and for the properties to be correctly set, 
  therefore, `wildfly-build-stream.*` must be declared first.
- When using `-Pwildfly-build-stream.jboss-eap.81,wildfly-target-distribution.jboss-eap` - i.e. when building or testing 
  JBoss EAP 8.1.x applications, be sure to exclude applications that require MicroProfile support, by adding 
`'-Dgroups=!wildfly-target-distribution.requires-microprofile'` to the `mvn` command, since only WildFly and JBoss EAP 
  XP provide integrate the MicroProfile specs.
- When using `-Pwildfly-build-stream.jboss-eap.81` the JBoss EAP 8.1 **Beta** GA bits coordinates will be used by default, 
  since JBoss EAP 8.1.0 is still not available.
- When using `-Pwildfly-build-stream.jboss-eap-xp.6` the JBoss EAP XP **5.x** GA bits coordinates will be used by default, 
  since JBoss EAP XP 6 is still not available.

## Supported configuration properties

WildFly/JBoss EAP 8.x application descriptors can implement [WildflyApplicationConfiguration](/org/jboss/intersmash/applications/wildfly/WildflyApplicationConfiguration.java) in
order to expose configuration properties, e.g.: the WildFly Galleon Pack Feature Pack Location (FPL), and
propagate such properties to s2i builds.

| Name                                                                                                          | Default (WildFly)                                        | Default (JBoss EAP 8.1.x, currently using JBoss 8.1 Beta bits)                                                    | Default (JBoss EAP XP 6.x, currently using JBoss EAP XP 5 bits)     |
|---------------------------------------------------------------------------------------------------------------|----------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------|
| **WildFly/JBoss EAP 8.z Maven Plugin coordinates**                                                            |                                                          |                                                                                                                   |                                                                     |
| wildfly-maven-plugin.groupId                                                                                  | org.wildfly.plugins                                      | org.jboss.eap.plugins                                                                                             | org.jboss.eap.plugins                                               |
| wildfly-maven-plugin.artifactId                                                                               | wildfly-maven-plugin                                     | eap-maven-plugin                                                                                                  | eap-maven-plugin                                                    |
| wildfly-maven-plugin.version                                                                                  | 5.1.3.Final                                              | 1.0.1.Final-redhat-00022                                                                                          | 1.0.0.Final-redhat-00014                                            |
| **Default WildFly/JBoss EAP 8.z stream version**                                                              |                                                          |                                                                                                                   |                                                                     |
| wildfly.version                                                                                               | 37.0.0.Beta1                                             | N/A - Not used                                                                                                    | N/A - Not used                                                      |
| **Default WildFly/JBoss EAP 8 `ee` BOMs version is set here and can be overridden for pulling the right BOM** |                                                          |                                                                                                                   |                                                                     |
| bom.wildfly-ee.groupId                                                                                        | org.wildfly.bom                                          | org.jboss.bom                                                                                                     | N/A - Not used                                                      |
| bom.wildfly-ee.artifactId                                                                                     | wildfly-ee                                               | jboss-eap-ee                                                                                                      | N/A - Not used                                                      |
| bom.wildfly-ee.version                                                                                        | ${wildfly.version}                                       | 8.1.0.Beta-redhat-00018                                                                                           | N/A - Not used                                                      |
| **Default WildFly `microprofile` BOM version is set here and can be overridden for pulling the right BOM**    |                                                          |                                                                                                                   |                                                                     |
| bom.wildfly-microprofile.groupId                                                                              | org.wildfly.bom                                          | N/A - Not used (JBoss EAP does not support MicroProfile)                                                          | org.jboss.bom                                                       |
| bom.wildfly-microprofile.artifactId                                                                           | wildfly-expansion                                        | N/A - Not used (JBoss EAP does not support MicroProfile)                                                          | jboss-eap-xp-microprofile                                           |
| bom.wildfly-microprofile.version                                                                              | ${wildfly.version}                                       | N/A - Not used (JBoss EAP does not support MicroProfile)                                                          | 5.0.0.GA-redhat-00009                                               |
| **Default WildFly/JBoss EAP 8.z Feature Pack coordinates**                                                    |                                                          |                                                                                                                   |                                                                     |
| wildfly.feature-pack.location                                                                                 | org.wildfly:wildfly-galleon-pack:${wildfly.version}      | ${wildfly.ee-feature-pack.location} (JBoss EAP only supports the `wildfly.ee-feature-pack.location` feature pack) | org.jboss.eap.xp:wildfly-galleon-pack:5.0.0.GA-redhat-00005         |
| wildfly.ee-feature-pack.location                                                                              | org.wildfly:wildfly-ee-galleon-pack:${wildfly.version}   | org.jboss.eap:wildfly-ee-galleon-pack:8.1.0.Beta-redhat-00007                                                     | N/A - Not used                                                      |
| **Default WildFly/JBoss EAP 8.z Cloud Feature Pack coordinates**                                              |                                                          |                                                                                                                   |                                                                     |
| wildfly.cloud-feature-pack.location                                                                           | org.wildfly.cloud:wildfly-cloud-galleon-pack:8.0.0.Final | org.jboss.eap.cloud:eap-cloud-galleon-pack:2.0.0.Beta1-redhat-00013                                               | org.jboss.eap.cloud:eap-cloud-galleon-pack:1.0.0.Final-redhat-00008 |
| **Channel manifest coordinates**                                                                              |                                                          |                                                                                                                   |                                                                     |
| wildfly.ee-channel.groupId                                                                                    | org.wildfly.channels                                     | org.jboss.eap.channels                                                                                            | org.jboss.eap.channels                                              |
| wildfly.ee-channel.artifactId                                                                                 | wildfly-ee                                               | eap-8.1                                                                                                           | eap-8.0                                                             |
| wildfly.ee-channel.version                                                                                    | ${wildfly.version}                                       | 1.0.0.Beta-redhat-00023                                                                                           | 1.0.1.GA-redhat-00003                                               |
| wildfly.xp-channel.groupId                                                                                    | N/A - Not used                                           | N/A - Not used                                                                                                    | org.jboss.eap.channels                                              |
| wildfly.xp-channel.artifactId                                                                                 | N/A - Not used                                           | N/A - Not used                                                                                                    | eap-xp-5.0                                                          |
| wildfly.xp-channel.version                                                                                    | N/A - Not used                                           | N/A - Not used                                                                                                    | 1.0.0.GA-redhat-00006                                               |
