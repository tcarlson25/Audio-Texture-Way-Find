package edu.tamu.github.audiotextilewayfind;

import android.content.Context;
import android.graphics.PorterDuff;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;
import java.util.Vector;

import static edu.tamu.github.audiotextilewayfind.VerbalQueues.BookStacks;
import static edu.tamu.github.audiotextilewayfind.VerbalQueues.Elevators;
import static edu.tamu.github.audiotextilewayfind.VerbalQueues.EntranceExit;
import static edu.tamu.github.audiotextilewayfind.VerbalQueues.RestRooms;
import static edu.tamu.github.audiotextilewayfind.VerbalQueues.StudySpace;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    //The floating action button that the user can press to get points or specify a location
    private ImageButton whereButton;
    private ImageButton updateButton;
    private ImageButton emergencyExitButton;
    private ImageButton infoButton;
    private Button searchButton;
    //Reference to this
    private static Context context;
    TextToSpeech t1;
    WhereButtonHandle whereButtonHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Generate Evans Map:
        final Vector<Intersection> intersections = new Vector<Intersection>(10,10);

        final Path entrancePath = new Path (EntranceExit, "Entrance Path");
        final Path mainPath_1 = new Path (Elevators, "First Floor Access Path 1");
        final Path mainPath_2 = new Path (Elevators, "First Floor Access Path 2");
        final Path mainPath_3 = new Path (RestRooms, "First Floor Access Path 3");
        final Path studyPath_1 = new Path (StudySpace, "Study Path 1");
        final Path studyPath_2 = new Path (StudySpace, "Study Path 2");
        final Path studyPath_3 = new Path (BookStacks, "Study Path 3");
        final Path studyPath_4 = new Path (StudySpace, "Study Path 4");
        final Path studyPath_5 = new Path (BookStacks, "Study Path 5");
        final Path studyPath_6 = new Path (BookStacks, "Study Path 6");
        final Path emergencyPath_Access = new Path (EntranceExit, "Emergency Path Access");
        final Path emergencyPath_1 = new Path (EntranceExit, "Emergency Path 1");
        final Path emergencyPath_2 = new Path (EntranceExit, "Emergency Path 2");
        final Path emergencyPath_3 = new Path (EntranceExit, "Emergency Path 3");

        //Intersections between paths:
        Intersection entrance = new Intersection("Evans Entrance", Arrays.asList(entrancePath));
        Intersection lobby = new Intersection("Evans Lobby",Arrays.asList(entrancePath, mainPath_1, studyPath_1));
        Intersection elevator_1_floor_1 = new Intersection("Elevators",Arrays.asList(mainPath_1, mainPath_2));
        Intersection elevator_2_floor_1 = new Intersection("Elevators",Arrays.asList(mainPath_2, mainPath_3));
        Intersection studyArea_1 = new Intersection("First Study Area",Arrays.asList(studyPath_1, studyPath_2));
        Intersection studyArea_2 = new Intersection("Second Study Area",Arrays.asList(studyPath_2, studyPath_3, emergencyPath_3));
        Intersection studyArea_3 = new Intersection("Third Study Area",Arrays.asList(studyPath_4, emergencyPath_Access));
        Intersection shelves_BestSellers = new Intersection("Best Seller Bookshelves",Arrays.asList(studyPath_3, studyPath_5, studyPath_6));
        Intersection studyLobby = new Intersection("Study Space Lobby",Arrays.asList(mainPath_3, studyPath_6, studyPath_4));
        Intersection emergencyHub_1 = new Intersection("Emergency Exit Access",Arrays.asList(emergencyPath_Access, emergencyPath_1));
        Intersection emergencyHub_2 = new Intersection("Emergency Exit Access",Arrays.asList(emergencyPath_Access, emergencyPath_2));

        intersections.add(entrance);
        intersections.add(lobby);
        intersections.add(elevator_1_floor_1);
        intersections.add(elevator_2_floor_1);
        intersections.add(studyArea_1);
        intersections.add(studyArea_2);
        intersections.add(studyArea_3);
        intersections.add(shelves_BestSellers);
        intersections.add(studyLobby);
        intersections.add(emergencyHub_1);
        intersections.add(emergencyHub_2);
        //End Generations

        whereButtonHandle = new WhereButtonHandle();
        whereButtonHandle.setPreviousPath (entrancePath);
        whereButtonHandle.setPreviousIntersection (intersections.elementAt(0));

        //Assigns a reference to this activities context to the context variable
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMapIfNeeded();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.616788, -96.339240),14.0f));

        //Establishes a location listener to get the current GPS location of the device
        final LocationListener locationListener = new MyLocationListener();
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, .001f, locationListener);

        AutoCompleteTextView searchBar = (AutoCompleteTextView) findViewById(R.id.searchBar);
        String[] Prompts={"Evans", "MSC", "Annex", "Commons", "WCL", "MPHY", "RDMC", "HRBB", "CHEN", "SCC", "HELD"};
        searchBar.setThreshold(1);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,Prompts);

        searchBar.setAdapter(adapter);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });

        //set the buttons on click listener
        whereButton = (ImageButton) findViewById(R.id.whereButton);
        final Context c = this;
        whereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toSpeak = whereButtonUpdate(whereButtonHandle, intersections, entrancePath, mainPath_1, mainPath_2, mainPath_3, studyPath_4);

                System.out.println("ToSpeak: " + toSpeak);
                Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        whereButton.getBackground().setColorFilter(0xFF0000FF, PorterDuff.Mode.MULTIPLY);

        //set the buttons on click listener
        updateButton = (ImageButton) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = "In the future, this would be used for relaying information about their present position.";
                int duration = Toast.LENGTH_LONG;
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                Toast toast = Toast.makeText(MapsActivity.getContext(), toSpeak, duration);
                toast.show();
            }
        });
        updateButton.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);

        //set the buttons on click listener
        emergencyExitButton = (ImageButton) findViewById(R.id.emergencyExitButton);
        emergencyExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = "In the future, this would be used for detecting and notifying the user about emergency exits.";
                int duration = Toast.LENGTH_LONG;
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                Toast toast = Toast.makeText(MapsActivity.getContext(), toSpeak, duration);
                toast.show();
            }
        });
        emergencyExitButton.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);

        infoButton = (ImageButton) findViewById(R.id.settings);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = "Developed for TAMU Diversity Accesibility Hackathon 02/18/2017 \n Uses Icons from Icons8 https://icons8.com \n Under Creative commons Attribution-NoDerivs 3.0 Unported";
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_LONG).show();
            }
        });

        //set the buttons on click listener
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int duration = Toast.LENGTH_LONG;
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        //button.setPadding(60, 60, 60, 60);
        //button.setAdjustViewBounds(true);
        //button.setScaleType(ImageView.ScaleType.FIT_CENTER);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    public static Context getContext() {
        return context;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private String whereButtonUpdate(WhereButtonHandle whereButtonHandle, Vector<Intersection> intersections, Path entrancePath, Path mainPath_1, Path mainPath_2, Path mainPath_3, Path studyPath_4){

        Intersection currentIntersection = whereButtonHandle.getPreviousIntersection();
        Path previousPath = whereButtonHandle.getPreviousPath();
        String toSpeak = "You are currently at " + currentIntersection.toString();

        if(currentIntersection.paths.toArray().length >= 2) {
            toSpeak += " which contains the ";
        }

        for (int i = 0; i < currentIntersection.paths.toArray().length - 1; i++) {
            if(currentIntersection.paths.toArray()[i] != previousPath)
                toSpeak += currentIntersection.paths.toArray()[i].toString() + " as well as ";
        }
        if(currentIntersection.paths.toArray()[currentIntersection.paths.toArray().length - 1] != previousPath) {
            toSpeak += currentIntersection.paths.toArray()[currentIntersection.paths.toArray().length - 1].toString() + ".";
        }

        if(previousPath == entrancePath){
            whereButtonHandle.setPreviousPath(mainPath_1);
            whereButtonHandle.setPreviousIntersection(intersections.elementAt(1));
        } else if (previousPath == mainPath_1){
            whereButtonHandle.setPreviousPath(mainPath_2);
            whereButtonHandle.setPreviousIntersection(intersections.elementAt(2));
        } else if (previousPath == mainPath_2){
            whereButtonHandle.setPreviousPath(mainPath_3);
            whereButtonHandle.setPreviousIntersection(intersections.elementAt(3));
        } else if (previousPath == mainPath_3){
            whereButtonHandle.setPreviousPath(studyPath_4);
            whereButtonHandle.setPreviousIntersection(intersections.elementAt(8));
        } else {
            whereButtonHandle.setPreviousPath(entrancePath);
            whereButtonHandle.setPreviousIntersection(intersections.elementAt(0));
        }

        return toSpeak;
    }
}
