package com.koshka.origami.fragments.main.map;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.DatabaseRefUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition.Builder;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;
import com.koshka.origami.R;
import com.koshka.origami.activites.main.MainActivity;
import com.koshka.origami.google.maps.OrigamiMarker;
import com.koshka.origami.google.maps.OrigamiMarkerRenderer;
import com.koshka.origami.model.SimpleTextOrigami;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 8/27/16.
 */
public class OrigamiMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, ValueEventListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowLongClickListener, SensorEventListener {

    private static final String TAG = "OrigamiMap";

    @BindView(R.id.map_sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;

    @BindView(R.id.my_location_button_map)
    TextView myLocationButton;

    @BindView(R.id.map_settings_button)
    TextView mapSettingsButton;

    @BindView(R.id.location_text_view)
    TextView pickedLocation;

    @BindView(R.id.editOrigamiText)
    EditText editOrigamiText;

    @BindView(R.id.origami_button_map)
    TextView origamiButton;

    private Place place;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;


    private Marker myLocationMarker; //My locationMarker
    private ClusterManager<OrigamiMarker> origamiMarkerClusterManager;

    //Firebase stuff
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference myLocationRef;
    private ValueEventListener followListener;
    private ValueEventListener notFollowListener;


    // Google maps circles
    private Circle origamiCircle;
    private Circle userCircle;

    private double mDeclination;
    private double bearing = 0;
    private LatLng currentUserLatLng;

    private boolean isInFollowMeMode = false;

    private List<ValueEventListener> valueEventListenerList;


    //Ref for public origamis
    private DatabaseReference publicOrigamiRef;

    private List<Place> placeList = new ArrayList<>();

    private MainActivity activity;

    private SensorManager mSensorManager;

    private float[] mAccelerometerReading = new float[3];
    private float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private boolean firstFollowPress = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_origami_map, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
 /*       buildAndConnectGoogleApiClient();
        initFirebase();
        activity = (MainActivity) getActivity();*/

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     FragmentManager fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {

            GoogleMapOptions options = new GoogleMapOptions();
            options.compassEnabled(false);
            options.mapToolbarEnabled(false);
            fragment = SupportMapFragment.newInstance(options);
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
        }

        fragment.getMapAsync(this);


        final Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/origamibats.ttf");
        final Typeface font2 = Typeface.createFromAsset(getContext().getAssets(), "fonts/heydings_icons.ttf");

        if (font != null) {
            origamiButton.setTypeface(font);
        }

        if (font2 != null) {
            myLocationButton.setTypeface(font2);
            mapSettingsButton.setTypeface(font2);
        }
    /*
        fragment.getMapAsync(this);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);


*/

    }

    private void initFirebase() {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        publicOrigamiRef = DatabaseRefUtil.getPublicOrigamiRef();
        publicOrigamiRef.addValueEventListener(this);

        String uid = currentUser.getUid();
        myLocationRef = DatabaseRefUtil.getUserCurrentLocationRef(uid);


    }

    private void buildAndConnectGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mGoogleApiClient.connect();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setBuildingsEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
        setUpClusterer();

        mMap.addMarker(new MarkerOptions()
        .position(new LatLng(40, 43))
                .title("My custom marker"));

    }

    @OnClick(R.id.map_settings_button)
    public void settingsButtonPressed() {
        slidingUpPanelLayout.setAnchorPoint(0.3f);
        slidingUpPanelLayout.setShadowHeight(0);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }


    @OnClick(R.id.my_location_button_map)
    public void followMe(View view) {

        /*registerSensorListeners();
        final Resources res = getResources();
        if (!isInFollowMeMode) {
            if (notFollowListener != null) {
                myLocationRef.removeEventListener(notFollowListener);
            }
            isInFollowMeMode = true;
            myLocationButton.setTextColor(res.getColor(R.color.material_red_a2001));
            followListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (myLocationMarker != null) {
                        myLocationMarker.remove();
                    }
                    if (userCircle != null) {
                        userCircle.remove();
                    }
                    final Coordinate coordinate = dataSnapshot.getValue(Coordinate.class);
                    if (coordinate != null) {

                        currentUserLatLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
                        myLocationMarker = mMap.addMarker(new MarkerOptions().position(currentUserLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));

                        userCircle = mMap.addCircle(new CircleOptions()
                                .center(myLocationMarker.getPosition())
                                .radius(1)
                                .strokeColor(res.getColor(R.color.transparent6))
                                .fillColor(res.getColor(R.color.transparent6)));


                        mMap.setMaxZoomPreference(20);
                        mMap.setMinZoomPreference(15);

                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                new Builder()
                                        .target(currentUserLatLng)
                                        .tilt(50)
                                        .zoom(17)
                                        .build()));
                        firstFollowPress = false;

                    } else {
                        //
                        //Notify user he should activate location
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            myLocationRef.addValueEventListener(followListener);

        } else {
            unregisterSensorListeners();
            if (followListener != null) {
                myLocationRef.removeEventListener(followListener);
            }
            isInFollowMeMode = false;
            myLocationButton.setTextColor(res.getColor(R.color.white));
            firstFollowPress = true;
            mMap.animateCamera(CameraUpdateFactory.zoomOut());

            mMap.setMaxZoomPreference(25);
            mMap.setMinZoomPreference(1);

            notFollowListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (myLocationMarker != null) {
                        myLocationMarker.remove();
                    }
                    final Coordinate coordinate = dataSnapshot.getValue(Coordinate.class);
                    if (coordinate != null) {
                        LatLng latLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
                        myLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));
                    } else {
                        //nothing
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            myLocationRef.addValueEventListener(notFollowListener);
        }
*/
    }


    private void animateFollowCamera(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new Builder()
                        .target(latLng)
                        .tilt(50)
                        .bearing((float) bearing)
                        .zoom(mMap.getCameraPosition().zoom)
                        .build()));

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //ON NEW ORIGAMI ADDED TO DB
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            final SimpleTextOrigami post = postSnapshot.getValue(SimpleTextOrigami.class);
            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
            Places.GeoDataApi.getPlaceById(mGoogleApiClient, post.getPlaceId())
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                final Place myPlace = places.get(0);
                                placeList.add(myPlace);
                                myLocationMarker = mMap.addMarker(new MarkerOptions().position(myPlace.getLatLng()).title(post.getText()).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                      /*          addOrigamiMarker(myPlace.getLatLng(), post.getText());*/
                            } else {
                            }
                            places.release();
                        }
                    });
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
/*
    @Override
    public void onPause() {
        super.onPause();

        mGoogleApiClient.disconnect();

        // Don't receive any more updates from either sensor.
        mSensorManager.unregisterListener(this);
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }*/

    @Override
    public void onMapLongClick(LatLng latLng) {
        myLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        pickedLocation.setText(latLng.latitude + " ," + latLng.longitude);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (origamiCircle != null) {
            origamiCircle.remove();
        }
        Resources res = getResources();
        origamiCircle = mMap.addCircle(new CircleOptions()
                .center(marker.getPosition())
                .radius(50)
                .strokeColor(res.getColor(R.color.transparent6))
                .fillColor(res.getColor(R.color.transparent6)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));

        return false;
    }

   /* @Override
    public void onInfoWindowClick(Marker marker) {
        boolean userIsInOrigamiRange = false;

        MainActivity activity = (MainActivity) getActivity();

        int RANGE = 50; //Range in meters
        Location currentUserLocation = activity.getUserLocation();
        LatLng origamiLocation = marker.getPosition();

        double currentUserLocationLat;
        double currentUserLocationLong;
        double currentUserLocationAlt;
        if (currentUserLocation != null) {

            currentUserLocationLat = currentUserLocation.getLatitude();
            currentUserLocationLong = currentUserLocation.getLongitude();
            currentUserLocationAlt = currentUserLocation.getAltitude();

        } else {
            currentUserLocationLat = 0;
            currentUserLocationAlt = 0;
            currentUserLocationLong = 0;
        }

        double currentOrigamiLocationLat = origamiLocation.latitude;
        double currentOrigamiLocationLong = origamiLocation.longitude;


        double distance = HaversineAlgorithm.getHaversineDistance(currentUserLocationLat, currentOrigamiLocationLat, currentUserLocationLong, currentOrigamiLocationLong, 0, 0);

        Toast.makeText(getActivity(), "" + distance, Toast.LENGTH_SHORT);

        userIsInOrigamiRange = distance < RANGE;

        if (userIsInOrigamiRange) {


            Intent intent = new Intent(getContext(), OpenedOrigamiActivity.class);

            String string = marker.getTitle();
            int spacePos = string.indexOf(" ");
            if (spacePos > 0) {
                String youString = string.substring(0, spacePos);
                intent.putExtra("username", "IT WORKS!!!");
            }

            startActivity(intent);
        } else {

            final SweetAlertDialog successDialog = new SweetAlertDialog(getActivity())
                    .setTitleText("Sorry!")
                    .setContentText("You are not around this origami")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    });
            successDialog.show();
        }
    }*/


    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {
        //Nothing to do with this yet.
    }

    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        origamiMarkerClusterManager = new ClusterManager<OrigamiMarker>(getContext(), mMap);


        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(origamiMarkerClusterManager);

        origamiMarkerClusterManager.setRenderer(new OrigamiMarkerRenderer(getContext(), mMap, origamiMarkerClusterManager));
    }

    private void addOrigamiMarker(LatLng latlng, String title) {

        OrigamiMarker offsetItem = new OrigamiMarker(latlng.latitude, latlng.longitude);
        offsetItem.setOrigamiTitle(title);
        origamiMarkerClusterManager.addItem(offsetItem);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelerometerReading = sensorEvent.values;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mMagnetometerReading = sensorEvent.values;
        }
        if (mAccelerometerReading != null && mMagnetometerReading != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mAccelerometerReading,
                    mMagnetometerReading);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimut = orientation[0];
                bearing = (Math.toDegrees(azimut) + 360) % 360;
                if (currentUserLatLng != null) {
                } else {
                    LatLng latLng = new LatLng(0, 0);
                }

            }
        }


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
/*
    private void registerSensorListeners() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_UI);
    }*/

    private void unregisterSensorListeners() {
        // Don't receive any more updates from either sensor.
        mSensorManager.unregisterListener(this);
    }

    public GoogleMap getMap() { return  mMap; }

}
