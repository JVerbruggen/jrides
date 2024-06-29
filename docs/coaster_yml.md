# Creating a Coaster (coaster.yml)

Your coaster yml file defines all specifics on how it should be loaded into jrides. It defines the coaster description,
location, track sections, train and cart configurations, and more.

## Disclaimer
jrides has a lot of configuration files that you need to create and configure yourself. It would be wise to have a nice text editor 
with a built-in folder explorer, for example VS Code. (https://code.visualstudio.com/)

## What is rides.yml
This file exists for the sole purpose of telling jrides what rides it should load. The file exists in the plugins root folder.

```
plugins/
├─ jrides/
│  ├─ rides.yml
```

After you created this file, the contents should be as follows:

```yml
config:
  your_ride_identifier:
    identifier: your_ride_identifier
    type: coaster  
  your_second_ride_identifier:
    identifier: your_second_ride_identifier
    type: coaster
```

Whatever ride identifiers are in here, jrides will try to load them. Hint: its also a nice way of temporarily disabling rides that don't work well yet!

---
<br/>
<br/>

Next, we are going to define the coaster: What tracks should be loaded, what is its name, where should it be, what should it do?

All of this is defined in the coaster.yml

## How to create coaster.yml

### a) Semi-automated creation
It is possible to generate a template file for you to get started quicker, and get a feel for the files that have to be created.

After starting jrides, run `/jrides generate <coaster/flatride> <identifier>` to create a new coaster or flatride.

### b) Manual creation
To create a coaster.yml file:
1. Go to your file explorer and navigate to `<your spigot server root>/plugins/jrides/coasters`.
2. Create a folder the same as your ride_identifier *(only characters [A-Za-z0-9_-])*. Navigate into the folder.
3. Create a file `your_ride_identifier.coaster.yml` (*i.e. taron.coaster.yml*)

In addition, you may want to add your track now.

1. In the same folder, create a new folder named `track`. Navigate into the folder.
2. Dump your NoLimits 2 csv export file in here and rename it to `your_ride_identifier.default.csv`
 (*i.e. taron.default.csv*) <br/>*To validate if your CSV will work correctly in jrides, use the `nolimits_csv_validator.py` tool. (see [docs/tools/nolimits_csv_validator.md](tools/nolimits_csv_validator.md))*

Your folder structure should now look something like this.

```
plugins/
├─ jrides/
│  ├─ rides.yml
│  ├─ coasters/
│  │  ├─ your_ride_identifier/
│  │  │  ├─ your_ride_identifier.coaster.yml
│  │  │  ├─ track/
│  │  │  │  ├─ your_ride_identifier.default.csv
```


## A basic coaster.yml file

Now open the `your_ride_identifier.coaster.yml` file.

Copy and paste the following contents to your file to get started.

```yml
config:
  manifestVersion: v1
  identifier: your_ride_identifier
  displayName: My First Ride
  displayItem: 
    material: DIRT
  warpLocation: [0.5, 60, 0.5, 0, 0]
  vehicles:
    trains: 3
    carts: 4
  cartSpec:
    default:
      model:
        item:
          material: DIRT
      seats:
        positions:
          - [-0.5, 0.0, -0.5]
          - [-0.5, 0.0, 0.5]
          - [-1.8, 0.0, -0.5]
          - [-1.8, 0.0, 0.5]
  gates:
    station:
      entry:
        positions:
          - [-2, 61, 10]
          - [-1, 61, 10]
          - [0, 61, 10]
          - [1, 61, 10]
          - [2, 61, 10]
```

This is the first half of your coaster.yml file, which describes all of its basic properties. What is its name, where do players warp to, how many vehicles are on its track, what do the carts look like?

There are more properties that can be set, but because we're just getting started, we'll leave it to these.

The second half will be the track definition. The track definition states at which frames of the track (that is defined in the CSV file), which section should be present.

```yml
config:
  manifestVersion: v1
  identifier: your_ride_identifier
  # ...
  track:
    position: [0, 70, 0]
    parts: ['default']
    sections:
      station:
        range: [10, 400]
        nextSection: mychainlift
        type: station
        stationSection:
          driveSpeed: 1.0
          minimumWaitIntervalSeconds: 40
          maximumWaitIntervalSeconds: 60
          ejectLocation: [10, 70, 0]
      mychainlift:
        range: [400, 1300]
        nextSection: mytrack
        type: drive
        driveSection:
          driveSpeed: 1.0
      mytrack:
        range: [1300, 6000]
        nextSection: endingbrakes
        type: track
      endingbrakes:
        type: brake
        range: [6000, 10]
        nextSection: station
        brakeSection:
          driveSpeed: 1.0
          deceleration: 0.2
```

As you can see, this is a very simple coaster. Coming out of the station, it immediately sees a chain lift. 
Then it races down the track, directly into the brakes. Finally, the brakes circle back into the station.

There's a lot more types of tracks available, including transfers, launches, and most importantly: **block brakes**. Refer to [sections.md](advanced_coaster_yml/sections.md).

## Coaster.yml attributes

- **manifestVersion**: In case of future updates, this key ensures your config will work as long as possible
- **identifier**: your_ride_identifier, same as used in the folder name and file names
- **displayName**: The visible name of the coaster in-game
- **displayDescription**: The visible description of the coaster in-game
- **displayItem**: The visible icon (item in inventory) of the coaster in-game
- - **material**: Icon material
- - **damage**: Damage property
- - **unbreakable**: Unbreakable property
- **warpLocation**: Warp location in [x,y,z,yaw,pitch]
- **gravityConstant**: Constant that affects gravity-based sections in your coaster
- **dragConstant**: Constant that affects how much drag is induced on a `track` section
- **rideOverviewMapId**: Map ID (that is already present in world) that should be used for the ride overview visualization
- **canExitDuringRide**: Whether a player can exit the coaster while its operating
- **sounds**: Several configurable sounds (from ResourcePack)
- - **onrideWind**: Wind sound that is pitched according to vehicle speed
- - **restraintOpen**: Sound created when restraints open
- - **restraintClose**: Sound created when restraints close
- - **dispatch**: Sound created when vehicle is dispatched
- **vehicles**:
- - **trains**: Amount of trains present on the track
- - **carts**: Amount of carts per train
- **cartSpec**: General cart configuration
- - **default**: Default cart configuration
- - - **model**: Item that is used as the cart model
- - - - **item**:
- - - - - **material**: DIRT
- - - - - .. and other item attributes
- - - - **position**: Model offset in [x,y,z] compared to cart origin 
- - - - **rotation**: Model offset in [rx,ry,rz] compared to cart origin 
- **gates**: Entry and exit gates for players to wait behind at a coaster
- - **<station_section_name>**: Name of the station section that the gates belong to
- - - **entry**:
- - - - **positions**: List of positions that the entry gates of a station are at (should be openable objects such as fence gates)
- - - - - [0,0,0] Coordinate in [x,y,z]
- - - **exit**:
- - - - **positions**: List of positions that the exit gates of a station are at (should be openable objects such as fence gates)
- - - - - [0,0,0] Coordinate in [x,y,z]
- **track**: All track definitions
- - **position**: Position in world coordinates [x,y,z] that the CSV coordinates are translated to (location of your coaster)
- - **parts**: Array of tracks that are used (different CSV files)
- - **sections**: List of sections
- - - **<section_name>**:
- - - - **range**: The range of frames (from CSV file) the section spans from [from_frame, to_frame(, "default")]. You can also use ranges of tracks from different CSV files using track_name. Your default track always has the name 'default'.
- - - - **nextSection**: Next logical section that is attached to the end of the current section
- - - - **type**: Type definition of section behaviour
- - - - **jumpAtStart**: Whether the train should be teleported when arriving at the start of the section (advanced stuff)
- - - - **jumpAtEnd**: Whether the train should be teleported when arriving at the end of the section (advanced stuff)
- - - - **forwards**: Whether vehicles are facing forwards compared to the track direction (important for backwards track traversal) (advanced stuff)
- **controller**: Complex controller definitions
- - **type**: Controller type (check [controller.md](advanced_coaster_yml/controller.md))
- - **spec**: Controller specifications (different per type)
- **interactionEntities**: World entities that interact with the coaster
- - **stationController**: An entity that opens the station controller for a player
- - - **item**:
- - - - **material**: DIRT
- - - - .. and other item attributes
- - - **position**: Entity position in world coordinates [x,y,z]
- - - **rotation**: Entity rotation [rx,ry,rz]
