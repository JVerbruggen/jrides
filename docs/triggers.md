# Triggers

Triggers are tools that are used for creating all sorts of effects alongside the rides themselves.
'jrides' has various types of triggers, each of them listed below.

## Adding triggers to a track

### Summary

Triggers can only exist when defined. Definitions can be found in multiple places.

1. In the `<coaster_identifier>.default.trigger.yml` file, used for placing triggers alongside the default track.
2. In the `<coaster_identifier>.coaster.yml` file ([link](./coaster_yml.md)), used for section types that support triggers, such as the `type: station` section type.
3. In another track's trigger file, other than the default track. Just like the `.default.trigger.yml` file, each registered track can have its own trigger file. The trigger file name should be called `<coaster_identifier>.<track_identifier>.trigger.yml`.

### File structure

As said, the file name of the general trigger.yml file is as follows: `<coaster_identifier>.<track_identifier>.trigger.yml`. The contents of this file is defined here.

```yml
triggers:
    <trigger-identifier>:
        frame: <frame-number>
    <another-trigger-identifier>:
        frame: <frame-number>
    ...
```

for example:

```yml
triggers:
    start-chain-hook-sound:
        frame: 500
    stop-chain-hook-sound:
        frame: 2300
    people-scream-sound:
        frame: 3100
```

Add as many triggers as you'd like.

## Defining trigger behaviour

### Summary

Every trigger identifier has it's own configuration file as of what it does. These files can be found in the `coasters/<coaster_identifier>/triggers` folder. 

Each trigger that is defined in the general trigger.yml, needs a corresponding file in the triggers folder. The name of this file is equal to `<trigger-identifier>.yml`

For example, when using a trigger named `start-chain-hook-sound` that triggers at frame 500, a file with name `start-chain-hook-sound.yml` needs to be created in the triggers folder.

### File structure

Since jrides has multiple types of triggers, the structure of this file changes depending on which trigger is used. For every trigger, the following base structure is the same:

```yml
trigger:
    type: <trigger_type>
```

The `trigger_type` can be either one of the following values:

* [type: multi-entity-movement](#type-multi-entity-movement)
* [type: command](#type-command)
* [type: command-for-player](#type-command-for-player)
* [type: command-as-player](#type-command-as-player)
* [type: external](#type-external-for-developers)

<br/><br/>

## Trigger specifications

### type: multi-entity-movement

The `multi-entity-movement` trigger can move several entities at once. Each entity has its own unique name under the `entities` attribute.

An entity can have either an item that is displayed, or it can be a minecraft entity, such as a chicken or a horse. To see all item configuration options, check out [item.md](item.md).

Motions can be defined in two distinct ways.
1. From position -> To position (using the `locationFrom`, `locationTo`, `rotationFrom` and `rotationTo` keys)
2. Initial position -> Continuous motion (until animation time ticks is reached)

<br/>

**From -> to animation**

The from -> to animation type uses keys `locationFrom`, `locationTo`, `rotationFrom` and `rotationTo`, to create a linear path between two points.

```yml
trigger:
    type: multi-entity-movement
    entities:
        <entity_name>:
            item:
                material: APPLE
            entity: # item: and entity: cannot be used simultaneously
                type: HORSE
            locationFrom: [50.0, 10.0, 50.0] # [x, y, z]
            locationTo: [50.0, 14.0, 50.0]
            rotationFrom: [0.0, 0.0, 0.0] # [pitch, yaw, roll]
            rotationTo: [0.0, 90.0, 0.0]
            animationTimeTicks: 60 # Duration of animation in ticks
```

**Initial position -> Continuous motion**

the initial -> Continuous motion type uses keys `initialLocation`, `initialRotation`, `locationDelta` and `rotationDeta` to create a continuous path after an initial location. Each tick, the delta location and rotation is added to the initials, creating a continuous motion. This can be useful for example for turning wheels.

```yml
trigger:
    type: multi-entity-movement
    entities:
        <entity_name>:
            item:
                material: APPLE
            initialLocation: [50.0, 10.0, 50.0] # [x, y, z]
            initialRotation: [0, 0, 0] # [pitch, yaw, roll]
            locationDelta: [0.0, 0.1, 0.0] 
            rotationDelta: [0.0, 1.0, 0.0]
            animationTimeTicks: 60 # Duration of animation in ticks
```


---

### type: command

The `command` trigger allows for flexible linkage of jrides triggers to your plugins or minecraft commands. The command defined in this trigger is ran by the console, so be careful about what is filled in here.

**! Be aware !** Using commands is usually not a 'best-practice' in most cases, and should only be used when the [external](#type-external-for-developers) trigger type is not a viable solution.

```yml
trigger:
    type: command
    command: <minecraft_command_to_be_executed_by_server>
```

For example, if you want the server to run the command `/time set day`, the config would look as follows:

```yml
trigger:
    type: command
    command: time set day
```

If you want a command to be executed that is bound to a player, you should use the `command-for-player` type.

---

### type: command-for-player

Just like the [command](#type-command) type, the `command-for-player` type is executed by the server. The main difference is that the command is executed for all passengers of the train that is passing the trigger. This means that for i.e. 6 passengers, the command is executed 6 times.

```yml
trigger:
    type: command-for-player
    command: <minecraft_command_to_be_executed_by_server>
```

Use the `%PLAYER%` tag in your command to replace that bit of the command with the player's name.

```yml
trigger:
    type: command-for-player
    command: tell %PLAYER% Please stay seated!
```

If you'd like a command to be executed as if the player ran it themselves, you should use the `command-as-player` type.

---

### type: command-as-player

In contrast to the [command](#type-command) type, the `command-as-player` type is executed by the player. 

```yml
trigger:
    type: command-as-player
    command: <minecraft_command_to_be_executed_by_player>
```

---

### type: external (for developers)

The external trigger allows you to listen for a bukkit event, that is dispatched by jrides. The event that is dispatched can be found in [ExternalTriggerEvent.java](../src/main/java/com/jverbruggen/jrides/event/ride/ExternalTriggerEvent.java).

The trigger config file has a `data` attribute, where you can store any key and value as strings. Be aware not to use anything other than strings as values to this data attribute.

```yml
trigger:
    type: external
    data:
        <key>: <value>
        <another_key>: <another_value>
```