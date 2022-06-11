package com.amanjha.quizapp;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SummaryScreenFragment extends Fragment {

    private Button btnRestart, btnExit;
    private TextView totalScore, timeTaken;
    private FragmentActivity context;
    private int time;
    private MainViewModel mainViewModel;
    private QuestionsViewModel questionsViewModel;
    private int totalTime = 3 * 60 * 1000;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (FragmentActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary_screen, container, false);

        time = getArguments().getInt("time_left", -1);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        questionsViewModel = new ViewModelProvider(requireActivity()).get(QuestionsViewModel.class);
        btnRestart = view.findViewById(R.id.restart_button);
        btnExit = view.findViewById(R.id.exit_app);
        totalScore = view.findViewById(R.id.score);
        timeTaken = view.findViewById(R.id.time_taken);

        if (time == 0) {
            Toast.makeText(context, "Quiz automatically submitted", Toast.LENGTH_SHORT).show();
        }


        ArrayList<String> selectedOptions = questionsViewModel.getSelectedOption().getValue();
        ArrayList<String> correctAnswers = new ArrayList<>();
        if (selectedOptions != null) {
            for (int i = 0; i < selectedOptions.size(); i++) {
                correctAnswers.add(mainViewModel.getQuestionsListLivaData().getValue().get(i).getCorrectOption());
            }

            int correctCount = 0;
            for (int i = 0; i < selectedOptions.size(); i++) {
                String userSelected = selectedOptions.get(i);
                String correctAnswer = correctAnswers.get(i);
                if (userSelected.equals(correctAnswer)) {
                    correctCount++;
                }
            }

            totalScore.setText("Score: " + correctCount + " out of " + 10);

            if (time == 0) {
                timeTaken.setText("time taken: 3 min");
            } else {
                time = totalTime - time;
                int min = (time / 1000) / 60;
                int sec = (time / 1000) % 60;
                timeTaken.setText("time taken: " + min + " min & " + sec + " sec");
            }
        } else {
            time = totalTime - time;
            int min = (time / 1000) / 60;
            int sec = (time / 1000) % 60;
            timeTaken.setText("time taken: " + min + " min & " + sec + " sec");
            totalScore.setText("Score: 0/ out of " + 10);
        }

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
                startActivity(context.getIntent());
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                context.finish();
            }
        });

        return view;
    }
}