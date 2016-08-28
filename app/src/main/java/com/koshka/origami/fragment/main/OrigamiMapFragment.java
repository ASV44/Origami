package com.koshka.origami.fragment.main;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.koshka.origami.R;
import com.koshka.origami.activity.friends.AddFriendActivity;
import com.koshka.origami.activity.friends.FriendProfileActivity;
import com.koshka.origami.activity.main.MainActivity;
import com.koshka.origami.activity.origami.CreatePublicOrigamiActivity;
import com.koshka.origami.activity.origami.OpenedOrigamiActivity;
import com.koshka.origami.model.Coordinate;
import com.koshka.origami.model.SimpleTextOrigami;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by imuntean on 8/27/16.
 */
public class OrigamiMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, ValueEventListener {

    @BindView(R.id.multiple_actions_map)
    FloatingActionsMenu plusButton;

    @BindView(R.id.follow_me_button)
    TextView followMeTextButton;


    private GoogleApiClient mGoogleApiClient;

    private GoogleMap mMap;


    private  Marker marker; //My locationMarker

    private boolean isInFollowMeMode = false;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref = DatabaseRefUtil.getmRef();
    private DatabaseReference myLocationRef =ref.child("user_current_location").child(currentUser.getUid());
    private ValueEventListener followListener;
    private ValueEventListener notFollowListener;


    private  Circle circle;
    private Circle userCircle;
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

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mOrigamiPlaceIds = DatabaseRefUtil.getmRef().child("public_origami");
        mOrigamiPlaceIds.addValueEventListener(this);

        mGoogleApiClient.connect();

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

    }

    @OnClick(R.id.create_origami_button)
    public void createOrigami(View view){

        startActivity(new Intent(getActivity(), CreatePublicOrigamiActivity.class));
        plusButton.collapse();
    }

    @OnClick(R.id.follow_me_button)
    public void followMe(View view){

        final  Resources res = getResources();
        if(!isInFollowMeMode){
            if (notFollowListener != null){
                myLocationRef.removeEventListener(notFollowListener);
            }
            isInFollowMeMode = true;
            followMeTextButton.setText("Exit follow mode");
            followListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (marker != null){
                        marker.remove();
                    }
                    if(userCircle != null){
                        userCircle.remove();
                    }
                    final Coordinate coordinate = dataSnapshot.getValue(Coordinate.class);
                    LatLng latLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
                    marker = mMap.addMarker( new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));

                    userCircle= mMap.addCircle(new CircleOptions()
                            .center(marker.getPosition())
                            .radius(50)
                            .strokeColor(res.getColor(R.color.transparent6))
                            .fillColor(res.getColor(R.color.transparent6)));


                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            myLocationRef.addValueEventListener(followListener);

        } else {
            if (followListener != null){
                myLocationRef.removeEventListener(followListener);
            }
            isInFollowMeMode = false;
            followMeTextButton.setText("Follow mode");
            mMap.animateCamera(CameraUpdateFactory.zoomOut());

            notFollowListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (marker != null){
                        marker.remove();
                    }
                    final Coordinate coordinate = dataSnapshot.getValue(Coordinate.class);
                    LatLng latLng = new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
                    marker = mMap.addMarker( new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            myLocationRef.addValueEventListener(notFollowListener);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setBuildingsEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                marker = mMap.addMarker( new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (circle != null){
                    circle.remove();
                }
                Resources res = getResources();
                circle = mMap.addCircle(new CircleOptions()
                        .center(marker.getPosition())
                        .radius(50)
                        .strokeColor(res.getColor(R.color.transparent6))
                        .fillColor(res.getColor(R.color.transparent6)));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));

                return false;
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {

                boolean userIsInOrigamiRange = false;

                MainActivity activity = (MainActivity) getActivity();

                int RANGE = 50; //Range in meters
                Location currentUserLocation = activity.getUserLocation();
                LatLng origamiLocation = arg0.getPosition();

                double currentUserLocationLat;
                double currentUserLocationLong;
                double currentUserLocationAlt;
                if (currentUserLocation != null){

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

                double distance = distance(currentUserLocationLat, currentOrigamiLocationLat, currentUserLocationLong, currentOrigamiLocationLong, 0, 0 );

                Toast.makeText(getActivity(), ""+distance, Toast.LENGTH_SHORT);

                userIsInOrigamiRange = distance < RANGE;

                if(userIsInOrigamiRange) {


                    Intent intent = new Intent(getContext(), OpenedOrigamiActivity.class);

                    String string = arg0.getTitle();
                    int spacePos = string.indexOf(" ");
                    if (spacePos > 0) {
                        String youString = string.substring(0, spacePos);
                        intent.putExtra("username", "IT WORKS!!!");
                    }

                    startActivity(intent);
                } else {

                    final SweetAlertDialog successDialog =new SweetAlertDialog(getActivity())
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
        });

    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    private double distanceBetweenTwoCoordinates(double lat1, double long1, double lat2, double long2){
        double φ1 = lat1; //
        double φ2 = lat2;
        double Δφ = lat2-lat1;
        double Δλ = long2-long1;
        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double R = 6371e3; // metres

        double d = R * c;

        return d;

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            final SimpleTextOrigami post = postSnapshot.getValue(SimpleTextOrigami.class);
            if(!mGoogleApiClient.isConnected()){
                mGoogleApiClient.connect();
            }
            Places.GeoDataApi.getPlaceById(mGoogleApiClient, post.getPlaceId())
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                final Place myPlace = places.get(0);
                                MarkerOptions marker = new MarkerOptions().position(myPlace.getLatLng()).title(post.getCreatedBy()+" " + post.getText()).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));

                                mMap.addMarker(marker);
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
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }



}