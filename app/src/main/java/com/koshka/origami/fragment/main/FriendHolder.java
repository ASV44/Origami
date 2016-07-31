package com.koshka.origami.fragment.main;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.koshka.origami.R;

import butterknife.OnClick;

/**
 * Created by imuntean on 7/31/16.
 */
public class FriendHolder extends RecyclerView.ViewHolder {

    View view;

    private ImageView friendPic;

    private TextView friendName;
    private TextView friendNickname;
    private TextView friendEmail;
    private ImageView friendPicture;
    private ImageButton button;

    public FriendHolder(View itemView) {
        super(itemView);
        this.view = itemView;
    }

    public void setFriendNickname(String nickname) {
        friendNickname = (TextView) view.findViewById(R.id.friend_nickname);
        friendNickname.setText(nickname);
    }
/*
    public void setFriendName(String displayName) {
        friendName = (TextView) view.findViewById(R.id.friend_name);
        friendName.setText(displayName);

    }*/
    public void setFriendEmail(String email){
        friendEmail = (TextView) view.findViewById(R.id.friend_email);
        friendEmail.setText(email);
    }

    public void setFriendPicture(Context context, String pictureUrl){
        friendPicture = (ImageView) view.findViewById(R.id.imageView);
        Glide.with(context)
                .load(pictureUrl)
                .fitCenter()
                .into(friendPicture);
    }
}
