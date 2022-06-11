package com.amanjha.quizapp;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainViewModel extends AndroidViewModel implements Response.Listener<String>, Response.ErrorListener {

    private MutableLiveData<List<QuestionModel>> questionsListLivaData = new MutableLiveData<>();
    private MutableLiveData<Integer> timeLeft = new MutableLiveData<>();
    private RequestQueue requestQueue;
    private MutableLiveData<RequestStatus> requestStatusLiveData = new MutableLiveData<>();
    private String questionUrl = "https://raw.githubusercontent.com/tVishal96/sample-english-mcqs/master/db.json";
    public MainViewModel(Application application) {
        super(application);
        requestQueue = Volley.newRequestQueue(application);
        requestStatusLiveData.postValue(RequestStatus.IN_PROGRESS);
        timeLeft.setValue(-1);
        fetchQuestions();
    }

    public void refetchQuestions() {
        requestStatusLiveData.postValue(RequestStatus.IN_PROGRESS);
        fetchQuestions();
    }

    public LiveData<List<QuestionModel>> getQuestionsLiveData() {
        return questionsListLivaData;
    }

    public MutableLiveData<Integer> getTimeLeft() {
        return timeLeft;
    }
    public void changeTimeLeftValue(int time) {
        timeLeft.setValue(time);
    }

    private void fetchQuestions() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, questionUrl, this, this);
        requestQueue.add(stringRequest);
    }

    public LiveData<RequestStatus> getRequestStatusLiveData() {
        return requestStatusLiveData;
    }

    @Override
    public void onResponse(String response) {
        try {
            List<QuestionModel> questionsList = parseResponse(response);
            questionsListLivaData.postValue(questionsList);
            requestStatusLiveData.postValue(RequestStatus.SUCCEEDED);
        } catch (JSONException e) {
            e.printStackTrace();
            requestStatusLiveData.postValue(RequestStatus.FAILED);
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        requestStatusLiveData.postValue(RequestStatus.FAILED);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private List<QuestionModel> parseResponse(String response) throws JSONException {
        List<QuestionModel> questionModels = new ArrayList<>();
        JSONObject res = new JSONObject(response);
        JSONArray questionsData = res.optJSONArray("questions");

        if(questionsData == null) {
            return questionModels;
        }
        for (int i=0; i< questionsData.length(); i++ ) {
            JSONObject obj = (JSONObject) questionsData.get(i);
            String question = obj.optString("question");
            JSONArray options = obj.optJSONArray("options");
            ArrayList<String> options_list = new ArrayList<>();
            for(int j=0; j<options.length(); j++) {
                options_list.add(options.getString(j));
            }
            String correct_option = options_list.get(obj.optInt("correct_option"));

            Collections.shuffle(options_list);

            QuestionModel questionModel = new QuestionModel(question, correct_option, options_list);
            questionModels.add(questionModel);
        }
        Collections.shuffle(questionModels);

        for(int i=0;i<questionModels.size();i++){
            QuestionModel questionModel = questionModels.get(i);
            questionModel.setQuestion("Ques-"+(i+1)+". "+questionModel.getQuestion());
            questionModels.set(i,questionModel);
        }
        return questionModels;
    }

    public MutableLiveData<List<QuestionModel>> getQuestionsListLivaData() {
        return questionsListLivaData;
    }



    public enum RequestStatus {
        /* Show API is in progress. */
        IN_PROGRESS,

        /* Show API request is failed. */
        FAILED,

        /* Show API request is successfully completed. */
        SUCCEEDED
    }
}
