package com.koshka.origami.fragments.main.friends.groups;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koshka.origami.R;

import java.util.List;

/**
 * Created by qm0937 on 10/2/16.
 */

public class GroupsRecycleViewAdapter extends RecyclerView.Adapter<GroupsRecyclerViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<Group> itemList;
    private Context context;

    private boolean itemClicked = false;

    private CardView previousView;
    private int previousId;

    public GroupsRecycleViewAdapter(Context context, List<Group> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public GroupsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_group_row_layout, null);
        GroupsRecyclerViewHolder viewHolder = new GroupsRecyclerViewHolder(layoutView);

        layoutView.setOnClickListener(this);
        layoutView.setOnLongClickListener(this);
        layoutView.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupsRecyclerViewHolder holder, int position) {
        holder.getGroupName().setText(itemList.get(position).getGroupName());
        holder.getGroupPhoto().setImageDrawable(itemList.get(position).getGroupPic());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    @Override
    public void onClick(View v) {

   /*     int currentViewId = v.getId();

        if (currentViewId == previousId) {
            CardView view = (CardView) v;
            if (itemClicked) {
                view.setCardBackgroundColor(v.getResources().getColor(R.color.white));
            } else {
                view.setCardBackgroundColor(v.getResources().getColor(R.color.transparent6));
                itemClicked = true;
                previousId = currentViewId;
            }
        }
*/

        /*CardView view = (CardView) v ;

        if (previousView != null){
            previousView.setCardBackgroundColor(v.getResources().getColor(R.color.white));
        }

        view.setCardBackgroundColor(v.getResources().getColor(R.color.transparent6));

        previousView = view;*/

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
