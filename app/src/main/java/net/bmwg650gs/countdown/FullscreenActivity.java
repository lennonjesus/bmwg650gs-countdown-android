package net.bmwg650gs.countdown;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static java.lang.String.format;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {


    private final int SEGUNDOS_POR_DIA = 86400;

    private final int MIL = 1000;

    private int daysLeft = 0;

    private final Handler mHideHandler = new Handler();

    private View mContentView;

    private View mControlsView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        countdown();
    }

    public void shareCount(View view) throws FileNotFoundException {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/png");

        Uri uri = Uri.parse("android.resource://net.bmwg650gs/drawable/" + R.drawable.logo_nacional);

        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        shareIntent.putExtra(Intent.EXTRA_TEXT, format("Faltam %2d dias para o 5º Encontro Nacional bmwg650gs.net", daysLeft));
        startActivity(Intent.createChooser(shareIntent, "Compartilhe a contagem..."));

    }

    public void shareApp(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/png");

        Uri uri = Uri.parse("android.resource://net.bmwg650gs/drawable/" + R.drawable.logo_nacional);

        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        StringBuilder shareMessage = new StringBuilder();

        shareMessage
                .append("Acompanhe a contagem regressiva para o 5º Encontro Nacional bmwg650gs.net com seu smartphone Android:")
                .append("\n")
                .append("https://goo.gl/NhPpNy")
        ;

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage.toString());
        startActivity(Intent.createChooser(shareIntent, "Compartilhe o app..."));
    }

    public void countdown() {

        final TextView txtDaysLeft = (TextView) findViewById(R.id.txtDaysLeft);

        long diffMillis = getTypeLeftInMillis();

        new CountDownTimer(diffMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                daysLeft = getDaysLeft(millisUntilFinished);

                txtDaysLeft.setText(format("Faltam %2d dias para o Encontro Nacional 2016!", daysLeft));
            }

            @Override
            public void onFinish() {
                txtDaysLeft.setText(R.string.nextEvent);
            }
        }.start();
    }

    private long getTypeLeftInMillis() {
        Time dataEvento = new Time();
        dataEvento.set(21, 3, 2016);
        dataEvento.normalize(true);

        Time today = new Time();
        today.setToNow();
        today.normalize(true);

        long dataEventoMillis = dataEvento.toMillis(true);
        long todayMillis = today.toMillis(true);

        return dataEventoMillis - todayMillis;
    }

    private int getDaysLeft(long millisUntilFinished) {
        return (int) ((millisUntilFinished / MIL) / SEGUNDOS_POR_DIA);
    }


}
