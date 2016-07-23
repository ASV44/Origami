package com.koshka.origami.activity.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.koshka.origami.R;
import com.koshka.origami.model.Coordinate;
import com.koshka.origami.model.FirstOrigami;
import com.koshka.origami.model.Friend;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";

    @BindView(R.id.coordinates_text_view)
    TextView coordinates;

    @BindView(R.id.send_origami1)
    Button sendOrigami;

    @BindView(R.id.friend_email1)
    EditText friendEmail;

    @BindView(R.id.editTextOrigami)
    EditText origamiText;

    Friend friend = null;


    private GoogleMap mMap;
    private MarkerOptions marker;
    private LatLng origamiPosition = new LatLng(-34, 151);
    private DatabaseReference mFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ButterKnife.bind(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        marker = new MarkerOptions().position(origamiPosition).title("Origami position").draggable(true);
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origamiPosition));
        coordinates.setText(marker.getPosition().toString());

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.position(latLng);
                mMap.clear();
                mMap.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                coordinates.setText(marker.getPosition().toString());
            }
        });


    }

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, MapsActivity.class);
        return in;
    }

    @OnClick(R.id.send_origami1)
    public void sendOrigami(View view){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mUserRef = mRef.child("users");

        final DatabaseReference mMe = mUserRef.child(mAuth.getCurrentUser().getUid()).child("friendList");
        final DatabaseReference mMyOrigami = mUserRef.child(mAuth.getCurrentUser().getUid()).child("origamiList");

        final FirstOrigami origami = new FirstOrigami();
        origami.setAuthorUid(mAuth.getCurrentUser().getUid());
        origami.setOrigamiName("Just a test origami");
        origami.setText(origamiText.getText().toString());
        final Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(marker.getPosition().latitude);
        coordinate.setLongitude(marker.getPosition().longitude);
        origami.setOrigamiCoordinate(coordinate);

        final Query query = mMe.orderByChild("email").equalTo(friendEmail.getText().toString());
        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    friend = dataSnapshot.getValue(Friend.class);
                    mMyOrigami.push().setValue(origami);
                    mFriend = mUserRef.child(friend.getUid()).child("origamiList");
                    mFriend.push().setValue(origami);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
