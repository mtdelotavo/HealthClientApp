package team7.seshealthpatient.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.telecom.ConnectionService;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import team7.seshealthpatient.Activities.MainActivity;
import team7.seshealthpatient.Activities.ProfileActivity;
import team7.seshealthpatient.Patient;
import team7.seshealthpatient.R;

public class DiagnosisHistoryFragment extends Fragment {

    private final static String TAG = "DiagnosisHistoryFragment";
    private FirebaseUser mUser;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ListView diagnosisListView;

    @BindView(R.id.noDiagnosticsMadeTV)
    TextView noDiagnosticsMadeTV;

    public DiagnosisHistoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Diagnosis History");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        mUser = ((MainActivity)getActivity()).getFirebaseAuth().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diagnosis_history, container, false);

        ButterKnife.bind(this, view);
        
        final List<String[]> diagnosisList = new ArrayList<>();

        final ListAdapter diagnosisListAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                diagnosisList
        );

        diagnosisListView.setAdapter(diagnosisListAdapter);

        String uuid = mUser.getUid();

        reference.child(uuid).child("Diagnosis")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        diagnosisList.clear();
                        noDiagnosticsMadeTV.setVisibility(View.VISIBLE);

                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String key = child.getKey();
                            String timeStamp = (child.child("Timestamp").getValue() != null)
                                    ? child.child("Timestamp").getValue().toString() : null;
                            if (timeStamp != null) {
                                String[] diagnosisArr = new String[] {key, timeStamp};
                                diagnosisList.add(diagnosisArr);
                                diagnosisListView.invalidateViews();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Now that the view has been created, we can use butter knife functionality
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
