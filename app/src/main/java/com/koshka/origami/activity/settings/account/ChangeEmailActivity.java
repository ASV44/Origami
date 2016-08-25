package com.koshka.origami.activity.settings.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.ui.email.field_validators.EmailFieldValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 8/11/16.
 */
public class ChangeEmailActivity extends AppCompatActivity {

    private static final String TAG = "ChangeEmailActivity";

    @BindView(R.id.toolbar_email_change)
    Toolbar toolbar;

    @BindView(R.id.change_email)
    EditText changeEmailEditText;

    @BindView(R.id.email_change_layout)
    TextInputLayout emailChangeLayout;

    @BindView(R.id.current_email)
    TextView currentEmailTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.email_change_layout);
        ButterKnife.bind(this);

        currentEmailTextView.setText(currentUser.getEmail());

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @OnClick(R.id.button_change_email)
    public void changeEmail(View view) {


        final String email = changeEmailEditText.getText().toString();
        EmailFieldValidator validator = new EmailFieldValidator(emailChangeLayout);


        boolean emailIsValid = validator.validate(email);

        if (emailIsValid) {

            Resources res = getResources();

            final EditText passwordEditText = new EditText(this);
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(res.getString(R.string.change_email_warning))
                    .setView(passwordEditText)
                    .setPositiveButton(res.getString(R.string.positive_change), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                            String email = currentUser.getEmail();
                            String password = passwordEditText.getText().toString();
                            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                            currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        Log.d(TAG, "User re-authenticated.");
                                        String updatedEmail = changeEmailEditText.getText().toString();
                                        if (updatedEmail != null && !updatedEmail.isEmpty()) {
                                            currentUser.updateEmail(updatedEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.d(TAG, "Email was updated.");
                                                        currentEmailTextView.setText(currentUser.getEmail());
                                                    }
                                                    else {
                                                        Log.d(TAG, "Email was not updated.");
                                                    }
                                                }
                                            });
                                        }
                                    }else {
                                        Log.d(TAG, "User was not re-authenticated.");
                                    }

                                }
                            });
                        }
                    })
                    .setNegativeButton(res.getString(R.string.cancel), null)
                    .create();

            dialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
