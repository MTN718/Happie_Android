package top.golvaje.me.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.golvaje.me.R;

public class Utility {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final String TAG = Utility.class.getSimpleName();
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static String PREFERENCES = "taskuser";
    private static boolean hasImmersive;
    private static final int BUFFER_SIZE = 1024;
    private static boolean cached = false;
    private static Dialog dialog;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getDeviceId(Context context) {

        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    public static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 6;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return false;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return false;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public static String getUUID() {
        return java.util.UUID.randomUUID().toString();
    }


    public static String timeLeft(int delivery_time, String updatedDate) {

        try {

            int minutes = delivery_time;
            long millis_minutes = minutes * 60 * 1000;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = sdf.parse(updatedDate);
            long totalMinutes = millis_minutes + date.getTime();
            SimpleDateFormat sdf1 = new SimpleDateFormat();
            sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            long leftMinutes = totalMinutes - getUTCDateTimeAslong();
            return leftMinutes + "";
        } catch (Exception e) {
            Log.e("Error in Utility 63", "" + e);
            e.printStackTrace();
        }
        return "";
    }


    public static long getUTCDateTimeAslong() {

        Date dateTime1 = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("IST"));
            Date date = new Date();
            SimpleDateFormat dateParser = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            dateTime1 = null;
            try {
                dateTime1 = dateParser.parse(format.format(date));
            } catch (ParseException e) {
                Log.e("Error in Utility 84", "" + e);
                e.printStackTrace();
            }
        } catch (java.text.ParseException e) {
            Log.e("Error in Utility 88", "" + e);
            e.printStackTrace();
        }

        return dateTime1.getTime();
    }


    public static void logLargeString(String str) {

        if (str.length() > 3000) {

            System.out.print(str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            System.out.print(str);
        }
    }

    public static int getNavBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public final static Bitmap getBitmapFromFile(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // options.inSampleSize = 4;
        options.inSampleSize = calculateInSampleSize(options, width, height);

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(filePath);
        } catch (IOException e) {
            AppLogger.getInstance().writeException(e);
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                // bitmap = rotateImage(bitmap, 180);
                bitmap = rotateImage(bitmap, 90);
                break;
        }

        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 && reqHeight == 0) {
            return 1;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    private static Bitmap rotateImage(Bitmap pBitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(pBitmap, 0, 0, pBitmap.getWidth(), pBitmap.getHeight(), matrix, true);
    }

    @SuppressLint("NewApi")
    public static boolean hasImmersive(Context ctx) {

        if (!cached) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                hasImmersive = false;
                cached = true;
                return false;
            }
            Display d = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasImmersive = (realWidth > displayWidth) || (realHeight > displayHeight);
            cached = true;
        }

        return hasImmersive;
    }

    public static String getDeviceType(Context mContext) {
        String ua = new WebView(mContext).getSettings().getUserAgentString();

        if (ua.contains("Mobile")) {

            System.out.println("Type:Mobile");
            return "ANDROID MOBILE";
            // Your code for Mobile
        } else {
            // Your code for TAB
            System.out.println("Type:TAB");
            return "ANDROID TAB";
        }

    }

    public static Boolean write(String fname, String fcontent) {
        try {

            String fpath = "/sdcard/" + fname + ".txt";

            File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            Log.d("Suceess", "Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static Boolean writehtml(String fname, String fcontent) {
        try {

            String fpath = "/sdcard/" + fname + ".html";

            File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            Log.d("Suceess", "Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static String getFormatedDate(String strDate, String sourceFormate,
                                         String destinyFormate) {
        SimpleDateFormat df;
        df = new SimpleDateFormat(sourceFormate);
        Date date = null;
        try {
            date = df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        df = new SimpleDateFormat(destinyFormate);
        return df.format(date);
    }

    public static String getFirstDayofWeek() {
        // String str=""+Calendar.getInstance().get(field);
        String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();
        try {
            myDate = sdf.parse(str);
        } catch (ParseException pe) {
            // Do Something
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        System.out.println("getFirstDayofWeek:" + sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    public static String getDBCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getCurrentDateMMMddyyyy() {
        return new SimpleDateFormat("MMM dd, yyyy").format(new Date());
    }

    public static String getCurrentMMddyy() {
        return new SimpleDateFormat("MM-dd-yy").format(new Date());
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static String getLastDayofWeek() {
        // String str="2015-05-12";
        String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();
        try {
            myDate = sdf.parse(str);
        } catch (ParseException pe) {
            // Do Something
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.set(Calendar.DAY_OF_WEEK, 7);
        return sdf.format(cal.getTime());
    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = "1755018674765832";
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static void setBooleanPreferences(Context context, String key,
                                             boolean isCheck) {
        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean(key, isCheck);
        editor.commit();
    }

    public static boolean getBooleanPreferences(Context context, String key) {
        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);
        return setting.getBoolean(key, false);
    }

    public static void setStringPreferences(Context context, String key,
                                            String value) {
        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);

        SharedPreferences.Editor editor = setting.edit();

        editor.putString(key, value);
        editor.commit();

    }

    public static String getStringPreferences(Context context, String key) {

        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);
        return setting.getString(key, "");

    }

    public static void setIntegerPreferences(Context context, String key,
                                             int value) {
        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);

        SharedPreferences.Editor editor = setting.edit();

        editor.putInt(key, value);
        editor.commit();

    }

    public static void clearAllSharedPreferences(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static int getIntegerPreferences(Context context, String key) {

        SharedPreferences setting = (SharedPreferences) context
                .getSharedPreferences(PREFERENCES, 0);
        return setting.getInt(key, 0);

    }

    public static void showAlert(Context mContext, String title, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(msg);
        // Set behavior of negative button

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });

        AlertDialog alert = builder.create();
        try {
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlert(Context mContext, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle(mContext.getString(R.string.app_name));
        builder.setMessage(msg);
        // Set behavior of negative button

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });

        AlertDialog alert = builder.create();
        try {
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgressHUD(Context context, String title,
                                       String message) {
        try {

            if (title == null)
                title = "";
            if (message == null)
                message = "";
            dialog = ProgressDialog.show(context, title, message);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgressHUD(Context context) {
        try {
            dialog = ProgressDialog.show(context, context.getString(R.string.app_name), "Please Wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showProgressHUD(Context context, String message) {
        try {
            if (message == null)
                message = "";
            dialog = ProgressDialog.show(context, context.getString(R.string.app_name), message);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideProgressHud() {
        try {
            if (dialog != null)
                dialog.cancel();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getCurrentMMddyyyy() {

        return new SimpleDateFormat("MM-dd-yyyy").format(new Date());
    }

    public static void Alert_NoFilter() {

    }

    public static boolean isConnectingToInternet(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    /**
     * String myBase64Image = encodeToBase64(myBitmap, Bitmap.CompressFormat.JPEG, 100);
     * Bitmap myBitmapAgain = decodeBase64(myBase64Image);
     *
     * @param compressFormat
     * @param quality
     * @return
     */
    public static String encodeToBase64(Bitmap bitmap, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }

    /**
     * String myBase64Image = encodeToBase64(myBitmap, Bitmap.CompressFormat.JPEG, 100);
     * Bitmap myBitmapAgain = decodeBase64(myBase64Image);
     *
     * @param input
     * @return
     */
    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void displayLocationSettingsRequest(final Context context, final int REQUEST_CHECK_SETTINGS) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.e("check", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e("check", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("check", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("check", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    public static String getCompleteAddressthroughLatLngString(Context mContext, double LATITUDE, double LONGITUDE) {

        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static HashMap<String, Object> getModeltoMap(Object object) {

        Gson gson = new Gson();
        String temp = gson.toJson(object);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map = (HashMap<String, Object>) gson.fromJson(temp, map.getClass());

        return map;
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getErrorMsg(String msg) {
        try {
            if (msg != null) {
                JSONObject jsonObject = new JSONObject(msg);
                return jsonObject.getString("errorMsg");
            } else {
                return MessageConstants.SOMETHING_WENT_WRONG;
            }
        } catch (JSONException e) {
            AppLogger.getInstance().writeException(e);
            return MessageConstants.SOMETHING_WENT_WRONG;
        }

    }

    public static String setErrorMsg(String msg) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (msg != null) {
                jsonObject.put(MessageConstants.ERROR_MSG, msg);
                return jsonObject.toString();
            } else {
                jsonObject.put(MessageConstants.ERROR_MSG, MessageConstants.SOMETHING_WENT_WRONG);
                return jsonObject.toString();
            }
        } catch (JSONException e) {
            AppLogger.getInstance().writeException(e);
            return MessageConstants.SOMETHING_WENT_WRONG;
        }
    }
    public synchronized static String readData(InputStreamReader rd) {
        try {
            StringBuffer sb = new StringBuffer();
            char[] charBuffer = new char[BUFFER_SIZE];
            while (true) {
                int read = rd.read(charBuffer, 0, charBuffer.length);
                if (read == -1)
                    break;
                sb.append(charBuffer, 0, read);
            }
            return sb.toString();
        } catch (IOException e) {
        } finally {
            try {
                rd.close();
            } catch (IOException e) {
            }
        }
        return "";
    }

    public synchronized static byte[] readData(InputStream rd) {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[BUFFER_SIZE];
        try {
            while ((nRead = rd.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        String expression = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static String getFormatedJokeDate(String dateString) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        Date myDate = new Date();
        try {
            myDate = simpleDateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return "";
        }
        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
// Pass date object
        return simpleDateFormat.format(myDate.getTime());
        // Do Something
    }

}
