package com.koshka.origami.fragments.main.friends.groups;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koshka.origami.R;

/**
 * Created by qm0937 on 10/2/16.
 */

public class GroupsRecyclerViewHolder extends RecyclerView.ViewHolder{

    private TextView groupName;
    private ImageView groupPhoto;


    public GroupsRecyclerViewHolder(View itemView) {
        super(itemView);
        groupName = (TextView)itemView.findViewById(R.id.group_name);
        groupPhoto = (ImageView)itemView.findViewById(R.id.group_photo);

    }


    public TextView getGroupName() {
        return groupName;
    }

    public void setGroupName(TextView groupName) {
        this.groupName = groupName;
    }

    public ImageView getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(ImageView groupPhoto) {
        this.groupPhoto = groupPhoto;
    }
}
