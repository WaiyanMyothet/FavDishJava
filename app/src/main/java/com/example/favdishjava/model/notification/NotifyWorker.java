package com.example.favdishjava.model.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.favdishjava.R;
import com.example.favdishjava.utils.Constants;
import com.example.favdishjava.view.activities.AddUpdateDishActivity;
import com.example.favdishjava.view.activities.MainActivity;

public class NotifyWorker extends Worker {
    private Context context;

    public NotifyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @Override
    public Result doWork() {
        SendNotification();
        return Result.success();
    }

    private void SendNotification() {
        int notification_id = 0;

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(Constants.NOTIFICATION_ID, notification_id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String titleNotification = getApplicationContext().getString(R.string.notification_title);
        String subtitleNotification = getApplicationContext().getString(R.string.notification_subtitle);
        Bitmap bitmap = vectorToBitmap(R.drawable.ic_vector_logo);

        NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .bigLargeIcon(null);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_small_icon_notification)
                .setContentTitle(titleNotification)
                .setContentText(subtitleNotification)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(bigPicStyle)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(Constants.NOTIFICATION_CHANNEL);
            Uri ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            AudioAttributes audioAttributes =
                    new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            NotificationChannel channel =
                    new NotificationChannel(
                            Constants.NOTIFICATION_CHANNEL,
                            Constants.NOTIFICATION_NAME,
                            NotificationManager.IMPORTANCE_HIGH
                    );
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.setSound(ringtoneManager, audioAttributes);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(notification_id, builder.build());

    }

    private Bitmap vectorToBitmap(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(this.context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}