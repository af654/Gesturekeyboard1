package com.example.annafaytelson.gesturekeyboard1;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 12/27/2015.
 */
public class LocationViewUTUS extends View {
    public static class PointerState {
        private final ArrayList<Float> mXs = new ArrayList<Float>();
        private final ArrayList<Float> mYs = new ArrayList<Float>();
        private final ArrayList<Float> mPressure = new ArrayList<Float>();
    }
    public static PointerState ps;
    private final Paint mTextPaint;
    private final Paint mTextBackgroundPaint;
    private final Paint mTextLevelPaint;
    private final Paint mPaint;
    private final Paint mTargetPaint;
    private final Paint mPathPaint;
    private final Paint.FontMetricsInt mTextMetrics = new Paint.FontMetricsInt();
    private final ArrayList<PointerState> mPointers = new ArrayList<PointerState>();
    static FileIO tempFileService;
    String fileGesture;
    static String filePath;
    private DatabaseReference mDatabase;

    static int TouchDownCount=0;
    public int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,			// colors[1] = first finger color
            Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
            Color.LTGRAY, Color.YELLOW };

    public LocationViewUTUS(Context c) {
        super(c);
        setFocusable(true);
        ViewConfiguration.get(c);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(10 * getResources().getDisplayMetrics().density);
        mTextPaint.setARGB(255, 0, 0, 0);
        mTextBackgroundPaint = new Paint();
        mTextBackgroundPaint.setAntiAlias(false);
        mTextBackgroundPaint.setARGB(128, 255, 255, 255);
        mTextLevelPaint = new Paint();
        mTextLevelPaint.setAntiAlias(false);
        mTextLevelPaint.setARGB(192, 255, 0, 0);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mTargetPaint = new Paint();
        mTargetPaint.setAntiAlias(false);
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(false);
        mPathPaint.setStrokeWidth(5);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.setValue("Hello, World!");
        // [END initialize_database_ref]

        tempFileService=new FileIO();
        PointerState ps = new PointerState();
        mPointers.add(ps);
        //traverse through the coordinates and add every one to the database
        for (int i = 0; i < ps.mXs.size(); i++) {
            float x = ps.mXs.get(i);
            float y = ps.mYs.get(i);
            String coordinate = "("+ x + y +")";
            mDatabase.child("Xcoordinate").child("gesture1").setValue(getX());
            mDatabase.child("Ycoordinate").child("gesture1").setValue(getY());
        }

    }

    public void setFileGesture(String gestureFile){
        fileGesture=gestureFile;
    }
    public void setFilePath(String filePathtemp){
        filePath=filePathtemp;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTextPaint.getFontMetricsInt(mTextMetrics);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        synchronized (mPointers) {
            final int NP = 1;

            double[][] tempMultiXY=new double[NP][2];
            String[] tempMultiXYstr=new String[NP];

            int ZeroCount=0;
            for (int p = 0; p < NP; p++) {
                final PointerState ps = mPointers.get(p);

                final int N = ps.mXs.size();
                float lastX = 0, lastY = 0;
                float pressure=0;
                boolean haveLast = false;
                mPaint.setColor(colors[p]);
                mPathPaint.setColor(colors[p]);

                for (int i = 0; i < N; i++) {
                    float x = ps.mXs.get(i);
                    float y = ps.mYs.get(i);
                    pressure=ps.mPressure.get(i);

                    if (haveLast){
                        canvas.drawLine(lastX, lastY, x, y, mPathPaint);
                        canvas.drawPoint(lastX, lastY, mPaint);
                    }

                    lastX = x;
                    lastY = y;
                    haveLast=true;
                    mDatabase.child("coordinate"+i).child("gesture1").setValue(lastX + "," + lastY);

                }
                tempMultiXY[p][0] = lastX;
                tempMultiXY[p][1] = lastY;

                if ((lastX == 0) || (lastY == 0)) {
                    ZeroCount = ZeroCount + 1;
                }
                tempMultiXYstr[p] = lastX + " " + lastY+" "+pressure;
            }
            String tempMultiXYs;
            String tempMultiXYsFinal="";
            for (int iXY=0; iXY<NP; iXY++){
                tempMultiXYs=tempMultiXYstr[iXY]+" ";
                tempMultiXYsFinal=tempMultiXYsFinal+tempMultiXYs;
            }
            tempMultiXYsFinal=tempMultiXYsFinal+"\r\n";
            if (ZeroCount==0){
                try {
                    tempFileService.save(fileGesture, tempMultiXYsFinal,true);
                } catch (IOException e) {
                    System.out.println("App crashed test");
                    e.printStackTrace();
                }
            }
        }
    }

    public void clear(){
        ps.mXs.clear();
        ps.mYs.clear();
        TouchDownCount=0;
        invalidate();
    }
    public void addTouchEvent(MotionEvent event) {
        synchronized (mPointers) {
            int action = event.getAction();
//            int NP = mPointers.size();
            int NP = 1;
            if (action == MotionEvent.ACTION_DOWN) {
                String tempTemplateName=fileGesture;
//                checkIsExist(tempTemplateName);
                for (int p = 0; p < NP; p++) {
                    ps = mPointers.get(p);
                    ps.mXs.clear();
                    ps.mYs.clear();
                    tempFileService.delete(filePath,fileGesture);
                    String tempTouchDown=0 +" "+0+"\r\n";
                    try {
                        tempFileService.save(fileGesture, tempTouchDown,true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                TouchDownCount=TouchDownCount+1;
//                System.out.println("TouchCount:"+TouchDownCount);
            }
            if (action==MotionEvent.ACTION_MOVE){
//            final int NI = event.getPointerCount();
                final int NI = 1;
                for (int i = 0; i < NI; i++) {
                    final int id = event.getPointerId(i);
//                    System.out.println("Move ID crash=" + id);


                    if (id==0){
                        final PointerState ps = mPointers.get(id);
                        final int N = event.getHistorySize();
                        for (int j = 0; j < N; j++) {
                            ps.mXs.add(event.getHistoricalX(i, j));
                            ps.mYs.add(event.getHistoricalY(i, j));
                            ps.mPressure.add(event.getHistoricalPressure(i,j));
                        }
                        ps.mXs.add(event.getX(i));
                        ps.mYs.add(event.getY(i));
                        ps.mPressure.add(event.getPressure(i));
                    }
                }
            }

            if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN) {
            }

            if ((action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP) {
            }
            if (action == MotionEvent.ACTION_UP) {
                final int NI = 1;
                for (int i = 0; i < NI; i++) {
                    final int id = event.getPointerId(i);
//                    System.out.println("Up ID crash=" + id);

                    if(id==0){
                        final PointerState ps = mPointers.get(id);

                        final int N = event.getHistorySize();
                        for (int j = 0; j < N; j++) {
                            ps.mXs.add(event.getHistoricalX(i, j));
                            ps.mYs.add(event.getHistoricalY(i, j));
                            ps.mPressure.add(event.getPressure(i));
                        }
                        ps.mXs.add(Float.NaN);
                        ps.mYs.add(Float.NaN);
                        ps.mPressure.add(event.getPressure(i));
                    }
                }
            }
            postInvalidate();
        }
    }
    public boolean checkIsExist(String newTemplateName){
        // check if the .txt file is existing, if existing, clear it.
        File f=new File(filePath+newTemplateName);
        try{
            if(!f.exists()){
                return false;
            }
        }catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        FileWriter writer;
        try {
            writer = new FileWriter(f,false);
            writer.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        addTouchEvent(event);
        return true;
    }
    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return super.onTrackballEvent(event);
    }

}