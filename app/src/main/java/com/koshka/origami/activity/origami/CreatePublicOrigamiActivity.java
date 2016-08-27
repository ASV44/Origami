package com.koshka.origami.activity.origami;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koshka.origami.R;
import com.koshka.origami.model.Origami;
import com.koshka.origami.model.SimpleTextOrigami;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 8/27/16.
 */
public class CreatePublicOrigamiActivity extends AppCompatActivity {

    private static final String TAG = "CreateOrigamiActivity";

    @BindView(R.id.picked_place_text_view)
    TextView pickedPlaceTextView;

    @BindView(R.id.editOrigamiText)
    EditText editOrigamiText;

    private Place place;


    int PLACE_PICKER_REQUEST = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_public_origami);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.pick_a_place_button)
    public void pickAPlace(View view){

        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(data, this);
                pickedPlaceTextView.setText(place.getAddress());
       /*         String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();*/
            }
        }
    }


    @OnClick(R.id.create_public_origami_button)
    public void createPublicOrigami(View view){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference publicOrigamiRef = ref.child("public_origami");

        if (place != null){

            SimpleTextOrigami origami = new SimpleTextOrigami();
            origami.setPlaceId(place.getId());
            origami.setText(editOrigamiText.getText().toString());
            origami.setCreatedBy(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            publicOrigamiRef.push().setValue(origami).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.d(TAG, "Origami pushed succesfully");
                        goHome();
                    } else {
                        Log.d(TAG, "Could not push origami");
                    }
                }
            });

        }
    }


    private void goHome(){
        Intent intent = NavUtils.getParentActivityIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NavUtils.navigateUpTo(this, intent);
    }

}
