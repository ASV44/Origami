package com.koshka.origami.fragments.main.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.koshka.origami.R;

/**
 * Created by imuntean on 7/31/16.
 */
public class FriendHolder extends RecyclerView.ViewHolder {

    View view;

    private ImageView friendPic;

    private TextView friendName;
    private TextView usernameTextView;
    private TextView friendEmail;
    private ImageView friendPicture;
    private ImageButton button;

    public FriendHolder(View itemView) {
        super(itemView);
        this.view = itemView;
    }

    public void setUsername(String username) {
        usernameTextView = (TextView) view.findViewById(R.id.friend_nickname);
        usernameTextView.setText(username);
    }


    public void setFriendPicture(Context context, String pictureUrl){
        friendPicture = (ImageView) view.findViewById(R.id.friendPicView);
        Glide.with(context)
                .load(pictureUrl)
                .fitCenter()
                .into(friendPicture);
    }
}
