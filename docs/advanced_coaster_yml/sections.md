# Sections
Sections define exactly what a series of points in the track csv file should behave like.

## Overview
[type: track](#type-track)<br/>
[type: station](#type-station)<br/>
[type: drive](#type-drive)<br/>
[type: brake](#type-brake)<br/>
[type: blocksection](#type-blocksection)<br/>
[type: launch](#type-launch)<br/>
[type: driveAndRelease](#type-driveandrelease)<br/>
[type: proximityDrive](#type-proximitydrive)<br/>
[type: transfer](#type-transfer)<br/>

### type: track

A physics-based section, typically used as normal coaster track.

**Specification**
- No attributes

**Example**

```yml
my_section:
  range: [0, 500]
  nextSection: my_next_section
  type: track
```

### type: station

A section where players can enter and exit vehicles.

**Specification**
- ejectLocation: Location in [x,y,z,yaw,pitch] where player is ejected when vehicle enters station
- canSpawn: Whether a vehicle can spawn here upon plugin load
- driveSpeed: Vehicle speed when passing through the section
- engage: Percentage of how far along the section the vehicle should engage with the behaviour (0.0 to 1.0)
- minimumWaitIntervalSeconds: How long the vehicle should wait (minimum) before it can be dispatched
- maximumWaitIntervalSeconds: How long the vehicle can wait (maximum) before it should be dispatched
- effects: A list of effect triggers that should play upon different events occurring on the section
- - entryBlocking: Vehicle entering the station, waits with releasing restraints
- - exitBlocking: Vehicle exiting the station, waits with dispatching
- - exit: Vehicle exiting station, dispatches immediately
- passThroughCount: Amount of times the same vehicle should be able to pass through the section (for coasters with multiple rounds)
- forwardsDispatch: Whether the station should push the vehicle forwards upon dispatch. If false it goes backwards

**Example**

```yml
my_section:
  range: [0, 500]
  nextSection: my_next_section
  type: station
  stationSection:
    ejectLocation: [0.0, 65.0, 0.0, 0.0, 0.0]
    canSpawn: true                                # (optional [default=true])
    driveSpeed: 1.0                               # (optional [default=1.0])
    engage: 0.5                                   # (optional [default=0.5])
    minimumWaitIntervalSeconds: 10                # (optional [default=0])
    maximumWaitIntervalSeconds: 60                # (optional [default=60])
    effects:                                      # (optional)
      entryBlocking: []
      exitBlocking: []
      exit: ['some-exit-effect']
    passThroughCount: 1                           # (optional [default=0], advanced)
    forwardsDispatch: true                        # (optional [default=true], advanced)
```

### type: drive

A section driving the vehicle at a fixed speed.

**Specification**
- driveSpeed: Vehicle speed when passing through the section
- acceleration: Vehicle acceleration when going too slow
- deceleration: Vehicle deceleration when going too fast
- ignoreDirection: Whether the direction of driving is ignored. If true, the drive section drives in the direction the vehicle is already traveling at

**Example**

```yml
my_section:
  range: [0, 500]
  nextSection: my_next_section
  type: drive
  driveSection:
    driveSpeed: 1.0                               # (optional [default=1.0])
    acceleration: 0.05                            # (optional [default=1.0])
    deceleration: 0.05                            # (optional [default=*acceleration*])
    ignoreDirection: false                        # (optional [default=false], advanced)
```

### type: brake

A section braking the vehicle towards a fixed speed (`type: drive` can also be used for this).

**Specification**
- driveSpeed: Vehicle speed when passing through the section
- deceleration: Vehicle deceleration when going too fast

**Example**

```yml
my_section:
  range: [0, 500]
  nextSection: my_next_section
  type: brake
  brakeSection:
    driveSpeed: 1.0                               # (optional [default=1.0])
    deceleration: 0.05                            # (optional [default=*acceleration*])
```

### type: blocksection

A section that a vehicle can wait on. The section will release the vehicle when the next section is available for use.

**Specification**
- canSpawn: Whether a vehicle can spawn here upon plugin load
- engage: Percentage of how far along the section the vehicle should engage with the behaviour (0.0 to 1.0)
- driveSpeed: Vehicle speed when passing through the section
- acceleration: Vehicle acceleration when going too slow
- deceleration: Vehicle deceleration when going too fast
- minWaitTicks: Amount of ticks the vehicle waits after the next section has come available

**Example**

```yml
my_section:
  range: [0, 500]
  nextSection: my_next_section
  type: blocksection
  blockSection:
    canSpawn: false                               # (optional [default=false])
    engage: 0.2                                   # (optional [default=0.01])
    driveSpeed: 1.0                               # (optional [default=1.0])
    acceleration: 0.1                             # (optional [default=0.1])
    deceleration: 0.1                             # (optional [default=*acceleration*])
    minWaitTicks: 20                              # (optional [default=0])
```

### type: launch

A section that launches a vehicle forwards or backwards.

**Specification**
- canSpawn: Whether a vehicle can spawn here upon plugin load
- engage: Percentage of how far along the section the vehicle should engage with the behaviour (0.0 to 1.0)
- driveSpeed: Vehicle speed when passing through the section
- acceleration: Vehicle acceleration when going too slow
- deceleration: Vehicle deceleration when going too fast
- waitIntervalTicks: Amount of ticks the vehicle waits after the next section has come available before launching
- launchSpeed: Vehicle speed when launching out of the section
- launchSpeedBackward: Vehicle speed when launching out of the section (if going backward)
- launchAcceleration: Vehicle acceleration during launch
- forwardsLaunch: Whether the vehicle should be launched going forwards towards the next section
- effects: A list of effect triggers that should play upon different events occurring on the section
- - launch: Vehicle started launching out of the section

**Example**

```yml
my_section:
  range: [0, 500]
  nextSection: my_next_section
  type: launch
  launchSection:
    canSpawn: false                               # (optional [default=false])
    engage: 0.2                                   # (optional [default=0.01])
    driveSpeed: 1.0                               # (optional [default=1.0])
    acceleration: 0.1                             # (optional [default=0.1])
    deceleration: 0.1                             # (optional [default=*acceleration*])
    waitIntervalTicks: 20                         # (optional [default=-1])
    launchSpeed: 10.0                             # (optional [default=5.0])
    launchSpeedBackward: 10.0                     # (optional [default=*launchSpeed*])
    launchAcceleration: 0.2                       # (optional [default=0.1])
    forwardsLaunch: true                          # (optional [default=true])
    effects:                                      # (optional)
      launch: ['some-launch-effect']
```

### type: driveAndRelease

A section that a vehicle drives on, waits, and is released with gravity.

**Specification**
- engage: Percentage of how far along the section the vehicle should engage with the behaviour (0.0 to 1.0)
- driveSpeed: Vehicle speed when passing through the section
- acceleration: Vehicle acceleration when going too slow
- deceleration: Vehicle deceleration when going too fast
- waitIntervalTicks: Amount of ticks the vehicle waits before releasing it to gravity

**Example**

```yml
my_section:
  range: [0, 500]
  nextSection: my_next_section
  type: driveAndRelease
  driveAndReleaseSection:
    engage: 0.2                                   # (optional [default=0.5])
    driveSpeed: 1.0                               # (optional [default=1.0])
    acceleration: 0.1                             # (optional [default=1.0])
    deceleration: 0.1                             # (optional [default=*acceleration*])
    waitIntervalTicks: 20                         # (optional [default=1])
```


### type: proximityDrive

A section that multiple vehicles can occupy, driving closely after each other

**Specification**
- canSpawn: Whether a vehicle can spawn here upon plugin load
- driveSpeed: Vehicle speed when passing through the section
- acceleration: Vehicle acceleration when going too slow
- deceleration: Vehicle deceleration when going too fast
- minTrainDistance: Minimum amount of frames (in csv file) that any train should keep from any other train

**Example**

```yml
my_section:
  range: [0, 500]
  nextSection: my_next_section
  type: proximityDrive
  proximityDriveSection:
    canSpawn: false                               # (optional [default=false])
    driveSpeed: 1.0                               # (optional [default=1.0])
    acceleration: 0.1                             # (optional [default=1.0])
    deceleration: 0.1                             # (optional [default=*acceleration*])
    minTrainDistance: 70                          # (optional [default=50])
```

### type: transfer

A section that can move a vehicle away from its track, onto another

**NOTE** Transfers currently have some limitations.
- Transfers are difficult to set up and may require extra fail-safe measures such as a longer section and extensive tweaking of attributes. (Check the examples down below)
- There can only be 2 transfer positions
- Only 1 train can occupy a transfer at a time

**Specification**
- engage: Percentage of how far along the section the vehicle should engage with the behaviour (0.0 to 1.0)
- enterDriveSpeed: Vehicle speed when passing through the section
- exitDriveSpeed: Vehicle speed when passing through the section
- acceleration: Vehicle acceleration when going too slow
- deceleration: Vehicle deceleration when going too fast
- origin: Origin coordinate in world [x,y,z] the transfer is located at
- modelOffsetPosition: Offset in [x,y,z] for the transfer model
- modelOffsetRotation: Offset in [rx,ry,rz] for the transfer model
- positions: Transfer positions
- - sectionAtStart: The section that connects to the start of the transfer (start is the lower frame)
- - sectionAtEnd: The section that connects to the end of the transfer (end is the upper frame)
- - sectionAtStartForwards: The section that connects to the start of the transfer is one that vehicles are facing forwards on (regardless of travel direction)
- - sectionAtEndForwards: The section that connects to the end of the transfer is one that vehicles are facing forwards on (regardless of travel direction)
- - sectionAtStartConnectsToStart: The section that connects to the start of the transfer, connects at its own start
- - sectionAtEndConnectsToStart: The section that connects to the end of the transfer, connects at its own start
- - position: Position in [x,y,z] of the transfer position compared to its origin
- - rotation: Rotation in [rx,ry,rz] of the transfer position compared to its origin
- - moveTicks: Amount of ticks it takes to move the transfer towards this position

**Examples**

---

**TRANSFER CASE 1: Forwards arrival, Backwards departure, Backwards track**

![transfer-type-backwards.png](..%2Fassets%2Ftransfer-type-backwards.png)

```yml
my_transfer_section:
  range: [0, 500]
  nextSection: my_section_B
  type: transfer
  jumpAtStart: true                                   # [Important! Always set this to true when using transfers] Should it teleport when crossing the transfer section at the start point? [default = false]
  jumpAtEnd: true                                     # [Important! Always set this to true when using transfers] Should it teleport when crossing the transfer section at the end point? [default = false]
  transferSection:
    origin: [0.0, 65, 0.0]
    modelOffsetPosition: [0,1.0,0]
    modelOffsetRotation: [0,90,0]
    engage: 0.5
    positions:
      towards_section_A:                              # Defining transfer position
        sectionAtStart: my_section_A                    # connection to start of transfer at SECTION A
        sectionAtStartForwards: true                    # .. train is facing forwards (compared to the track direction) when it is on section A: Yes! so 'true'
        sectionAtStartConnectsToStart: false            # .. does the transfer attach to the start of section A: No! it is at the end of section A, so 'false'
        rotation: [0,0,0]
        position: [0,0,0]
      towards_section_B:                              # Defining next transfer position
        sectionAtStart: my_section_B                    # connecting to start of transfer at SECTION B
        sectionAtStartForwards: false                   # .. train is facing forwards (compared to the track direction) when it is on section B: No! It will be going backwards, so 'false'
        sectionAtStartConnectsToStart: true             # .. does the transfer attach to the start of section B: Yes!
        moveTicks: 40                                   # How long it takes to move the transfer in ticks
        position: [0,0,0]
        rotation: [0,-40,0]                           # Turn around the origin
```
---

**TRANSFER CASE 2: Forwards arrival, Forwards departure, Forwards track**

![transfer-type-forwards.png](..%2Fassets%2Ftransfer-type-forwards.png)

```yml
my_transfer_section:
  range: [0, 500]
  nextSection: my_section_B
  type: transfer
  jumpAtStart: true                                   # [Important! Always set this to true when using transfers] Should it teleport when crossing the transfer section at the start point? [default = false]
  jumpAtEnd: true                                     # [Important! Always set this to true when using transfers] Should it teleport when crossing the transfer section at the end point? [default = false]
  transferSection:
    origin: [0.0, 65, 0.0]
    modelOffsetPosition: [0,1.0,0]
    modelOffsetRotation: [0,90,0]
    engage: 0.5
    positions:
      towards_section_A:                              # Defining transfer position
        sectionAtStart: my_section_A                    # connection to start of transfer at SECTION A
        sectionAtStartForwards: true                    # .. train is facing forwards (compared to the track direction) when it is on section A: Yes! so 'true'
        sectionAtStartConnectsToStart: false            # .. does the transfer attach to the start of section A: No! it is at the end of section A, so 'false'
        rotation: [0,0,0]
        position: [0,0,0]
      towards_section_B:                              # Defining next transfer position
        sectionAtEnd: my_section_B                      # connecting to end of transfer at SECTION B
        sectionAtEndForwards: true                      # .. train is facing forwards (compared to the track direction) when it is on section B: Yes! so 'true'
        sectionAtEndConnectsToStart: true               # .. does the transfer attach to the start of section B: Yes!
        moveTicks: 100                                  # How long it takes to move the transfer in ticks
        position: [8.0,0,0]                           # Move to the side
        rotation: [0,0,0]
```

**TRANSFER CASE 3: Forwards arrival, Backwards departure, Forwards track**

![transfer-type-back-and-forwards.png](..%2Fassets%2Ftransfer-type-back-and-forwards.png)


```yml
my_transfer_section:
  range: [0, 500]
  nextSection: my_section_B_0
  type: transfer
  jumpAtStart: true                                   # [Important! Always set this to true when using transfers] Should it teleport when crossing the transfer section at the start point? [default = false]
  jumpAtEnd: true                                     # [Important! Always set this to true when using transfers] Should it teleport when crossing the transfer section at the end point? [default = false]
  transferSection:
    origin: [0.0, 65, 0.0]
    modelOffsetPosition: [0,1.0,0]
    modelOffsetRotation: [0,90,0]
    engage: 0.5
    enterDriveSpeed: 1.0
    exitDriveSpeed: -5.0                              # This specific transfer launches the train backwards when departing
    acceleration: 0.1
    deceleration: 0.5
    positions:
      towards_section_A:                              # Defining transfer position
        sectionAtStart: my_section_A                    # connection to start of transfer at SECTION A
        sectionAtStartForwards: true                    # .. train is facing forwards (compared to the track direction) when it is on section A: Yes! so 'true'
        sectionAtStartConnectsToStart: false            # .. does the transfer attach to the start of section A: No! it is at the end of section A, so 'false'
        rotation: [0,0,0]
        position: [0,0,0]
      towards_section_B_0:                            # Defining next transfer position
        sectionAtStart: my_section_B_0                  # connecting to start of transfer at SECTION B-0
        sectionAtStartForwards: true                    # .. train is facing forwards (compared to the track direction) when it is on section B-0: Yes! Although it will be traveling backwards, its direction is still forwards. So 'true'
        sectionAtStartConnectsToStart: false            # .. does the transfer attach to the start of section B: No! It connects to the end since B-0's start is a dead end. B-0's end connects to B-1, which continues the track.
        moveTicks: 100                                  # How long it takes to move the transfer in ticks
        position: [8.0,0,0]                           # Move to the side
        rotation: [0,0,0]
my_section_B_0:
  range: [600,700]  # Usually there is a gap between the upper range of the transfer and the lower range of the next section. This is normal, and `jumpAtEnd` makes sure the train teleports.
  nextSection: my_section_B_1
  type: track
```