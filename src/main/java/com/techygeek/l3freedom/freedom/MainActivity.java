package com.techygeek.l3freedom.freedom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Is this application running on a LG Optimus L3 II (vee3e)?
        String line = "";
        String CorrectDevice = "vee3e_open_eu";
        String CorrectDevice2 = "vee3e";

        // Get property and save to String line, then compare with StringDevice
        // If the output from getprop doesnt't match CorrectDevice, display a warning.
        try {
            Process ifc = Runtime.getRuntime().exec("getprop ro.product.name");
            BufferedReader bis = new BufferedReader(new InputStreamReader(ifc.getInputStream()));
            line = bis.readLine();
        } catch (java.io.IOException e) {
        }

        if (!line.equals(CorrectDevice)) {
            if (!line.equals(CorrectDevice2)) {
                AlertDialog.Builder IncorrectDevice = new AlertDialog.Builder(this);
                IncorrectDevice.setIcon(R.drawable.warn);
                IncorrectDevice.setTitle("Wrong Device!");
                IncorrectDevice.setMessage("You are probably not running this application on the LG Optimus L3 II. Please note that this application is intended to run on the LG Optimus L3 II. If you are using a LG Optimus L3 II, please contact your ROM developer and tell him, that the \nro.product.name should be vee3e_open_eu.");
                IncorrectDevice.setCancelable(false);
                IncorrectDevice.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                IncorrectDevice.show();
            }
        }
    }

	public void recoveryInstaller(View view){
		//goto the recovery installer intent
		Intent intent = new Intent(this, RecoveryInstallerActivity.class);
		startActivity(intent);
	}
	
	public void recoveryUninstaller(View view){
		//goto the recovery uninstaller intent
		Intent intent = new Intent(this, RecoveryUninstallerActivity.class);
		startActivity(intent);
	}

    public void rebooter(View view){
        //i dont know if this will need an intent
        //goto the recovery installer intent
        //Intent intent = new Intent(this, rebooter.class);

        root_tools.execute("reboot recovery");

    }
	
		public void info(View view) {
        AlertDialog.Builder about = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        about.setTitle(R.string.title_activity_info);
        about.setCancelable(false);
        about.setIcon(R.drawable.apple);
        about.setView(inflater.inflate(R.layout.activity_info, null));
        about.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        about.show();
    }
 }
