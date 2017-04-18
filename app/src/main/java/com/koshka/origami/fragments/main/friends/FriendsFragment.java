package com.koshka.origami.fragments.main.friends;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.R;
import com.koshka.origami.fragments.main.friends.groups.Group;
import com.koshka.origami.fragments.main.friends.groups.GroupsRecycleViewAdapter;
import com.koshka.origami.helpers.fragment.FriendsFragmentHelper;
import com.koshka.origami.utils.KeyboardUtil;
import com.koshka.origami.utils.TypefaceUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 7/20/16.
 */
public class FriendsFragment extends Fragment {

    private static final String TAG = "FriendsFragment";
    private static final String ICONS_FONT = "fonts/heydings_icons.ttf";
    private static final String SOCIAL_ICONS_FONT = "fonts/social_icons_2.ttf";

    @BindView(R.id.friends_sliding_layout)
    SlidingUpPanelLayout slidingPaneLayout;

    @BindView(R.id.main_friends_panel_elements_layout)
    LinearLayout mainPanelElementsLayout;

    @BindView(R.id.collapsed_panel_elements)
    RelativeLayout collapsedLayout;

    //------------------------------------------------

    @BindView(R.id.invite_friend_relative_layout)
    RelativeLayout inviteFriendLayout;

    @BindView(R.id.search_friend_layout)
    RelativeLayout searchFriendLayout;

    @BindView(R.id.add_friend_relative_layout)
    RelativeLayout addFriendLayout;

    @BindView(R.id.friends_small_bar)
    LinearLayout friendsSmallBarLayout;

    //------------------------------------------------

    @BindView(R.id.add_friend_button)
    TextView addFriendButton;

    @BindView(R.id.find_friend_button)
    TextView findFriendButton;

    @BindView(R.id.invite_friend_button)
    TextView inviteFriendButton;

    @BindView(R.id.friends_search_edit_text)
    EditText searchFriendEditText;

    @BindView(R.id.friends_email_nick_edit_text)
    EditText nicknameEditText;

    //------------------------------------------------

    @BindView(R.id.facebook_invite_button)
    TextView facebookInviteButton;

    @BindView(R.id.twitter_invite_button)
    TextView twitterInviteButton;

    @BindView(R.id.google_invite_button)
    TextView googleInviteButton;

    @BindView(R.id.email_invite_button)
    TextView emailInviteButton;

    @BindView(R.id.invite_friend_email_layout)
    RelativeLayout inviteFriendEmailLayout;

    @BindView(R.id.invite_email_text)
    EditText inviteEmailEditText;

    //------------------------------------------------
    //FRIENDS RECYCLE VIEW

    @BindView(R.id.main_page_layout)
    RelativeLayout friendsMainLayout;

    @BindView(R.id.friends_recycler_view)
    RecyclerView friendsRecycleView;

    //GROUPS RECYCLE VIEW

    @BindView(R.id.groups_recycler_view)
    RecyclerView groupsRecycleView;

    @BindView(R.id.groups_main_layout)
    RelativeLayout groupsMainLayout;

    //------------------------------------------------
    @BindView(R.id.my_friends_button)
    TextView myFriendsSmallButton;

    @BindView(R.id.my_groups_button)
    TextView myGroupsSmallButton;

    ViewPager mPager;


    private int previousState;


    //------------------------------------------------
    //RECYCLE STAFF

    private RecyclerView.Adapter mFriendsAdapter;
    private RecyclerView.LayoutManager mFriendsLayoutManager;

    private RecyclerView.Adapter mGroupsAdapter;
    private GridLayoutManager mGroupsLayoutManager;

    //------------------------------------------------

    private SlidingUpPanelLayout.PanelSlideListener mainPanelSlideListener;

    //------------------------------------------------

/*    @BindView(R.id.blurring_view_expanded)
    BlurringView blurringViewExpanded;*/

    //------------------------------------------------

    private FriendsFragmentHelper friendsFragmentHelper;

    //Keep panel number on touch
    private int panelNumber;
    private boolean sendEmailInvitationButtonPressed = false;
    private FirebaseAuth mAuth;

    //------------------------------------------------

    private View view;

    private ArrayList<String> friendsList;
    private ArrayList<String> friendsSuggestionsList;
    private ArrayAdapter<String> friendsSearchAdapter;
    private DatabaseReference mDatabase;
    private String UserIdNewFriend;
    private ArrayList<String> localSearchFriendsList;
    private FriendsRecyclerViewAdapter localSearchAdapter = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_fragment, container, false);

        friendsFragmentHelper = new FriendsFragmentHelper(getActivity());
        mPager = (ViewPager) getActivity().findViewById(R.id.main_view_pager);
        this.view = view;

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();

        friendsList = new ArrayList<>();
        //mDatabase = FirebaseDatabase.getInstance().getReference("users");

        setUpUI();

        searchFriendEditText.setFocusableInTouchMode(true);
        searchFriendEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    hideKeyboard();
                    returnSlidingPanelToInitialState();
                    return true;
                }
                return false;
            }
        });
    }


    //-----------------------------ONCLICK BUTTONS-------------------------------------------------

    @OnClick(R.id.my_friends_button)
    public void myFriendsSmallButtonPress() {

        restoreSmallButtonsState();
        myFriendsSmallButton.setBackground(getResources().getDrawable(R.color.transparent4));
        groupsMainLayout.setVisibility(View.INVISIBLE);
        friendsMainLayout.setVisibility(View.VISIBLE);

    }

    //---------------------------------------------------------------------------------------------

    @OnClick(R.id.my_groups_button)
    public void myGroupsSmallButtonPress() {
        restoreSmallButtonsState();
        myGroupsSmallButton.setBackground(getResources().getDrawable(R.color.transparent4));
        friendsMainLayout.setVisibility(View.INVISIBLE);
        groupsMainLayout.setVisibility(View.VISIBLE);
    }

    //---------------------------------------------------------------------------------------------

    @OnClick(R.id.button_add_friend)
    public void buttonAddFriend() {
        hideKeyboard();
        if (friendsSuggestionsList.size() != 0) {
            String friend = friendsSuggestionsList.get(0);
            if (!friendsList.contains(friend)) {
                friendsList.add(0, friend);
                mFriendsAdapter.notifyDataSetChanged();
                nicknameEditText.setText("");
                friendsSuggestionsList.clear();
                friendsSearchAdapter.notifyDataSetChanged();
                slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }
    }

    //---------------------------------------------------------------------------------------------

    @OnClick(R.id.facebook_invite_button)
    public void facebookInvite() {

        friendsFragmentHelper.buildFacebookInviteIntent();
    }

    //---------------------------------------------------------------------------------------------

    @OnClick(R.id.email_invite_button)
    public void email_invite_button() {


        if (sendEmailInvitationButtonPressed) {
            sendEmailInvitationButtonPressed = false;
            getInviteLayoutToInitState();
        } else {
            sendEmailInvitationButtonPressed = true;
            setUpEmailInvitationLayout();
        }


    }

    //---------------------------------------------------------------------------------------------

    @OnClick(R.id.google_invite_button)
    public void sendEmailInvitation() {

        friendsFragmentHelper.buildEmailInviteIntent();

    }


    //---------------------------------UI SETUP----------------------------------------------------

    private void setUpUI() {

        setTypefaces();

        attachRecycleViews();

        mainPanelSlideListener = new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

                if (slideOffset > 0.8) {
                    friendsSmallBarLayout.setVisibility(View.INVISIBLE);
                } else {
                    friendsSmallBarLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    //Collapsing the panel should return elements to init state in any case
                    returnSlidingPanelToInitialState();
                } else if (newState == SlidingUpPanelLayout.PanelState.ANCHORED) {
                    friendsSmallBarLayout.setVisibility(View.VISIBLE);
                    //Different behavior on Anchored state
                    switch (panelNumber) {
                        case 0: {
                            return;
                        }
                        case 1: {
                            return;
                        }
                        case 2: {
                            sendEmailInvitationButtonPressed = false;
                            getInviteLayoutToInitState();


                            hideKeyboard();
                        }
                    }
                } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    friendsSmallBarLayout.setVisibility(View.GONE);
                    switch (panelNumber) {
                        case 0: {
                            slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                            return;
                        }
                        case 1: {
                            // openKeyboardForEditText(nicknameEditText);

                            return;
                        }
                        case 2: {
                            sendEmailInvitationButtonPressed = true;
                            setUpEmailInvitationLayout();

                            //TODO: Problematic when dragging back while keyboard is coming
                            //openKeyboardForEditText(inviteEmailEditText);
                        }
                    }
                }
            }
        };

        slidingPaneLayout.addPanelSlideListener(mainPanelSlideListener);
        setTouchListenersForPanel();
        setUpFriendsSearch();
        setUpLocalSearch();
        getUserFriends();
    }

    private void attachRecycleViews() {
        // use a linear layout manager
        mFriendsLayoutManager = new LinearLayoutManager(getActivity());
        friendsRecycleView.setLayoutManager(mFriendsLayoutManager);
        mFriendsAdapter = new FriendsRecyclerViewAdapter(mPager,friendsList);
        friendsRecycleView.setAdapter(mFriendsAdapter);

        mGroupsLayoutManager = new GridLayoutManager(getContext(), 3);
        groupsRecycleView.setLayoutManager(mGroupsLayoutManager);
        mGroupsAdapter = new GroupsRecycleViewAdapter(getContext(), getDummyGroupArrayList());
        groupsRecycleView.setAdapter(mGroupsAdapter);


    }

    private void setTypefaces() {
        final Typeface bottomBarIcons = TypefaceUtil.getTypeFace(getContext(), ICONS_FONT);
        final Typeface socialFont = TypefaceUtil.getTypeFace(getContext(), SOCIAL_ICONS_FONT);

        if (bottomBarIcons != null) {
            addFriendButton.setTypeface(bottomBarIcons);
            findFriendButton.setTypeface(bottomBarIcons);
            inviteFriendButton.setTypeface(bottomBarIcons);
        }

        if (socialFont != null) {
            twitterInviteButton.setTypeface(socialFont);
            facebookInviteButton.setTypeface(socialFont);
            googleInviteButton.setTypeface(socialFont);
            emailInviteButton.setTypeface(socialFont);
        }

    }

    //-----------------------------UI UTIL METHODS-------------------------------------------------


    private void returnSlidingPanelToInitialState() {

        addFriendButton.setVisibility(View.VISIBLE);
        addFriendButton.setGravity(Gravity.CENTER_HORIZONTAL);
        inviteFriendButton.setVisibility(View.VISIBLE);
        inviteFriendButton.setGravity(Gravity.LEFT);
        findFriendButton.setVisibility(View.VISIBLE);
        findFriendButton.setGravity(Gravity.RIGHT);

        addFriendLayout.setVisibility(View.GONE);
        searchFriendLayout.setVisibility(View.GONE);
        inviteFriendLayout.setVisibility(View.GONE);
        friendsSmallBarLayout.setVisibility(View.VISIBLE);
        KeyboardUtil.hide(getActivity());

        if(friendsRecycleView.getAdapter() != mFriendsAdapter) {
            friendsRecycleView.setAdapter(mFriendsAdapter);
        }

    }

    private void setTouchListenersForPanel() {

        addFriendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                addFriendLayoutGO();
                return false;
            }
        });


        inviteFriendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inviteFriendLayoutGO();
                return false;
            }
        });


        findFriendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchLayoutGO();
                return false;
            }
        });

    }

    //-----------------------------SEARCH LAYOUT SETUP----------------------------------------------
    private void searchLayoutGO() {
        panelNumber = 0;
        findFriendButton.setGravity(Gravity.RIGHT);
        slidingPaneLayout.setAnchorPoint(0.01f);
        addFriendButton.setVisibility(View.GONE);
        inviteFriendButton.setVisibility(View.GONE);
        searchFriendLayout.setVisibility(View.VISIBLE);
        searchFriendEditText.requestFocus();
    }

    //-----------------------------ADD FRIEND LAYOUT SETUP------------------------------------------
    private void addFriendLayoutGO() {
        panelNumber = 1;
        slidingPaneLayout.setAnchorPoint(1.0f);
        addFriendLayout.setVisibility(View.VISIBLE);
        inviteFriendButton.setVisibility(View.GONE);
        findFriendButton.setVisibility(View.GONE);
        nicknameEditText.requestFocus();
    }

    //-----------------------------INVITE FRIEND LAYOUT SETUP---------------------------------------
    private void inviteFriendLayoutGO() {
        panelNumber = 2;
        inviteFriendButton.setGravity(Gravity.CENTER_HORIZONTAL);
        slidingPaneLayout.setAnchorPoint(0.3f);
        inviteFriendLayout.setVisibility(View.VISIBLE);
        addFriendButton.setVisibility(View.GONE);
        findFriendButton.setVisibility(View.GONE);
    }

    private void getInviteLayoutToInitState() {
        slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        inviteFriendEmailLayout.setVisibility(View.GONE);
        facebookInviteButton.setVisibility(View.VISIBLE);
        googleInviteButton.setVisibility(View.VISIBLE);
        twitterInviteButton.setVisibility(View.VISIBLE);
    }

    private void setUpEmailInvitationLayout() {

        slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        inviteFriendEmailLayout.setVisibility(View.VISIBLE);
        inviteEmailEditText.requestFocus();
    }

    private void restoreSmallButtonsState() {
        myFriendsSmallButton.setBackground(getResources().getDrawable(R.color.transparent6));
        myGroupsSmallButton.setBackground(getResources().getDrawable(R.color.transparent6));
    }

    private void hideKeyboard() {
        KeyboardUtil.hide(getActivity());
    }


    private ArrayList<String> getDummyArrayList() {

        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            list.add(i, "Hello");
        }
        return list;

    }

    private ArrayList<Group> getDummyGroupArrayList() {

        ArrayList<Group> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            list.add(i, new Group("Test group", getResources().getDrawable(R.drawable.origami_logo)));
        }

        list.add(0, new Group("dkajskdjalsdjkasdjklasjdklasjdlkasdjlkasjdlkasdjafkjdfkljsdlkfjdlskfjldksfjlkdsfjlkdsjflkdsjflkdjslfkjsdlfkjdslkfjldksjflkdsjflkdsjflkdjsfkldjslfjdsklfjdslkfj", getResources().getDrawable(R.drawable.mapmarker)));
        return list;

    }

    public void setUpFriendsSearch() {
        ListView friendsSearchSuggestion = (ListView) view.findViewById(R.id.friends_suggestions);
        friendsSuggestionsList = new ArrayList<>();
        friendsSearchAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.search_friends_suggestion_list_item, friendsSuggestionsList);
        friendsSearchSuggestion.setAdapter(friendsSearchAdapter);
        nicknameEditText.addTextChangedListener(getNewTextWatcher());
        friendsSearchSuggestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                String friend = textView.getText().toString();
                if (!friendsList.contains(friend)) {
                    friendsList.add(0, friend);
                    addFriendToDB(friend);
                    mFriendsAdapter.notifyDataSetChanged();
                    nicknameEditText.setText("");
                    friendsSuggestionsList.clear();
                    friendsSearchAdapter.notifyDataSetChanged();
                    slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        });
    }

    public TextWatcher getNewTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Search_Friend_onChanged","" + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String request = s.toString();
                Log.d("Search_Friend", request);
                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                friendsSuggestionsList.clear();
                if (!request.equals("")) {
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                if (user.child("email").exists() && user.child("username").exists()
                                        && (user.child("email").getValue().toString().contains(request)
                                        || user.child("username").getValue().toString().contains(request))) {
                                    friendsSuggestionsList.add(user.child("username").getValue().toString()
                                            + "\n" + user.child("email").getValue().toString());
                                }
                            }
                            friendsSearchAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    friendsSearchAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    public void addFriendToDB(String friend) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Map<String, String> friendProfile = new HashMap<String, String>();
        friendProfile.put("username", friend.substring(0, friend.indexOf("\n")));
        friendProfile.put("email", friend.substring(friend.indexOf("\n") + 1));
        DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference("friends");
        String key = dataBase.child(currentUser.getUid()).push().getKey();
        dataBase.child(currentUser.getUid()).child(key).setValue(friendProfile);
        addToFriendDB(friend.substring(0, friend.indexOf("\n")), currentUser.getDisplayName(), currentUser.getEmail());
    }

    public void getUserFriends() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference("friends/" + currentUser.getUid());
        dataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsList.clear();
                for (DataSnapshot friend : dataSnapshot.getChildren()) {
                    String friendData = friend.child("username").getValue().toString() + "\n"
                            + friend.child("email").getValue().toString();
                    friendsList.add(0, friendData);
                }
                mFriendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private  void addToFriendDB(final String username, final String self_username, final String self_email) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    if (user.child("username").exists()
                            && user.child("username").getValue().toString().equals(username)) {
                        //UserIdNewFriend =
                        //Log.d("UserIdNewFriend",UserIdNewFriend);
                        Map<String, String> friendProfile = new HashMap<String, String>();
                        friendProfile.put("username", self_username);
                        friendProfile.put("email", self_email);
                        DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference("friends");
                        String key = dataBase.child(user.getKey()).push().getKey();
                        dataBase.child(user.getKey()).child(key).setValue(friendProfile);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUpLocalSearch() {
        searchFriendEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String request = s.toString();
                if(localSearchFriendsList == null) {
                    localSearchFriendsList = new ArrayList<String>();
                }
                if(localSearchAdapter == null) {
                    localSearchAdapter = new FriendsRecyclerViewAdapter(mPager,localSearchFriendsList);
                }
                localSearchFriendsList.clear();
                for(String friend : friendsList) {
                    if(friend.contains(request)) {
                        localSearchFriendsList.add(0,friend);
                    }
                }
                if(friendsRecycleView.getAdapter() != localSearchAdapter) {
                    friendsRecycleView.setAdapter(localSearchAdapter);
                }
                localSearchAdapter.notifyDataSetChanged();
            }
        });
    }
}






