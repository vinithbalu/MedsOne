package com.gopher.MedsOne;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button scanButton;
    Button hamburgerButton;
    TextView welcomeTextView;
    RecyclerView dailyMedRecyclerView;
    LinearLayoutManager horizontalLayoutManager;
    HorizontalRecyclerViewAdapter horizontalAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        scanButton = view.findViewById(R.id.scan_button);
        hamburgerButton = view.findViewById(R.id.hamburgerButton);
        welcomeTextView = view.findViewById(R.id.welcomeTextView);
        dailyMedRecyclerView = view.findViewById(R.id.dailyMedRecyclerView);
        horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontalAdapter = new HorizontalRecyclerViewAdapter(DailyMed.loadDailyMedsList(), new OnDailyMedsClickListener() {
            @Override public void onItemClick(DailyMed dailyMed) {
                System.out.println("DailyMed "+dailyMed.getDate()+" clicked!");
            }
        });
        dailyMedRecyclerView.setNestedScrollingEnabled(true);
        dailyMedRecyclerView.setLayoutManager(horizontalLayoutManager);
        dailyMedRecyclerView.setAdapter(horizontalAdapter);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("scanButtonPressed");
                showAlertDialogButtonClicked(v);
            }
        });
        hamburgerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("hamburgerButton Pressed");
            }
        });
        return view;
    }

    public void showAlertDialogButtonClicked(View view) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select scan");

        // add the buttons
        String[] animals = {"QR Scan", "Prescription Scan", "Medicine Scan", "Cancel"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // qr
                        Intent i= new Intent(getContext(),ScanQRcodeActivity.class);
                        startActivity(i);
                        break;
                    case 1: // pres
                        Intent intent= new Intent(getContext(),PaperPrescriptionScan.class);
                        startActivity(intent);
                    case 2: // med
                    case 3: // cancel
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.MyView> {
        private List<DailyMed> dailyMeds;
        private final OnDailyMedsClickListener listener;

        public class MyView
                extends RecyclerView.ViewHolder {

            TextView dateTextView;
            TextView timeTextView;
            TextView medOneTextView;
            TextView medTwoTextView;
            public MyView(View view) {
                super(view);
                dateTextView = (TextView) view.findViewById(R.id.dateTextView);
                timeTextView = (TextView) view.findViewById(R.id.timeTextView);
                medOneTextView = (TextView) view.findViewById(R.id.medOneTextView);
                medTwoTextView = (TextView) view.findViewById(R.id.medTwoTextView);
            }
            public void bind(final DailyMed dailyMed, final OnDailyMedsClickListener listener) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        listener.onItemClick(dailyMed);
                    }
                });
            }
        }
        public HorizontalRecyclerViewAdapter(List<DailyMed> dailyMeds, OnDailyMedsClickListener listener) {
            this.dailyMeds = dailyMeds;
            this.listener = listener;
        }
        @NonNull
        @Override
        public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.daily_med_item, parent, false);
            return new MyView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyView holder, int position) {
            holder.dateTextView.setText(dailyMeds.get(position).getDate());
            holder.timeTextView.setText(dailyMeds.get(position).getTime());
            String medOne = dailyMeds.get(position).getMedsList().get(0);
            Boolean medOneTaken = dailyMeds.get(position).getMedsTakenList().get(0);
            holder.medOneTextView.setText(medOne + " " + medOneTaken);
            String medTwo = dailyMeds.get(position).getMedsList().get(1);
            Boolean medTwoTaken = dailyMeds.get(position).getMedsTakenList().get(1);
            holder.medTwoTextView.setText(medTwo + " " + medTwoTaken);
            holder.bind(dailyMeds.get(position), listener);
        }

        @Override
        public int getItemCount() {
            return dailyMeds.size();
        }
    }
}