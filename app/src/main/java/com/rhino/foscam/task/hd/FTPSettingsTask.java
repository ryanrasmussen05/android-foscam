package com.rhino.foscam.task.hd;

import com.rhino.foscam.accessor.CameraUtilsHD;
import com.rhino.foscam.activity.hd.CameraMenuActivity;
import com.rhino.foscam.dialog.ConnectDialog;
import com.rhino.foscam.pojo.Camera;
import com.rhino.foscam.pojo.hd.FTP;
import com.rhino.foscam.pojo.hd.SystemTime;

import android.os.AsyncTask;
import android.widget.Toast;

public class FTPSettingsTask extends AsyncTask<Void, Void, Integer>{

    public ConnectDialog connectDialog;
    public CameraMenuActivity context;
    public Camera camera;
    public FTP ftp;

    public static final Integer CONNECTION_ERROR = 0;
    public static final Integer SUCCESS = 1;

    public FTPSettingsTask(CameraMenuActivity context, Camera camera) {
        this.context = context;
        this.camera = camera;
    }

    @Override
    protected void onPreExecute() {
        connectDialog = new ConnectDialog();
        connectDialog.setCancelable(false);
        connectDialog.show(context.getFragmentManager(), "connect");
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            ftp = CameraUtilsHD.getFTPConfig(camera);
        } catch (Exception e) {
            return CONNECTION_ERROR;
        }
        return SUCCESS;
    }

    @Override
    protected void onPostExecute(Integer result) {
        try {
            connectDialog.dismiss();
        } catch(Exception e){}

        if(result == CONNECTION_ERROR) {
            Toast toast = Toast.makeText(context, "Failed to get response, try again", Toast.LENGTH_LONG);
            toast.show();
        } else if(result == SUCCESS) {
            context.startFTPSettingsActivity(ftp);
        }
    }

    @Override
    protected void onCancelled(Integer result) {
        try {
            connectDialog.dismiss();
        } catch(Exception e){}
    }
}
