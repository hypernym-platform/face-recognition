package com.hypernym.smartsurveillance.utilities



object AppConstants {
    const val AZURE_ENDPOINT = "https://facial-recognition-ds.cognitiveservices.azure.com/face/v1.0"
   // const val AZURE_ENDPOINT = "https://iotface.cognitiveservices.azure.com/"
    const val AZURE_SUBCRIPTION_KEY = "5f01c93a4f264d0686e64e93c17fdda6"
    const val LOGIN_ENDPOINT = "api/users/login/"
    const val SIGNALR = "api/SignalRConnection"
    const val DASHBOARD_ENDPOINT = "iof/customer_dashboard"
    const val FORGOT_PASSWORD_ENDPOINT = "api/users/regenerate_reset_token/"
    const val VERIFY_OTP_ENDPOINT = "api/users/verfiy_user_token/"
    const val CHANGE_PASSWORD_ENDPOINT = "api/users/change_password/"
    const val CHANGE_PASSWORD_MOBILE_ENDPOINT = "api/users/change_password_mobile"
    const val GET_PROFILE = "api/users/customer_user/"
    const val UPDATE_PROFILE = "api/users/update_user_profile/"
    const val GET_VEHICLE_SEARCH = "iof/vehicle"
    const val GET_NOTIFICATIONS = "iof/get_notifications_ios"
    const val GET_JOBS = "hypernet/entity/job/template_listing"
    const val GET_DRIVERS = "hypernet/entity/get_entity_type_dropdown/"
    const val GET_VIOLATIONS = "iof/get_violations_list"
    const val FLEETS_DASHBOARD_CARDS = "iof/dashboard/card"
    const val LOGOUT = "api/users/logout/"
    const val FLEETS_LISTING = "iof/fleet"
    const val JOB_TASK_LISTING = "iof/contract_list2/"
    const val DRIVERS = "iof/get_entity_new/"
    const val CREATE_JOB = "hypernet/entity/job/create_template/"
    const val FLEETS_LIST_KEY = "FLEETS_LIST"
    const val FLEET_VEHICLES_KEY = "FLEET_VEHICLES_KEY"
    const val VEHICLES_KEY = "VEHICLES_KEY"
    const val MARKER_LAT = "MARKER_LAT"
    const val MARKER_LNG = "MARKER_LNG"
    const val VEHICLES_MAP = "VEHICLES_MAP"
    const val VEHICLES_NAME = "VEHICLES_NAME"
    const val GET_FLEET = "iof/fleet_dashboard"
    const val GET_MAPTRAIL_ORSM = "match/v1/{profile}/{coordinates}"

    const val GET_MAP_TRAIL = "iof/map_trail"
    const val TOKEN = "TOKEN"
    const val LANGUAGE = "LANGUAGE"
    const val USER_EMAIL = "USER_EMAIL"
    const val IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN"
    const val POP_UP_NOTIFICATION_CHECK = "pop_notification"
    const val DATA = "data"
    const val FRAGMENT_NAME = "fragment name"

    const val MASTER_FRAGMENT_NAME = "master fragment name"
    const val MASTER_FRAGMENT_DATA = "master fragment data"

    const val DETAILS_FRAGMENT_NAME = "detail fragment name"
    const val DETAIL_FRAGMENT_DATA = "detail fragment data"


    const val USER = "user"
    const val Profile = "profile"
    const val VEHICLES = "VEHICLES"
    const val ALL_DRIVERS = "hypernet/GetDriverEntityView"

    const val FIREBASE_RT_DB_BASE = "https://vodafone-platform-default-rtdb.firebaseio.com/"
    const val CHAT_ROOT_NODE = "chat"
    const val MESSAGES_NODE = "messages"
    const val CHAT_ROOM_NODE = "chats"
    const val unread_message_count_node = "unread_message_count"
    const val USERS_NODE = "users"
    const val FCM_TOKEN = "FCM_TOKEN"
    const val DRIVER_ID = "DRIVER_ID"
    const val CHAT_ID_KEY = "CHAT_ID_KEY"
    const val DRIVER_NAME = "DRIVER_NAME"

    const val TOTAL_PAGES = 50
    const val TOTAL_JOBS_PAGES = 20
    const val TOTAL_NOTIFICATION = 10
    const val TOTAL_JOBS = 20
    const val THRESHOLD = 2.0
    const val NOTIFICATION_KEY = "notification_fragment"

       const val JOB_DATA_KEY = "JOB_DATA_KEY"

}