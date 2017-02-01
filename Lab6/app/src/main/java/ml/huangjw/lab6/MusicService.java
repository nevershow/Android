package ml.huangjw.lab6;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

/**
 * Created by Huangjw on 2016/11/4.
 */
public class MusicService extends Service {
  public MediaPlayer mediaPlayer;
  private final IBinder binder;
  private String path;
  private MusicService.STATE state;
  public enum STATE {
    IDLE, Playing, Pause, Stop
  }

  public MusicService() {
    state = MusicService.STATE.IDLE;
    binder = new MyBinder();
  }

  public class MyBinder extends Binder {
    MusicService getService() {
      return MusicService.this;
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mediaPlayer = new MediaPlayer();

//     交作业使用内置的mp3
    mediaPlayer = MediaPlayer.create(this, R.raw.music);
    path = "Built-in";

    // 使用本地sd卡的mp3
//      try {
//        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/music.mp3";
//        mediaPlayer.setDataSource(path);
//        mediaPlayer.prepare();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//
    mediaPlayer.setLooping(true);
  }

  public void play() {
    mediaPlayer.start();
    state = STATE.Playing;
  }

  public void pause() {
    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
      mediaPlayer.pause();
      state = STATE.Pause;
    }
  }

  public void stop() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
      state = STATE.Stop;
      try {
        mediaPlayer.prepare();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public MusicService.STATE getState() {
    return state;
  }

  public String getPath() {
    return path;
  }

  @Override
  public void onDestroy() {
    if (mediaPlayer != null) {
      mediaPlayer.stop();
      mediaPlayer.release();
    }
    super.onDestroy();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return Service.START_STICKY;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    return super.onUnbind(intent);
  }
}
