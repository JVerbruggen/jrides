# Creating a Flat Ride (flatride.yml)

Your flat ride yml file defines all specifics on how it should be loaded into jrides. It defines the flatride description,
location, physical structure, animation timings, and more.

## Disclaimer
jrides has a lot of configuration files that you need to create and configure yourself. It would be wise to have a nice text editor
with a built-in folder explorer, for example VS Code. (https://code.visualstudio.com/)

---

<br/>

If you're unfamiliar with jrides, please get started creating a coaster. Refer to [coaster_yml.md](coaster_yml.md).

## Overview
[Flatride.yml attributes](#flatrideyml-attributes) - List of all attributes in the flatride.yml file<br/>
[Bone types](#bone-types) - Types of bones you can structure your flatride with<br/>
[Actions](#actions) - Actions (animations) that are executed (also for Blender Animations)<br/>

## How to create flatride.yml

1. Go to your file explorer and navigate to `<your spigot server root>/plugins/jrides/flatrides`.
2. Create a folder the same as your ride_identifier *(only characters [A-Za-z0-9_-])*. Navigate into the folder.
3. Create a file `your_ride_identifier.flatride.yml` (*i.e. teacups.flatride.yml*)

Advanced users: In case you want to run your flat ride with Blender, refer to [Flat Ride Actions](#actions) and [docs/advanced_flatride_yml/blender.md](advanced_flatride_yml/blender.md).

Your folder structure should now look something like this.

```
plugins/
├─ jrides/
│  ├─ rides.yml
│  ├─ flatrides/
│  │  ├─ your_ride_identifier/
│  │  │  ├─ your_ride_identifier.flatride.yml
```

## Flatride.yml attributes

- **manifestVersion**: In case of future updates, this key ensures your config will work as long as possible
- **identifier**: your_ride_identifier, same as used in the folder name and file names
- **displayName**: The visible name of the coaster in-game
- **displayDescription**: The visible description of the coaster in-game
- **displayItem**: The visible icon (item in inventory) of the coaster in-game
- - **material**: Icon material
- - **damage**: Damage property
- - **unbreakable**: Unbreakable property
- **warpLocation**: Warp location in [x,y,z,yaw,pitch]
- **rideOverviewMapId**: Map ID (that is already present in world) that should be used for the ride overview visualization
- **canExitDuringRide**: Whether a player can exit the coaster while its operating
- **sounds**: Several configurable sounds (from ResourcePack)
- - **onrideWind**: Wind sound that is pitched according to vehicle speed
- - **restraintOpen**: Sound created when restraints open
- - **restraintClose**: Sound created when restraints close
- - **dispatch**: Sound created when vehicle is dispatched
- **gates**: Entry and exit gates for players to wait behind at a coaster
- - **<station_section_name>**: Name of the station section that the gates belong to
- - - **entry**:
- - - - **positions**: List of positions that the entry gates of a station are at (should be openable objects such as fence gates)
- - - - - [0,0,0] Coordinate in [x,y,z]
- - - **exit**:
- - - - **positions**: List of positions that the exit gates of a station are at (should be openable objects such as fence gates)
- - - - - [0,0,0] Coordinate in [x,y,z]
- **station**:
- - .. same as station section, refer to [sections.md](advanced_coaster_yml/sections.md).
- **structure**:
- - **<bone_name>**:
- - - **type**: Bone type, either 'limb', 'seat'
- - - **position**: World position in [x,y,z]
- - - **root**: Whether the bone is the root of the Flat Ride. A Flat Ride typically only has 1 root, however, **Blender** animated Flat Rides should always have root set to `true`, since those have absolute animation coordinates.
- - - **preloadAnim**: Which animation it should pre-load to (first frame of specified animation)
- - - **models**: Item models for visualizing the Flat Ride
- - - - **<model_name>**: Can have multiple models
- - - - - **item**:
- - - - - - **material**: DIRT
- - - - - - .. and other item attributes
- - - - - **position**: Position in [x,y,z] compared to the bone position
- - - - - **rotation**: Rotation in [rx,ry,rz] compared to the bone rotation


- - - *.. Typically a bone is either a root or an arm*
- - - **arm**: The other <bone_name> that it is an arm to. The arm 'child' always refers to its 'parent' in this way.
- - - **armOffset**: Offset in [forwards,upwards,sideways] compared to the other bone
- - - **armDuplicate**: The amount of times the arm should be duplicated
- - - **phase**: Phases that the duplicated arms should have in [0, 120, 240] or [0, 180] (any length)
- **timing**:
- - **<phase_name>**: Flat Rides have phases which they go through, which are defined here
- - - **durationTicks**: Duration of the phase in ticks
- - - **actions**: All actions inflicted on different parts on the Flat Ride during the phase
- - - - **<bone_name>**: Name should be same as the bone that it is targetting
- - - - - **speed**: Speed property of the Flat Ride bone
- - - - - **accelerate**: Acceleration property of the Flat Ride bone
- - - - - **decelerate**: Deceleration property of the Flat Ride bone
- - - - - **targetPosition**: Towards that position the bone should move (integer, based on rotor or linear actuator position)
- - - - - **allowControl**: Whether player control should be toggled on or off (true, false)


- - - - - *.. Typically an action is either a manual action (as noted above) or a blender animation (noted below)*
- - - - - **animation**: Blender animation target, the file name prefix. This key should be 'my_animation' if the file is named 'my_flatride.my_animation.csv'. Refer to [blender.md](advanced_flatride_yml/blender.md).


## Bone types
### type: rotor

**Specification:**
- speed: Speed that the rotor is turning at (typically 0 since it should be standing still unless it is animated)
- axis: Axis that the rotor is turning around (x,y,z)
- minSpeed: Speed that the rotor can be at a minimum
- maxSpeed: Speed that the rotor can be at a maximum
- mode: Rotor mode (what controls it), either of type 'sine' (forwards then backwards) or 'continuous'
- size: Size of the rotor mode (in case of sine it lengthens it)
- control: Rotor player control
- - type: Type of control, either 'a_d_control', 'w_s_control' or 'space_bar_control'
- - speed: Speed in [min, max] when player takes control
- - accelerate: Acceleration for player control
- - accumulator: What happens if multiple players in the same cart control it. Currently only 'sum' (the inputs are added).

**Example:**

```yml
type: rotor
axis: x
position: [0, 65, 0]
root: true
speed: 0
minSpeed: -3
maxSpeed: 3
control:
  type: a_d_control
  speed: [-5, 5]
  accelerate: 0.5
  accumulator: sum
models:
  rotor_model:
    item:
      material: DIRT
    rotation: [0, 45, 0]
```

### type: linear_actuator

**Specification:**
- speed: Speed that the linear actuator is going at (typically 0 since it should be standing still unless it is animated)
- minSpeed: Speed that the linear actuator can be at a minimum
- maxSpeed: Speed that the linear actuator can be at a maximum
- lowerBound: Amount of blocks the linear actuator can deviate from its own position
- upperBound: Amount of blocks the linear actuator can deviate from its own position
- mode: Linear actuator mode (what controls it), either of type 'sine' (forwards then backwards) or 'continuous' (straight)

**Example:**

```yml
type: linear_actuator
axis: x
position: [0, 65, 0]
root: true
speed: 0
minSpeed: -6
maxSpeed: 6
```

### type: seat

**Specification:**
- seatRotationDegrees: Number of degrees that seat is offset with (0-360)

**Example:**

```yml
type: seat
seatRotationDegrees: 180
models: # (optional)
  seat_model:
    item:
      material: DIRT
    position: [0, 0, 0]
    rotation: [0, -90, 0]
```


### type: limb

Limb is just an intermediate node for complex movements in a Flat Ride

**Specification:**
- No additional specification

**Example:**

```yml
type: limb
models: # (optional)
  seat_model:
    item:
      material: DIRT
    position: [0, 0, 0]
    rotation: [0, -90, 0]
```

---

## Actions

Flat rides have timings, which can be configured under the `timing` attribute. In here, you can define phases that run certain actions for configured bones.

There are several attributes available for controlling flat ride bones, including running a Blender animation.

- [speed, accelerate](#speed-accelerate)
- [allowControl](#allowcontrol)
- [targetPosition](#targetposition)
- [animation](#animation) (Blender Animations)

### speed, accelerate

Changes the bone's speed property. Used to accelerate and decelerate your flat ride.

**Example:**
```yml
config:
  # ...
  timing:
    # Phase 1 accelerates the flat ride
    phase_1:
      durationTicks: 100
      actions:
        bone_1:
          speed: 3
          accelerate: 0.03
    # Phase 2 changes nothing, flat ride keeps turning at same speed
    phase_2:
      durationTicks: 500
    # Phase 3 decelerates it back before the flat ride ends
    phase_3:
      durationTicks: 100
      actions:
        bone_1:
          speed: 0
          accelerate: 0.03
```

### allowControl

Sets the player control flag to allowed or denied. Provides control over when a player is allowed to control a piece of the ride.

Only works with bones that have control options configured.

**Example:**
```yml
config:
  # ...
  timing:
    # Phase 1 accelerates the flat ride
    phase_1:
      durationTicks: 100
      actions:
        bone_1:
          speed: 3
          accelerate: 0.03
    # Phase 2 allows player to control, flat ride keeps turning at same speed
    phase_2:
      durationTicks: 500
      actions:
        bone_2:
          allowControl: true
    # Phase 3 decelerates it back before the flat ride ends, and denies control
    phase_3:
      durationTicks: 100
      actions:
        bone_1:
          speed: 0
          accelerate: 0.03
        bone_2:
          allowControl: false
```

### targetPosition

Moves the actuator to a fixed position while the phase is running.

**Example:**
```yml
config:
  # ...
  timing:
    # Phase 1 accelerates the flat ride, extends linear actuator
    phase_1:
      durationTicks: 100
      actions:
        bone_1:
          speed: 3
          accelerate: 0.03
        linear_actuator_bone: # Extends the linear actuator to position '10'
          targetPosition: 10
          speed: 3
          accelerate: 0.05
    # Phase 2 changes nothing, flat ride keeps turning at same speed
    phase_2:
      durationTicks: 500
    # Phase 3 decelerates it back before the flat ride ends, and resets linear actuator
    phase_3:
      durationTicks: 100
      actions:
        bone_1:
          speed: 0
          accelerate: 0.03
        linear_actuator_bone: # Extends the linear actuator to position '0'
          targetPosition: 0
          speed: 3
          accelerate: 0.05
```

### animation

Plays a blender animation for a bone whilst the phase is running. The animation name (in this case 'main_animation') should be the same as your Blender animation file. (see also [docs/advanced_flatride_yml/blender.md](advanced_flatride_yml/blender.md))

**Example:**
```yml
config:
  # ...
  timing:
    my_animation_phase:
      durationTicks: 1500
      actions:
        main_rotor_bone:
          animation: main_animation
        teacup_bone:
          animation: main_animation
```