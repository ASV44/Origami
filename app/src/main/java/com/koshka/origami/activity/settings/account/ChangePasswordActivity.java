package com.koshka.origami.activity.settings.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.activity.settings.account.field_validator.CurrentPasswordFieldValidator;
import com.koshka.origami.activity.settings.account.field_validator.NewPasswordFieldValidator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by imuntean on 8/9/16.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";

    @BindView(R.id.toolbar_change_password)
    Toolbar toolbar;

    @BindView(R.id.current_password_layout)
    TextInputLayout currentPasswordLayout;

    @BindView(R.id.new_password_layout)
    TextInputLayout newPasswordLayout;

    @BindView(R.id.confirm_new_password_layout)
    TextInputLayout confirmNewPasswordLayout;

    @BindView(R.id.button_next)
    Button nextButton;

    @BindView(R.id.button_change_password)
    Button changePasswordButton;

    @BindView(R.id.current_password)
    TextView currentPasswordTextView;

    @BindView(R.id.new_password)
    TextView newPasswordTextView;

    @BindView(R.id.confirm_new_password)
    TextView confirmNewPasswordTextView;

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

        setContentView(R.layout.password_change_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(true); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
            actionBar.setDisplayShowTitleEnabled(false);
        }

        clearPasswords();

    }

    private void clearPasswords() {

        if (currentPasswordTextView != null) {
            currentPasswordTextView.setText("");
        }

        if (newPasswordTextView != null) {
            newPasswordTextView.setText("");
        }

        if (confirmNewPasswordTextView != null) {
            confirmNewPasswordTextView.setText("");
        }


    }

    @OnClick(R.id.button_next)
    public void next(View view) {

        CurrentPasswordFieldValidator validator = new CurrentPasswordFieldValidator(currentPasswordLayout, 6);
        String currentPassword = currentPasswordTextView.getText().toString();
        boolean isCurrentPasswordValid = validator.validate(currentPassword);

        if (isCurrentPasswordValid) {
            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);

            currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        currentPasswordLayout.setVisibility(View.GONE);
                        nextButton.setVisibility(View.GONE);
                        newPasswordLayout.setVisibility(View.VISIBLE);
                        confirmNewPasswordLayout.setVisibility(View.VISIBLE);
                        changePasswordButton.setVisibility(View.VISIBLE);


                    } else {
                        currentPasswordLayout.setError("Password is incorrect");
                    }
                }
            });
        }
    }

    @OnClick(R.id.button_change_password)
    public void changePassword(View view) {

        String newPassword = newPasswordTextView.getText().toString();
        String newPasswordConfirmation = confirmNewPasswordTextView.getText().toString();

        NewPasswordFieldValidator validator = new NewPasswordFieldValidator(newPasswordLayout, 6);
        boolean newPasswordIsOk = validator.validate(newPassword);

        NewPasswordFieldValidator validator1 = new NewPasswordFieldValidator(confirmNewPasswordLayout, 6);
        boolean newPasswordConfirmationIsOk = validator1.validate(newPasswordConfirmation);

        final SweetAlertDialog successDialog =new SweetAlertDialog(this)
                .setTitleText("Success!")
                .setContentText("Your password was changed")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        goHome();
                    }
                });

        successDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

        if (newPasswordIsOk && newPasswordConfirmationIsOk) {

            if (newPassword.equals(newPasswordConfirmation)) {
                currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "Password changed");
                       successDialog.show();
                    }
                });
            } else {
                confirmNewPasswordLayout.setError("The password don't match");
            }

        }

    }
    private void goHome(){
        Intent intent = NavUtils.getParentActivityIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NavUtils.navigateUpTo(this, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            goHome();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
