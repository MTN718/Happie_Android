package top.golvaje.me.helper;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import top.golvaje.me.util.AppLogger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aniruddh on 19/12/16.
 */

public class UIHelper
{	
	Activity activity;
	private ProgressDialog progressDialog;

	public UIHelper(Activity activity)
	{
		this.activity = activity;
	}	
	
	public void showProgressDialog(final String title ,final String message){
		if (activity != null){
			activity.runOnUiThread(new Runnable(){
				public void run(){
					progressDialog = ProgressDialog.show(activity, title, message);
				}
			});
		}
	}
	
	public boolean isProgressActive(){
		if (progressDialog != null)
		{
			return progressDialog.isShowing();
		} 
		else
		{
			return false;
		}
	}
	public void dismissProgressDialog(){		
		if (activity != null && progressDialog != null)
		{
			progressDialog.dismiss();
		}
	}	

	public void showAlertDialog(final String Title, final String Message){
		activity.runOnUiThread(new Runnable() {
			public void run() {
				new AlertDialog.Builder(activity).setMessage(Message).setTitle(Title).setPositiveButton("OK", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int id){
						dialog.dismiss();
					}
				}).create().show();
			}
		});		
	}	

	public void showToast(final String msg) {
		if (activity != null){
			activity.runOnUiThread(new Runnable() 
			{
				public void run() 
				{
					Toast.makeText(activity,msg, Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	
	public boolean IsNumeric(String strNumber){
		try{
			double isNumberic = Double.parseDouble(strNumber);
			} catch (Exception e) {
				return false;
			}
			
			return true;
		}
	
	@SuppressLint("SimpleDateFormat") 
	public String ConvertJsonDate(String strdate, String strDateFormate)
	{	
		String formattedDate = null;
		try{
//			Calendar cal = Calendar.getInstance();
//			TimeZone tz = cal.getTimeZone();
			String strAcuire = strdate.substring(6, strdate.length()-2);
			String ackwardRipOff = strAcuire.replace("+", "");
			long lngDate = Long.parseLong(ackwardRipOff.substring(0, 13));
//			Date createdOn = new Date((lngDate+(0530*36000)));
			Date createdOn = new Date(lngDate);
			SimpleDateFormat sdf = new SimpleDateFormat(strDateFormate);
//			sdf.setTimeZone(tz);
			formattedDate = sdf.format(createdOn);
		}catch (Exception e) {
			// TODO: handle exception
			AppLogger.getInstance().writeException(e);
		}
		return formattedDate;		
	}
	
	public String getJsonFormatDate()
	{
		String jsonRetun =null;// "/Date(1444977583891+0530)/"
		try{

			long millisStart = Calendar.getInstance().getTimeInMillis();
			jsonRetun = "/Date("+millisStart+"+0530)/";

		}catch (Exception e) {
			// TODO: handle exception
			AppLogger.getInstance().writeException(e);
		}

		return jsonRetun;

	}
	


}
