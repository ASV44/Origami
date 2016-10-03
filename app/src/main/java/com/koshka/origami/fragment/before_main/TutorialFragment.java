package com.koshka.origami.fragment.before_main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koshka.origami.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.message;
import static android.app.Activity.RESULT_OK;

/**
 * Created by qm0937 on 10/3/16.
 */

public class TutorialFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.last_page_set_up_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.finish_set_up_button)
    public void button(){


        Intent intent=new Intent();
        intent.putExtra("MESSAGE",message);

        getActivity().setResult(RESULT_OK, intent);

        getActivity().finish();
    }

}
