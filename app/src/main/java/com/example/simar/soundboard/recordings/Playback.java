package com.example.simar.soundboard.recordings;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Simar on 12/22/2017.
 */

public class Playback {

    //Variable initialization
    MediaPlayer mediaPlayer;
    String sourcePath;

    /**
     * Constructor
     *
     * @param mediaPlayer
     * @param sourcePath
     */
    public Playback(MediaPlayer mediaPlayer, String sourcePath) {
        this.mediaPlayer = mediaPlayer;
        this.sourcePath = sourcePath;
    }

    /**
     * Constructor
     *
     * @param sourcePath
     */
    public Playback(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    /**
     * Blank constructor
     */
    public Playback() {
    }

    /**
     * Returns the mediaplayer object
     *
     * @return
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Method sets the media player object
     * @param mediaPlayer
     */
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    /**
     * Method returns the source path for the playback
     * @return
     */
    public String getSourcePath() {
        return sourcePath;
    }

    /**
     * Method sets the source path
     * @param sourcePath
     */
    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    /**
     * method starts playing the audio file form the source
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    public void startPlaying() throws IllegalArgumentException, SecurityException, IllegalStateException {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(sourcePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method stops the playing of the file from the source path
     */
    public void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
