package com.rhino.foscam.task.sd;

import com.rhino.foscam.accessor.CameraUtilsSD;
import com.rhino.foscam.activity.sd.PTSettingsActivity;
import com.rhino.foscam.dialog.ConnectDialog;
import com.rhino.foscam.pojo.Camera;
import com.rhino.foscam.pojo.sd.MiscParams;

import android.os.AsyncTask;
import android.widget.Toast;

public class SetPTTask extends AsyncTask<Void, Void, Integer>{
	
	public ConnectDialog connectDialog;
	public PTSettingsActivity context;
	public Camera camera;
	public MiscParams miscParams;
	
	public static final Integer CONNECTION_ERROR = 0;
	public static final Integer FAIL = 1;
	public static final Integer SUCCESS = 2;
	
	public SetPTTask(PTSettingsActivity context, Camera camera, MiscParams miscParams) {
		this.context = context;
		this.camera = camera;
		this.miscParams = miscParams;
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
			success = CameraUtilsSD.setMisc(camera, miscParams);
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
			Toast toast = Toast.makeText(context, "Settings updated successfully", Toast.LENGTH_SHORT);
			toast.show();
			context.saveComplete();
		}
	}
	
	@Override
	protected void onCancelled(Integer result) {
		connectDialog.dismiss();
	}
}
