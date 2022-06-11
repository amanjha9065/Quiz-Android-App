package com.amanjha.quizapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomHolder> {

    private List<QuestionModel> questionsList;
    private List<Boolean> answerStatusList;
    private OnItemClickListener itemClickListener;
    private List<Boolean> bookmarkStatusList;

    public CustomAdapter(List<QuestionModel> questionsList, List<Boolean> answerStatusList, List<Boolean> bookmarkStatusList,  @NonNull OnItemClickListener listener) {
        this.questionsList = questionsList;
        this.answerStatusList = answerStatusList;
        this.bookmarkStatusList = bookmarkStatusList;
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public CustomAdapter.CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_design,parent,false);
        return  new CustomHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        holder.bind(questionsList.get(position), answerStatusList.get(position), bookmarkStatusList.get(position));
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    static class CustomHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;
        private TextView bookMark, question, answer;
        private OnItemClickListener itemClickListener;
        public CustomHolder( View view, OnItemClickListener listener) {
            super(view);
            itemClickListener = listener;
            context = view.getContext();
            question = view.findViewById(R.id.question);
            answer = view.findViewById(R.id.answer);
            bookMark = view.findViewById(R.id.bookmark);
            view.setOnClickListener(this);
        }

        public  void bind( QuestionModel questionModel, boolean answerStatus, boolean bookmarkStatus) {
            question.setText(questionModel.getQuestion());
            answer.setText("Not Answered");
            if(answerStatus) {
                answer.setText("Answered");
            }

            bookMark.setText("Not Bookmarked");

            if(bookmarkStatus) {
                bookMark.setText("Bookmarked");
            }

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(getAbsoluteAdapterPosition());
        }
    }

    public interface OnItemClickListener  {
        void onItemClicked(int position);
    }
}