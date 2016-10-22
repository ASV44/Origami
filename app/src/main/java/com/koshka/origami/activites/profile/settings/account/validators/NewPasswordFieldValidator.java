package com.koshka.origami.activites.profile.settings.account.validators;

import android.support.design.widget.TextInputLayout;

import com.firebase.ui.auth.ui.email.field_validators.BaseValidator;

/**
 * Created by imuntean on 8/25/16.
 */
public class NewPasswordFieldValidator extends BaseValidator {
    private int mMinLength;

    public NewPasswordFieldValidator(TextInputLayout errorContainer, int minLength) {
        super(errorContainer);
        mMinLength = minLength;
        mErrorMessage = "Password incorrect";
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return charSequence.length() >= mMinLength;
    }

}
