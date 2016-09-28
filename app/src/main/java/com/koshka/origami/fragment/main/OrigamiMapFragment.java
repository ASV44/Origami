package com.koshka.origami.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.DatabaseRefUtil;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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
import com.koshka.origami.DistanceCalculator;
import com.koshka.origami.R;
import com.koshka.origami.activity.main.MainActivity;
import com.koshka.origami.activity.origami.CreatePublicOrigamiActivity;
import com.koshka.origami.activity.origami.OpenedOrigamiActivity;
import com.koshka.origami.google_maps.OrigamiMarker;
import com.koshka.origami.google_maps.OrigamiMarkerRenderer;
import com.koshka.origami.model.Coordinate;
import com.koshka.origami.model.SimpleTextOrigami;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by imuntean on 8/27/16.
 */
public class OrigamiMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, ValueEventListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowLongClickListener, SensorEventListener {

    @BindView(R.id.multiple_actions_map)
    FloatingActionsMenu plusButton;

    @BindView(R.id.follow_me_button)
    TextView followMeTextButton;

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
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buildAndConnectGoogleApiClient();
        initFirebase();
        activity = (MainActivity) getActivity();

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
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        TextView textView = (TextView) getActivity().findViewById(R.id.origami_button_map);

        final Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/origamibats.ttf");

        if (font != null){
            textView.setTypeface(font);
        }

    }

    private void initFirebase(){

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        publicOrigamiRef = DatabaseRefUtil.getPublicOrigamiRef();
        publicOrigamiRef.addValueEventListener(this);

        String uid = currentUser.getUid();
        myLocationRef = DatabaseRefUtil.getUserCurrentLocationRef(uid);


    }

    private void buildAndConnectGoogleApiClient(){

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        mGoogleApiClient.connect();

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setBuildingsEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
        setUpClusterer();

    }


  /*  @Override
    public void onDestroy() {
        super.onDestroy();
        //REMOVE LISTENERES
        FirebaseDbUtils.removeEventListener(publicOrigamiRef,this);

    }*/

    @OnClick(R.id.origami_button_map)
    public void createOrigami(View view) {

        startActivity(new Intent(getActivity(), CreatePublicOrigamiActivity.class));
        plusButton.collapse();
    }

    @OnClick(R.id.follow_me_button)
    public void followMe(View view) {

        registerSensorListeners();
        final Resources res = getResources();
        if (!isInFollowMeMode) {
            if (notFollowListener != null) {
                myLocationRef.removeEventListener(notFollowListener);
            }
            isInFollowMeMode = true;
            followMeTextButton.setText("Exit follow mode");
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
                                        .bearing((float) bearing)
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
            followMeTextButton.setText("Follow mode");
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
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                new Builder()
                                        .target(latLng)
                                        .build()));
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
                                addOrigamiMarker(myPlace.getLatLng(), post.getText());
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

    @Override
    public void onPause() {
        super.onPause();

        mGoogleApiClient.stopAutoManage(getActivity());
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
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        myLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        //SHOULD APPEAR SOME FRAGMENT FOR ADDING A NEW ORIGAMI RIGHT THERE
    }


    @Override
    public void onResume() {
        super.onResume();

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

    @Override
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


        double distance = DistanceCalculator.getHaversineDistance(currentUserLocationLat, currentOrigamiLocationLat, currentUserLocationLong, currentOrigamiLocationLong, 0, 0);

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
    }

    @Override
    public void onCameraMove() {

  /*      if (previousZoomLevel != mMap.getCameraPosition().zoom) {
            isZooming = true;
            if (followListener != null) {
                myLocationRef.removeEventListener(followListener);
            }
            isInFollowMeMode = false;
            followMeTextButton.setText("Follow mode");
        }

        previousZoomLevel = mMap.getCameraPosition().zoom;*/
    }

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

    private void registerSensorListeners() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_UI);
    }

    private void unregisterSensorListeners() {
        // Don't receive any more updates from either sensor.
        mSensorManager.unregisterListener(this);
    }
}
