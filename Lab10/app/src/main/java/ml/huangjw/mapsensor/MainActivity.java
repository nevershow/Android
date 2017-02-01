package ml.huangjw.mapsensor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

public class MainActivity extends AppCompatActivity {
  private MapView mMapView;
  private BaiduMap baiduMap;
  private SensorManager mSensorManager;
  private Sensor mMagneticSensor;
  private Sensor mAccelerometerSensor;
  private SensorEventListener mSensorEventListener;
  private ImageButton toggle;
  private LocationManager mLocationManager;
  private Vibrator vibrator;

  private boolean first = true;
  private final String TAG = "lab10";
  private final int SPEED_SHRESHOLD = 3800;//这个值越大需要越大的力气来摇晃手机
  private long lastShakeTime = System.currentTimeMillis(), lastUpdateTime = 0;
  private int clicktimes = 0;
  private float currentRotation = 0;
  private float lastX, lastY, lastZ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //在使用SDK各组件之前初始化context信息，传入ApplicationContext
    //注意该方法要在setContentView方法之前实现
    SDKInitializer.initialize(getApplicationContext());
    setContentView(R.layout.activity_main);
    initMap();
    initVariable();
  }

  private void initMap() {
    mMapView = (MapView) findViewById(R.id.bmapView);
    baiduMap = mMapView.getMap();

    // 设置地图图标
    Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.pointer), 100, 100, true);
    BitmapDescriptor bitmapD = BitmapDescriptorFactory.fromBitmap(bitmap);

    // 开启定位图层
    baiduMap.setMyLocationEnabled(true);
    MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapD);
    baiduMap.setMyLocationConfigeration(config);

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {return;}

    mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        // 位置发生变化,构造定位数据并更新位置
        LatLng desLatLng = locationConverter(location);
        MyLocationData locdata = new MyLocationData.Builder().latitude(desLatLng.latitude).longitude(desLatLng.longitude).direction(currentRotation).build();
        mMapView.getMap().setMyLocationData(locdata);
      }

      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) {}
      @Override
      public void onProviderEnabled(String provider) {}
      @Override
      public void onProviderDisabled(String provider) {}
    });

    // 设置初始位置, 第一次获取的位置为空
    mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    LatLng desLatLng = locationConverter(mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
    MapStatus status = new MapStatus.Builder().target(desLatLng).build();
    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(status);
    baiduMap.setMapStatus(mapStatusUpdate);

    // 触摸屏幕切换图标的模式
    baiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
      @Override
      public void onTouch(MotionEvent motionEvent) {
        if (clicktimes == 0) {
          clicktimes = 1;
          toggle.setImageResource(R.mipmap.define_location);
        }
      }
    });
  }

  private void initVariable() {
    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    mSensorEventListener = new SensorEventListener() {
      float[] accValues = null;
      float[] magValues = null;

      @Override
      public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
          // 加速度传感器变化, 计算速度,判断是否摇一摇
          case Sensor.TYPE_ACCELEROMETER:
            accValues = event.values.clone();
            if (System.currentTimeMillis() - lastUpdateTime > 50) {
              lastUpdateTime = System.currentTimeMillis();
              float deltaX = accValues[0] - lastX;
              float deltaY = accValues[1] - lastY;
              float deltaZ = accValues[2] - lastZ;
              lastX = accValues[0];
              lastY = accValues[1];
              lastZ = accValues[2];
              double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ  * deltaZ) * 200;

              // 速度超过阈值,前后两次间隔大于2秒
              if (speed >= SPEED_SHRESHOLD && lastUpdateTime - lastShakeTime > 2000 && !first) {
                lastShakeTime = lastUpdateTime;
                vibrator.vibrate(500);
                if (baiduMap.getMapType() == BaiduMap.MAP_TYPE_SATELLITE) {
                  mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_NORMAL);
                }
                else {
                  mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                }
              }
              first = false;
            }
            break;
          case Sensor.TYPE_MAGNETIC_FIELD:
            magValues = event.values.clone();
            break;
        }

        // 传感器的值不为空
        if (accValues != null && magValues != null) {
          float[] R = new float[9];
          float[] values = new float[3];
          SensorManager.getRotationMatrix(R, null, accValues, magValues);
          SensorManager.getOrientation(R, values);
          float newRotation = (float) Math.toDegrees((double) values[0]);

          if (Math.abs(newRotation - currentRotation) > 0.5) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {return;}
            // 更新箭头位置和方向
            LatLng desLatLng = locationConverter(mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            MyLocationData.Builder data = new MyLocationData.Builder();
            data.latitude(desLatLng.latitude);
            data.longitude(desLatLng.longitude);
            data.direction(newRotation);
            mMapView.getMap().setMyLocationData(data.build());

            // 箭头处于地图中心
            if (clicktimes == 0) {
              MapStatus status = new MapStatus.Builder().target(desLatLng).build();
              MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(status);
              mMapView.getMap().setMapStatus(mapStatusUpdate);
            }
          }
          currentRotation = newRotation;
        }
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {
      }
    };

    toggle = (ImageButton) findViewById(R.id.toggle);
    toggle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        clicktimes = (clicktimes + 1) % 2;
        int state[] = {R.mipmap.center_direction, R.mipmap.define_location};
        toggle.setImageResource(state[clicktimes]);


        // 设置当前位置在屏幕中间
        if (clicktimes == 0) {
          if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
          }
          LatLng desLatLng = locationConverter(mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
          MapStatus status = new MapStatus.Builder().target(desLatLng).build();
          MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(status);
          mMapView.getMap().setMapStatus(mapStatusUpdate);
        }
      }
    });
  }

  // 将原始坐标转换成百度坐标
  private LatLng locationConverter(Location location) {
    CoordinateConverter converter = new CoordinateConverter();
    converter.from(CoordinateConverter.CoordType.GPS);
    converter.coord(new LatLng(location.getLatitude(), location.getLongitude()));
    return converter.convert();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    //在activity执行onDestroy时执行mMapView.onDestroy()
    mMapView.onDestroy();
  }

  @Override
  protected void onResume() {
    super.onResume();
    //在activity执行onResume时执行mMapView.onResume()
    mMapView.onResume();
    mSensorManager.registerListener(mSensorEventListener, mMagneticSensor,
        SensorManager.SENSOR_DELAY_GAME);
    mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor,
        SensorManager.SENSOR_DELAY_GAME);
  }

  @Override
  protected void onPause() {
    super.onPause();
    //在activity执行onPause时执行mMapView.onPause()
    mMapView.onPause();
    mSensorManager.unregisterListener(mSensorEventListener);
  }
}
