package com.example.annafaytelson.gesturekeyboard1;

/**
 * Created by annafaytelson on 3/24/17.
 */

import android.os.Environment;

import java.io.File;

public class ConstantValue {
    int session2GesturePerUser;
    int reSampleRate;
    int TotalGesture;
    double SimiThreshold;
    int ErrorCount;
    int ErrorCountThres;
    String filePath;

    public ConstantValue(){
        session2GesturePerUser=30;
        reSampleRate=64;
        TotalGesture=2;
        SimiThreshold=1.8;
        ErrorCount=0;
        ErrorCountThres=2;
        filePath= Environment.getExternalStorageDirectory().toString()+ File.separator+ "Attack"+ File.separator;

    }
}
