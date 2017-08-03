package top.golvaje.me.service;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import top.golvaje.me.doc.Config;
import top.golvaje.me.doc.Globals;
import top.golvaje.me.model.HappieModel;
import top.golvaje.me.util.HttpUtil;

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
            hModel.mTitle = jokeJson.getString("title");
            hModel.user_avatar = jokeJson.getString("user_avatar");
            return hModel;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void serviceLoadJokes(final IServiceResult caller, final int category, final int type)
    {
        String url = Config.mLoadJokes + "/" + String.valueOf(category) + "/" + String.valueOf(type);
        HttpUtil.get(url,new AsyncHttpResponseHandler() {
            public void onFailure(Throwable paramThrowable) {
                String s = "fail";
                caller.onResponse(400,category,type);
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
                            caller.onResponse(400,category,type);
                            return;
                        }
                        else
                        {
                            Globals.lstJokes.add(hModel);
                        }
                    }
                    caller.onResponse(200,category,type);
                } catch (Exception e) {
                    e.printStackTrace();
                    caller.onResponse(400,category,type);
                }

            }
        });
    }
}
