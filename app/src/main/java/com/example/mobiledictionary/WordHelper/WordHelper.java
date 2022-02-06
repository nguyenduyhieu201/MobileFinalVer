package com.example.mobiledictionary.WordHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mobiledictionary.English.EnglishWord;
import com.example.mobiledictionary.Notification.Receiver;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class WordHelper extends SQLiteOpenHelper {

    private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window
    //destination path (location) of our database on device
    private static String DB_PATH = "";
    private static String DB_NAME = "TuDienSqlite.db";// Database name
    private SQLiteDatabase mDataBase;
    private final Context mContext;


    //    SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase("TuDienSqlite.db", null);
    public WordHelper(Context context,
                      @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DB_NAME, factory, version);
        if (android.os.Build.VERSION.SDK_INT >= 17) {
//            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            DB_PATH = "/data/user/0/com.example.mobiledictionary/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        mContext = context;
    }


    public void createDataBase() {
        //If the database does not exist, copy it from the assets.
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                //Copy the database from assests
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }
    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public void QueryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    public Cursor GetData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery(sql, null);
    }

    public Cursor SearchWord (String word, String tableName) {
        return GetData("Select * from " + tableName + " where EngWord = '" + word + "'");
    }

    public void HighlightWord (int id, String tableName) {
        QueryData("Update '" + tableName + "' SET 'Highlight' = 1 where Id = '"
                + id + "'");
    }

    public void UnHighlightWord (int id, String tableName) {
        QueryData("Update '" + tableName + "' SET 'Highlight' = 0 where Id = '"
                + id + "'");
    }
    public int getHighlightWord (int id, String tableName) {
        Cursor HighlightWord = GetData("Select * from " + tableName + " where Id = " + id);
        HighlightWord.moveToNext();
        int highlightValue = HighlightWord.getInt(3);
        return highlightValue;
    }


    public List<EnglishWord> getHighlightList (String tableNameEng, String tableNameViet) {
        List<EnglishWord> courseCourses = new ArrayList<>();
        Cursor highlightList = GetData ("Select * from " + tableNameEng
                + " where Highlight = 1 UNION ALL Select * from " + tableNameViet +
                " where Highlight = 1");
        if (highlightList.getCount() == 0) {
            return null;
        }
        else {
            while (highlightList.moveToNext()) {
                courseCourses.add(new EnglishWord(highlightList.getInt(0),
                        highlightList.getString(1),
                        highlightList.getString(2),
                        highlightList.getInt(3),
                        highlightList.getString(4)));
            }
        }
        return courseCourses;
    }

    public void NoteWord (String note, int id, String tableName) {
        QueryData("Update '" + tableName + "' SET Note = '" + note + "' where Id = '"
                + id + "'");
    }

    public String getNoteWord (int id, String tableName) {
        Cursor Note = GetData("Select * from " + tableName + " where Id = " + id);
        Note.moveToNext();
        String noteValue = Note.getString(4);
        if (noteValue == null) {
            return "";
        }
        return noteValue;
    }

    public void CreateData (String tableName) {
        QueryData("CREATE TABLE IF NOT " +
                "EXISTS " + tableName + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, EngWord VARCHAR(200)," +
                "Meaning VARCHAR(200), Highlight INTEGER, Note VARCHAR(200) DEFAULT null)");
    }

    public void InsertData (String tableName, String engWord, String meaning) {
        QueryData("INSERT INTO " + tableName + " Values (null, " + '"' + engWord + '"' + ", '" + meaning
        + "',0,null)");

    }

    public void DeleteTable(String tableName) {
        QueryData("DROP TABLE " + tableName);
    }

    public List<EnglishWord> getAllWord() {
        SQLiteDatabase database = getWritableDatabase();;

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = database.rawQuery("SELECT * FROM " + "NoiDung", null);

        // on below line we are creating a new array list.
        List<EnglishWord> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new EnglishWord(cursorCourses.getInt(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getInt(3),
                        cursorCourses.getString(4)));
            } while (cursorCourses.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorCourses.close();
        return courseModalArrayList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
