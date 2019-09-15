package com.example.allergyapp.ui.ProfileFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.allergyapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ProfileFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();

    String UID   = user.getUid();
    TextView allergies;
    Button submitBtn;
    View rootview;

    TextView tx3;

    private CheckBox check1, check2, check3, check4, check5, check6;

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
    String temp="Empty";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootview =  inflater.inflate(R.layout.profile_fragment, container, false);
        submitBtn = rootview.findViewById(R.id.submit);
        check1 = (CheckBox)rootview.findViewById(R.id.sesame);
        check2 = (CheckBox)rootview.findViewById(R.id.peanuts);
        check3 = (CheckBox)rootview.findViewById(R.id.tree_nuts);
        check4 = (CheckBox)rootview.findViewById(R.id.eggs);
        check5 = (CheckBox)rootview.findViewById(R.id.fish);
        check6 = (CheckBox)rootview.findViewById(R.id.soy);
        tx3 = rootview.findViewById(R.id.textView3);
        tx3.setText("Welcome "+new String(Character.toChars(0x1F44B)));

        //add listener for single value event
                (FirebaseDatabase.getInstance().getReference()).child("users").child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        temp = snapshot.getValue().toString();

                        if (temp.contains("Dairy")) {
                            check1.setChecked(true);
                        }if (temp.contains("Peanuts")) {
                            check2.setChecked(true);
                        }if (temp.contains("Tree Nuts")) {
                            check3.setChecked(true);
                        }if (temp.contains("Eggs")) {
                            check4.setChecked(true);
                        }if (temp.contains("Shellfish")) {
                            check5.setChecked(true);
                        }if (temp.contains("Soy")) {
                            check6.setChecked(true);
                        }
                        temp = "Empty";
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
        submitBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onClickSave(v);
            }

        });

        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }
    public void onClickSave(View view){

        String s = "";
        check1 = (CheckBox)rootview.findViewById(R.id.sesame);
        check2 = (CheckBox)rootview.findViewById(R.id.peanuts);
        check3 = (CheckBox)rootview.findViewById(R.id.tree_nuts);
        check4 = (CheckBox)rootview.findViewById(R.id.eggs);
        check5 = (CheckBox)rootview.findViewById(R.id.fish);
        check6 = (CheckBox)rootview.findViewById(R.id.soy);

        if (check1.isChecked()) {
            s += "Dairy,";
        }if (check2.isChecked()) {
            s += "Peanuts,";
        }if (check3.isChecked()) {
            s += "Tree Nuts,";
        }if (check4.isChecked()) {
            s += "Eggs,";
        }if (check5.isChecked()) {
            s += "Shellfish,";
        }if (check6.isChecked()) {
            s += "Soy,";
        }
        if(!check1.isChecked() && !check2.isChecked() && !check3.isChecked() && !check4.isChecked() &&
        !check5.isChecked() && !check6.isChecked()){
            s += "Empty,";
        }
        if (s.length() > 1) {
            s = s.substring(0,s.length()-1);
            FirebaseDatabase.getInstance().getReference().child("users").child(UID).setValue(s);
        }
        //Toast toast = Toast.makeText(ProfileFragment.this,"Generating PDF",Toast.LENGTH_LONG);

        //ADD TOAST IN HERE LATER !
    }
}
