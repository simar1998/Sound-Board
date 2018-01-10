package com.example.simar.soundboard.soundboard;

import android.util.Log;

import com.example.simar.soundboard.MainActivity;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Simar on 12/24/2017.
 */

public class Recordings {

    ArrayList<Recording> recordings = new ArrayList<Recording>();

    public Recordings(ArrayList<Recording> recordings){
        this.recordings = recordings;
    }

    public Recordings(){}

    public ArrayList<Recording> getRecordings() {
        Log.v("Recording size", ""+recordings.size());
        return recordings;
    }

    public void setRecordings(ArrayList<Recording> recordings) {
        this.recordings = recordings;
    }

    public void addRecording(Recording recording){
        recordings.add(recording);
    }

    public void addRecording(String recordingName, String recordingDesc, String recordingPath){
        recordings.add(new Recording(recordingName, recordingDesc,recordingPath));
    }

    public Recording getRecording(int i){
        return this.recordings.get(i);
    }

    public int getLength(){
        return recordings.size();
    }

    /**
     *
    public void removeRecording(Recording recording){
        int index = Collections.binarySearch((List<? extends Comparable<? super Recording>>) this.recordings, recording);
        File file = new File(this.recordings.get(index).getRecordingPath());
        file.delete();
    }
     */

    public Recording getRecording(String recordingName) {
        for (int i = 0; i < recordings.size(); i++) {
            if (recordings.get(i).recordingName.equals(recordingName)) {
                return recordings.get(i);
            }
        }
        return new Recording();
    }





}
