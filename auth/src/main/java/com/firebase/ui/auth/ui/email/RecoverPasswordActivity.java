/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.firebase.ui.auth.ui.email;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.auth.R;
import com.firebase.ui.auth.ui.ActivityHelper;
import com.firebase.ui.auth.ui.AppCompatBase;
import com.firebase.ui.auth.ui.ExtraConstants;
import com.firebase.ui.auth.ui.FlowParameters;
import com.firebase.ui.auth.ui.TaskFailureLogger;
import com.firebase.ui.auth.ui.email.field_validators.EmailFieldValidator;
import com.firebase.ui.auth.util.LoginActionBarHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPasswordActivity extends AppCompatBase implements View.OnClickListener {
    private static final String TAG = "RecoverPasswordActivity";
    private static final int RC_CONFIRM = 3;
    private EditText mEmailEditText;
    private EmailFieldValidator mEmailFieldValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.recover_password_title);
        setContentView(R.layout.forgot_password_layout);
        String email = getIntent().getStringExtra(ExtraConstants.EXTRA_EMAIL);

        mEmailFieldValidator = new EmailFieldValidator((TextInputLayout) findViewById(R.id
                .email_layout));

        mEmailEditText = (EditText) findViewById(R.id.email);
        Button nextButton = (Button) findViewById(R.id.button_done);

        LoginActionBarHelper recoveryActionBarHelper = new LoginActionBarHelper(this, R.id.toolbar_recover_password);
        recoveryActionBarHelper.buildTitlePlusIndicatorActionBar("Recovery");

        if (email != null) {
            boolean emailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (emailValid){
                mEmailEditText.setText(email);
            } else {
                mEmailEditText.setText("");
                mEmailEditText.requestFocus();
            }

        }
        nextButton.setOnClickListener(this);
    }

    private void next(final String email) {
        FirebaseAuth firebaseAuth = mActivityHelper.getFirebaseAuth();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnFailureListener(
                        new TaskFailureLogger(TAG, "Error sending password reset email"))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mActivityHelper.dismissDialog();
                        Intent confirmIntent = ConfirmRecoverPasswordActivity.createIntent(
                                RecoverPasswordActivity.this,
                                mActivityHelper.getFlowParams(),
                                task.isSuccessful(),
                                email);
                        startActivityForResult(confirmIntent, RC_CONFIRM);
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CONFIRM) {
            finish(RESULT_OK, new Intent());
        }
    }


    @Override
    public void onClick(View view) {
        hideKeyboard();
        if (view.getId() == R.id.button_done) {
            if (!mEmailFieldValidator.validate(mEmailEditText.getText())) {
                return;
            }
            mActivityHelper.showLoadingDialog(R.string.progress_dialog_sending);
            next(mEmailEditText.getText().toString());
        }
    }

    public static Intent createIntent(Context context, FlowParameters flowParams, String email) {
        return ActivityHelper.createBaseIntent(context, RecoverPasswordActivity.class, flowParams)
                .putExtra(ExtraConstants.EXTRA_EMAIL, email);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
