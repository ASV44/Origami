package com.koshka.origami.activites.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.koshka.origami.R;
import com.koshka.origami.activites.main.MainActivity;
import com.koshka.origami.helpers.activity.LoginActivityHelper;
import com.koshka.origami.utils.ui.theme.OrigamiThemeHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by imuntean on 7/19/16.
 */
public class LoginActivity extends GenericLoginActivity{

    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 100;

    private JSONObject profileData;

    //----------------------------------------------------------------------------------------------

    @BindView(R.id.login_pager)
    ViewPager mPager;

    @BindView(R.id.sign_in)
    Button mSignIn;

    @BindView(R.id.title_text)
    TextView titleTextView;

    @BindView(R.id.indicator)
    CircleIndicator circleIndicator;

    //----------------------------------------------------------------------------------------------

    private LoginActivityHelper loginActivityHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startSignInActivity();

    }

    //----------------------------------------------------------------------------------------------


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("LoginActivity","OnActivityResult_requestCode = " + requestCode);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }
        //showSnackbar(R.string.unknown_response);
    }

    @MainThread
    public void handleSignInResponse(int resultCode, Intent data) {
        Log.d("LogInActivity","ResultCode = " + resultCode);
        if (resultCode == RESULT_OK) {
            startActivity(MainActivity.createIntent(this).putExtra("theme",activityHelper.getThemeHelper().getSelectedTheme()));
            finish();
            return;
        }

        if (resultCode == RESULT_CANCELED) {
            //showSnackbar(R.string.sign_in_cancelled);
            finish();
            return;
        }
        //TODO:UNKOWN SIGN IN RESPONSE, HANDLE THIS BY SENDING USER TO SUPPORT OR HELP PAGE.
        showSnackbar(R.string.unknown_sign_in_response);
    }


    //util method for intent creation from other activities
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, LoginActivity.class);
        return in;
    }


    //----------------------------------------------------------------------------------------------

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //----------------------------------------------------------------------------------------------

    public void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.d("HandleFacebookAccess","Credential = " + credential);
        getProfileData(token);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        handleSignInResponse(-1,null);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            showSnackbar(R.string.authentication_failed);
                        }

                        // ...
                    }
                });
    }

    public void checkConection() {
        if (!loginActivityHelper.isNetworkOn()) {
            showSnackbar(R.string.no_internet_connection);
        }
    }

    public void getProfileData(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken,new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.v("ResponseData","" + object);
                profileData = object;
                try {
                    Log.d("HandleFacebookAccess","" + mAuth.fetchProvidersForEmail(profileData.get("email").toString()).getResult());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,first_name,last_name,gender,id,link,location,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void startSignInActivity() {
        OrigamiThemeHelper themeHelper = activityHelper.getThemeHelper();
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(themeHelper.getSelectedTheme())
                        .setLogo(themeHelper.getSelectedLogo())
                        .setProviders(themeHelper.getSelectedProviders())
                        .setTosUrl(themeHelper.getSelectedTosUrl())
                        .build(),
                RC_SIGN_IN);
    }
}
