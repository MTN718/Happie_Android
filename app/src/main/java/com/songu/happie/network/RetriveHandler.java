package com.songu.happie.network;

/**
 * Created by Aniruddh on 19/12/16.
 */
public interface RetriveHandler {

    enum REQUEST_TYPE {
        LOGINREQUEST, VERIFY_OTP, SIGNUP, DEVICE_LIST, DEVICE_SPECIFIC_ALARMS, GET_ASSIGN_DEVICES, FENCE_LIST, UNASSIGN_DEVICES, UNASSIGN_ONLINE_DEVICES,
        UNASSIGN_OFFLINE_DEVICES, DEVICE_LOCATION, FENCE_ADD_EDIT, UPLOAD_ASSIGN_DEVICES, LOGOUT, GET_DEVICES, ALARM_COUNT,
        ACTIVATION_DATE, EDIT_DEVICE_NAME, BARCODE, UNASSIGN_FENCE, UNASSIGN_FENCE_LIST, ASSIGN_FENCE_DATE_TIME, UPLOAD_FENCE_ACTIVATION_PERIOD,
        MEMBER_PIC,ADD_MEMBER,ALL_MEMBERS,ALL_ALARMS
    }

    enum METHOD_TYPE {
        POST, GET, UPLOAD_FILE, LOGIN, LOGOUT, PUT
    }

    void onSuccess(String result, REQUEST_TYPE request_type);

    void onError(String result, REQUEST_TYPE request_type);

}
