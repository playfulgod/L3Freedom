package com.techygeek.l3freedom.freedom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class RecoveryUninstallerActivity extends Activity {
     String tagname = "Recovery Restore";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recovery_uninstaller);

	}

		void install(){
            String dir = Environment.getExternalStorageDirectory() + "/L3Freedom/recovery";
            String cmd_install = "busybox dd if="+ dir + "/stock-recovery.img" + " of=/dev/block/platform/msm_sdcc.3/by-num/p17";
            root_tools.execute(cmd_install);
            Log.i(tagname, "Stock Recovery restored.");
		
		
	}
    // Progessbar
    public void uninstaller(View view) {
        final ProgressDialog RingProgressDialog = ProgressDialog.show(RecoveryUninstallerActivity.this, "Please Wait", "Restoring Recovery", true);
        RingProgressDialog.setCancelable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //this is the runnable stuff for the progress bar
                    install();
                } catch (Exception e) {
                    Log.e("Recovery Uninstaller", "something went wrong");
                }
                RingProgressDialog.dismiss();

            }
        }).start();
    }
}
