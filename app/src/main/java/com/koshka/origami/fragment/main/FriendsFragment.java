package com.koshka.origami.fragment.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.ui.TaskFailureLogger;
import com.firebase.ui.database.DatabaseRefUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.R;
import com.koshka.origami.activity.friends.AddFriendActivity;
import com.koshka.origami.activity.friends.FriendProfileActivity;
import com.koshka.origami.activity.friends.InviteFriendActivity;
import com.koshka.origami.model.User;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by imuntean on 7/20/16.
 */
public class FriendsFragment extends Fragment {


    private static final String TAG = "FriendsFragment";
/*

    @BindView(R.id.multiple_actions)
    FloatingActionsMenu floatingMenu;

    @BindView(R.id.friends_recycler_view)
    RecyclerView friendsRecyclerView;
*/

    @BindView(R.id.friends_sliding_layout)
    SlidingUpPanelLayout slidingPaneLayout;

    @BindView(R.id.add_friend_button)
    TextView addFriendButton;

    @BindView(R.id.find_friend_button)
    TextView findFriendButton;

    @BindView(R.id.invite_friend_button)
    TextView inviteFriendButton;

    @BindView(R.id.friends_email_nick_edit_text)
    EditText nicknameEditText;

    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<User, FriendHolder> mRecyclerViewAdapter;

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
/*
        mAuth = FirebaseAuth.getInstance();
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(false);
        friendsRecyclerView.setHasFixedSize(false);
        friendsRecyclerView.setLayoutManager(mManager);

        floatingMenu.collapse();*/


        final Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/heydings_icons.ttf");

        if (font != null){
            addFriendButton.setTypeface(font);
            findFriendButton.setTypeface(font);
            inviteFriendButton.setTypeface(font);
        }

        slidingPaneLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){
                    addFriendButton.setVisibility(View.INVISIBLE);
                    inviteFriendButton.setVisibility(View.INVISIBLE);
                    nicknameEditText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(nicknameEditText, InputMethodManager.SHOW_IMPLICIT);
                }
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    addFriendButton.setVisibility(View.VISIBLE);
                    inviteFriendButton.setVisibility(View.VISIBLE);
                    hideKeyboard();
                }
            }
        });

    }

    @OnClick(R.id.button_add_friend)
    public void buttonAddFriend(){
        hideKeyboard();


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final String string = nicknameEditText.getText().toString();
        boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(string).matches();

        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity())
                .setTitleText(string)
                .setContentText("Here")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                });

        if (!string.equals(mAuth.getCurrentUser().getEmail()) && !string.equals(mAuth.getCurrentUser().getDisplayName())) {
            if (isEmail) {
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                if (string != null && !string.isEmpty()) {
                    firebaseAuth
                            .fetchProvidersForEmail(string)
                            .addOnFailureListener(
                                    new TaskFailureLogger(TAG, "Error fetching providers for email"))
                            .addOnCompleteListener(
                                    new OnCompleteListener<ProviderQueryResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                            if (task.isSuccessful()) {
                                                startEmailHandler(string, task.getResult().getProviders());
                                            }
                                        }
                                    });
                }

            } else {
                startUsernameHandler(string);
            }
        } else {
            dialog.setTitleText("Oops");
            dialog.setContentText("Seems to be you");
            dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            dialog.show();
        }
    }

    private void startUsernameHandler(final String username) {
        //PREPARE DIALOG
        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity())
                .setTitleText(username)
                .setContentText("Here")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                });

        //START QUERY
        final Query findFriendQuery = DatabaseRefUtil.getFindUserByUsernameQuery(username);

        final String lowerCaseUserName = username.toLowerCase();

        DatabaseReference mUsernameRef = DatabaseRefUtil.getUsersRef();
        Query mUsernameQuery = mUsernameRef.orderByChild("username").equalTo(lowerCaseUserName);
        mUsernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //USER EXISTS
                    dialog.setTitleText("Found");
                    dialog.setContentText(username);
                    dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    dialog.setConfirmText("Add");
                    dialog.setCancelText("Cancel");
                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            findFriendQuery.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    final User user = dataSnapshot.getValue(User.class);
                                    DatabaseReference mMeRef = DatabaseRefUtil.getUserFriendsRef(mAuth.getCurrentUser().getUid());
                                    User friend = new User();
                                    friend.setUsername(user.getUsername());
                                    mMeRef.push().setValue(friend, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                                            if (databaseError != null) {
                                                Log.e(TAG, "Failed to store user to db", databaseError.toException());
                                            }
                                        }
                                    });

                                    dialog.setTitleText(user.getEmail());
                                    dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    dialog.setConfirmText("OK");
                                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            slidingPaneLayout.computeScroll();
                                        }
                                    });
                                    dialog.show();
                                    slidingPaneLayout.computeScroll();
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
                    });
                    dialog.show();

                } else {
                    //USER COULD NOT BE FOUND, propose to invite him
                    dialog
                            .setTitleText("Not found")
                            .setContentText("Would you like to invite " + username + "?")
                            .setConfirmText("Invite")
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    sweetAlertDialog.setTitleText(username + " invited");
                                    sweetAlertDialog.setContentText("Hope to see your friend here soon");
                                    sweetAlertDialog.setConfirmText("OK");
                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                                    sweetAlertDialog.show();

                                }
                            });
                    dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void startEmailHandler(final String email, List<String> providers) {

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        SweetAlertDialog dialog = null;
        if (providers.isEmpty() || providers == null) {
            dialog = new SweetAlertDialog(getActivity())
                    .setTitleText("Not found")
                    .setContentText("Would you like to invite " + email + "?")
                    .setConfirmText("Invite")
                    .setCancelText("Cancel")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialog.setTitleText(email + " invited");
                            sweetAlertDialog.setContentText("Hope to see your friend here soon");
                            sweetAlertDialog.setConfirmText("OK");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                            sweetAlertDialog.show();
                            slidingPaneLayout.computeScroll();

                        }
                    });
            dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            dialog.show();
        } else {
            final Query findFriendQuery = DatabaseRefUtil.getFindUserByUsernameQuery(email);
            dialog = new SweetAlertDialog(getActivity())
                    .setTitleText("Found")
                    .setContentText(email)
                    .setConfirmText("Add")
                    .setCancelText("Cancel")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(final SweetAlertDialog sDialog) {
                            DatabaseReference mUsernameRef = DatabaseRefUtil.getUsersRef();
                            Query mEmailQuery = mUsernameRef.orderByChild("email").equalTo(email);

                            mEmailQuery.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    final User user = dataSnapshot.getValue(User.class);
                                    DatabaseReference mMeRef = DatabaseRefUtil.getUserFriendsRef(currentUser.getUid());
                                    mMeRef.push().setValue(user, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                                            if (databaseError != null) {
                                                Log.e(TAG, "Failed to store user to db", databaseError.toException());
                                            }
                                        }
                                    });

                                    sDialog.setTitleText(user.getUsername());
                                    sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    sDialog.setConfirmText("OK");
                                    sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                                    sDialog.show();
                                    slidingPaneLayout.computeScroll();
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

                            sDialog.setTitleText("Success");
                            sDialog.setConfirmText("OK");
                            sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                            sDialog.show();
                        }
                    });

            dialog.show();
        }
    }

/*

    private void attachRecyclerViewAdapter() {
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        Query lastFifty = DatabaseRefUtil.getUserFriendsRef(uid).limitToLast(50);
        mRecyclerViewAdapter = new FirebaseRecyclerAdapter<User, FriendHolder>(
                User.class, R.layout.friend_layout, FriendHolder.class, lastFifty) {

            @Override
            public void populateViewHolder(FriendHolder friendView, User friend, int position) {
                friendView.setUsername(friend.getUsername());
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
        friendsRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        TextView usernameView = (TextView) view.findViewById(R.id.friend_nickname);
                        String username = usernameView.getText().toString();

                        Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                })
        );
    }
*/

/*    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }*/
/*
    @OnClick(R.id.add_friend_button)
    public void actionAddFriend(View view) {

        startActivity(new Intent(getActivity(), AddFriendActivity.class));
        floatingMenu.collapse();
    }


    @OnClick(R.id.invite_friend_button)
    public void actionInviteFriend(View view) {

        startActivity(new Intent(getActivity(), InviteFriendActivity.class));
        floatingMenu.collapse();
    }*/

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}






