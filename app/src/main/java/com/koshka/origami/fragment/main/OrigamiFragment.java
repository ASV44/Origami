package com.koshka.origami.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koshka.origami.activity.main.MapsActivity;
import com.koshka.origami.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 7/20/16.
 */
public class OrigamiFragment extends Fragment {


    @BindView(R.id.origami_list)
    ListView origami_list;

    @BindView(R.id.send_origami)
    Button sendOrigamiButton;


    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    private DatabaseReference mRef;
    private DatabaseReference mOrigamiRef;
    private FirebaseAuth mAuth;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_origami, container, false);
        ButterKnife.bind(this,view);
        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrayList = new ArrayList<String>();


        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simplerow, arrayList);

        origami_list.setAdapter(adapter);

        mOrigamiRef = mRef.child("users").child(mAuth.getCurrentUser().getUid()).child("origamiList");

        mOrigamiRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                arrayList.add(dataSnapshot.child("text").getValue().toString());
                adapter.notifyDataSetChanged();
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

    @OnClick(R.id.send_origami)
    public void sendOrigami(View view) {

        startActivity(MapsActivity.createIntent(getActivity()));


    }

    }
