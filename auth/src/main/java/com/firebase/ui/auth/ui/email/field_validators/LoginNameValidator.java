package com.firebase.ui.auth.ui.email.field_validators;

import android.support.design.widget.TextInputLayout;

import com.firebase.ui.auth.R;

/**
 * Created by imuntean on 7/29/16.
 */
public class LoginNameValidator extends BaseValidator {

    public LoginNameValidator(TextInputLayout errorContainer) {
        super(errorContainer);
        mErrorMessage = mErrorContainer.getContext().getResources().getString(
                R.string.invalid_email_address);
        mEmptyMessage = mErrorContainer.getResources().getString(R.string.missing_email_address);
    }
}
