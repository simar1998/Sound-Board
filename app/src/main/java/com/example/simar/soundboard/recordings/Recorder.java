package com.example.simar.soundboard.recordings;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.file.SecureDirectoryStream;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.example.simar.soundboard.MainActivity;

/**
 * Created by Simar on 12/21/2017.
 */

public class Recorder extends AppCompatActivity {

    //Variable decelerations and some initializations
    String path = "";
    MediaRecorder mediaRecorder;
    String recordingName = "";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    Context context;
    Playback playback;
    boolean isRecording = false;
    Activity activity;

    /**
     * Constructor for the Recorder class
     *
     * @param path
     * @param mediaRecorder
     * @param recordingName
     * @param mediaPlayer
     * @param context
     */
    public Recorder(String path, MediaRecorder mediaRecorder, String recordingName, MediaPlayer mediaPlayer, Context context, Activity activity) {
        this.path = path;
        this.mediaRecorder = mediaRecorder;
        this.recordingName = recordingName;
        this.mediaPlayer = mediaPlayer;
        this.context = context;
        this.activity = activity;
    }

    /**
     * Constructor for the Recorder class
     *
     * @param path
     * @param recordingName
     * @param context
     */
    public Recorder(String path, String recordingName, Context context, Activity activity) {
        this.path = path;
        this.recordingName = recordingName;
        this.context = context;
        this.activity = activity;
    }

    public Recorder() {
    }


    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        Log.d("Audio Source", MediaRecorder.AudioSource.MIC + "");
        mediaRecorder.setAudioSource(0);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(path);
    }

    /**
     * Simple getter
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Simple getter
     *
     * @return
     */
    public MediaRecorder getMediaRecorder() {
        return mediaRecorder;
    }

    /**
     * Setter for the parameter bellow
     *
     * @param mediaRecorder
     */
    public void setMediaRecorder(MediaRecorder mediaRecorder) {
        this.mediaRecorder = mediaRecorder;
    }

    /**
     * Simple getter
     *
     * @return
     */
    public String getRecordingName() {
        return recordingName;
    }

    /**
     * Setter for the parameter bellow
     *
     * @param recordingName
     */
    public void setRecordingName(String recordingName) {
        this.recordingName = recordingName;
    }

    /**
     * Simple getter
     *
     * @return
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Setter for the parameter bellow
     *
     * @param mediaPlayer
     */
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    /**
     * Simple getter
     *
     * @return
     */
    public Context getContext() {
        return context;
    }

    /**
     * Setter for the parameter bellow
     *
     * @return
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Method starts the recording and saving it to the file path specified
     */
    public void startRecording() {
        if (MainActivity.writePermission && MainActivity.recordPermission) {

            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + recordingName + ".mp3";

            MediaRecorderReady();

            try {
                Log.v("RECORDING STATUS", "Commencing");
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            Toast.makeText(context, "Recorder started", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Insufficient permission parameters", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method stops the recording process
     */
    public void stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            isRecording = false;
            Toast.makeText(context, "Recorder Completed",
                    Toast.LENGTH_LONG).show();
            Log.v("AUDIO SAVE PATH", path);
        }
    }

    /**
     * Method returns the playback object
     *
     * @return
     */
    public Playback getPlayback() {
        playback.setSourcePath(path);
        return playback;
    }

    /**
     * Method starts the playback process
     */
    public void startPlaying() {
        playback.startPlaying();
    }

    /**
     * Method stops the playback process
     */
    public void stopPlaying() {
        playback.stopPlaying();
    }


    /**
     * Method requests permiisions from the mobile device
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(context, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    /**
     * Method checks the permisions given to the phone by the user and compares it to the ones required
     *
     * @return
     */
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context,
                WRITE_EXTERNAL_STORAGE);
        Log.v("WRITE_PERMISSION", (result == PackageManager.PERMISSION_GRANTED) + "");
        int result1 = ContextCompat.checkSelfPermission(context,
                RECORD_AUDIO);
        Log.v("RECORD_PERMISSION", (result == PackageManager.PERMISSION_GRANTED) + "");
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isRecording() {
        return isRecording;
    }
}

