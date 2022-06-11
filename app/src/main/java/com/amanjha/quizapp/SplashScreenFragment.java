package com.amanjha.quizapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SplashScreenFragment extends Fragment {

    private FragmentActivity context;
    private int time = 1500;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (FragmentActivity) context;
    }

    public SplashScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            time = savedInstanceState.getInt("timer");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        if(time != 0) {
            new CountDownTimer(time, 100) {

                @Override
                public void onTick(long millisUntilFinished) {
                    time = (int) millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    time = 0;
                    FragmentManager fragmentManager = context.getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

                    if(fragment != null) {
                        FragmentFactory fragmentFactory = fragmentManager.getFragmentFactory();
                        String setup_screen = SetupScreenFragment.class.getName();
                        Fragment fragment1 = fragmentFactory.instantiate(context.getClassLoader(),setup_screen);
                        FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainer,fragment1).commit();
                    }
                }
            }.start();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("timer",time);
    }
}