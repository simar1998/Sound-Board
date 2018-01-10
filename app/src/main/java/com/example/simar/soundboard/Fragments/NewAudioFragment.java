package com.example.simar.soundboard.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.simar.soundboard.R;
import com.example.simar.soundboard.recordings.Playback;
import com.example.simar.soundboard.recordings.Recorder;
import com.example.simar.soundboard.soundboard.Recording;
import com.example.simar.soundboard.soundboard.Recordings;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewAudioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewAudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAudioFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    //Decleration of the variables and objects that would be used in this Fragment
    Toolbar toolbar;
    EditText recordingNameEditText;
    TabHost tabHost;
    ProgressBar recordingProgress;

    FragmentManager fragmentManager;
    FragmentTransaction transition;
    ToggleButton toggleButton;
    ToggleButton playButton;
    TextView progresTimeText;
    Activity activity;
    Recorder recorder;
    Recordings recordings;
    boolean hasRecording = false;
    Recording currentRecording;

    private OnFragmentInteractionListener mListener;

    public NewAudioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewAudioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewAudioFragment newInstance(String param1, String param2) {
        NewAudioFragment fragment = new NewAudioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * All initializations of the objects will go here.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_audio, container, false);

        //Object initialization and object setup
        toggleButton = (ToggleButton) view.findViewById(R.id.recordBtn);
        toggleButton.setTextOff("RECORD");
        toggleButton.setTextOn("RECORDING");
        toggleButton.setChecked(false);

        recordingNameEditText = (EditText) view.findViewById(R.id.recordingNameEditText);
        tabHost = (TabHost) view.findViewById(R.id.tabHost);
        playButton = (ToggleButton) view.findViewById(R.id.playButtons);
        progresTimeText = (TextView) view.findViewById(R.id.progressTimeText);
        activity = getActivity();

        tabHost.setup();

        //Tab object initial initialization
        TabHost.TabSpec micTab = tabHost.newTabSpec("Mic");
        TabHost.TabSpec filesTab = tabHost.newTabSpec("Files");

        //Mic Tab setup
        micTab.setIndicator("Mic");
        micTab.setContent(R.id.micTab);
        tabHost.addTab(micTab);

        //Files tab setup
        filesTab.setIndicator("Files");
        filesTab.setContent(R.id.filesTab);
        tabHost.addTab(filesTab);

        //Progress bar object intialization and setup
        // TODO: Proper setup fot he progress bar setup
        recordingProgress = (ProgressBar) view.findViewById(R.id.recordingProgress);
        recordingProgress.setIndeterminate(false);
        recordingProgress.setMax(100);

        recordings = jsonFromFile();

        Bundle arguments = getArguments();
        if (Integer.parseInt(arguments.getString("INCOMING_FRAGMENT")) >= 1 && arguments != null) {
            recordingNameEditText.setText(recordings.getRecording(Integer.parseInt(arguments.getString("INCOMING_FRAGMENT"))).getRecordingName());
            currentRecording = recordings.getRecording(Integer.parseInt(arguments.getString("INCOMING_FRAGMENT")));
            hasRecording = true;
        } else if (arguments.getString("INCOMING_FRAGMENT").equals("-1")) {
            recordingNameEditText.setText("");
            currentRecording = new Recording();
        }
        // Inflate the layout for this fragment
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * This method loops the code in the method. Do not add initializations here as they will increase RAM
     *
     * @param view
     * @param savedInstance
     */
    public void onViewCreated(View view, Bundle savedInstance) {

        super.onViewCreated(view, savedInstance);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}
                    , 1);

        } else {
            //this.finishAffinity();
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , 1);

        } else {
            //this.finishAffinity();
        }
        recordingProgress.setIndeterminate(false);
        recordingProgress.setProgress(100);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            //Block of code for the recording of the audio
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                recorder = new Recorder(Environment.getExternalStorageDirectory().toString(),
                        recordingNameEditText.getText().toString(), getContext(), activity);
                if (b) {
                    if (recordingNameEditText.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Please enter the name for the recording first", Toast.LENGTH_SHORT).show();
                    } else {

                        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                recordingNameEditText.getText().toString() + ".mp3";

                        new CountDownTimer(10000, 1000) {


                            public void onTick(long millisUntilFinished) {
                                if ((millisUntilFinished / 1000) < 10) {
                                    progresTimeText.setText("0:0" + millisUntilFinished / 1000);
                                    recordingProgress.setProgress(0);
                                    recordingProgress.setProgress((int) (millisUntilFinished / 1000) * 10);
                                } else {
                                    progresTimeText.setText("0:" + millisUntilFinished / 1000);
                                    recordingProgress.setProgress(0);
                                    recordingProgress.setProgress((int) (millisUntilFinished / 1000) * 10);
                                }
                            }

                            public void onFinish() {
                                recorder.stopRecording();
                                toggleButton.setChecked(false);
                                progresTimeText.setText("0:00");
                                Recording newRecording = new Recording(recorder.getRecordingName(), "", path);
                                recordings.addRecording(newRecording);
                                recordings.validateRecordings();
                                jsonToFile(recordings);
                                currentRecording = newRecording;
                            }
                        }.start();
                    }
                } else {
                }
            }
        });

        playButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d("Playback Path", currentRecording.getRecordingPath() + "/" + currentRecording.getRecordingName() + ".mp3");
                    Playback playback = new Playback(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + currentRecording.getRecordingName() + ".mp3");
                    playback.startPlaying();
                }
            }
        });
        //TODO: Fix the fucking bug that presists when the context is focused on an GUI element and the listener doesnt fucking work
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() != KeyEvent.ACTION_DOWN) {
                    recordingNameEditText.clearFocus();
                    backAction();
                    return true;
                }
                return false;
            }
        });


    }

    /**
     * Method handles what to do when the back button is pressed in the NewRecordingFragment
     * Makes a prompt asking the user for the confirmation for going back to the parent fragment.
     */
    public void backAction() {
        new AlertDialog.Builder(getContext())
                .setTitle("Go back?")
                .setMessage("Going back will discard your progress?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        getFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void jsonToFile(Recordings recordings) {
        try (FileWriter file = new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Recordings.json")) {
            Log.v("GSON STATUS", "Commencing GSON to file");
            Log.e("JSON String", (new Gson()).toJson(recordings) + "");
            file.write((new Gson()).toJson(recordings));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Recordings jsonFromFile() {
        Recordings recordings = new Recordings();
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Recordings.json"));
            recordings = gson.fromJson(reader, Recordings.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (recordings == null) {
            return new Recordings();
        }
        return recordings;
    }

}