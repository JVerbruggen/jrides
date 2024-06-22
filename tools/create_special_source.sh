#!/bin/bash

MINECRAFT_VERSION=$1
PLUGIN=$2

if [[ "$PLUGIN" == "" || "$MINECRAFT_VERSION" == "" ]]; then
    echo "Usage create_special_source.sh <minecraft_version> <plugin>"
    echo -n "Press enter to continue.."
    read exitread
    exit 1
fi

# https://www.spigotmc.org/threads/spigot-bungeecord-1-17-1-17-1.510208/#post-4184317
java -cp $HOME/.m2/repository/net/md-5/SpecialSource/1.11.0/SpecialSource-1.11.0-shaded.jar:$HOME/.m2/repository/org/spigotmc/spigot/$MINECRAFT_VERSION-R0.1-SNAPSHOT/spigot-$MINECRAFT_VERSION-R0.1-SNAPSHOT-remapped-mojang.jar net.md_5.specialsource.SpecialSource --live -i $PLUGIN.jar -o $PLUGIN-obf.jar -m $HOME/.m2/repository/org/spigotmc/minecraft-server/$MINECRAFT_VERSION-R0.1-SNAPSHOT/minecraft-server-$MINECRAFT_VERSION-R0.1-SNAPSHOT-maps-mojang.txt --reverse
java -cp $HOME/.m2/repository/net/md-5/SpecialSource/1.11.0/SpecialSource-1.11.0-shaded.jar:$HOME/.m2/repository/org/spigotmc/spigot/$MINECRAFT_VERSION-R0.1-SNAPSHOT/spigot-$MINECRAFT_VERSION-R0.1-SNAPSHOT-remapped-obf.jar net.md_5.specialsource.SpecialSource --live -i $PLUGIN-obf.jar -o $PLUGIN.jar -m $HOME/.m2/repository/org/spigotmc/minecraft-server/$MINECRAFT_VERSION-R0.1-SNAPSHOT/minecraft-server-$MINECRAFT_VERSION-R0.1-SNAPSHOT-maps-spigot.csrg

read exitread
exit 0