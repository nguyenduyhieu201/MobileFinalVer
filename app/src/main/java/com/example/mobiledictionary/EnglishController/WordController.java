package com.example.mobiledictionary.EnglishController;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledictionary.English.EnglishWord;
import com.example.mobiledictionary.R;
import com.example.mobiledictionary.WordHelper.WordHelper;

import java.util.Calendar;
import java.util.List;


public class WordController{
    public WordController() {

    }
    public int search(EditText search_1, TextView word, WordHelper englishWordHelper, String tableName,
                      TextView meaning, CompoundButton mButtonHighlight,
                      Button mButtonOpen_Dialog_Note, Button mEngSpeakButton) {
        String key1 = search_1.getText().toString().trim();
        word.setText(key1);
        Cursor meaningCursor = englishWordHelper.SearchWord(key1, tableName);

        int idWord = 0;
        String value = "";
        int count = meaningCursor.getCount();
        if (meaningCursor.getCount() > 0) {
            meaningCursor.moveToNext();
            value = meaningCursor.getString(2);
            idWord = meaningCursor.getInt(0);
        }
        else value = null;
        if (value == null) {
            meaning.setText("Khong co tu nao");
        }
        else {
            mButtonHighlight.setVisibility(View.VISIBLE);
            mButtonOpen_Dialog_Note.setVisibility(View.VISIBLE);
            mEngSpeakButton.setVisibility(View.VISIBLE);
            mButtonHighlight.setChecked(false);
            if (englishWordHelper.getHighlightWord(idWord,tableName) == 0) {
                mButtonHighlight.setChecked(false);
                mButtonHighlight.setButtonDrawable(R.drawable.icon_star_48);
            }
            else {
                mButtonHighlight.setChecked(true);
                mButtonHighlight.setButtonDrawable(R.drawable.icon_star);
            }
            meaning.setText(value.replace(Character.toString((char) 94).charAt(0),Character.toString((char) 39).charAt(0)));
        }
        return idWord;
    }
    public void HighlightAWord (CompoundButton mButtonHighlight, WordHelper englishWordHelper,
                               int idWord, String tableName) {
        mButtonHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((ToggleButton) v).isChecked();
                //checked = true thi highlight = 1
                if (checked){
                    englishWordHelper.HighlightWord(idWord,tableName);
                    Log.d("id", String.valueOf(idWord));
                    mButtonHighlight.setButtonDrawable(R.drawable.icon_star);
                }
                //checked = false thi highlight = 0
                else{
                    englishWordHelper.UnHighlightWord(idWord,tableName);
                    Log.d("id", String.valueOf(idWord));
                    mButtonHighlight.setButtonDrawable(R.drawable.icon_star_48);
                }
            }
        });
    }

    public void open_Dialog_Note (Context context, int idWord, int gravity,
                                   WordHelper englishWordHelper, String tableName) {
    final Dialog dialog = new Dialog(context);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.layout_dialog_note);
    Window window = dialog.getWindow();
    if(window == null) {
        return;
    }
    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    WindowManager.LayoutParams windowAttributes = window.getAttributes();
    windowAttributes.gravity = gravity;
    window.setAttributes(windowAttributes);

    if(Gravity.CENTER == gravity) {
        dialog.setCancelable(true);
    } else {
        dialog.setCancelable(false);
    }

    EditText edittext_note = dialog.findViewById(R.id.edit_text_note);
    Button button_add_note = dialog.findViewById(R.id.button_add_note);
    Button button_cancer_dialog_note = dialog.findViewById(R.id.button_cancer_note);

    String note_1 = englishWordHelper.getNoteWord(idWord, tableName);
    if (note_1 == "") {
    } else {
        edittext_note.setText(note_1, TextView.BufferType.EDITABLE);
    }

    button_cancer_dialog_note.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    });

    button_add_note.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String note = edittext_note.getText().toString().trim();
            englishWordHelper.NoteWord(note, idWord, tableName);
        }
    });
    dialog.show();
    }

    public boolean compareTime (Calendar calendar, int hour24, int minutes) {
        int curHour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        Log.d("curH", String.valueOf(curHour24hrs));
        int curMinutes = calendar.get(Calendar.MINUTE);
        if (hour24 == curHour24hrs && minutes == curMinutes) {
            return true;
        }
        else return false;
    }

    public List<EnglishWord> getListHighlight(WordHelper englishWordHelper, String tableEng,
                                              String tableViet) {
        return englishWordHelper.getHighlightList(tableEng,tableViet);
    }

    //
    public EnglishWord getRandomWord (List<EnglishWord> highlightList) {
        if (highlightList != null) {
            int lenHighlightList = highlightList.size() - 1;
            int random = 0 + (int) (Math.random() * ((lenHighlightList - 0) + 1));
            return highlightList.get(random);
        }
        else {
            return new EnglishWord("Bạn chưa thêm từ mới vào",
                    "Bạn hãy thêm bổ sung từ mới");
        }
    }

}
