package com.firebase.ui.auth.ui.email.field_validators;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import com.firebase.ui.auth.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by imuntean on 8/6/16.
 */
public class UsernameValidator extends BaseValidator  {

    private Pattern pattern;
    private Matcher matcher;


    private static final String USERNAME_PATTERN = "^[A-Za-z][-_.a-zA-Z0-9]+[A-Za-z0-9]${3,33}$";
   /* private static final String USERNAME_PATTERN =
            "(^[a-zA-Z0-9_-].(?=.*[a-zA-Z]).{6,20})";*/

    public UsernameValidator(TextInputLayout errorContainer) {
        super(errorContainer);
        pattern = Pattern.compile(USERNAME_PATTERN);
        mErrorMessage = mErrorContainer.getContext().getResources().getString(
                R.string.invalid_username);
        mEmptyMessage = mErrorContainer.getResources().getString(R.string.empty_nickname);
    }

    @Override
    protected boolean isValid(CharSequence charSequence) {
        final String username = charSequence.toString();

        if (!username.matches("^[A-Za-z].*$")){
            mErrorMessage = mErrorContainer.getContext().getResources().getString(
                    R.string.username_no_character_start);
            return false;
        } else if(!username.matches("^.+?[A-Za-z0-9]$")){
            mErrorMessage = mErrorContainer.getContext().getResources().getString(
                    R.string.username_no_character_end);
            return false;
        }

        if (username.length() < 3){
            mErrorMessage = mErrorContainer.getContext().getResources().getString(
                    R.string.username_too_short);
            return false;
        } else if(username.length() > 33 ) {
            mErrorMessage = mErrorContainer.getContext().getResources().getString(
                    R.string.username_too_long);
            return false;
        }
        final String lowerCaseUserName = username.toLowerCase();
        matcher = pattern.matcher(lowerCaseUserName);
        return matcher.matches();

    }

}
