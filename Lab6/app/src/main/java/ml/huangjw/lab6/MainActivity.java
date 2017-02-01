package ml.huangjw.lab6;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private Button play;
  private TextView status, starttime, endtime;
  private ImageView cover;
  private SeekBar seekBar;

  private MusicService musicService;
  private ServiceConnection sc;
  private SimpleDateFormat time;
  final Handler handler = new Handler();
  private Runnable seekBarRunnable;
  private Runnable rotation;
  private Button stop;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    sc = new ServiceConnection() {
      public void onServiceDisconnected(ComponentName name) {
        musicService = null;
      }

      public void onServiceConnected(ComponentName name, IBinder service) {
        musicService = ((MusicService.MyBinder) (service)).getService();
      }
    };
    Intent intent = new Intent(this, MusicService.class);
    bindService(intent, sc, Context.BIND_AUTO_CREATE);
    init();
    setHandler();
  }

  private void setHandler() {
    seekBarRunnable = new Runnable() {
      @Override
      public void run() {
        int process = (int) (musicService.mediaPlayer.getCurrentPosition() * 1.0 * seekBar.getMax() / musicService.mediaPlayer.getDuration());
        seekBar.setProgress(process);
        handler.postDelayed(seekBarRunnable, 100);
      }
    };

    rotation = new Runnable() {
      @Override
      public void run() {
        cover.setRotation((cover.getRotation() + 1));
        handler.postDelayed(rotation, 100);
      }
    };
  }

  private void init() {
    time = new SimpleDateFormat("mm:ss");

    play = (Button) findViewById(R.id.play);
    stop = (Button) findViewById(R.id.stop);
    Button exit = (Button) findViewById(R.id.exit);
    play.setOnClickListener(this);
    stop.setOnClickListener(this);
    exit.setOnClickListener(this);
//    stop.setEnabled(false);

    cover = (ImageView) findViewById(R.id.cover);

    status = (TextView) findViewById(R.id.status);
    starttime = (TextView) findViewById(R.id.starttime);
    endtime = (TextView) findViewById(R.id.endtime);
    final String inittime = "00:00", idle = "IDLE";
    starttime.setText(inittime);
    endtime.setText(inittime);
    status.setText(idle);

    seekBar = (SeekBar) findViewById(R.id.progressbar);
    seekBar.setProgress(0);
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (musicService.getState() == MusicService.STATE.IDLE) {
          String end = time.format(musicService.mediaPlayer.getDuration());
          endtime.setText(end);
          TextView filename = (TextView) findViewById(R.id.filename);
          filename.setText(musicService.getPath());
        }
        int playedtime = (int) (progress * 1.0 * musicService.mediaPlayer.getDuration() / seekBar.getMax());
        String current = time.format(playedtime);
        starttime.setText(current);
        if (fromUser) {
          musicService.mediaPlayer.seekTo((int) (seekBar.getProgress() * 1.0 * musicService.mediaPlayer.getDuration() / seekBar.getMax()));  //跳到该曲该秒
        }
      }
      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {}
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {}
    });
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.play:
        if (musicService.getState() == MusicService.STATE.IDLE) {
          String end = time.format(musicService.mediaPlayer.getDuration());
          endtime.setText(end);
          TextView filename = (TextView) findViewById(R.id.filename);
          filename.setText(musicService.getPath());
          musicService.mediaPlayer.seekTo((int) (seekBar.getProgress() * 1.0 * musicService.mediaPlayer.getDuration() / seekBar.getMax()));  //跳到该曲该秒
        }
        if (musicService.getState() != MusicService.STATE.Playing) {
          status.setText("Playing");
          play.setText("PAUSE");
          stop.setEnabled(true);
          musicService.play();
          handler.post(seekBarRunnable);
          handler.post(rotation);
        } else {
          status.setText("Pause");
          play.setText("PLAY");
          musicService.pause();
          handler.removeCallbacks(seekBarRunnable);
          handler.removeCallbacks(rotation);
        }

        break;
      case R.id.stop:
        status.setText("Stop");
        play.setText("PLAY");
        stop.setEnabled(false);
        musicService.mediaPlayer.seekTo(0);
        musicService.stop();
        seekBar.setProgress(0);
        handler.removeCallbacks(seekBarRunnable);
        handler.removeCallbacks(rotation);
        break;
      case R.id.exit:
        handler.removeCallbacks(seekBarRunnable);
        handler.removeCallbacks(rotation);
        unbindService(sc);
        this.finish();
        System.exit(0);
        break;
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      moveTaskToBack(false);
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }
}

