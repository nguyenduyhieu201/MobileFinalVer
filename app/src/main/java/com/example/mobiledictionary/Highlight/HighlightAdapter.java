package com.example.mobiledictionary.Highlight;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.mobiledictionary.EnglishController.EnglishWordHelper;
import com.example.mobiledictionary.Notification.NotificationWord;
import com.example.mobiledictionary.WordHelper.WordHelper;
import com.example.mobiledictionary.English.EnglishWord;
import com.example.mobiledictionary.R;

import java.util.List;

public class HighlightAdapter extends RecyclerView.Adapter<HighlightAdapter.HighlightViewHolder>{
    private List<EnglishWord> mListHighlight;
    private Context mContext;
    public HighlightAdapter(Context context, List<EnglishWord> mListHighlight) {
        this.mContext =  context;
        this.mListHighlight = mListHighlight;
    }

    @NonNull
    @Override
    public HighlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new HighlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightViewHolder holder, int position) {
        EnglishWord englishWord = mListHighlight.get(position);
        if(englishWord == null) {
            return;
        }
        holder.myword.setText(englishWord.getWord());
        holder.foreground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToNotification(englishWord);
            }
        });
    }

    private void onClickGoToNotification(EnglishWord englishWord) {
        Intent intent = new Intent(mContext, NotificationWord.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("object_englishWord", englishWord);
        intent.putExtra("Serializable", englishWord);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(mListHighlight != null) {
            return mListHighlight.size();
        }
        return 0;
    }

    public class HighlightViewHolder extends RecyclerView.ViewHolder{
        TextView myword;
        LinearLayout foreground;
        public HighlightViewHolder(@NonNull View itemView) {
            super(itemView);
            myword = itemView.findViewById(R.id.textView_myword);
            foreground = itemView.findViewById(R.id.layout_foreground);
        }
    }

    public void removeItem(WordHelper englishWordHelper, int index) {
        int id = mListHighlight.get(index).getId();
        englishWordHelper.UnHighlightWord(id,"NoiDung");
        mListHighlight.remove(index);
        notifyItemRemoved(index);
    }

    public void undoItem(EnglishWord englishWord, int index) {
        mListHighlight.add(index, englishWord);
        notifyItemInserted(index);
    }
}