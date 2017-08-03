package top.golvaje.me.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

/**
 * In Case of fragment you can use
 * Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_container);
 * if (f instanceof MyAccountFragment) {
 * marshMallowPermissionFragment = new MarshMallowPermission(mContext, f);
 * }
 * <p>
 * In Case of activity
 * marshMallowPermissionFragment = new MarshMallowPermission(mContext);
 * <p>
 * <p>
 * Also you should in your Override in your activity or in fragment
 *
 * @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 * super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 * if (requestCode == marshMallowPermissionFragment.READ_STORAGE_PERMISSION_REQUEST_CODE) {
 * if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
 * // Permission granted.
 * <p>
 * } else {
 * // User refused to grant permission.
 * }
 * }
 * }
 */
public class MarshMallowPermission {
    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 4;
    public static final int READ_PHONE_STATE_PERMISSION_REQUEST_CODE = 6;
    public static final int READ_CONTACT_PERMISSION_REQUEST_CODE = 5;
    public static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 7;
    public static final int GET_ACCOUNTS_PERMISSION_REQUEST_CODE = 8;
    public static final int READ_SMS_PERMISSION_REQUEST_CODE = 9;
    public static final int RECEIVE_SMS_PERMISSION_REQUEST_CODE = 10;
    public static final int READ_RECEIVE_SMS_PERMISSION_REQUEST_CODE = 11;
    private final Context mContext;

    Fragment fragment;
    private String TAG = MarshMallowPermission.class.getSimpleName();

    /**
     * @param mContext
     * @param fragment
     */
    public MarshMallowPermission(Context mContext, Fragment fragment) {

        Log.e(TAG, "MarshMallowPermission() called with: " + "mContext = [" + mContext + "], activity = [" + fragment + "]");
        this.fragment = fragment;
        this.mContext = mContext;
    }

    /**
     * @param mContext
     */
    public MarshMallowPermission(Context mContext) {
        this.mContext = mContext;
    }

    public void requestPermissionForGetAccounts() {
        Log.e(TAG, "requestPermissionForGetAccounts() called");
        if (fragment == null) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.GET_ACCOUNTS},
                    GET_ACCOUNTS_PERMISSION_REQUEST_CODE);
        } else {
            fragment.requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, GET_ACCOUNTS_PERMISSION_REQUEST_CODE);
        }
    }

    public boolean isPermissionForGetAccounts() {
        Log.e(TAG, "isPermissionForGetAccounts() called");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = mContext.checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void requestPermissionForGetReadSms() {
        if (fragment == null) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_SMS},
                    READ_SMS_PERMISSION_REQUEST_CODE);
        } else {
            fragment.requestPermissions(new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    public boolean isPermissionForGetReadSms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = mContext.checkSelfPermission(Manifest.permission.READ_SMS);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }


    public void requestPermissionForGetRead_And_ReceiveSms() {
        if (fragment == null) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS}, READ_RECEIVE_SMS_PERMISSION_REQUEST_CODE);
        } else {
            fragment.requestPermissions(new String[]{Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS}, READ_RECEIVE_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    public boolean isPermissionForGetRead_And_ReceiveSms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int resultReadSms = mContext.checkSelfPermission(Manifest.permission.READ_SMS);
            int resultReceiveSms = mContext.checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            if (resultReadSms == PackageManager.PERMISSION_GRANTED && resultReceiveSms == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (resultReceiveSms == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(mContext, "Please Grant Receive Sms Permission first.", Toast.LENGTH_SHORT).show();
                }
                if (resultReadSms == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(mContext, "Please Grant Read Sms Permission first.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }
        return true;
    }

    public void requestPermissionForGetReceiveSms() {
        if (fragment == null) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECEIVE_SMS},
                    RECEIVE_SMS_PERMISSION_REQUEST_CODE);
        } else {
            fragment.requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, RECEIVE_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    public boolean isPermissionForGetReceiveSms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = mContext.checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void requestPermissionForRecord() {
        if (fragment == null) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_PERMISSION_REQUEST_CODE);
        } else {
            fragment.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_REQUEST_CODE);
        }
    }

    public boolean isPermissionForRecord() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = mContext.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void requestPermissionForLocation() throws Exception {
        try {
            if (fragment == null) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean isPermissionForLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void requestPermissionForContact() throws Exception {
        try {
            if (fragment == null) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_CONTACTS},
                        READ_CONTACT_PERMISSION_REQUEST_CODE);
            } else {
                fragment.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = mContext.checkSelfPermission(Manifest.permission.READ_CONTACTS);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void requestPermissionForReadPhone() throws Exception {
        try {
            if (fragment == null) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE},
                        READ_PHONE_STATE_PERMISSION_REQUEST_CODE);
            } else {
                fragment.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean isPermissionForReadPhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = mContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            if (fragment == null) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean isPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void requestPermissionForWriteExternalStorage() throws Exception {
        try {
            if (fragment == null) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean isPermissionForWriteExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void requestPermissionForCamera() throws Exception {
        try {
            if (fragment == null) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                fragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean isPermissionForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = mContext.checkSelfPermission(Manifest.permission.CAMERA);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
