package com.koshka.origami.fragment.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.firebase.ui.auth.ui.TaskFailureLogger;
import com.firebase.ui.database.DatabaseRefUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
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
import com.koshka.origami.activity.main.MainActivity;
import com.koshka.origami.model.User;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by imuntean on 7/20/16.
 */
public class FriendsFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "FriendsFragment";
    private static final int REQUEST_INVITE = 0;

    @BindView(R.id.friends_sliding_layout)
    SlidingUpPanelLayout slidingPaneLayout;

    @BindView(R.id.invite_friend_relative_layout)
    RelativeLayout inviteFriendLayout;

    @BindView(R.id.add_friend_relative_layout)
    RelativeLayout addFriendLayout;

    @BindView(R.id.add_friend_button)
    TextView addFriendButton;

    @BindView(R.id.find_friend_button)
    TextView findFriendButton;

    @BindView(R.id.invite_friend_button)
    TextView inviteFriendButton;

    @BindView(R.id.friends_email_nick_edit_text)
    EditText nicknameEditText;

    @BindView(R.id.facebook_invite_button)
    TextView facebookInviteButton;

    @BindView(R.id.twitter_invite_button)
    TextView twitterInviteButton;

    @BindView(R.id.google_invite_button)
    TextView googleInviteButton;

    @BindView(R.id.email_invite_button)
    TextView emailInviteButton;

    @BindView(R.id.invite_friend_email_layout)
    RelativeLayout inviteFriendEmailLayout;

    @BindView(R.id.main_friends_panel_elements_layout)
    LinearLayout mainPanelElementsLayout;

    @BindView(R.id.invite_email_text)
    EditText inviteEmailEditText;


    private SlidingUpPanelLayout.PanelSlideListener mainPanelSlideListener;

    private SlidingUpPanelLayout.PanelSlideListener addFriendPanelListener;
    private SlidingUpPanelLayout.PanelSlideListener inviteFriendPanelListener;
    private SlidingUpPanelLayout.PanelSlideListener searchFriendPanelListener;

    private int panelNumber;

    private boolean sendEmailInvitationButtonPressed = false;

    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<User, FriendHolder> mRecyclerViewAdapter;

    private FirebaseAuth mAuth;

    private GoogleApiClient mGoogleApiClient;

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

        setTypefaces();

        mainPanelSlideListener = new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    returnSlidingPanelToInitialState();
                } else if (newState == SlidingUpPanelLayout.PanelState.ANCHORED){
                    switch (panelNumber){
                        case 0:{

                            return;
                        }
                        case 1:{
                            return;
                        }
                        case 2:{
                            sendEmailInvitationButtonPressed = false;
                            getInviteLayoutToInitState();
                            hideKeyboard();
                        }
                    }
                } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){

                    switch (panelNumber){
                        case 0:{

                            return;
                        }
                        case 1:{
                           // openKeyboardForEditText(nicknameEditText);
                            return;
                        }
                        case 2:{
                            sendEmailInvitationButtonPressed = true;
                            setUpEmailInvitationLayout();

                            //TODO: Problematic when dragging back while keyboard is coming
                            //openKeyboardForEditText(inviteEmailEditText);
                        }
                    }
                }
            }
        };

        slidingPaneLayout.addPanelSlideListener(mainPanelSlideListener);
        setTouchListenersForPanel();
    }


    //Opens the keyboard focusing on a edit text view
    private void openKeyboardForEditText(EditText editText){
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    //TODO: HArd coded, I know
    private void setTypefaces() {
        final Typeface friendsIcons = Typeface.createFromAsset(getContext().getAssets(), "fonts/heydings_icons.ttf");

        if (friendsIcons != null) {
            addFriendButton.setTypeface(friendsIcons);
            findFriendButton.setTypeface(friendsIcons);
            inviteFriendButton.setTypeface(friendsIcons);
        }

        final Typeface socialFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/social_icons_2.ttf");

        if (socialFont != null) {
            twitterInviteButton.setTypeface(socialFont);
            facebookInviteButton.setTypeface(socialFont);
            googleInviteButton.setTypeface(socialFont);
            emailInviteButton.setTypeface(socialFont);
        }

    }

    private void returnSlidingPanelToInitialState() {

        addFriendButton.setVisibility(View.VISIBLE);
        addFriendButton.setGravity(Gravity.CENTER_HORIZONTAL);
        inviteFriendButton.setVisibility(View.VISIBLE);
        inviteFriendButton.setGravity(Gravity.LEFT);
        findFriendButton.setVisibility(View.VISIBLE);
        findFriendButton.setGravity(Gravity.RIGHT);

        addFriendLayout.setVisibility(View.GONE);
        inviteFriendLayout.setVisibility(View.GONE);
        hideKeyboard();

    }

    private void setTouchListenersForPanel() {


        addFriendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                panelNumber = 1;
                slidingPaneLayout.setAnchorPoint(1.0f);
                addFriendLayout.setVisibility(View.VISIBLE);
                inviteFriendButton.setVisibility(View.GONE);
                findFriendButton.setVisibility(View.GONE);
                nicknameEditText.requestFocus();
                return false;
            }
        });


        addFriendButton.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                panelNumber = 1;
                slidingPaneLayout.setAnchorPoint(1.0f);
                addFriendLayout.setVisibility(View.VISIBLE);
                inviteFriendButton.setVisibility(View.GONE);
                findFriendButton.setVisibility(View.GONE);
                return false;
            }
        });


        addFriendButton.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                panelNumber = 1;
                slidingPaneLayout.setAnchorPoint(1.0f);
                addFriendLayout.setVisibility(View.VISIBLE);
                inviteFriendButton.setVisibility(View.GONE);
                findFriendButton.setVisibility(View.GONE);
                nicknameEditText.requestFocus();
                return false;
            }
        });


        inviteFriendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                panelNumber = 2;
                inviteFriendButton.setGravity(Gravity.CENTER_HORIZONTAL);
                slidingPaneLayout.setAnchorPoint(0.3f);
                inviteFriendLayout.setVisibility(View.VISIBLE);
                addFriendButton.setVisibility(View.GONE);
                findFriendButton.setVisibility(View.GONE);
                return false;
            }
        });


        findFriendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                panelNumber = 0;
                findFriendButton.setGravity(Gravity.CENTER_HORIZONTAL);
                slidingPaneLayout.setAnchorPoint(0.3f);
                addFriendButton.setVisibility(View.GONE);
                inviteFriendButton.setVisibility(View.GONE);

                return false;
            }
        });

    }

    private void setAddFriendLayout() {


    }

    private void setInviteFriendLayout() {

    }

    private void setFindFriendLayout() {

    }

    @OnClick(R.id.button_add_friend)
    public void buttonAddFriend() {
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

    @OnClick(R.id.facebook_invite_button)
    public void facebookInvite(){

        String appLinkUrl, previewImageUrl;

        appLinkUrl = "https://www.facebook.com/origamiworld1";
        previewImageUrl = "https://scontent.xx.fbcdn.net/v/t1.0-9/13164238_993899774051176_1452307493559266800_n.jpg?oh=18b0dcedb417430f865bc30e49102b89&oe=58AAA284";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .setPromotionDetails("Example", "EXAMPLE")
                    .build();
            AppInviteDialog.show(this, content);
        }
    }

    @OnClick(R.id.email_invite_button)
    public void email_invite_button() {


        if (sendEmailInvitationButtonPressed) {
            sendEmailInvitationButtonPressed = false;
            getInviteLayoutToInitState();
        } else {
            sendEmailInvitationButtonPressed = true;
            setUpEmailInvitationLayout();
        }


    }

    @OnClick(R.id.google_invite_button)
    public void sendEmailInvitation(){
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);

    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }*/



    private void getInviteLayoutToInitState() {
        slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        inviteFriendEmailLayout.setVisibility(View.GONE);
        facebookInviteButton.setVisibility(View.VISIBLE);
        googleInviteButton.setVisibility(View.VISIBLE);
        twitterInviteButton.setVisibility(View.VISIBLE);
    }

    private void setUpEmailInvitationLayout() {

        slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

        inviteFriendEmailLayout.setVisibility(View.VISIBLE);
        inviteEmailEditText.requestFocus();
    }


    //Hide soft keyboard
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}






