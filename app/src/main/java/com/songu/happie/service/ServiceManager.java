package com.songu.happie.service;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.songu.happie.doc.Config;
import com.songu.happie.doc.Globals;
import com.songu.happie.model.HappieModel;
import com.songu.happie.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2/20/2017.
 */
public class ServiceManager {

    public static HappieModel parseJsonJoke(JSONObject jokeJson)
    {
        try {
            HappieModel hModel = new HappieModel();
            hModel.mId = jokeJson.getString("id");
            hModel.mContent = jokeJson.getString("content");
            hModel.mUserId = jokeJson.getString("user_id");
            hModel.mUserName = jokeJson.getString("username");
            hModel.mCreateAt = jokeJson.getString("created_at");
            hModel.mPublished = jokeJson.getString("published");
            hModel.mUpVotes = jokeJson.getString("upvotes");
            hModel.mDownVotes = jokeJson.getString("downvotes");
            hModel.mPhoto = jokeJson.getString("photo");
            hModel.mNsfw = jokeJson.getString("nsfw");
            hModel.mComments = jokeJson.getString("comments");
            hModel.mViews = jokeJson.getString("views");
            hModel.mShares = jokeJson.getString("shares");
            return hModel;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void serviceLoadJokes(final IServiceResult caller,int category,int type)
    {
        String url = Config.mLoadJokes + "/" + String.valueOf(category) + "/" + String.valueOf(type);
        HttpUtil.get(url,new AsyncHttpResponseHandler() {
            public void onFailure(Throwable paramThrowable) {
                String s = "fail";
                caller.onResponse(400);
            }
            public void onFinish() {
                String s = "finish";
            }
            public void onSuccess(String paramString) {  //that is return when success..
                try {
                    Globals.lstJokes.clear();
                    JSONArray jokeJsonArray = new JSONArray(paramString);
                    for (int i = 0;i < jokeJsonArray.length();i++)
                    {
                        JSONObject jokeJson = jokeJsonArray.getJSONObject(i);
                        HappieModel hModel = parseJsonJoke(jokeJson);
                        if (hModel == null){
                            caller.onResponse(400);
                            return;
                        }
                        else
                        {
                            Globals.lstJokes.add(hModel);
                        }
                    }
                    caller.onResponse(200);
                } catch (Exception e) {
                    e.printStackTrace();
                    caller.onResponse(400);
                }

            }
        });
    }
}
