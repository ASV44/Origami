package com.koshka.origami.fragment.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.DatabaseRefUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.R;
import com.koshka.origami.model.Friend;
import com.koshka.origami.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 7/20/16.
 */
public class FriendsFragment extends Fragment {

    @BindView(R.id.search_friend_button)
    Button searchFriendButton;

    @BindView(R.id.user_email_nickname)
    EditText input;

    @BindView(R.id.friends_recycler_view)
    RecyclerView friendsRecyclerView;

    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<Friend, FriendHolder> mRecyclerViewAdapter;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(false);
        friendsRecyclerView.setHasFixedSize(false);
        friendsRecyclerView.setLayoutManager(mManager);

    }

    @OnClick(R.id.search_friend_button)
    public void searchFriend(View view) {

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mMe = DatabaseRefUtil.getUserRefByUid(mAuth.getCurrentUser().getUid());
        final DatabaseReference mMyFriends = mMe.child("friendList");
        // Creating alert Dialog with one Button
        final AlertDialog.Builder alertUserInfoDialog = new AlertDialog.Builder(getActivity());

        // Creating alert Dialog with one Button
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        String userInput = input.getText().toString();

        if (userInput != null && !userInput.isEmpty()) {
            boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(userInput).matches();
            if (isEmail) {
                String email = userInput;
                if (email != null && !email.isEmpty()) {
                    final Query query = DatabaseRefUtil.getFindUserByEmailQuery(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                query.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        final User user = dataSnapshot.getValue(User.class);
                                        if (!user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {

                                            alertUserInfoDialog.setTitle("Is that him?");
                                            if (user.getDisplayName() == null) {
                                                alertUserInfoDialog.setMessage(user.getNickname() + ", " + user.getEmail() + "," + user.getUid());
                                            } else {
                                                alertUserInfoDialog.setMessage(user.getDisplayName() + ", " + user.getEmail() + "," + user.getUid());
                                            }
                                            alertUserInfoDialog.setPositiveButton("Yes, that's him", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Friend friend = new Friend();
                                                    friend.setDisplayName(user.getDisplayName());
                                                    friend.setEmail(user.getEmail());
                                                    friend.setUid(user.getUid());
                                                    friend.setNickname(user.getNickname());
                                                    mMyFriends.push().setValue(friend);
                                                }
                                            });
                                            alertUserInfoDialog.create();
                                            alertUserInfoDialog.show();
                                        } else {

                                            alertUserInfoDialog.setMessage("That seems to be you. :)");
                                            alertUserInfoDialog.create();
                                            alertUserInfoDialog.show();
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
                            } else {
                                alertUserInfoDialog.setTitle("No such user in Origami");
                                alertUserInfoDialog.setMessage("Do you want to invite " + input.getText().toString() + "?");
                                alertUserInfoDialog.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                alertUserInfoDialog.create();
                                alertUserInfoDialog.show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                alertDialog.create();
                alertDialog.show();
            } else {
                String nickname = userInput;
                final Query query = DatabaseRefUtil.getFindUserByNicknameQuery(nickname);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            query.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    final User user = dataSnapshot.getValue(User.class);
                                    if (!user.getNickname().equals(mAuth.getCurrentUser().getDisplayName())) {
                                        alertUserInfoDialog.setTitle("Is that him?");
                                        if (user.getDisplayName() == null) {
                                            alertUserInfoDialog.setMessage(user.getNickname() + ", " + user.getEmail() + "," + user.getUid());
                                        } else {
                                            alertUserInfoDialog.setMessage(user.getDisplayName() + ", " + user.getEmail() + "," + user.getUid());
                                        }
                                        alertUserInfoDialog.setPositiveButton("Yes, that's him", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Friend friend = new Friend();
                                                friend.setDisplayName(user.getDisplayName());
                                                friend.setEmail(user.getEmail());
                                                friend.setUid(user.getUid());
                                                friend.setNickname(user.getNickname());
                                                mMyFriends.push().setValue(friend);
                                            }
                                        });
                                        alertUserInfoDialog.create();
                                        alertUserInfoDialog.show();
                                    } else {

                                        alertUserInfoDialog.setMessage("That seems to be you. :)");
                                        alertUserInfoDialog.create();
                                        alertUserInfoDialog.show();
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
                        } else {
                            alertUserInfoDialog.setTitle("No such user in Origami");
                            alertUserInfoDialog.setMessage("Do you want to invite " + input.getText().toString() + "?");
                            alertUserInfoDialog.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            alertUserInfoDialog.create();
                            alertUserInfoDialog.show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            alertDialog.create();
            alertDialog.show();

            }
        }

    private void attachRecyclerViewAdapter() {
        Query lastFifty = DatabaseRefUtil.getmMyFriendsRef(mAuth.getCurrentUser().getUid()).limitToLast(50);
        mRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friend, FriendHolder>(
                Friend.class, R.layout.friend_layout, FriendHolder.class, lastFifty) {

            @Override
            public void populateViewHolder(FriendHolder friendView, Friend friend, int position) {
                friendView.setFriendNickname(friend.getNickname());
                friendView.setFriendEmail(friend.getEmail());
                friendView.setFriendPicture(getContext(), "http://209.132.179.3/uploads/big/2df7cab0a2305743db864ef472b6c8b9.png");

            }
        };
        // Scroll to bottom on new messages
        mRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mManager.smoothScrollToPosition(friendsRecyclerView, null, mRecyclerViewAdapter.getItemCount());
            }
        });

        friendsRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        attachRecyclerViewAdapter();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }
}




        /*private class SampleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public ListData getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_layout, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData item = getItem(position);

            // provide support for selected state
            updateCheckedState(holder, item);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // when the image is clicked, update the selected state
                    ListData data = getItem(position);
                    data.setChecked(!data.isChecked);
                    updateCheckedState(holder, data);
                }
            });
            holder.textView.setText(item.data);

            return convertView;
        }

        private void updateCheckedState(ViewHolder holder, ListData item) {
            if (item.isChecked) {
                holder.imageView.setImageDrawable(mDrawableBuilder.build(" ", 0xff616161));
                holder.view.setBackgroundColor(HIGHLIGHT_COLOR);
                holder.checkIcon.setVisibility(View.VISIBLE);
            } else {
                TextDrawable drawable = mDrawableBuilder.build(String.valueOf(item.data.charAt(0)), mColorGenerator.getColor(item.data));
                holder.imageView.setImageDrawable(drawable);
                holder.view.setBackgroundColor(Color.TRANSPARENT);
                holder.checkIcon.setVisibility(View.GONE);
            }
        }*/





