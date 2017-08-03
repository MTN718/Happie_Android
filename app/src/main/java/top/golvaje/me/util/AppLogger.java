package top.golvaje.me.util;

import android.util.Log;

/**
 * Created by innoeye on 13/5/16.
 */
public class AppLogger {

    public static AppLogger instance;
    public  static boolean IsWriteEnable = true;

    private AppLogger(){

    }
    public static AppLogger getInstance(){
        if(instance==null){
            instance = new AppLogger();
        }
        return  instance;

    }

    public void writeError(String tag, String msg){
        if(IsWriteEnable){
            Log.e(tag, msg);
        }

    }
    public void writeInfo(String tag, String msg){
        if(IsWriteEnable){
          Log.i(tag, msg);
        }
    }

    public void writeDebug(String tag, String msg){
        if(IsWriteEnable){
            Log.d(tag, msg);
        }
    }

    public void writeException(Exception e){
        if(IsWriteEnable){
         e.printStackTrace();
        }

    }
}
