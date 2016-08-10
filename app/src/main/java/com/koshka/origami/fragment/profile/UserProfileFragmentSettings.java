package com.koshka.origami.fragment.profile;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayout;
import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.DatabaseRefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 8/6/16.
 */
public class UserProfileFragmentSettings extends Fragment {

    private DatabaseReference mMeRef;
    private FirebaseAuth mAuth;

    @BindView(R.id.origami_text_logo)
    TextView origamiTextLogo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_settings, container, false);
        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mMeRef = DatabaseRefUtil.getUserRefByUid(mAuth.getCurrentUser().getUid());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/origamibats.ttf");
        origamiTextLogo.setTypeface(font);
    }


    @OnClick(R.id.sign_out)
    public void signOut() {

        Resources res = getResources();

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(res.getString(R.string.log_out_warning))
                .setPositiveButton(res.getString(R.string.positive_log_out), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       signOutFromFirebase();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();

    }

    private void signOutFromFirebase(){
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LoginActivity.createIntent(getActivity()));
                            getActivity().finish();
                        } else {
                            showSnackbar(R.string.sign_out_failed);
                        }
                    }
                });
    }


    @OnClick(R.id.delete_account)
    public void deleteAccountClicked() {

        Resources res = getResources();

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(res.getString(R.string.delete_warning))
                .setPositiveButton(res.getString(R.string.positive_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccountFromDb();
                        deleteAccount();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }


    private void deleteAccountFromDb() {

        String user = mMeRef.getKey();
        if (user != null) {
            mMeRef.removeValue();

        }
    }


    private void deleteAccount() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser()
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LoginActivity.createIntent(getActivity()));
                            getActivity().finish();
                        } else {
                            showSnackbar(R.string.delete_account_failed);
                        }
                    }
                });
    }


    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(getView(), errorMessageRes, Snackbar.LENGTH_LONG)
                .show();
    }

}
