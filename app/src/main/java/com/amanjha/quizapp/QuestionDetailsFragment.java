package com.amanjha.quizapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class QuestionDetailsFragment extends Fragment {

    private QuestionsViewModel questionsViewModel;
    private MainViewModel mainViewModel;
    private TextView timerTextView, questionTextView;
    private Button nextButton, prevButton, submitButton;
    private Switch toggleButton;
    private RadioButton option1, option2, option3, option4;
    private List<QuestionModel> questions;
    private AlertDialog alertDialog;
    private FragmentActivity myContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = (FragmentActivity) context;
        questionsViewModel = new ViewModelProvider(requireActivity()).get(QuestionsViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_details, container, false);
        timerTextView = view.findViewById(R.id.timer2);
        questionTextView = view.findViewById(R.id.question);
        nextButton = view.findViewById(R.id.next_button);
        prevButton = view.findViewById(R.id.previous_button);
        toggleButton = view.findViewById(R.id.toggleButton);
        submitButton = view.findViewById(R.id.submit_button2);
        option1 = view.findViewById(R.id.option_1);
        option2 = view.findViewById(R.id.option_2);
        option3 = view.findViewById(R.id.option_3);
        option4 = view.findViewById(R.id.option_4);

        timerTextView.setText("Time Limit: " + mainViewModel.getTimeLeft().getValue() / 1000 + " sec");
        questionTextView.setText(questionsViewModel.getCurrentQuestion().getValue().getQuestion() + "");
        option1.setText(questionsViewModel.getCurrentQuestion().getValue().getOptions().get(0) + "");
        option2.setText(questionsViewModel.getCurrentQuestion().getValue().getOptions().get(1) + "");
        option3.setText(questionsViewModel.getCurrentQuestion().getValue().getOptions().get(2) + "");
        option4.setText(questionsViewModel.getCurrentQuestion().getValue().getOptions().get(3) + "");

        if (questionsViewModel.getQuestionPosition().getValue() == 0) {
            prevButton.setEnabled(false);
            prevButton.setClickable(false);
            prevButton.setBackgroundColor(getResources().getColor(R.color.gray));
        }

        if (questionsViewModel.getQuestionPosition().getValue() == 9) {
            nextButton.setEnabled(false);
            nextButton.setClickable(false);
            nextButton.setBackgroundColor(getResources().getColor(R.color.gray));
        }

        if (questionsViewModel.getBookmarkStatus().getValue() == null) {
            int size = mainViewModel.getQuestionsLiveData().getValue().size();
            ArrayList<Boolean> flag = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                flag.add(false);
            }
            questionsViewModel.setBookmarkStatus(flag);
            toggleButton.setChecked(false);
        } else {

            ArrayList<Boolean> bookmarkList = questionsViewModel.getBookmarkStatus().getValue();
            for (int i = 0; i < bookmarkList.size(); i++) {
                if (i == questionsViewModel.getQuestionPosition().getValue()) {
                    if (bookmarkList.get(i)) {
                        toggleButton.setChecked(true);
                    } else {
                        toggleButton.setChecked(false);
                    }
                    break;
                }
            }
        }

        if (questionsViewModel.getSelectedOption().getValue() == null) {
            int size = mainViewModel.getQuestionsLiveData().getValue().size();
            ArrayList<String> flag = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                flag.add("nothing");
            }
            questionsViewModel.setSelectedOption(flag);

        } else {
            setRadioButtons();
        }

        if (questionsViewModel.getAnswerStatus().getValue() == null) {
            int size = mainViewModel.getQuestionsLiveData().getValue().size();
            ArrayList<Boolean> flag = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                flag.add(false);
            }
            questionsViewModel.setAnswerStatus(flag);
        }

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Boolean> bookmarkList = questionsViewModel.getBookmarkStatus().getValue();
                for (int i = 0; i < bookmarkList.size(); i++) {
                    if (i == questionsViewModel.getQuestionPosition().getValue()) {
                        bookmarkList.set(i, !(bookmarkList.get(questionsViewModel.getQuestionPosition().getValue())));
                        break;
                    }
                }
                questionsViewModel.setBookmarkStatus(bookmarkList);
                changeBookMarkText();
            }
        });

        questions = mainViewModel.getQuestionsLiveData().getValue();

        prevButton.setOnClickListener(v -> {

            if (!nextButton.isEnabled()) {
                nextButton.setEnabled(true);
                nextButton.setClickable(true);
                nextButton.setBackgroundColor(getResources().getColor(R.color.whatsapp_green));
            }
            questionsViewModel.setQuestionPosition(questionsViewModel.getQuestionPosition().getValue() - 1);

            if (questionsViewModel.getQuestionPosition().getValue() >= 0) {
                questionsViewModel.setCurrentQuestion(questions.get(questionsViewModel.getQuestionPosition().getValue()));
                setQuestionAndOption();
                setRadioButtons();

                if (questionsViewModel.getQuestionPosition().getValue() == 0) {
                    prevButton.setEnabled(false);
                    prevButton.setClickable(false);
                    prevButton.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }
            changeBookMarkText();

        });

        nextButton.setOnClickListener(v -> {

            if (!prevButton.isEnabled()) {
                prevButton.setEnabled(true);
                prevButton.setClickable(true);
                prevButton.setBackgroundColor(getResources().getColor(R.color.whatsapp_green));
            }
            questionsViewModel.setQuestionPosition(questionsViewModel.getQuestionPosition().getValue() + 1);
            if (questionsViewModel.getQuestionPosition().getValue() < 10) {
                questionsViewModel.setCurrentQuestion(questions.get(questionsViewModel.getQuestionPosition().getValue()));
                setQuestionAndOption();
                setRadioButtons();

                if (questionsViewModel.getQuestionPosition().getValue() == 9) {
                    nextButton.setEnabled(false);
                    nextButton.setClickable(false);
                    nextButton.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }
            changeBookMarkText();

        });


        option1.setOnClickListener(v -> {
            setAnswerStatus();
            option4.setChecked(false);
            option3.setChecked(false);
            option2.setChecked(false);
            option1.setChecked(true);
            setSelectedOptionsList();
        });

        option2.setOnClickListener(v -> {
            setAnswerStatus();
            option4.setChecked(false);
            option3.setChecked(false);
            option1.setChecked(false);
            option2.setChecked(true);
            setSelectedOptionsList();
        });

        option3.setOnClickListener(v -> {
            setAnswerStatus();
            option4.setChecked(false);
            option1.setChecked(false);
            option2.setChecked(false);
            option3.setChecked(true);
            setSelectedOptionsList();
        });

        option4.setOnClickListener(v -> {
            setAnswerStatus();
            option1.setChecked(false);
            option2.setChecked(false);
            option3.setChecked(false);
            option4.setChecked(true);
            setSelectedOptionsList();
        });

        //starting timer on question detail screen
        if (mainViewModel.getTimeLeft().getValue() != 0) {
            new CountDownTimer(mainViewModel.getTimeLeft().getValue(), 1000) {

                public void onTick(long millisUntilFinished) {
                    int minute = (int) ((millisUntilFinished / 1000) / 60);
                    int second = (int) ((millisUntilFinished / 1000) % 60);

                    timerTextView.setText("time limit: " + minute + ":" + second);

                    mainViewModel.changeTimeLeftValue((int) millisUntilFinished);

                }

                public void onFinish() {
                    timerTextView.setText("time limit: 00:00");

                    final FragmentManager fragmentManager = myContext.getSupportFragmentManager();

                    final Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

                    if (alertDialog != null)
                        alertDialog.dismiss();

                    if (fragment instanceof QuestionDetailsFragment) {
                        showSummaryScreen();
                    }
                    mainViewModel.changeTimeLeftValue(0);
                }

            }.start();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog = new AlertDialog.Builder(myContext)
                        .setTitle("MCQ Quiz")
                        .setMessage("Are you sure ? you want to Submit the test ?")
                        .setPositiveButton("YES", (dialog, which) -> {
                            dialog.dismiss();
                            showSummaryScreen();
                        })
                        .setNegativeButton("NO", (dialog, which) -> {

                            dialog.dismiss();
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .create();

                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

            }
        });

        return view;
    }

    private void setAnswerStatus() {
        ArrayList<Boolean> answerList = questionsViewModel.getAnswerStatus().getValue();
        for (int i = 0; i < answerList.size(); i++) {
            if (i == questionsViewModel.getQuestionPosition().getValue()) {
                answerList.set(i, true);
                break;
            }
        }
        questionsViewModel.setAnswerStatus(answerList);
    }

    private void setSelectedOptionsList() {
        ArrayList<String> list = questionsViewModel.getSelectedOption().getValue();
        for (int i = 0; i < list.size(); i++) {
            if (i == questionsViewModel.getQuestionPosition().getValue()) {
                String text = "";
                if (option1.isChecked()) {
                    text += option1.getText().toString();
                } else if (option2.isChecked()) {
                    text += option2.getText().toString();
                } else if (option3.isChecked()) {
                    text += option3.getText().toString();
                } else {
                    text += option4.getText().toString();
                }
                list.set(i, text);
                break;
            }
        }
        questionsViewModel.setSelectedOption(list);
    }

    private void setRadioButtons() {
        String text = questionsViewModel.getSelectedOption().getValue().get(questionsViewModel.getQuestionPosition().getValue());
        if (text.equals("nothing")) {
            option1.setChecked(false);
            option2.setChecked(false);
            option3.setChecked(false);
            option4.setChecked(false);
        } else {

            if (option1.getText().toString().equals(text)) {
                option2.setChecked(false);
                option3.setChecked(false);
                option4.setChecked(false);
                option1.setChecked(true);
            } else if (option2.getText().toString().equals(text)) {
                option1.setChecked(false);
                option3.setChecked(false);
                option4.setChecked(false);
                option2.setChecked(true);

            } else if (option3.getText().toString().equals(text)) {
                option1.setChecked(false);
                option2.setChecked(false);
                option4.setChecked(false);
                option3.setChecked(true);
            } else {
                option1.setChecked(false);
                option2.setChecked(false);
                option3.setChecked(false);
                option4.setChecked(true);
            }
        }
    }

    private void changeBookMarkText() {
        if (questionsViewModel.getBookmarkStatus().getValue().get(questionsViewModel.getQuestionPosition().getValue()) == false) {
            toggleButton.setChecked(false);
        } else {
            toggleButton.setChecked(true);
        }
    }

    //showing SummaryScreen fragment
    private void showSummaryScreen() {
        final FragmentManager fragmentManager = myContext.getSupportFragmentManager();
        final Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if (fragment != null) {
            final FragmentFactory fragmentFactory = fragmentManager.getFragmentFactory();
            final String questionsScreen = SummaryScreenFragment.class.getName();
            final Fragment fragment1 = fragmentFactory.instantiate(myContext.getClassLoader(), questionsScreen);

            Bundle args = new Bundle();
            args.putInt("time_left", mainViewModel.getTimeLeft().getValue());
            fragment1.setArguments(args);
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, fragment1).commit();
        }

    }

    private void setQuestionAndOption() {
        questionTextView.setText(questionsViewModel.getCurrentQuestion().getValue().getQuestion() + "");
        option1.setText(questionsViewModel.getCurrentQuestion().getValue().getOptions().get(0) + "");
        option2.setText(questionsViewModel.getCurrentQuestion().getValue().getOptions().get(1) + "");
        option3.setText(questionsViewModel.getCurrentQuestion().getValue().getOptions().get(2) + "");
        option4.setText(questionsViewModel.getCurrentQuestion().getValue().getOptions().get(3) + "");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

}