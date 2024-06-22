# Configuration

## Folder structure

Default data folder
* `plugins/jrides` 

## rides.yml
* This file contains all active coasters that should be loaded on startup.
* This is the first file you create to get started.
* Location: `plugins/jrides/rides.yml` 

Example configuration:
```yml
config:
  silverstar:
    identifier: silverstar
    type: coaster
  wodan:
    identifier: wodan
    type: coaster
  teacups:
    identifier: teacups
    type: flatride
```

## language.yml
* Change all feedback that is visible to players.
* Location: `plugins/jrides/language.yml` 

Example configuration:
```yml
language:
  NOTIFICATION_RIDE_COUNTER_UPDATE: "&4-------\n \n&6You have been %RIDE_COUNT% times in %RIDE_DISPLAY_NAME%!\n \n&4-------"
  NOTIFICATION_CANNOT_ENTER_RIDE: "&7This ride is temporarily unavailable."
```

For all available language overrides, see [docs/language.md](./language.md)


## coaster.yml
* Coaster configuration
* Location: `plugins/jrides/coasters/<coaster_identifier>/<coaster_identifier>.coaster.yml`
* * Example: `plugins/jrides/coasters/silverstar/silverstar.coaster.yml`

The exact structure of this file can be found in [docs/coaster_yml.md](./coaster_yml.md)

## trigger.yml
* Add triggers to coaster tracks, to run effects, commands or animate entities.

For more information, see [docs/triggers.md](./triggers.md)

## flatride.yml
* Flatride configuration
* Location: `plugins/jrides/flatrides/<flatride_identifier>/<flatride_identifier>.flatride.yml`
* * Example: `plugins/jrides/flatrides/teacups/teacups.flatride.yml`

The exact structure of this file can be found in [docs/flatride_yml.md](./flatride_yml.md)
