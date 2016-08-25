package com.koshka.origami.activity.settings.account.validator;

import android.support.design.widget.TextInputLayout;

import com.firebase.ui.auth.ui.email.field_validators.BaseValidator;

/**
 * Created by imuntean on 8/25/16.
 */
public class CurrentPasswordFieldValidator extends BaseValidator {
    private int mMinLength;

    public CurrentPasswordFieldValidator(TextInputLayout errorContainer, int minLength) {
        super(errorContainer);
        mMinLength = minLength;
        mErrorMessage = "Password incorrect";
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        return charSequence.length() >= mMinLength;
    }

}
