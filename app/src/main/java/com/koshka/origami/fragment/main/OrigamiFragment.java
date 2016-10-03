package com.koshka.origami.fragment.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koshka.origami.R;
import com.koshka.origami.fragment.friends.FriendsRecyclerViewAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 7/20/16.
 */
public class OrigamiFragment extends Fragment {

    private static final String TAG = "OrigamiFragment";

    @BindView(R.id.origami_create_button)
    TextView origamiCreateButton;

    @BindView(R.id.origami_type_one_button)
    TextView origamiTypeOneButton;

    @BindView(R.id.origami_type_three_button)
    TextView origamiTypeThreeButton;


    @BindView(R.id.origami_sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;

    //------------------------------------------------
    @BindView(R.id.recycler_view)
    RecyclerView origamiRecycleView;
    private RecyclerView.Adapter mOrigamiAdapter;
    private RecyclerView.LayoutManager mOrigamiLayoutManager;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_origami, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        final Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/origamibats.ttf");

        if (font != null){
            origamiCreateButton.setTypeface(font);
            origamiTypeOneButton.setTypeface(font);
            origamiTypeThreeButton.setTypeface(font);
        }
        attachRecycleViews();

    }

    private void attachRecycleViews(){
        mOrigamiLayoutManager = new LinearLayoutManager(getActivity());

        origamiRecycleView.setLayoutManager(mOrigamiLayoutManager);

        mOrigamiAdapter = new FriendsRecyclerViewAdapter(getDummyArrayList());
        origamiRecycleView.setAdapter(mOrigamiAdapter);

    }

    private ArrayList<String> getDummyArrayList(){

        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i<100; i++){
            list.add(i, "Hello");
        }

        return list;


    }


}
