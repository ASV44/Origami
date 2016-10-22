package com.koshka.origami.helpers.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.koshka.origami.R;
import com.koshka.origami.helpers.slidingbar.FriendsSlidingUpPanelHelper;

/**
 * Created by qm0937 on 10/21/16.
 */

public class FriendsFragmentHelper {

    private Activity activity;

    private FriendsSlidingUpPanelHelper friendsSlidingUpPanelHelper;

    private static final int REQUEST_INVITE = 0;

    public FriendsFragmentHelper(Activity activity) {

        this.activity = activity;
        friendsSlidingUpPanelHelper = new FriendsSlidingUpPanelHelper(activity);
    }

    public void buildFacebookInviteIntent(){
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
            AppInviteDialog.show(activity, content);
        }
    }

    public void buildEmailInviteIntent(){

        Resources res = activity.getResources();

        Intent intent = new AppInviteInvitation.IntentBuilder(res.getString(R.string.invitation_title))
                .setMessage(res.getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(res.getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(res.getString(R.string.invitation_custom_image)))
                .setCallToActionText(res.getString(R.string.invitation_cta))
                .build();
        activity.startActivityForResult(intent, REQUEST_INVITE);

    }
}
