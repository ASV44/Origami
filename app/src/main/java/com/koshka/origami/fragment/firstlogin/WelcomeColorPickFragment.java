package com.koshka.origami.fragment.firstlogin;


import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.FirstTimeLoginActivity;
import com.firebase.ui.database.service.PreferenceServiceImpl;
import com.koshka.origami.utils.ui.theme.OrigamiThemeHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qm0937 on 10/3/16.
 */

public class WelcomeColorPickFragment extends Fragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.color_next_button)
    Button nextButton;

    @BindView(R.id.colors_grid_view)
    GridView colorsGridView;

    @BindView(R.id.welcoming_origami_text)
    TextView welcomeText;

    private View previousView;

    private Drawable pickedDrawable;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private OrigamiThemeHelper helper;
    private PreferenceServiceImpl preferenceService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.color_pick_layout, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helper = new OrigamiThemeHelper(getActivity());
        preferenceService = new PreferenceServiceImpl();

        final String uid = mAuth.getCurrentUser().getUid();

        typeFaceSetUp();

        colorsGridView.setAdapter(new ColorsAdapter(getContext()));
        colorsGridView.setOnItemClickListener(this);
    }

    @OnClick(R.id.color_next_button)
    public void next() {

        FirstTimeLoginActivity activity = (FirstTimeLoginActivity) getActivity();
        activity.goToNextFragment();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (previousView != null) {
            previousView.setBackgroundColor(getResources().getColor(R.color.transparent6));
        }

        view.setBackgroundColor(getResources().getColor(R.color.transparent4));

        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.color_layout);
        pickedDrawable = layout.getBackground();

        view.getRootView().setBackground(pickedDrawable);
        previousView = view;

        preferenceService.changeTheme(position);

        helper.firebaseSetAndSaveTheme(position);

    }

    private void typeFaceSetUp() {
        final Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/actonia.ttf");

        if (font != null) {
            welcomeText.setTypeface(font);
        }

    }


}
