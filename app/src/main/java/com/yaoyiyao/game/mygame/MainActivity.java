package com.yaoyiyao.game.mygame;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Vibrator vibrator;
    private static String strs[] = {"剪刀","石头","布"};
    private static int pics[] = {R.mipmap.p1,R.mipmap.p2,R.mipmap.p3};//定义图片资源

    private static final String TAG = "MainActivity";
    private TextView textView;
    private ImageView imageView;
    private static final int SENSOR_SHAKE = 15;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);//振动器
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    //重力感应器监听
    public SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //当传感器信息改变时
            float[] values = event.values;
            float x = values[0]; //x轴方向，向右为正
            float y = values[0]; //y轴方向，向前为正
            float z = values[0]; //z轴方向，向上为正
            Log.i(TAG, "x轴方向的重力加速度"+ x + "；y轴方向的重力加速度"+ y + "；z轴方向的重力加速度"+ z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = SENSOR_SHAKE;//临界值，不同手机数值不同
            if(Math.abs(x)>medumValue||Math.abs(x)>medumValue||Math.abs(x)>medumValue){
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                 //   Toast.makeText(MainActivity.this,"检测到摇晃，执行操作！",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "检测到摇晃，执行操作！");
                    java.util.Random r = new java.util.Random();
                    int num = Math.abs(r.nextInt())%3;
                    textView.setText(strs[num]);
                    imageView.setImageResource(pics[num]);
                    break;
            }
        }
    };
}
