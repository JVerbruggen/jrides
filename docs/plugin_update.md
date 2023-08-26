# Plugin update

To manuall build jrides on a new spigot version, follow this guide.

### TODO! THIS PAGE IS NOT DONE


## Run build tools
https://www.spigotmc.org/wiki/buildtools/

## Add new build profile to pom.xml
```xml
<profile>
    <id>jrides-1.20.1</id>
    <activation>
        <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
        <minecraft.version.short>1.20.1</minecraft.version.short>
        <minecraft.version.short.spigot>1.20</minecraft.version.short.spigot>
        <minecraft.version.apiversion>1.20</minecraft.version.apiversion>
    </properties>
</profile>
```

## Check dependency versioning
ProtocolLib usually has a new version for the new minecraft version.