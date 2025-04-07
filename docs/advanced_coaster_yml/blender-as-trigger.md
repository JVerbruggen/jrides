# Blender

Coasters can have blender animations along the track.

## Prerequisites
- Installed Blender version >2.80
- Downloaded a copy of the jrides Blender export tool `tools/jrides_blender_export_anim_plugin.py`

## How to

1. Create a Blender animations using object animations. (do not use rigging)
2. Name the animated object the same as your animation name, i.e. `main-animation`.
3. Select the object and export the Blender animation using the custom jrides export tool. (check the `tools` folder on GitHub: https://github.com/JVerbruggen/jrides)
   1. `tools/jrides_blender_export_anim_plugin.py`
4. Name the file in the format of `<coaster_identifier>.<animation_name>.csv`, i.e. `mycoaster.main_animation.csv`.
5. Paste the file in the jrides folder `jrides/coasters/<coaster_identifier>/animations`.
6. Create a trigger file in `jrides/coasters/<coaster_identifier>/triggers/<my-trigger.yml>`
   ```yml
   trigger:
       type: blender-animation
       location: [50.0, 10.0, 50.0]                        # [x, y, z]
       animationName: my-cool-animation                    # Animation identifier
       reuseEntity: my-coaster-my-cool-animation-entity    # A unique identifier for an entity so that it can be used for multiple animations
       preloadAnim: true                                   # Whether to load the first frame of the animation when loading the plugin
       item:                                               # Item that is animated
           material: YELLOW_WOOL
   ```

7. Use the trigger in either effect triggers for certain sections (i.e. the station section), or in the track trigger file. See [docs/triggers.md](../triggers.md).

   1. *Your folder structure should now look something like this.*
   
      ```
      plugins/
      ├─ jrides/
      │  ├─ rides.yml
      │  ├─ coasters/
      │  │  ├─ mycoaster/
      │  │  │  ├─ mycoaster.coaster.yml
      │  │  │  ├─ mycoaster.default.trigger.yml
      │  │  │  ├─ animations/
      │  │  │  │  ├─ mycoaster.main_animation.csv
      │  │  │  ├─ triggers/
      │  │  │  │  ├─ my-trigger.yml
      ```

And you're done.