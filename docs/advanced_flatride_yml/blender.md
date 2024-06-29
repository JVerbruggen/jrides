# Blender

Flat Rides can be programmed with blender animations.

## Prerequisites
- Installed Blender version >2.80
- Downloaded a copy of the jrides Blender export tool `tools/jrides_blender_export_anim_plugin.py`

## How to

1. Create a Blender animations using object animations. (do not use rigging)
2. Create a Flat Ride file with the same layout as your Blender animation objects.
3. Each object should have an equivalent jrides-configured Flat Ride bone.
4. Name all animated objects equal to the bone names. (Important! Jrides matches your Blender animation objects to its bones)
5. Export the Blender animation using the custom jrides export tool. (check the `tools` folder on GitHub: https://github.com/JVerbruggen/jrides)
   1. `tools/jrides_blender_export_anim_plugin.py`
6. Name the file in the format of `<flatride_identifier>.<animation_name>.csv`, i.e. `teacups.main_animation.csv`.
7. Paste the file in the jrides folder `jrides/flatrides/<flatride_identifier/animations`.
   1. *Your folder structure should now look something like this.*
   
      ```
      plugins/
      ├─ jrides/
      │  ├─ rides.yml
      │  ├─ flatrides/
      │  │  ├─ teacups/
      │  │  │  ├─ teacups.flatride.yml
      │  │  │  ├─ animations/
      │  │  │  │  ├─ teacups.main_animation.csv
      ```
8. Configure your bones to preload the animation.
    ```yml
    config:
      # ...
      structure:
        main_rotor_bone:
          type: limb
          position: [0, 65, 0]
          root: true
          preloadAnim: main_animation
        teacup_bone:
          type: limb
          position: [0, 65, 0]
          root: true
          preloadAnim: main_animation
    ```
9. Add a single timing phase to your flatride.yml, with an animation action for each bone.
    ```yml
    config:
      # ...
      timing:
        animation_phase:
          durationTicks: 1500
          actions:
            main_rotor_bone:
              animation: main_animation
            teacup_bone:
              animation: main_animation
    ```

And you're done.