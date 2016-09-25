package com.koshka.origami.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.DatabaseRefUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;
import com.koshka.origami.R;
import com.koshka.origami.activity.friends.AddFriendActivity;
import com.koshka.origami.activity.friends.FriendProfileActivity;
import com.koshka.origami.activity.friends.InviteFriendActivity;
import com.koshka.origami.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 7/20/16.
 */
public class FriendsFragment extends Fragment {


    @BindView(R.id.multiple_actions)
    FloatingActionsMenu floatingMenu;

    @BindView(R.id.friends_recycler_view)
    RecyclerView friendsRecyclerView;

    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<User, FriendHolder> mRecyclerViewAdapter;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(false);
        friendsRecyclerView.setHasFixedSize(false);
        friendsRecyclerView.setLayoutManager(mManager);

        floatingMenu.collapse();

    }


    private void attachRecyclerViewAdapter() {
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        Query lastFifty = DatabaseRefUtil.getUserFriendsRef(uid).limitToLast(50);
        mRecyclerViewAdapter = new FirebaseRecyclerAdapter<User, FriendHolder>(
                User.class, R.layout.friend_layout, FriendHolder.class, lastFifty) {

            @Override
            public void populateViewHolder(FriendHolder friendView, User friend, int position) {
                friendView.setUsername(friend.getUsername());
                friendView.setFriendPicture(getContext(), "http://209.132.179.3/uploads/big/2df7cab0a2305743db864ef472b6c8b9.png");

            }
        };
        // Scroll to bottom on new messages
        mRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mManager.smoothScrollToPosition(friendsRecyclerView, null, mRecyclerViewAdapter.getItemCount());
            }
        });

        friendsRecyclerView.setAdapter(mRecyclerViewAdapter);
        friendsRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        TextView usernameView = (TextView) view.findViewById(R.id.friend_nickname);
                        String username = usernameView.getText().toString();

                        Intent intent = new Intent(getActivity(), FriendProfileActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                })
        );
    }

    @Override
    public void onStart() {
        super.onStart();

        attachRecyclerViewAdapter();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }

    @OnClick(R.id.add_friend_button)
    public void actionAddFriend(View view) {

        startActivity(new Intent(getActivity(), AddFriendActivity.class));
        floatingMenu.collapse();
    }


    @OnClick(R.id.invite_friend_button)
    public void actionInviteFriend(View view) {

        startActivity(new Intent(getActivity(), InviteFriendActivity.class));
        floatingMenu.collapse();
    }
}






