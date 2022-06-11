package com.amanjha.quizapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class QuestionListFragment extends Fragment implements CustomAdapter.OnItemClickListener {

    FragmentActivity context;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    AlertDialog failureDialog;
    MainViewModel mainViewModel;
    TextView textViewTimer;
    Button btnSubmit;
    RecyclerView recyclerView;
    QuestionsViewModel questionsViewModel;
    CustomAdapter customAdapter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (FragmentActivity) context;

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        if (mainViewModel.getTimeLeft().getValue() == -1) {
            mainViewModel.getQuestionsLiveData().observe(this, list -> {
                showRecyclerViewList();
            });
            mainViewModel.getRequestStatusLiveData().observe(this, requestStatus -> {
                switch (requestStatus) {
                    case IN_PROGRESS:
                        showSpinner();
                        break;
                    case SUCCEEDED:
                        hideSpinner();
                        break;
                    case FAILED:
                        showError();
                        break;
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        textViewTimer = view.findViewById(R.id.time_left);
        btnSubmit = view.findViewById(R.id.submit_button);

        if (mainViewModel.getTimeLeft().getValue() == -1) {
            mainViewModel.changeTimeLeftValue(3 * 60 * 1000);
        } else {
            showRecyclerViewList();
        }


        return view;
    }

    public void showRecyclerViewList() {
        questionsViewModel = new ViewModelProvider(requireActivity()).get(QuestionsViewModel.class);

        Fragment fragment = context.getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        ArrayList<Boolean> empty = new ArrayList<>();
        for (int i = 0; i < mainViewModel.getQuestionsListLivaData().getValue().size(); i++) {
            empty.add(false);
        }
        if (questionsViewModel.getBookmarkStatus().getValue() == null) {
            if (questionsViewModel.getAnswerStatus().getValue() == null) {
                customAdapter = new CustomAdapter(mainViewModel.getQuestionsListLivaData().getValue(), empty, empty, (CustomAdapter.OnItemClickListener) fragment);
            } else {
                customAdapter = new CustomAdapter(mainViewModel.getQuestionsListLivaData().getValue(), questionsViewModel.getAnswerStatus().getValue(), empty, (CustomAdapter.OnItemClickListener) fragment);
            }
        } else {
            if (questionsViewModel.getAnswerStatus().getValue() != null) {
                customAdapter = new CustomAdapter(mainViewModel.getQuestionsListLivaData().getValue(), questionsViewModel.getAnswerStatus().getValue(), questionsViewModel.getBookmarkStatus().getValue(), (CustomAdapter.OnItemClickListener) fragment);
            } else {
                customAdapter = new CustomAdapter(mainViewModel.getQuestionsListLivaData().getValue(), empty, questionsViewModel.getBookmarkStatus().getValue(), (CustomAdapter.OnItemClickListener) fragment);
            }
        }
        recyclerView.setAdapter(customAdapter);

        textViewTimer.setText("time limit: " + mainViewModel.getTimeLeft().getValue() + " sec");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(context)
                        .setTitle("ENGLISH Quiz")
                        .setMessage("Are you sure ?")
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

        if (mainViewModel.getTimeLeft().getValue() != 0) {
            new CountDownTimer(mainViewModel.getTimeLeft().getValue(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int min = (int) ((millisUntilFinished / 1000) / 60);
                    int sec = (int) ((millisUntilFinished / 1000) % 60);

                    textViewTimer.setText("time limit: " + min + ":" + sec);
                    mainViewModel.changeTimeLeftValue((int) millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    mainViewModel.changeTimeLeftValue(0);
                    textViewTimer.setText("time limit: 00:00");
                    FragmentManager fragmentManager = context.getSupportFragmentManager();
                    Fragment fragment1 = fragmentManager.findFragmentById(R.id.fragmentContainer);
                    if (fragment1 instanceof QuestionListFragment) {
                        showSummaryScreen();
                    }
                }
            }.start();
        }
    }


    public void showSummaryScreen() {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if (fragment != null) {
            FragmentFactory fragmentFactory = fragmentManager.getFragmentFactory();
            String summaryFragment = SummaryScreenFragment.class.getName();
            Fragment fragment1 = fragmentFactory.instantiate(context.getClassLoader(), summaryFragment);
            Bundle args = new Bundle();
            args.putInt("time_left", mainViewModel.getTimeLeft().getValue());
            fragment1.setArguments(args);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, fragment1).commit();
        }
    }

    public void onItemClicked(int position) {
        QuestionModel questionModel = mainViewModel.getQuestionsLiveData().getValue().get(position);

        //show questions detail screen
        final FragmentManager fragmentManager = context.getSupportFragmentManager();

        final Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if (fragment != null) {
            final FragmentFactory fragmentFactory = fragmentManager.getFragmentFactory();
            final String detailScreen = QuestionDetailsFragment.class.getName();
            final Fragment fragment1 = fragmentFactory.instantiate(context.getClassLoader(), detailScreen);

            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, fragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            questionsViewModel.setCurrentQuestion(questionModel);
            questionsViewModel.setQuestionPosition(position);
        }
    }

    private void showSpinner() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Fetching Questions");
            progressDialog.setMessage("Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void hideSpinner() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void showError() {
        hideSpinner();
        if (failureDialog == null) {
            failureDialog = getFailureDialog();
        }
        failureDialog.show();
        failureDialog.setCanceledOnTouchOutside(false);
    }

    private AlertDialog getFailureDialog() {
        return new AlertDialog.Builder(context)
                .setTitle("Questions list request failed")
                .setMessage("Questions list fetching is failed, do you want to retry?")
                .setPositiveButton("Retry", (dialog, which) -> {
                    dialog.dismiss();
                    mainViewModel.refetchQuestions();
                })
                .setNegativeButton("Close app", (dialog, which) -> {
                    dialog.dismiss();
                    context.finish();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (failureDialog != null) {
            failureDialog.dismiss();
        }

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}