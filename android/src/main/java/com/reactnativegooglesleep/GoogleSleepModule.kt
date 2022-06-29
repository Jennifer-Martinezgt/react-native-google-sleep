package com.reactnativegooglesleep

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import androidx.datastore.preferences.createDataStore
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.SleepClassifyEvent
import com.google.android.gms.location.SleepSegmentEvent

class GoogleSleepModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    
    private static final String REACT_MODULE = "GoogleSleep";
    private GoogleSleepManager mGoogleSleepManager = null;

    override fun getName(): String {
        return REACT_MODULE
    }

    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    fun multiply(a: Int, b: Int, promise: Promise) {
          promise.resolve(a * b)
    }


    @Override
    public void initialize() {
        super.initialize();

        getReactApplicationContext().addLifecycleEventListener(this);
    }

    @ReactMethod
    public void startFitnessRecording(ReadableArray dataTypes) {
        mGoogleSleepManager.getRecordingApi().subscribe(dataTypes);
    }

    @ReactMethod
    public void getSleepSegmentEvent() {
        val hashMap: MutableMap<String, Any> = HashMap()

        if (SleepSegmentEvent.hasEvents(intent)) {
            val sleepSegmentEvent: List<SleepSegmentEvent> = SleepSegmentEvent.extractEvents(intent)
            Log.d(TAG, "SleepClassifyEvent List: $sleepClassifyEvent")
            if (sleepSegmentEvent.isNotEmpty() ){
                hashMap["beginTimeMilis"] = sleepSegmentEvent[0].startTimeMillis.toString()
                hashMap["endTimeMilis"] = sleepSegmentEvent[0].endTimeMillis.toString()
                hashMap["Status"] = sleepSegmentEvent[0].status.toString()
            }
        }

        return hashMap
    }

    @ReactMethod
    public void getSleepClassifyEvent() {
        val hashMap: MutableMap<String, Any> = HashMap()

        if (SleepClassifyEvent.hasEvents(intent)) {
            val sleepClassifyEvent: List<SleepClassifyEvent> = SleepClassifyEvent.extractEvents(intent)
            Log.d(TAG, "SleepClassifyEvent List: $sleepClassifyEvent")
            if (sleepClassifyEvent.isNotEmpty() ){
                hashMap["TimeZoneSeconds"] = sleepClassifyEvent[0].timestampSeconds.toString()
                hashMap["Confidence"] = sleepClassifyEvent[0].confidence.toString()
                hashMap["Motion"] = sleepClassifyEvent[0].motion.toString()
                hashMap["Light"] = sleepClassifyEvent[0].light.toString()
            }

        }

        return hashMap
    }

}
