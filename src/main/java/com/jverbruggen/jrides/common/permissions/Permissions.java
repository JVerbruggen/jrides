package com.jverbruggen.jrides.common.permissions;

public class Permissions {
    /// Default
    public static final String RIDE_ENTER = "jrides.default.ride_enter"; // Allow entering a ride
    public static final String RIDE_WARP = "jrides.default.warp"; // Allow warping (through menu)
    public static final String COMMAND_RIDE_WARP = "jrides.command.default.warp"; // Allow warping (through command)
    public static final String COMMAND_RIDES_MENU = "jrides.command.default.rides"; // Allow opening '/jrides rides' menu

    /// Operator
    public static final String CABIN_OPERATE = "jrides.operator.cabin_operate"; // Allow to operate in a ride cabin
    public static final String COMMAND_CONTROL = "jrides.command.operator"; // Allow '/jrides control' command
    public static final String COMMAND_CONTROL_DISPATCH = "jrides.command.operator.dispatch"; // Allow '/jrides control dispatch' command
    public static final String COMMAND_CONTROL_MENU = "jrides.command.operator.menu"; // Allow '/jrides control menu' command

    /// Elevated (admin)
    public static final String ELEVATED_RESTRAINT_OVERRIDE = "jrides.elevated.restraint_override"; // Allow to override restraint lock
    public static final String ELEVATED_OPERATOR_OVERRIDE = "jrides.elevated.operator_override"; // Allow to take over an operator in a cabin
    public static final String ELEVATED_RIDE_CLOSED_ENTER_OVERRIDE = "jrides.elevated.closed_ride_enter_override"; // Allow to override closed ride entering
    public static final String ELEVATED_RIDE_OPEN_STATE_CHANGE = "jrides.elevated.open_state_change"; // Allow to open or close a ride
    public static final String COMMAND_ELEVATED_BASE = "jrides.command.elevated"; // Base for elevated commands (for admins)
    public static final String COMMAND_ELEVATED_ADMIN_MENU = "jrides.command.elevated.admin_menu"; // Allow to open ride admin menu
    public static final String COMMAND_ELEVATED_RIDE_OVERVIEW = "jrides.command.elevated.ride_overview"; // Allow for generating a ride overview map
    public static final String COMMAND_ELEVATED_RIDE_COUNTER_MAP = "jrides.command.elevated.ride_counter_map"; // Allow for generating a ride counter map
    public static final String COMMAND_ELEVATED_BLOCK_SECTION = "jrides.command.elevated.block_section"; // Allow for getting block section occupation message (debug)
    public static final String COMMAND_ELEVATED_VISUALIZE = "jrides.command.elevated.visualize"; // Allow to visualize a coaster spline (debug)

    /// Debug
    public static final String ELEVATED_STATUS_INSPECTION = "jrides.elevated.status_inspection"; // Allow to inspect coaster frames (debug)
    public static final String ELEVATED_DISPATCH_PROBLEMS_DEBUG = "jrides.elevated.dispatch_problems_debug";
}
