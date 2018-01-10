package com.example.simar.soundboard.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.simar.soundboard.Fragments.NewAudioFragment;
import com.example.simar.soundboard.MainActivity;
import com.example.simar.soundboard.R;
import com.example.simar.soundboard.soundboard.Recording;
import com.example.simar.soundboard.soundboard.Recordings;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.simar.soundboard.MainActivity.REVIEW_TYPE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordingsFragment extends Fragment implements NewAudioFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView listView;
    Recordings recordings = new Recordings();
    //Object Declaration
    FloatingActionButton fab;


    FragmentManager fragmentManager;
    FragmentTransaction transition;

    NewAudioFragment newAudioFragment = new NewAudioFragment();


    private OnFragmentInteractionListener mListener;

    public RecordingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordingsFragment newInstance(String param1, String param2) {
        RecordingsFragment fragment = new RecordingsFragment();
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

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        addRecording(fab);
        listView = (ListView) getView().findViewById(R.id.recordingList);
        ArrayList<String> list = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.list_view, list);
        listView.setAdapter(adapter);
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Recordings.json"));
            Recordings recordingsFromFile = gson.fromJson(reader,Recordings.class);
            Log.d("Recording From File", gson.toJson(recordingsFromFile));
            if(recordingsFromFile != null){
                recordings = recordingsFromFile;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(recordings.getLength() > 0) {
            for (int i = 0; i < recordings.getLength(); i++) {
                list.add(recordings.getRecordings().get(i).getRecordingName());
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                newAudioFragment = new NewAudioFragment();
                fragmentManager = getFragmentManager();
                Bundle arguments = new Bundle();
                arguments.putString("INCOMING_FRAGMENT",""+i);
                newAudioFragment.setArguments(arguments);
                transition = fragmentManager.beginTransaction();
                transition.replace(R.id.contentFragment, newAudioFragment);
                transition.addToBackStack("");
                transition.commit();
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recordings = MainActivity.jsonFromFile();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recordings, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Transitions tot he new audio frament when the Floating Button is pressed, must be implemented in runtime
     *
     * @param fab
     */
    public void addRecording(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAudioFragment = new NewAudioFragment();
                fragmentManager = getFragmentManager();
                transition = fragmentManager.beginTransaction();
                transition.replace(R.id.contentFragment, newAudioFragment);
                //Adds the current fragment into the backstack so when the back button is pressed it commes back to Recordings fragment.
                transition.addToBackStack("");
                transition.commit();
            }
        });
    }



}
