package com.amanjha.quizapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SetupScreenFragment extends Fragment {

    FragmentActivity context;
    Button start_btn;

    public SetupScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context =(FragmentActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup_screen, container, false);
        start_btn = view.findViewById(R.id.start_quiz_button);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = context.getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

                if(fragment != null) {
                    FragmentFactory fragmentFactory = fragmentManager.getFragmentFactory();
                    String questionList = QuestionListFragment.class.getName();
                    Fragment fragment1 = fragmentFactory.instantiate(context.getClassLoader(),questionList);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment1).commit();
                }
            }
        });

        return view;
    }

}