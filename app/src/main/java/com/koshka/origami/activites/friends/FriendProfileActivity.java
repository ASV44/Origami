package com.koshka.origami.activites.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activites.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by imuntean on 8/26/16.
 */
public class FriendProfileActivity extends AppCompatActivity {


    @BindView(R.id.friend_username_text_view)
    TextView usernameTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);

        final String username = getIntent().getStringExtra("username");

        if (username != null){
            usernameTextView.setText(username);
        }
    }

    @OnClick(R.id.delete_friend_button)
    public void deleteFriend(View view){

        String friendNickname = usernameTextView.getText().toString();
        final SweetAlertDialog successDialog =new SweetAlertDialog(this)
                .setTitleText("Wait!")
                .setContentText("Do you really want to delete "+ friendNickname +"!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        goHome();
                    }
                });

        successDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
        successDialog.show();

    }

    private void goHome(){
        Intent intent = NavUtils.getParentActivityIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NavUtils.navigateUpTo(this, intent);
    }

}
