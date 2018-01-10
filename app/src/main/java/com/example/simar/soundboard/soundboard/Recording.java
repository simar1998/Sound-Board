package com.example.simar.soundboard.soundboard;

import java.io.File;

/**
 * Created by Simar on 12/23/2017.
 */

public class Recording {

    String recordingName;
    String recordingPath;
    String recordingDesc;
    File recording;
    boolean hasRecording;

    /**
     * Constructor
     * @param recordingName
     * @param recordingDesc
     */
    public Recording(String recordingName, String recordingDesc) {
        this.recordingName = recordingName;
        this.recordingDesc = recordingDesc;
        hasRecording = false;
    }

    /**
     * Constructor
     * @param recordingName
     * @param recordingDesc
     * @param recordingPath
     */
    public Recording(String recordingName, String recordingDesc, String recordingPath){
        this.recordingName = recordingName;
        this.recordingDesc = recordingDesc;
        this.recordingPath = recordingPath;
        this.hasRecording = true;
    }

    /**
     *Blank constructor
     */
    public Recording(){
        this.hasRecording = false;
    }

    /**
     * Getter for recording name
     * @return
     */
    public String getRecordingName() {
        return recordingName;
    }

    /**
     * Setter for recording name
     * @param recordingName
     */
    public void setRecordingName(String recordingName) {
        this.recordingName = recordingName;
    }

    /**
     * Getter for the recording path
     * @return
     */
    public String getRecordingPath() {
        return recordingPath;
    }

    /**
     * Setter for the recording path
     * @param recordingPath
     */
    public void setRecordingPath(String recordingPath) {
        this.recordingPath = recordingPath;
    }

    /**
     * Getter for recording description
     * @return
     */
    public String getRecordingDesc() {
        return recordingDesc;
    }

    /**
     * Setter for recording description
     * @param recordingDesc
     */
    public void setRecordingDesc(String recordingDesc) {
        this.recordingDesc = recordingDesc;
    }

    /**
     * Getter for recording file
     * @return
     */
    public File getRecording() {
        if(recording == null){
            recording = new File(recordingPath);
        }
        return recording;
    }

    /**
     * Setter recording
     * @param recording
     */
    public void setRecording(File recording) {
        this.recording = recording;
    }

    /**
     * Getter for the boolean that shows if the file object has been created
     * @return
     */
    public boolean isHasRecording() {
        return hasRecording;
    }

    /**
     * Setter for the boolean that shows if the file object has been created
     * @param hasRecording
     */
    public void setHasRecording(boolean hasRecording) {
        this.hasRecording = hasRecording;
    }
}
