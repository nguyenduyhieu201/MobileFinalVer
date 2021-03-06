package com.example.mobiledictionary;

import static androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.mobiledictionary.English.EngViet;
import com.example.mobiledictionary.English.EnglishWord;
import com.example.mobiledictionary.EnglishController.WordController;
import com.example.mobiledictionary.Highlight.MyWords;
import com.example.mobiledictionary.Notification.NotificationWord;
import com.example.mobiledictionary.Notification.Receiver;
import com.example.mobiledictionary.TranslateOnline.TranslateOnline;
import com.example.mobiledictionary.Vietnamese.VietEng;
import com.example.mobiledictionary.WordHelper.WordHelper;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    protected int id;
    protected EditText search;
    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";
    public WordController wordController = new WordController();
    public Calendar rightNow = Calendar.getInstance();
    private WordHelper highlightWordHelper = new WordHelper(this,
            null, 1);

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public int hour24;
    public int minute;
    public Calendar calendar = Calendar.getInstance();
    long current = Calendar.getInstance().getTimeInMillis();
    public int hourPos;
    public int minPos;
    public String speedSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sharedPref = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        String languageSelected = sharedPref.getString("accent","");
        if (languageSelected.equals("English")) {
            editor.putBoolean("anhAnhSelected",true);
            editor.apply();
        }
        else {
            editor.putBoolean("anhAnhSelected",false);
            editor.apply();
        }

        speedSpeak = sharedPref.getString("speedSelect","");
        if (speedSpeak.equals("fast")) {
            editor.putBoolean("selectFast", true);
        }
        else {
            editor.putBoolean("selectFast", false);
        }

        hour24 = Integer.valueOf(sharedPref.getString("hourSet","00"));
        minute = Integer.valueOf(sharedPref.getString("minSet","00"));
        calendar.set(Calendar.HOUR_OF_DAY, hour24);
        calendar.set(Calendar.MINUTE,minute);
        long timeSet = calendar.getTimeInMillis();

        hourPos = sharedPref.getInt("hourPos", 0);
        minPos = sharedPref.getInt("minPos", 0);
        editor.putInt("hourSetting", hourPos);
        editor.putInt("minSetting", minPos);
        editor.apply();

        highlightWordHelper.createDataBase();
//        highlightWordHelper.CreateData("NoiDung");
//        highlightWordHelper.CreateData("VietEngDemo");

        String pathdb = getDatabasePath("tudiensqlite.db").getPath();
        Log.d("path la", pathdb);
        View.OnClickListener handler = new View.OnClickListener(){
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bSearch:
                        openEngViet();
                        break;

                    case R.id.bTuCuaBan:
                        openHighlight();
                        break;

                    case R.id.bVietAnh:
                        openVietAnh();
                        break;

                    case R.id.bCaiDat:
                        openSetting();
                        break;
//                    case R.id.addDB:
//                        addDatabase();
//                        break;
                    case R.id.finish:
                        FinishApp();
                        break;
                    case R.id.training:
                        openTraining();
                        break;
                    case R.id.bDichVB:
                        openDichVB();
                        break;
                }
            }
        };

        findViewById(R.id.bSearch).setOnClickListener(handler);
        findViewById(R.id.bTuCuaBan).setOnClickListener(handler);
        findViewById(R.id.bVietAnh).setOnClickListener(handler);
        findViewById(R.id.bCaiDat).setOnClickListener(handler);
        findViewById(R.id.finish).setOnClickListener(handler);
        findViewById(R.id.training).setOnClickListener(handler);
        findViewById(R.id.bDichVB).setOnClickListener(handler);
//        findViewById(R.id.addDB).setOnClickListener(handler);


        //noi dung va tieu de notification
        List<EnglishWord> mListHighlight =
                highlightWordHelper.getHighlightList("NoiDung","VietEngDemo");
        EnglishWord randomHighlightWord = wordController.getRandomWord(mListHighlight);

        //notification
        createNotificationChannel();
        Intent intent = new Intent(MainActivity.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //c??i ?????t th???i gian nh???c nh???
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        2, pendingIntent);

        if (timeSet > current) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeSet,
                    24 * 60 * 60 * 1000, pendingIntent);
        }
        else {
            hour24 = hour24 + 1;
            calendar.set(Calendar.HOUR_OF_DAY, hour24);
            timeSet = calendar.getTimeInMillis();
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeSet,
                    24 * 60 * 60 * 1000, pendingIntent);
        }
        // th??m v??o db ti???ng Vi???t

        //th??m db ti???ng Anh

    }

    // ?????c file txt
    private String ReadStaticfile(int rsID) {
        String result = null;
        try {
            Resources resources = getResources();
            InputStream is = resources.openRawResource(rsID);
            // buoc 2 doc file
            int flength = is.available();


            if (flength != 0) {
                byte[] buffer = new byte[flength];
                if (is.read(buffer) != -1) {
                    result = new String(buffer);
//                    System.out.println(result);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
//            LogUtil.LogE(TAG, e.toString());
        }

        return result;
    }

    //chuy???n sang c???a s??? tra t??? Anh-Vi???t
    public void openEngViet() {
        search = (EditText) findViewById(R.id.edittext_main_search);
        String text = search.getText().toString();

        Intent intent = new Intent(this, EngViet.class);
        intent.putExtra(EXTRA_TEXT, text);
        startActivity(intent);
    }
    //chuy???n sang c???a s??? highlight
    public void openHighlight() {
        Intent intent = new Intent(this, MyWords.class);
        startActivity(intent);
    }

    //chuy???n sang c???a s??? tra vi???t anh
    public void openVietAnh() {
        Intent intent = new Intent(this, VietEng.class);
        startActivity(intent);
    }

    public void openSetting() {
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
    }

    public void openTraining() {
        Intent intent = new Intent(this, Training.class);
        startActivity(intent);
    }

    public void openDichVB() {
        Intent intent = new Intent(this, TranslateOnline.class);
        startActivity(intent);
    }

    //t???o channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "studentChannel";
            String description = "Channel for student notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void FinishApp () {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        // Tao su kien ket thuc app
//        highlightWordHelper.DeleteTable("NoiDung");
//        highlightWordHelper.DeleteTable("VietEngDemo");
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startActivity(startMain);
        finish();
    }

    public void addDatabase() {
        try {
            String[] line =ReadStaticfile(R.raw.ve).split("[\n]");
            for (int i = 0; i < line.length; i++) {
                String[] data = line[i].split(" : ");
                String word = data[0];
                String meaning = "";
                for (int j = 1; j < data.length; j++) {
                    meaning += data[j] + "\n";
                }
                // ?????i k?? t??? ' th??nh ^
                meaning = meaning.replace(Character.toString((char) 39).charAt(0),Character.toString((char) 94).charAt(0));

                System.out.println(word + "\n" + meaning);
                System.out.println(i);
                highlightWordHelper.InsertData("VietEngDemo",word,meaning);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Loi Viet Anh");
        }
        //th??m db ti???ng Anh
        try {
            String[] line =ReadStaticfile(R.raw.dict).split("[\n]");
            for (int i = 0; i < line.length; i++) {
                String[] data = line[i].split("[\t|]");
                String word = data[0].substring(1);
                String meaning = "";
                for (int j = 1; j < data.length; j++) {
                    meaning += data[j] + "\n";
                }
                // ?????i k?? t??? ' th??nh ^
                meaning = meaning.replace(Character.toString((char) 39).charAt(0),Character.toString((char) 94).charAt(0));

                System.out.println(meaning + "\n");
                System.out.println(i);
                highlightWordHelper.InsertData("NoiDung",word,meaning);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Loi Anh Viet");
        }
    }
    public void DropTable () {
        highlightWordHelper.DeleteTable("NoiDung");
        highlightWordHelper.DeleteTable("VietEngDemo");

    }
}


//    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
////        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
////                SystemClock.elapsedRealtime() +
////                        10 * 1000, pendingIntent);
//        Intent intent = new Intent(this, NotificationWord.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        startActivity(intent);
//
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
//                "lemubitA")
//                .setSmallIcon(R.drawable.icon_star)
//                .setContentTitle(randomHighlightWord.getWord())
//                .setContentText(randomHighlightWord.getMeaning())
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setVisibility(VISIBILITY_PUBLIC);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

//        highlightWordHelper.InsertData("VietEngDemo","xin chao","hi");
//        highlightWordHelper.InsertData("VietEngDemo","xin chao 3","hello");
//        highlightWordHelper.InsertData("VietEngDemo","dog","cho");
//        highlightWordHelper.InsertData("VietEngDemo","cat","meo");
//        highlightWordHelper.InsertData("VietEngDemo","tot","goodbye");
//        highlightWordHelper.InsertData("NoiDung", "hi", "xin chao");
//       highlightWordHelper.InsertData("NoiDung","asd","* danh t???,  s??? nhi???u as,  a's|- (th??ng t???c) lo???i a, h???ng nh???t, h???ng t???t nh???t h???ng r???t t???t|=  his health is a|    + s???c kho??? anh ta v??o lo???i a|- (??m nh???c) la|=  a sharp|    + la th??ng|=  a flat|    + la gi??ng|- ng?????i gi??? ?????nh th??? nh???t; tr?????ng h???p gi??? ?????nh th??? nh???t|=  from a to z|    + t??? ?????u ?????n ??u??i, t?????ng t???n|=  not to know a from b|    + kh??ng bi???t t?? g?? c???; m???t ch??? b??? ????i c??ng kh??ng bi???t|*");

