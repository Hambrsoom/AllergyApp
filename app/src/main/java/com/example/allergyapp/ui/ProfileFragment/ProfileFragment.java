package com.example.allergyapp.ui.ProfileFragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class ProfileFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();

    String UID   = user.getUid();
    TextView allergies;
    Button submitBtn;
    Button resetBtn;
    View rootview;
    TextView userName;

    TextView tx3;

    private CheckBox checkSesame, checkPeanuts, checkTreeNuts, checkEggs, checkFish, checkSoy, checkMilk, checkShellFish,checkWheat;

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
    String temp="Empty";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootview        =  inflater.inflate(R.layout.profile_fragment, container, false);
        submitBtn       = rootview.findViewById(R.id.submit);
        resetBtn        = rootview.findViewById(R.id.resetbtn);
        checkSesame     = rootview.findViewById(R.id.sesame);
        checkPeanuts    = rootview.findViewById(R.id.peanuts);
        checkTreeNuts   = rootview.findViewById(R.id.tree_nuts);
        checkEggs       = rootview.findViewById(R.id.eggs);
        checkFish       = rootview.findViewById(R.id.fish);
        checkSoy        = rootview.findViewById(R.id.soy);
        checkMilk       = rootview.findViewById(R.id.milk);
        checkShellFish  = rootview.findViewById(R.id.shellFish);
        checkWheat      = rootview.findViewById(R.id.wheat);
        tx3             = rootview.findViewById(R.id.textView3);
        userName        = rootview.findViewById(R.id.userName);
        tx3.setText("Welcome "+new String(Character.toChars(0x1F44B)));

        //add listener for single value event
                (FirebaseDatabase.getInstance().getReference()).child("users").child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        temp = snapshot.getValue().toString();

                        if (temp.contains("sesame")) {
                            checkSesame.setChecked(true);
                        }if (temp.contains("peanuts")) {
                            checkPeanuts.setChecked(true);
                        }if (temp.contains("tree nuts")) {
                            checkTreeNuts.setChecked(true);
                        }if (temp.contains("eggs")) {
                            checkEggs.setChecked(true);
                        }if (temp.contains("shellfish")) {
                            checkFish.setChecked(true);
                        }if (temp.contains("soy")) {
                            checkSoy.setChecked(true);
                        }if (temp.contains("milk")) {
                            checkMilk.setChecked(true);
                        }if (temp.contains("shellfish")){
                            checkShellFish.setChecked(true);
                        }if (temp.contains("wheat")){
                            checkWheat.setChecked(true);
                        }
                        temp = "Empty";
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
        (FirebaseDatabase.getInstance().getReference()).child("user_profile").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                String tempUser = snapshot.getValue().toString();

                userName.setText(tempUser);
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
        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onClickReset(v);
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

        if (checkSesame.isChecked()) {
            s += "sesame,";
        }if (checkPeanuts.isChecked()) {
            s += "peanuts,";
        }if (checkTreeNuts.isChecked()) {
            s += "tree nuts,";
        }if (checkEggs.isChecked()) {
            s += "eggs,";
        }if (checkFish.isChecked()) {
            s += "fish,";
        }if (checkSoy.isChecked()) {
            s += "soy,";
        }if (checkMilk.isChecked()){
            s+= "milk,";
        }if (checkWheat.isChecked()){
            s+="wheat,";
        }if (checkShellFish.isChecked()){
            s+="shellfish,";
        }
        if(!checkSesame.isChecked() && !checkPeanuts.isChecked() && !checkTreeNuts.isChecked() && !checkEggs.isChecked() &&
        !checkFish.isChecked() && !checkSoy.isChecked()){
            s += "Empty,";
        }
        if (s.length() > 1) {
            s = s.substring(0,s.length()-1);
            FirebaseDatabase.getInstance().getReference().child("users").child(UID).setValue(s);
        }
        Toast.makeText(getActivity(),"Saved", Toast.LENGTH_LONG).show();
    }
    public void onClickReset(View view){
        FirebaseDatabase.getInstance().getReference().child("users").child(UID).setValue("Empty");
        Toast.makeText(getActivity(),"Reset", Toast.LENGTH_LONG).show();
        checkSesame.setChecked(false);
        checkPeanuts.setChecked(false);
        checkTreeNuts.setChecked(false);
        checkEggs.setChecked(false);
        checkFish.setChecked(false);
        checkSoy.setChecked(false);
        checkMilk.setChecked(false);
        checkShellFish.setChecked(false);
        checkWheat.setChecked(false);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();

    }
}
