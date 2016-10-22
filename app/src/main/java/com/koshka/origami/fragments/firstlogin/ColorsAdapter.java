package com.koshka.origami.fragments.firstlogin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.koshka.origami.R;

/**
 * Created by qm0937 on 10/3/16.
 */

public class ColorsAdapter  extends BaseAdapter {
        private Context mContext;

        public ColorsAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {
                gridView = new View(mContext);

                // get layout from mobile.xml
                gridView = inflater.inflate(R.layout.settings_color_row, null);

                RelativeLayout layout = (RelativeLayout) gridView.findViewById(R.id.color_layout);
                layout.setBackground(mContext.getResources().getDrawable(mThumbIds[position]));

            } else {
                gridView = (View) convertView;
            }


            return gridView;
        }

        // references to our images
        private Integer[] mThumbIds = {

                R.drawable.amethist_gradient, R.drawable.bloody_mary_gradient,
                R.drawable.influenza_gradient, R.drawable.shroom_gradient,
                R.drawable.kashmir_gradient, R.drawable.grapefruit_sunset_gradient,
                R.drawable.moonrise_gradient, R.drawable.purple_bliss_gradient,
                R.drawable.passion_gradient, R.drawable.little_leaf_gradient,
                R.drawable.reef_gradient, R.drawable.sweet_morning_gradient,

        };
}