package com.rhino.foscam.task.sd;

import com.rhino.foscam.accessor.CameraUtilsSD;
import com.rhino.foscam.activity.sd.MailSettingsActivity;
import com.rhino.foscam.dialog.ConnectDialog;
import com.rhino.foscam.pojo.Camera;
import com.rhino.foscam.pojo.sd.CameraParams;

import android.os.AsyncTask;
import android.widget.Toast;

public class SetMailTask extends AsyncTask<Void, Void, Integer>{
	
	public ConnectDialog connectDialog;
	public MailSettingsActivity context;
	public Camera camera;
	public CameraParams cameraParams;
	public boolean test;
	
	public static final Integer CONNECTION_ERROR = 0;
	public static final Integer FAIL = 1;
	public static final Integer SUCCESS = 2;
	
	public SetMailTask(MailSettingsActivity context, Camera camera, CameraParams cameraParams, boolean test) {
		this.context = context;
		this.camera = camera;
		this.cameraParams = cameraParams;
		this.test = test;
	}
	
	@Override
	protected void onPreExecute() {
		connectDialog = new ConnectDialog();
		connectDialog.setCancelable(false);
		connectDialog.show(context.getFragmentManager(), "connect");
	}

	@Override
	protected Integer doInBackground(Void... params) {
		boolean success = false;
		try {
			success = CameraUtilsSD.setMail(camera, cameraParams);
		} catch (Exception e) {
			return CONNECTION_ERROR;
		}
		if(success) {
			return SUCCESS;
		} else {
			return FAIL;
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		try {
			connectDialog.dismiss();
		} catch(Exception e){}
		
		if(result == CONNECTION_ERROR) {
            Toast toast = Toast.makeText(context, "Failed to save changes, connection failed", Toast.LENGTH_LONG);
            toast.show();
		} else if(result == FAIL) {
            Toast toast = Toast.makeText(context, "Camera rejected settings, please try again", Toast.LENGTH_LONG);
            toast.show();
		} else if(result == SUCCESS){
			if(test) {
				context.beginTest();
			} else {
				Toast toast = Toast.makeText(context, "Settings updated successfully", Toast.LENGTH_SHORT);
				toast.show();
				context.saveComplete();
			}
		}
	}
	
	@Override
	protected void onCancelled(Integer result) {
		try {
			connectDialog.dismiss();
		} catch(Exception e){}
	}
}
