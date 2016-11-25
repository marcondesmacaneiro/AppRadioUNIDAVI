package br.com.marcondesmacaneiro.appradiounidavi2;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

import static android.widget.Toast.LENGTH_LONG;
import static br.com.marcondesmacaneiro.appradiounidavi2.VerificarConexao.conexaoDisponivel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Context context;
    private String URL_RADIO_UNIDAVI;
    private ProgressDialog dialog;
    private MediaPlayer player;
    private AudioManager audioManager;
    private ImageButton btnPlay;
    private SeekBar bar;
    private static boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        context = MainActivity.this;
        init();

    }

    private void init() {
        URL_RADIO_UNIDAVI = "http://200.9.102.73:8000";

        dialog = new ProgressDialog(context);
        dialog.setMessage("Verificando a conexão com a internet aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        player = new MediaPlayer();
        audioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        btnPlay = (ImageButton) findViewById(R.id.btnPlayPause);
        btnPlay.setImageResource(R.drawable.ic_menu_share);

        bar = (SeekBar) findViewById(R.id.btnControleVolume);
        bar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        bar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void iniciarRadio(View view) {
        if (conexaoDisponivel(context)) {
            dialog.show();
            if (!isPlaying) {
                btnPlay.setImageResource(R.drawable.ic_menu_send);
                tocarRadio();
            } else {
                pararRadio();
            }
        } else {
            if (isPlaying) {
                pararRadio();
            }
            Toast
                    .makeText(context, "Sem conexão com a Internet", LENGTH_LONG)
                    .show();

        }
    }

    public void pararRadio() {
        isPlaying = false;
        if (dialog != null) dialog.dismiss();
        if (player != null && player.isPlaying()) {
            player.stop();
            player.reset();
        }
    }

    public void tocarRadio() {
        new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    player.setAudioStreamType(audioManager.STREAM_MUSIC);
                    player.setDataSource(URL_RADIO_UNIDAVI);
                    player.prepareAsync();
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                            isPlaying = true;
                            if (dialog != null) dialog.dismiss();
                        }
                    });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        })).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
