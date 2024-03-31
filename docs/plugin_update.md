# Plugin update

To manually build jrides on a new spigot version, follow this guide.

## 1. Run build tools

Go to:
https://www.spigotmc.org/wiki/buildtools/

and download the latest BuildTools.

Run BuildTools with the '--remapped' option, or in the gui with 'Remapped Jars'.

## 2. Add new build profile to pom.xml
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

Make sure to select the new profile in your Maven configuration.

## 3. Check dependency versioning
ProtocolLib usually has a new version for the new minecraft version. Update all dependencies in maven and on your test server.

## 4. Create a new PacketSender instance
In the jrides repository, go to `com.jverbruggen.jrides.packets.impl` and create a new implementation for your new Minecraft version.

Also make sure to register this implementation in the PacketSenderFactory.

## 5. Build with Maven
Finally, run a jrides build with Maven.