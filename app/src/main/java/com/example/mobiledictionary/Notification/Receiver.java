package com.example.mobiledictionary.Notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mobiledictionary.English.EnglishWord;
import com.example.mobiledictionary.EnglishController.WordController;
import com.example.mobiledictionary.MainActivity;
import com.example.mobiledictionary.R;
import com.example.mobiledictionary.WordHelper.WordHelper;

import java.util.List;

public class Receiver extends BroadcastReceiver {
    public WordController wordController = new WordController();
    private WordHelper highlightWordHelper;
    List<EnglishWord> mListHighlight;
    EnglishWord randomHighlightWord;

    @Override
    public void onReceive(Context context, Intent intent) {
        highlightWordHelper = new WordHelper(context,
                null, 1);
        mListHighlight =
        highlightWordHelper.getHighlightList("NoiDung","VietEngDemo");
        randomHighlightWord = wordController.getRandomWord(mListHighlight);

        Intent intent1 = new Intent(context, NotificationWord.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyLemubit")
                .setSmallIcon(R.drawable.icon_star)
                .setContentTitle(randomHighlightWord.getWord())
                .setContentText(randomHighlightWord.getMeaning())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());

        SharedPreferences sharedPref = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("hlWord", randomHighlightWord.getWord());
        editor.putString("hlMean", randomHighlightWord.getMeaning());
        editor.apply();

    }
}