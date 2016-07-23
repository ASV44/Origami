package com.koshka.origami.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.koshka.origami.activity.main.MainActivity;
import com.koshka.origami.R;
import com.koshka.origami.model.Friend;
import com.koshka.origami.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 7/20/16.
 */
public class FriendsFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();


    @BindView(R.id.friendListView)
    ListView friendListView;

    @BindView(R.id.friendEmail)
    EditText friendEmail;

    @BindView(R.id.add_friend_button)
    Button addFriendButton;

    @BindView(R.id.invite_button)
    Button inviteFriendButton;

    @BindView(R.id.delete_friend_button)
    Button deleteFriendButton;

    @BindView(R.id.userResult)
    TextView userResult;


    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    private DatabaseReference mRef;
    private DatabaseReference mFriendsRef;
    private FirebaseAuth mAuth;

    private String friendUid ;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
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

        friendListView.setAdapter(adapter);

        mFriendsRef = mRef.child("users").child(mAuth.getCurrentUser().getUid()).child("friendList");

     /*   mFriendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<Friend> friends =  (ArrayList<Friend>) snapshot.getValue();

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Friend friend = postSnapshot.getValue(Friend.class);
                    arrayList.add(friend.getDisplayName());
                }
                adapter.notifyDataSetChanged();

            }
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });*/

        mFriendsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                arrayList.add(dataSnapshot.child("displayName").getValue().toString());
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

    @OnClick(R.id.add_friend_button)
    public void addFriend(View view) {

        DatabaseReference mUserRef = mRef.child("users");
        final DatabaseReference mMe = mUserRef.child(mAuth.getCurrentUser().getUid()).child("friendList");

        final Query query = mUserRef.orderByChild("email").equalTo(friendEmail.getText().toString());

        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null){
                    User user = dataSnapshot.getValue(User.class);
                    Friend friend = new Friend();
                    friend.setDisplayName(user.getDisplayName());
                    friend.setEmail(user.getEmail());
                    friend.setUid(user.getUid());
                    mMe.push().setValue(friend);
                    DatabaseReference mUserRef = mRef.child("users").child(user.getUid()).child("friendList");
                    Friend friend2 = new Friend();
                    friend2.setUid(mAuth.getCurrentUser().getUid());
                    friend2.setEmail(mAuth.getCurrentUser().getEmail());
                    friend2.setDisplayName(mAuth.getCurrentUser().getDisplayName());
                    mUserRef.push().setValue(friend2);




                } else {

                    userResult.setText("Doesn't exist");
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

    @OnClick(R.id.delete_friend_button)
    public void deleteFriend(View view) {
        String friendEmailString = friendEmail.getText().toString();

        DatabaseReference mUserRef = mRef.child("users");
        mUserRef = mRef.child(mAuth.getCurrentUser().getUid()).child("friendList");
/*

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        mUserRef = mRef.child("users").child(mAuth.getCurrentUser().getUid());
        String user = mUserRef.getKey();
        if (user != null){
            mUserRef.removeValue();
        }

*/

    }

    @OnClick(R.id.invite_button)
    public void inviteFriend(View view){



    }


}
