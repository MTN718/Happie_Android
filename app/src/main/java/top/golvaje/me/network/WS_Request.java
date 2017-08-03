package top.golvaje.me.network;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import top.golvaje.me.util.AppLogger;
import top.golvaje.me.util.Constant;
import top.golvaje.me.util.MessageConstants;
import top.golvaje.me.util.Utility;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ist-393 on 19/12/16.
 */
public class WS_Request {

    private static final String TAG = WS_Request.class.getSimpleName();
    private static final int CONNECTION_TIME_OUT = 60 * 1000;
    private static final int SOCKET_TIME_OUT = 60 * 1000;
    int statusCode = 0;
    public static WS_Request _instance;
    private URL url = null;
    private HttpURLConnection httpURLConnection = null;
    private String responseString;
    private String[] response = null;


    public static WS_Request getInstance() {
        if (_instance == null) {
            _instance = new WS_Request();
        }
        return _instance;
    }

    private WS_Request() {
    }




    private String[] HandleException(Exception exception) {
        String[] strMsg = new String[2];
        strMsg[0] = "0";
        try {
            if (exception instanceof MalformedURLException) {
                strMsg[1] = Utility.setErrorMsg(MessageConstants.SOMETHING_WENT_WRONG);
            } else if (exception instanceof ConnectException) {
                strMsg[1] = Utility.setErrorMsg(MessageConstants.SERVER_DOWN);
            } else if (exception instanceof ConnectTimeoutException) {
                strMsg[1] = Utility.setErrorMsg(MessageConstants.REQUEST_TIMEOUT);
            } else if (exception instanceof SocketException) {
                strMsg[1] = Utility.setErrorMsg(MessageConstants.REQUEST_TIMEOUT);
            } else if (exception instanceof SocketTimeoutException) {
                strMsg[1] = Utility.setErrorMsg(MessageConstants.REQUEST_TIMEOUT);
            } else if (exception instanceof NullPointerException) {
                strMsg[1] = Utility.setErrorMsg(MessageConstants.SOMETHING_WENT_WRONG);
            } else if (exception instanceof ProtocolException) {
                strMsg[1] = Utility.setErrorMsg(MessageConstants.SOMETHING_WENT_WRONG);
            } else if (exception instanceof UnknownHostException) {
                strMsg[1] = Utility.setErrorMsg(MessageConstants.SERVICE_UNAVAILABLE);
            } else {
                strMsg[1] = Utility.setErrorMsg(MessageConstants.SOMETHING_WENT_WRONG);
            }
        } catch (Exception ex) {
            AppLogger.getInstance().writeException(ex);
            strMsg[1] = MessageConstants.SOMETHING_WENT_WRONG;
        }
        return strMsg;
    }


    @SuppressWarnings("deprecation")
    public String[] sendLoginRequest(String submitUrl, String username, String password) {
        try {

            response = new String[2];
            url = new URL(submitUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(SOCKET_TIME_OUT);
            httpURLConnection.setConnectTimeout(CONNECTION_TIME_OUT);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpURLConnection.connect();
            statusCode = httpURLConnection.getResponseCode();
            AppLogger.getInstance().writeDebug("status code", "" + statusCode);
            if (statusCode == HttpURLConnection.HTTP_OK) {
                if (httpURLConnection != null && httpURLConnection.getHeaderField("Set-Cookie") != null) {
                    Constant.jSessionId = httpURLConnection.getHeaderField("Set-Cookie").split(";")[0].substring(11);
                }
                responseString = Utility.readData(new InputStreamReader(httpURLConnection.getInputStream()));
                response[0] = statusCode + "";
                response[1] = responseString;
            } else {
                response[0] = statusCode + "";
                response[1] = httpURLConnection.getResponseMessage();
            }
        } catch (Exception e) {
            AppLogger.getInstance().writeException(e);
            try {
                if (e.getMessage() != null && e.getMessage().contains("failed to connect")) {
                    response[0] = "0";
                    response[1] = Utility.setErrorMsg(MessageConstants.SERVER_DOWN);
                } else {
                    response = HandleException(e);
                }
            } catch (Exception e1) {
                AppLogger.getInstance().writeException(e1);
                return response;
            }
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return response;

    }


    public String[] sendGetRequest(String submitUrl) {
        try {
            response = new String[2];
            url = new URL(submitUrl);
            AppLogger.getInstance().writeDebug("submitUrl", submitUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(SOCKET_TIME_OUT);
            httpURLConnection.setConnectTimeout(CONNECTION_TIME_OUT);
            httpURLConnection.setRequestMethod("GET");

            if (Constant.jSessionId != null) {
                List<String> stringList = new ArrayList<>();
                stringList.add(0, "JSESSIONID=" + Constant.jSessionId);
                AppLogger.getInstance().writeDebug("jssseionid", "" + Constant.jSessionId);
                httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";", stringList));
            }

            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.connect();
            statusCode = httpURLConnection.getResponseCode();

            AppLogger.getInstance().writeDebug("status code", "" + statusCode);
            if (statusCode == HttpURLConnection.HTTP_OK) {
                responseString = Utility.readData(new InputStreamReader(httpURLConnection.getInputStream()));
                response[0] = statusCode + "";
                response[1] = responseString;
            } else {
                response[0] = statusCode + "";
                response[1] = httpURLConnection.getResponseMessage();
            }
        } catch (Exception e) {
            AppLogger.getInstance().writeException(e);
            if (e.getMessage() != null && e.getMessage().contains("failed to connect")) {
                response[0] = "0";
                response[1] = Utility.setErrorMsg(MessageConstants.SERVER_DOWN);
            } else {
                response = HandleException(e);
            }
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return response;
    }


    public String[] sendPostRequest(String submitUrl, String uploadJson) {
        try {
            response = new String[2];
            url = new URL(submitUrl);
            AppLogger.getInstance().writeDebug("url", "" + url);
            AppLogger.getInstance().writeDebug("uploadJson", "" + uploadJson);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(SOCKET_TIME_OUT);
            httpURLConnection.setConnectTimeout(CONNECTION_TIME_OUT);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);

            List<String> stringList = new ArrayList<>();
            stringList.add(0, "JSESSIONID=" + Constant.jSessionId);
            AppLogger.getInstance().writeDebug("jssseionid", "" + Constant.jSessionId);
            httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";", stringList));
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            if (uploadJson != null) {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                outputStreamWriter.write(uploadJson);
                outputStreamWriter.flush();
            }

            httpURLConnection.connect();
            statusCode = httpURLConnection.getResponseCode();

            AppLogger.getInstance().writeDebug("status code", "" + statusCode);
            if (statusCode == HttpURLConnection.HTTP_OK) {
                responseString = Utility.readData(new InputStreamReader(httpURLConnection.getInputStream()));
                response[0] = statusCode + "";
                response[1] = responseString;
            } else {
                response[0] = statusCode + "";
                response[1] = httpURLConnection.getResponseMessage();
            }

        } catch (Exception e) {
            AppLogger.getInstance().writeException(e);
            if (e.getMessage() != null && e.getMessage().contains("failed to connect")) {
                response[0] = "0";
                response[1] = Utility.setErrorMsg(MessageConstants.SERVER_DOWN);
            } else {
                response = HandleException(e);
            }
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return response;
    }


    public String[] sendLogOutRequest(String submitUrl) {
        try {
            response = new String[2];
            url = new URL(submitUrl);
            AppLogger.getInstance().writeDebug("submitUrl", submitUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(SOCKET_TIME_OUT);
            httpURLConnection.setConnectTimeout(CONNECTION_TIME_OUT);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

            List<String> stringList = new ArrayList<>();
            stringList.add(0, "JSESSIONID=" + Constant.jSessionId);
            AppLogger.getInstance().writeDebug("jssseionid", "" + Constant.jSessionId);
            httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";", stringList));
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.connect();
            statusCode = httpURLConnection.getResponseCode();

            AppLogger.getInstance().writeDebug("status code", "" + statusCode);
            if (statusCode == HttpURLConnection.HTTP_OK) {
                responseString = Utility.readData(new InputStreamReader(httpURLConnection.getInputStream()));
                response[0] = statusCode + "";
                response[1] = responseString;
                AppLogger.getInstance().writeDebug("jssseionid", "" + Constant.jSessionId);
            } else {
                response[0] = statusCode + "";
                response[1] = httpURLConnection.getResponseMessage();
            }
        } catch (Exception e) {
            AppLogger.getInstance().writeException(e);
            if (e.getMessage() != null && e.getMessage().contains("failed to connect")) {
                response[0] = "0";
                response[1] = Utility.setErrorMsg(MessageConstants.SERVER_DOWN);
            } else {
                response = HandleException(e);
            }
        } finally {

            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return response;
    }
    public String[] sendUploadRequest(String filePath, String Url) {


        try {
            response=new String[2];
            byte[] data = null;
            InputStream inputStream;
            File file=new File(filePath);
            String fileName=file.getName();

	/*	int index=fileName.lastIndexOf(".");
		String value=fileName.substring(index, fileName.length());
		System.out.println("nlueame===="+value);
		if("bmp".equalsIgnoreCase(value) || "jpeg".equalsIgnoreCase(value)
				||"jpg".equalsIgnoreCase(value) || "gif".equalsIgnoreCase(value)
				||"png".equalsIgnoreCase(value) || "ico".equalsIgnoreCase(value)
				||"tiff".equalsIgnoreCase(value) || "wepg".equalsIgnoreCase(value)
				){*/


            long fileSizeInBytes = file.length();
            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            long fileSizeInKB = fileSizeInBytes / 1024;
            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            long fileSizeInMB = fileSizeInKB / 1024;
            if (fileSizeInMB >= 10) {
                response[0]="7";
                response[1]="File size can not be more than 10MB";
                return response;
            } else if ((fileSizeInMB < 10 && fileSizeInMB > 3) && (fileName.endsWith("png") || fileName.endsWith("jpeg") || fileName.endsWith("jpg"))) {


                Bitmap bitmap = Utility.getBitmapFromFile(filePath, 0, 0);
                Log.d("bitmap", "value of" + bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (bitmap != null && (fileName.endsWith("jpeg") || fileName.endsWith("jpg")))
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

                else {
                    if (bitmap != null) {

                        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
                    }
                }
                data = stream.toByteArray();
                Log.d("byte array", "value of" + data);
                long lengthbmp = data.length;

                fileSizeInKB = lengthbmp / 1024;
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                fileSizeInMB = fileSizeInKB / 1024;
                System.out.println("upload file ,getting,btmap in byts=" + lengthbmp);
                System.out.println("upload file ,getting,btmap in kbs=" + fileSizeInKB);
                System.out.println("upload file ,getting,btmap in mbs=" + fileSizeInMB);

            } else {
                System.out.println("upload file ,getting, filepath=" + filePath);
                System.out.println("upload file ,getting, fileName=" + fileName);
                System.out.println("upload file ,getting,directly in kbs=" + fileSizeInKB);
                System.out.println("upload file ,getting,directly in mbs=" + fileSizeInMB);

                inputStream = new FileInputStream(new File(filePath));
                data = Utility.readData(inputStream);

            }


            //...............This is cahnges to get image size, but now it is in cheking.............

            System.out.println("upload file ,getting, filepath=" + filePath);
            System.out.println("upload file ,getting, fileName=" + fileName);
            System.out.println("upload file ,getting,directly in kbs=" + fileSizeInKB);
            System.out.println("upload file ,getting,directly in mbs=" + fileSizeInMB);



            HttpURLConnection httpURLConnection = null;
//            String urls=UPLOAD_URL;
            System.out.println("utl to b e uploaded ************" + Url);
            java.net.URL url = new URL(Url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(200 * 1000);
            httpURLConnection.setConnectTimeout(200 * 1000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
            //	connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");

            List<String> stringList = new ArrayList<>();
            stringList.add(0, "JSESSIONID=" + Constant.jSessionId);

            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data");
            httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";", stringList));
//            httpURLConnection.setRequestProperty("Referer", Common.refererValue);
            httpURLConnection.addRequestProperty("Authorization", "Basic " + Base64.encodeToString("rat#1:rat".getBytes(), Base64.NO_WRAP));

            InputStreamBody inputStreamBody = new InputStreamBody(new ByteArrayInputStream(data), fileName);

            MultipartEntity multipartEntity = new MultipartEntity();
            multipartEntity.addPart("filedata", inputStreamBody);

            DataOutputStream request = new DataOutputStream(httpURLConnection.getOutputStream());
            multipartEntity.writeTo(request);
            request.flush();
            request.close();

            statusCode = httpURLConnection.getResponseCode();
            Log.d("status code", "" + statusCode);
            if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
                responseString = Utility.readData(new InputStreamReader(httpURLConnection.getInputStream()));
                if(responseString!= null && responseString.contains("login.html")){
                    response[0]="401";
                    response[1]="Logout";
                }
                else if(responseString!=null && responseString.contains("failure"))
                {
                    response[0]="0";
                    response[1]= MessageConstants.SOMETHING_WENT_WRONG;
                }
                else{
                    response[0]=statusCode+"";
                    response[1]=responseString;
                }
            } else {
                response[0] = statusCode + "";
                response[1] = httpURLConnection.getResponseMessage();
            }

        }
        catch(ConnectException conex)
        {
            conex.printStackTrace();
            response[0]="0";
            response[1]= MessageConstants.SERVER_DOWN;
        }
        catch (SocketTimeoutException se)
        {
            se.printStackTrace();
            response[0]="0";
            response[1] = MessageConstants.CHECK_NETWORK_CONNECTIVITY;
        }
        catch (Exception e) {
            e.printStackTrace();
            response[0]="0";
            response[1]= MessageConstants.SOMETHING_WENT_WRONG;

        }finally {

            if(httpURLConnection!=null)
                httpURLConnection.disconnect();

        }
        return  response;
    }


}



