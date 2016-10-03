package com.koshka.origami.fragment.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.koshka.origami.R;
import com.koshka.origami.fragment.friends.FriendsRecyclerViewAdapter;
import com.koshka.origami.fragment.groups.Group;
import com.koshka.origami.fragment.groups.GroupsRecycleViewAdapter;
import com.koshka.origami.utils.KeyboardUtil;
import com.koshka.origami.utils.TypefaceUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

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
    private static final int REQUEST_INVITE = 0;

    @BindView(R.id.friends_sliding_layout)
    SlidingUpPanelLayout slidingPaneLayout;

    @BindView(R.id.main_friends_panel_elements_layout)
    LinearLayout mainPanelElementsLayout;


    //------------------------------------------------

    @BindView(R.id.invite_friend_relative_layout)
    RelativeLayout inviteFriendLayout;

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

    @BindView(R.id.friends_main_layout)
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

    //Keep panel number on touch
    private int panelNumber;
    private boolean sendEmailInvitationButtonPressed = false;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    //------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();

        setUpUI();

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

    }

    //---------------------------------------------------------------------------------------------

    @OnClick(R.id.facebook_invite_button)
    public void facebookInvite() {
        //TODO: Doesn't work yet
        String appLinkUrl, previewImageUrl;

        appLinkUrl = "https://www.facebook.com/origamiworld1";
        previewImageUrl = "https://scontent.xx.fbcdn.net/v/t1.0-9/13164238_993899774051176_1452307493559266800_n.jpg?oh=18b0dcedb417430f865bc30e49102b89&oe=58AAA284";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .setPromotionDetails("Example", "EXAMPLE")
                    .build();
            AppInviteDialog.show(this, content);
        }
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
        //TODO: Doesn't work yet
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);

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
    }

    private void attachRecycleViews() {
        // use a linear layout manager
        mFriendsLayoutManager = new LinearLayoutManager(getActivity());
        friendsRecycleView.setLayoutManager(mFriendsLayoutManager);
        mFriendsAdapter = new FriendsRecyclerViewAdapter(getDummyArrayList());
        friendsRecycleView.setAdapter(mFriendsAdapter);

        mGroupsLayoutManager = new GridLayoutManager(getContext(), 3);
        groupsRecycleView.setLayoutManager(mGroupsLayoutManager);
        mGroupsAdapter = new GroupsRecycleViewAdapter(getContext(), getDummyGroupArrayList());
        groupsRecycleView.setAdapter(mGroupsAdapter);


    }

    private void setTypefaces() {
        final Typeface bottomBarIcons = TypefaceUtil.getTypeFace(getContext(),ICONS_FONT);
        final Typeface socialFont = TypefaceUtil.getTypeFace(getContext(),SOCIAL_ICONS_FONT);

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
        inviteFriendLayout.setVisibility(View.GONE);
        friendsSmallBarLayout.setVisibility(View.VISIBLE);
        KeyboardUtil.hide(getActivity());

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
    private void searchLayoutGO(){
        panelNumber = 0;
        findFriendButton.setGravity(Gravity.RIGHT);
        slidingPaneLayout.setAnchorPoint(0.01f);
        addFriendButton.setVisibility(View.GONE);
        inviteFriendButton.setVisibility(View.GONE);

    }
    //-----------------------------ADD FRIEND LAYOUT SETUP------------------------------------------
    private void addFriendLayoutGO(){
        panelNumber = 1;
        slidingPaneLayout.setAnchorPoint(1.0f);
        addFriendLayout.setVisibility(View.VISIBLE);
        inviteFriendButton.setVisibility(View.GONE);
        findFriendButton.setVisibility(View.GONE);
        nicknameEditText.requestFocus();
    }
    //-----------------------------INVITE FRIEND LAYOUT SETUP---------------------------------------
    private void inviteFriendLayoutGO(){
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

        list.add(0,new Group("dkajskdjalsdjkasdjklasjdklasjdlkasdjlkasjdlkasdjafkjdfkljsdlkfjdlskfjldksfjlkdsfjlkdsjflkdsjflkdjslfkjsdlfkjdslkfjldksjflkdsjflkdsjflkdjsfkldjslfjdsklfjdslkfj",getResources().getDrawable(R.drawable.mapmarker) ));
        return list;

    }
}






