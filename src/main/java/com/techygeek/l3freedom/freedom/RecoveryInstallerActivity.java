package com.techygeek.l3freedom.freedom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class RecoveryInstallerActivity extends Activity {

    String tagname = "Recovery Install";
    String dir = Environment.getExternalStorageDirectory() + "/L3Freedom/recovery";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recovery_installer);
	}

	void install(){
		//we have to make sure the right aboot is used first...
		String cmd_aboot = "busybox dd if=" + dir + "/lock.bin" + " of=/dev/block/platform/msm_sdcc.3/by-num/p5";

        //where unsecure boot is installed
        String cmd_boot = "busybox dd if=" + dir + "/boot.img" + " of=/dev/block/platform/msm_sdcc.3/by-num/p9";

        //where the install will happen...
        String cmd_recovery = "busybox dd if=" + dir + "/e430-cwm.img" + " of=/dev/block/platform/msm_sdcc.3/by-num/p17";

        root_tools.execute(cmd_aboot);
        root_tools.execute(cmd_boot);
		root_tools.execute(cmd_recovery);

        Log.i(tagname, "CWM was Installed");
    }

    // Progressbar
    public void start(View view) {
        final ProgressDialog RingProgressDialog = ProgressDialog.show(RecoveryInstallerActivity.this, "Please Wait", "Freeing your L3 II!!!", true);
        RingProgressDialog.setCancelable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //this is the runnable stuff for the progress bar
                    install();
                } catch (Exception e) {
                    Log.e("Recovery Installer", "something went wrong");

                }
                RingProgressDialog.dismiss();

            }
        }).start();

    }

}
