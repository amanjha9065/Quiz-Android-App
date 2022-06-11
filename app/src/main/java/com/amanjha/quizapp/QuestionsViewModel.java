package com.amanjha.quizapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.amanjha.quizapp.MainViewModel;

import java.util.ArrayList;

public class QuestionsViewModel extends AndroidViewModel {
    private MutableLiveData<QuestionModel> currentQuestion = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Boolean>> bookmarkStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> position = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> selectedOption = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Boolean>> answerStatus = new MutableLiveData<>();

    public QuestionsViewModel(Application application) {
        super(application);
        currentQuestion.setValue(null);
        bookmarkStatus.setValue(null);
        position.setValue(null);
        selectedOption.setValue(null);
        answerStatus.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public MutableLiveData<QuestionModel> getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(QuestionModel question) {
        currentQuestion.setValue(question);
    }

    public MutableLiveData<ArrayList<Boolean>> getBookmarkStatus() {
        return bookmarkStatus;
    }

    public void setBookmarkStatus(ArrayList<Boolean> bookmarkstatus) {
        bookmarkStatus.setValue(bookmarkstatus);
    }

    public MutableLiveData<Integer> getQuestionPosition() {
        return position;
    }

    public void setQuestionPosition(int val) {
        position.setValue(val);
    }

    public MutableLiveData<ArrayList<String>> getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(ArrayList<String> choosenOption) {
        selectedOption.setValue(choosenOption);
    }

    public MutableLiveData<ArrayList<Boolean>> getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(ArrayList<Boolean> answer) {
        answerStatus.setValue(answer);
    }
}