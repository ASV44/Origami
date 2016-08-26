package com.koshka.origami.activity.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.ui.TaskFailureLogger;
import com.firebase.ui.database.DatabaseRefUtil;
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
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by imuntean on 8/26/16.
 */
public class AddFriendActivity extends AppCompatActivity {

    private final static String TAG = "AddFriendActivity";

    @BindView(R.id.friends_email_nick_edit_text)
    EditText emailNickname;

    @BindView(R.id.friends_text_input_layout)
    TextInputLayout friendNickInputLayout;

    @BindView(R.id.userExistsTextView)
    TextView userExistsTextView;

    private FirebaseUser currentUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.button_add_friend)
    public void addFriend(View view) {


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final String string = emailNickname.getText().toString();
        boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(string).matches();

        final SweetAlertDialog dialog = new SweetAlertDialog(this)
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
        final SweetAlertDialog dialog = new SweetAlertDialog(this)
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

        DatabaseReference mUsernameRef = DatabaseRefUtil.getmUsersRef();
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
                                    DatabaseReference mMeRef = DatabaseRefUtil.getmFriendsRef().child(currentUser.getUid());
                                    mMeRef.push().setValue(user, new DatabaseReference.CompletionListener() {
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
                                            goHome();
                                        }
                                    });
                                    dialog.show();
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
                                            goHome();
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
            dialog = new SweetAlertDialog(this)
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
                                    goHome();
                                }
                            });
                            sweetAlertDialog.show();

                        }
                    });
            dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            dialog.show();
        } else {
            final Query findFriendQuery = DatabaseRefUtil.getFindUserByUsernameQuery(email);
            dialog = new SweetAlertDialog(this)
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
                            DatabaseReference mUsernameRef = DatabaseRefUtil.getmUsersRef();
                            Query mEmailQuery = mUsernameRef.orderByChild("email").equalTo(email);

                            mEmailQuery.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    final User user = dataSnapshot.getValue(User.class);
                                    DatabaseReference mMeRef = DatabaseRefUtil.getmFriendsRef().child(currentUser.getUid());
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
                                            goHome();
                                        }
                                    });
                                    sDialog.show();
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
                                    goHome();
                                }
                            });
                            sDialog.show();
                        }
                    });

            dialog.show();
        }
    }

    private void goHome() {
        Intent intent = NavUtils.getParentActivityIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NavUtils.navigateUpTo(this, intent);
    }

}
/*
findFriendQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final User user = dataSnapshot.getValue(User.class);
                            if (!user.getEmail().equals(mAuth.getCurrentUser().getEmail()) && !user.getDisplayName().equals(mAuth.getCurrentUser().getDisplayName())) {

                                dialog.setTitleText(user.getEmail());
                                dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                dialog.show();
                            } else {

                                dialog.setTitleText("Oops");
                                dialog.setContentText("Seems to be you");
                                dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                dialog.show();
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
 */
