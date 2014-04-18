package com.techygeek.l3freedom.freedom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;


public class SplashScreen extends Activity {
    String tagname = "download";
    private ProgressBar progressBar;
    private int progressStatus = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //the dir to be created
        File dir = new File (Environment.getExternalStorageDirectory() + "/L3Freedom");
        dir.mkdirs();

        //start the download task
        new PrefetchData().execute();
    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //no pre execute stuff

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String outdir = Environment.getExternalStorageDirectory() + "/L3Freedom";
            String recovery = Environment.getExternalStorageDirectory() + "/L3Freedom" + "/recovery.zip";

            //download recovery.zip
            if(root_tools.fileExists(recovery) == false) {
                //download the assets
                downloadFiles("http://unleashedprepaids.com/upload/devs/playfulgod/phones/LG/L3/Assets/recovery.zip", "recovery.zip");
                Log.i(tagname, "recovery.zip not found. downloading.");
            }

            //unzip the files
            if(!root_tools.fileExists(outdir + "/recovery")) {
                Log.i("unzip", "Unzipping recovery.zip");
                root_tools.unzip(recovery, outdir + "/recovery");
                Log.i("unzip", "Recovery unzipped.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // will close this activity and lauch main activity
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);
            // close this activity
            finish();
        }

    }

    public void updateProgress(int downloadsize, int totalsize, String filename) {
        //update the progress bar
        progressStatus = ((downloadsize*100)/ totalsize);

        if(progressStatus % 10 == 0) {
            progressBar.setProgress(progressStatus);
            Log.i(filename, "Downloading: " + progressStatus );
        }
    }

    public void downloadFiles(String file2get, String filename){
        try {
            //set the download URL, a url that points to a file on the internet
            //this is the file to be downloaded
            URL url = new URL(file2get);

            //create the new connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //set up some things on the connection
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //and connect!
            urlConnection.connect();

            //set the path where we want to save the file
            //in this case, going to save it on the root directory of the
            //sd card.
            File SDCardDir = Environment.getExternalStorageDirectory();
            //create a new file, specifying the path, and the filename
            //which we want to save the file as.
            File file = new File(SDCardDir + "/L3Freedom/" + filename);

            //this will be used to write the downloaded data into the file we created
            FileOutputStream fileOutput = new FileOutputStream(file);

            //this will be used in reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file
            int totalSize = urlConnection.getContentLength();
            //variable to store total downloaded bytes
            int downloadedSize = 0;

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0; //used to store a temporary size of the buffer

            //now, read through the input buffer and write the contents to the file
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                //add the data in the buffer to the file in the file output stream (the file on the sd card
                fileOutput.write(buffer, 0, bufferLength);
                //add up the size so we know how much is downloaded
                downloadedSize += bufferLength;

                updateProgress(downloadedSize, totalSize, filename);

            }
            //close the output stream when done
            fileOutput.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}