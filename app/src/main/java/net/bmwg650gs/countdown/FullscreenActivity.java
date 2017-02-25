package net.bmwg650gs.countdown;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.String.format;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {


    private final int SEGUNDOS_POR_DIA = 86400;

    private static final int SEGUNDOS_POR_HORA = 3600;

    private static final int SEGUNDOS_POR_MINUTO = 60;

    private static final int MIL = 1000;

    private int daysLeft = 0;

    private int hoursLeft = 0;

    private int minsLeft = 0;

    private int secondsLeft = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        countdown();
    }

    public void shareCount(View view) throws FileNotFoundException {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

//        Uri uri = Uri.parse("android.resource://net.bmwg650gs/drawable/" + R.drawable.logo_nacional);

//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

//        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.checkin));
//        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.checkin_title));

        shareIntent.putExtra(Intent.EXTRA_TEXT, format("Faltam %2d dias para o ", daysLeft) + getResources().getString(R.string.app_name));
        startActivity(Intent.createChooser(shareIntent, "Compartilhe a contagem..."));

    }

    public void shareApp(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("text/plain");

//        Uri uri = Uri.parse("android.resource://net.bmwg650gs/drawable/" + R.drawable.logo_nacional);

//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        StringBuilder shareMessage = new StringBuilder();

        shareMessage
                .append(getResources().getString(R.string.share_invite))
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

                hoursLeft = (int) (((millisUntilFinished / MIL) - (daysLeft * SEGUNDOS_POR_DIA)) / SEGUNDOS_POR_HORA);
                minsLeft = (int) (((millisUntilFinished / MIL) - ((daysLeft * SEGUNDOS_POR_DIA) + (hoursLeft * SEGUNDOS_POR_HORA))) / SEGUNDOS_POR_MINUTO);
                secondsLeft = (int) ((millisUntilFinished / MIL) % SEGUNDOS_POR_MINUTO);

                StringBuilder textLeft = new StringBuilder();

                textLeft
                        .append("Faltam ")
                        .append(format("%02d", daysLeft))
                        .append(" dias ")
//                        .append(format("%02d", hoursLeft))
//                        .append(":")
//                        .append(format("%02d", minsLeft))
//                        .append(":")
//                        .append(format("%02d", secondsLeft))
//                        .append(" horas ")
                        .append("para o Encontro Nacional ")
                        .append(getResources().getString(R.string.anoEncontro))
                        .append("!");

                txtDaysLeft.setText(textLeft.toString());
            }

            @Override
            public void onFinish() {
                txtDaysLeft.setText(R.string.nextEvent);
            }
        }.start();
    }

    private long getTypeLeftInMillis() {

        Calendar dataEvento = GregorianCalendar.getInstance();

        dataEvento.set(Calendar.DAY_OF_MONTH, 15);
        dataEvento.set(Calendar.MONTH, Calendar.JUNE);
        dataEvento.set(Calendar.YEAR, 2017);
        dataEvento.set(Calendar.HOUR_OF_DAY, 0);
        dataEvento.set(Calendar.MINUTE, 0);
        dataEvento.set(Calendar.SECOND, 0);


        long dataEventoMillis = dataEvento.getTimeInMillis();
        long todayMillis = GregorianCalendar.getInstance().getTimeInMillis();

        return dataEventoMillis - todayMillis;
    }

    private int getDaysLeft(long millisUntilFinished) {
        return (int) ((millisUntilFinished / MIL) / SEGUNDOS_POR_DIA);
    }


}
