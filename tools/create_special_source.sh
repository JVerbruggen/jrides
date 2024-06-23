#!/bin/bash

##############################################################################################################
# GPLv3 License                                                                                              #
#                                                                                                            #
# Copyright (c) 2024-2024 JVerbruggen                                                                        #
# https://github.com/JVerbruggen/jrides                                                                      #
#                                                                                                            #
# This software is protected under the GPLv3 license,                                                        #
# that can be found in the project's LICENSE file.                                                           #
#                                                                                                            #
# In short, permission is hereby granted that anyone can copy, modify and distribute this software.          #
# You have to include the license and copyright notice with each and every distribution. You can use         #
# this software privately or commercially. Modifications to the code have to be indicated, and               #
# distributions of this code must be distributed with the same license, GPLv3. The software is provided      #
# without warranty. The software author or license can not be held liable for any damages                    #
# inflicted by the software.                                                                                 #
##############################################################################################################

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