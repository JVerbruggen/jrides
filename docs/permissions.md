# Permissions

## Default

Permission | Description
--- | ---
jrides.default.ride_enter | Allow entering a ride
jrides.default.warp | Allow warping (through menu)
jrides.command.default.warp | Allow warping (through command)

## Operator

Permission | Description
--- | ---
jrides.operator.cabin_operate | Allow to operate in a ride cabin
jrides.command.operator | Allow '/jrides control' command
jrides.command.operator.dispatch | Allow '/jrides control dispatch' command
jrides.command.operator.menu | Allow '/jrides control menu' command

## Elevated (admin)

Permission | Description
--- | ---
jrides.elevated.restraint_override | Allow to override restraint lock
jrides.elevated.operator_override | Allow to take over an operator in a cabin
jrides.elevated.closed_ride_enter_override | Allow to override closed ride entering
jrides.elevated.open_state_change | Allow to open or close a ride
jrides.command.elevated | Base for elevated commands (for admins)
jrides.command.elevated.admin_menu | Allow to open ride admin menu
jrides.command.elevated.ride_overview | Allow for generating a ride overview map
jrides.command.elevated.block_section | Allow for getting block section occupation message (debug)
jrides.command.elevated.visualize | Allow to visualize a coaster spline (debug)

## Debug

Permission | Description
--- | ---
jrides.elevated.status_inspection | Allow to inspect coaster frames (debug)

