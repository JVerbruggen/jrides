
# Language

Also see: [docs/config.md](./config.md)

Example language file (plugins/jrides/language.yml):
```yaml
language:
  NOTIFICATION_RIDE_COUNTER_UPDATE: "&4-------\n \n&6You have been %RIDE_COUNT% times in %RIDE_DISPLAY_NAME%!\n \n&4-------"
  NOTIFICATION_CANNOT_ENTER_RIDE: "&7This ride is temporarily unavailable."
```

Key | Default value
--- | ---
CHAT_FEEDBACK_PREFIX | "[jrides] "
CHAT_FEEDBACK_INFO_COLOR | "&7"
CHAT_FEEDBACK_WARNING_COLOR | "&e"
CHAT_FEEDBACK_SEVERE_COLOR | "&c"
COMMAND_RIDE_DISPATCHED_MESSAGE | "Ride %RIDE_DISPLAY_NAME% was dispatched!"
COMMAND_VISUALIZE_ADDED_VIEWER | "You are now viewing %RIDE_IDENTIFIER% in visualize mode"
COMMAND_VISUALIZE_REMOVED_VIEWER | "You are no longer viewing %RIDE_IDENTIFIER% in visualize mode"
NOTIFICATION_PLUGIN_STILL_LOADING | "Please try again later when jrides is loaded"
NOTIFICATION_RIDE_CONTROL_ACTIVE | "You are now controlling %RIDE_DISPLAY_NAME%"
NOTIFICATION_RIDE_CONTROL_INACTIVE | "You are no longer controlling %RIDE_DISPLAY_NAME%"
NOTIFICATION_RIDE_DISPATCH_PROBLEMS | "Cannot dispatch due to the following problems:"
NOTIFICATION_RIDE_NO_TRAIN_PRESENT | "No train present in station"
NOTIFICATION_RIDE_NEXT_BLOCK_OCCUPIED | "Next block section is occupied"
NOTIFICATION_RIDE_WAITING_TIME | "Waiting time has not passed yet"
NOTIFICATION_RIDE_RESTRAINTS_NOT_CLOSED | "Not all restraints are closed"
NOTIFICATION_RIDE_GATES_NOT_CLOSED | "Not all gates are closed"
NOTIFICATION_RIDE_GATE_NOT_CLOSED | "Gate %NAME% is not closed"
NOTIFICATION_RIDE_COUNTER_UPDATE | "\nYou've ridden %RIDE_DISPLAY_NAME% %RIDE_COUNT% times now\n"
NOTIFICATION_RIDE_STATE_OPEN | "%RIDE_DISPLAY_NAME% is now open"
NOTIFICATION_RIDE_STATE_CLOSED | "%RIDE_DISPLAY_NAME% is now closed"
NOTIFICATION_SHIFT_EXIT_CONFIRMATION | "Press shift again within 2 seconds to confirm exiting the ride"
NOTIFICATION_SHIFT_EXIT_CONFIRMED | "You just exited the ride while the restraints were closed"
NOTIFICATION_DISPATCH_WAIT_GENERIC | "Please wait until the ride is dispatched"
NOTIFICATION_DISPATCH_WAIT_SPECIFIC | "Waiting time: %TIME% seconds"
NOTIFICATION_RESTRAINT_ON_EXIT_ATTEMPT | "The restraints are closed"
NOTIFICATION_RESTRAINT_ON_ENTER_ATTEMPT | "The restraints are closed"
NOTIFICATION_RESTRAINT_ENTER_OVERRIDE | "You just entered the ride while the restraints were closed"
NOTIFICATION_CANNOT_ENTER_RIDE | "You currently cannot enter this ride, try again later"
NOTIFICATION_CANNOT_ENTER_RIDE_CLOSED | "This ride is currently closed"
NOTIFICATION_OPERATOR_IDLE_TOO_LONG | "You were idle for too long while operating %RIDE_DISPLAY_NAME%"
NOTIFICATION_WARPED | ""
ELEVATED_OPERATOR_OVERRIDE_VICTIM_MESSAGE | "Player %PLAYER% took over control of the operating cabin"
ERROR_SMOOTH_COASTERS_DISABLED | "Smoother ride experience is disabled, please install SmoothCoasters"
ERROR_GENERAL_NO_PERMISSION_MESSAGE | "You do not have permissions to execute this action"
ERROR_PLAYER_COMMAND_ONLY_MESSAGE | "Only players can execute this command"
ERROR_UNKNOWN_COMMAND_MESSAGE | "Unknown jrides command. Type '/jrides help' for help"
ERROR_OPERATING_CABIN_OCCUPIED | "You can not take this operating cabin since it is already in use by another operator"
ERROR_OPERATING_NO_PERMISSION | "You are not allowed to operate this ride"
ERROR_RIDE_CONTROL_MENU_NOT_FOUND | "Ride control menu was not found"
ERROR_RIDE_OVERVIEW_MAP_NOT_FOUND | "Could not retrieve map for ride %RIDE_IDENTIFIER%, was the map id configured?"
ERROR_RIDE_COUNTER_MAP_NOT_FOUND | "Could not retrieve ride counter map for ride %RIDE_IDENTIFIER%, was the map id configured?"
ERROR_CANNOT_WARP | "Could not warp to ride %RIDE_DISPLAY_NAME%"
MENU_RIDE_OVERVIEW_TITLE | "Ride overview menu"
MENU_RIDE_OVERVIEW_STATUS_OPEN | "This ride is currently opened"
MENU_RIDE_OVERVIEW_STATUS_CLOSED | "This ride is currently closed"
MENU_RIDE_OVERVIEW_STATUS_MAINTENANCE | "This ride is in maintenance"
MENU_RIDE_CONTROL_TITLE | "Ride control menu"
MENU_RIDE_COUNTER_INDICATOR | "&7Ride counter: %RIDE_COUNT%"
MENU_ADMIN_RIDE_CONTROL_TITLE | "Admin ride control menu"
BUTTON_CLAIM_CABIN | "Claim operating cabin"
BUTTON_CABIN_CLAIMED | "Claim operating cabin"
BUTTON_DISPATCH_STATE | "Dispatch"
BUTTON_DISPATCH_PROBLEM_STATE | "Not allowed"
BUTTON_PROBLEMS_STATE | "Problems"
BUTTON_GATES_OPEN_STATE | "Gates are open"
BUTTON_GATES_CLOSED_STATE | "Gates are closed"
BUTTON_RESTRAINTS_OPEN_STATE | "Restraints are open"
BUTTON_RESTRAINTS_CLOSED_STATE | "Restraints are closed"
