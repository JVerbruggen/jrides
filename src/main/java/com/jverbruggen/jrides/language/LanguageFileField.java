/************************************************************************************************************
 * GPLv3 License                                                                                            *
 *                                                                                                          *
 * Copyright (c) 2024-2024 JVerbruggen                                                                      *
 * https://github.com/JVerbruggen/jrides                                                                    *
 *                                                                                                          *
 * This software is protected under the GPLv3 license,                                                      *
 * that can be found in the project's LICENSE file.                                                         *
 *                                                                                                          *
 * In short, permission is hereby granted that anyone can copy, modify and distribute this software.        *
 * You have to include the license and copyright notice with each and every distribution. You can use       *
 * this software privately or commercially. Modifications to the code have to be indicated, and             *
 * distributions of this code must be distributed with the same license, GPLv3. The software is provided    *
 * without warranty. The software author or license can not be held liable for any damages                  *
 * inflicted by the software.                                                                               *
 ************************************************************************************************************/

package com.jverbruggen.jrides.language;

public enum LanguageFileField {
    CHAT_FEEDBACK_PREFIX,
    CHAT_FEEDBACK_INFO_COLOR,
    CHAT_FEEDBACK_WARNING_COLOR,
    CHAT_FEEDBACK_SEVERE_COLOR,

    COMMAND_RIDE_DISPATCHED_MESSAGE,

    COMMAND_VISUALIZE_ADDED_VIEWER,
    COMMAND_VISUALIZE_REMOVED_VIEWER,

    NOTIFICATION_PLUGIN_STILL_LOADING,

    NOTIFICATION_RIDE_CONTROL_ACTIVE,
    NOTIFICATION_RIDE_CONTROL_INACTIVE,

    NOTIFICATION_RIDE_DISPATCH_PROBLEMS,
    NOTIFICATION_RIDE_NO_TRAIN_PRESENT,
    NOTIFICATION_RIDE_NEXT_BLOCK_OCCUPIED,
    NOTIFICATION_RIDE_WAITING_TIME,
    NOTIFICATION_RIDE_RESTRAINTS_NOT_CLOSED,
    NOTIFICATION_RIDE_GATES_NOT_CLOSED,
    NOTIFICATION_RIDE_GATE_NOT_CLOSED,

    NOTIFICATION_RIDE_COUNTER_UPDATE,
    NOTIFICATION_RIDE_STATE_OPEN,
    NOTIFICATION_RIDE_STATE_CLOSED,

    NOTIFICATION_SHIFT_EXIT_CONFIRMATION,
    NOTIFICATION_SHIFT_EXIT_CONFIRMED,
    NOTIFICATION_DISPATCH_WAIT_GENERIC,
    NOTIFICATION_DISPATCH_WAIT_SPECIFIC,
    NOTIFICATION_RESTRAINT_ON_EXIT_ATTEMPT,
    NOTIFICATION_RESTRAINT_ON_ENTER_ATTEMPT,
    NOTIFICATION_RESTRAINT_ENTER_OVERRIDE,
    NOTIFICATION_CANNOT_ENTER_RIDE,
    NOTIFICATION_CANNOT_ENTER_RIDE_CLOSED,

    NOTIFICATION_OPERATOR_IDLE_TOO_LONG,
    NOTIFICATION_WARPED,

    ELEVATED_OPERATOR_OVERRIDE_VICTIM_MESSAGE,

    ERROR_SMOOTH_COASTERS_DISABLED,
    ERROR_GENERAL_NO_PERMISSION_MESSAGE,
    ERROR_PLAYER_COMMAND_ONLY_MESSAGE,
    ERROR_UNKNOWN_COMMAND_MESSAGE,
    ERROR_OPERATING_CABIN_OCCUPIED,
    ERROR_OPERATING_NO_PERMISSION,
    ERROR_RIDE_CONTROL_MENU_NOT_FOUND,
    ERROR_RIDE_OVERVIEW_MAP_NOT_FOUND,

    MENU_RIDE_OVERVIEW_TITLE,
    MENU_RIDE_OVERVIEW_STATUS_OPEN,
    MENU_RIDE_OVERVIEW_STATUS_CLOSED,
    MENU_RIDE_OVERVIEW_STATUS_MAINTENANCE,
    MENU_RIDE_CONTROL_TITLE,
    MENU_ADMIN_RIDE_CONTROL_TITLE,

    BUTTON_CLAIM_CABIN,
    BUTTON_CABIN_CLAIMED,
    BUTTON_DISPATCH_STATE,
    BUTTON_DISPATCH_PROBLEM_STATE,
    BUTTON_PROBLEMS_STATE,
    BUTTON_GATES_OPEN_STATE,
    BUTTON_GATES_CLOSED_STATE,
    BUTTON_RESTRAINTS_OPEN_STATE,
    BUTTON_RESTRAINTS_CLOSED_STATE
}
