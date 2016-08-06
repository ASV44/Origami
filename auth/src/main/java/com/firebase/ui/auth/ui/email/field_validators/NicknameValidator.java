package com.firebase.ui.auth.ui.email.field_validators;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import com.firebase.ui.auth.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by imuntean on 8/6/16.
 */
public class NicknameValidator extends BaseValidator  {

    private Pattern pattern;
    private Matcher matcher;


    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,21}$";
   /* private static final String USERNAME_PATTERN =
            "(^[a-zA-Z0-9_-].(?=.*[a-zA-Z]).{6,20})";*/

    public NicknameValidator(TextInputLayout errorContainer) {
        super(errorContainer);
        pattern = Pattern.compile(USERNAME_PATTERN);
        mErrorMessage = mErrorContainer.getContext().getResources().getString(
                R.string.invalid_nickname);
        mEmptyMessage = mErrorContainer.getResources().getString(R.string.empty_nickname);
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        final String nickname = charSequence.toString();
        final String lowerCaseUserName = nickname.toLowerCase();
        matcher = pattern.matcher(lowerCaseUserName);
        return matcher.matches();

    }

}
