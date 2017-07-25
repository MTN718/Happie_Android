package com.songu.happie.network;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.songu.happie.activity.LoginActivity;
import com.songu.happie.helper.UIHelper;
import com.songu.happie.util.AppLogger;
import com.songu.happie.util.MessageConstants;



/**
 * Created by Aniruddh on 19/12/16.
 */
public class RetriveData extends AsyncTask<String,Void,String[]>   {
    private RetriveHandler retriveHandler=null;
    private UIHelper uiHelper=null;
    private Activity mContext;
    private String title, msg;
    private RetriveHandler.REQUEST_TYPE requestType;
    private RetriveHandler.METHOD_TYPE method_type;
    private String TAG = RetriveData.class.getSimpleName();

    public RetriveData(RetriveHandler retriveHandler, Activity ctx, String title, String msgs , RetriveHandler.REQUEST_TYPE req_Type, RetriveHandler.METHOD_TYPE method) {
        this.retriveHandler=retriveHandler;
        this.mContext = ctx;
        this.uiHelper = new UIHelper(mContext);
        this.title = title;
        this.msg = msgs;
        this.method_type=method;
        this.requestType = req_Type;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(msg!=null) {
            uiHelper.showProgressDialog(title, msg);
        }
    }

    @Override
    protected String[] doInBackground(String...params) {
        System.out.println("URL Is "+params[0]);
        switch(method_type){
            case GET:
               return  WS_Request.getInstance().sendGetRequest(params[0]);
            case POST:
                return  WS_Request.getInstance().sendPostRequest(params[0],params[1]);
            case LOGIN:
                return WS_Request.getInstance().sendLoginRequest(params[0],params[1],params[2]);
            case LOGOUT:
                return  WS_Request.getInstance().sendLogOutRequest(params[0]);
           case UPLOAD_FILE:
                return  WS_Request.getInstance().sendUploadRequest(params[0], params[1]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        try {
           AppLogger.getInstance().writeDebug(TAG, "Response Code " + result[0]);
           AppLogger.getInstance().writeDebug(TAG, "Response Is " + result[1]);

            if(result!=null && result.length>0 ) {
                if(result[0]!=null && (result[0].equalsIgnoreCase("200") || result[0].equalsIgnoreCase("204")))
                {
                    if(result[1]!=null && result[1].contains(MessageConstants.ERROR_MSG)){
                        retriveHandler.onError(result[1],requestType);
                    }
                    else if(result[1]!=null && (result[1].contains(MessageConstants.DOCTYPE_HTML) || result[1].contains(MessageConstants.DOCTYPE_html))){
                        if (result[1].contains(MessageConstants.ERROR_LOGIN_PAGE)) {
                            uiHelper.showToast(MessageConstants.SESSION_EXPIRED);
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);
                        }else{
                            retriveHandler.onError(MessageConstants.SOMETHING_WENT_WRONG, requestType);
                        }
                    }
                    else
                    {
                        retriveHandler.onSuccess(result[1], requestType);
                    }

                } else {
                    retriveHandler.onError(result[1],requestType);
                }
            }

            if(uiHelper.isProgressActive()){
                uiHelper.dismissProgressDialog();
            }
        }catch (Exception e){
            AppLogger.getInstance().writeException(e);
            if(result!=null) {
                retriveHandler.onError(result[1], requestType);
            }
            else
            {
                retriveHandler.onError(MessageConstants.SOMETHING_WENT_WRONG, requestType);
            }
            if(uiHelper.isProgressActive()){
                uiHelper.dismissProgressDialog();
            }
        }
    }
}
