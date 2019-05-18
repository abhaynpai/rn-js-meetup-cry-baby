//  Created by react-native-create-bridge

package com.javascriptmeetup.crybaby;

import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

public class CryBabyModule extends ReactContextBaseJavaModule implements SensorEventListener {
    public static final String REACT_CLASS = "SensorBridge";
    private static ReactApplicationContext reactContext = null;

    private double lastReadingTs = (double) System.currentTimeMillis();

    double lastXval = 0;
    double lastYval = 0;
    double lastZval = 0;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    int interval = 100; // milliseconds

    public CryBabyModule(ReactApplicationContext context) {
        // Pass in the context to the constructor and save it so you can emit events
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        super(context);

        reactContext = context;

        sensorManager = (SensorManager) reactContext.getSystemService(Service.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public String getName() {
        // Tell React the name of the module
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        return REACT_CLASS;
    }

    @Override
    public Map<String, Object> getConstants() {
        // Export any constants to be used in your native module
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        final Map<String, Object> constants = new HashMap<>();
        constants.put("EXAMPLE_CONSTANT", "example");

        return constants;
    }

    @ReactMethod
    public void startListening() {
        sensorManager.registerListener(this, accelerometerSensor, this.interval * 1000);
    }

    @ReactMethod
    public void stopListening() {
        sensorManager.unregisterListener(this, accelerometerSensor);
    }

    @ReactMethod
    public void exampleMethod() {
        // An example native method that you will expose to React
        // https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
    }

    private static void emitDeviceEvent(String eventName, @Nullable WritableMap eventData) {
        // A method for emitting from the native side to JS
        // https://facebook.github.io/react-native/docs/native-modules-android.html#sending-events-to-javascript
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, eventData);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double currentTimeMillis = (double) System.currentTimeMillis();
        double readTsDiff = currentTimeMillis - lastReadingTs;

        boolean isAccelerometer = event.sensor.getType() == Sensor.TYPE_ACCELEROMETER;
        boolean intervalExceeded = readTsDiff >= interval;

        if (!isAccelerometer || !intervalExceeded) {
            return;
        }

        lastReadingTs = currentTimeMillis;

        double xVal = event.values[0];
        double yVal = event.values[1];
        double zVal = event.values[2];

        double speed = Math.abs(xVal + yVal + zVal - lastXval - lastYval - lastZval) / readTsDiff * 10000;

        WritableMap map = Arguments.createMap();

        map.putDouble("xVal", xVal);
        map.putDouble("yVal", yVal);
        map.putDouble("zVal", zVal);

        map.putDouble("speed", speed);

        Log.d("Cry-Baby", "onSensorChanged: SPeed " + speed);

        emitDeviceEvent("accelerometer", map);

        lastXval = xVal;
        lastYval = yVal;
        lastZval = zVal;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
