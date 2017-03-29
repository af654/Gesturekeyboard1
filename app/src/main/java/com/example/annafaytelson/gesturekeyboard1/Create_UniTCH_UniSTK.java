package com.example.annafaytelson.gesturekeyboard1;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 12/23/2015.
 */
public class Create_UniTCH_UniSTK extends Activity {
    Button BtClear;
    Button BtEnter;

    String NewUsername;
    String filePathSession1;
    RelativeLayout relativelayout;

    /*
    String filePath;
    int reSampleRate;
    int TotalGesture;
    double SimiThreshold;
    int ErrorCount;
    int ErrorCountThres;
*/
    ConstantValue constantValue=new ConstantValue();
    int reSampleRate=constantValue.reSampleRate;
    int TotalGesture=constantValue.TotalGesture;
    double SimiThreshold=constantValue.SimiThreshold;
    int ErrorCount=constantValue.ErrorCount;
    int ErrorCountThres=constantValue.ErrorCountThres;
    String filePath=constantValue.filePath;

    String fileGestureType;
    String fileGesture;
    String tempGesture;
    String tempGesturePre;
    String tempGesture2;

    int TPLNum=0;
    int MisMatchCount=0;


    LocationViewUTUS touchView;
    FileIO tempFileService;
    mProtractor simiTool;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //CustomActivityOnCrash.install(this);
        //CustomActivityOnCrash.setShowErrorDetails(false);
        //CustomActivityOnCrash.setRestartActivityClass(ErrorHandling.class);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //remove title
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // remove the status bar of phone
        setContentView(R.layout.activity_main); // experiment use

        relativelayout=(RelativeLayout)findViewById(R.id.frameSession1);

        Intent intentCreateActivity=getIntent();
        NewUsername=intentCreateActivity.getStringExtra("UserName");

        filePathSession1=filePath+File.separator+"Session1"+File.separator;
        BtClear=(Button)findViewById(R.id.clearSession1);
        BtEnter=(Button)findViewById(R.id.enterSession1);
        BtClear.setOnClickListener(AddTemplatesListener);
        BtEnter.setOnClickListener(AddTemplatesListener);

        fileGestureType="UTUS";
        fileGesture=NewUsername+fileGestureType+" ("+TPLNum+")";
        tempGesture=fileGestureType+"temp";
        tempGesture2=fileGestureType+"temp2";
        tempGesturePre=fileGestureType+"tempPre";

        tempFileService=new FileIO();
        tempFileService.setPath(filePathSession1);
        touchView = new LocationViewUTUS(this);

        touchView.setFileGesture(tempGesture);
        touchView.setFilePath(filePathSession1);
        relativelayout.addView(touchView);

        simiTool=new mProtractor();
        simiTool.setReSampleRate(reSampleRate);
        simiTool.setPath(filePathSession1);

        welcomAlert();
    }

    @Override
    public void onBackPressed() {
    }

    private View.OnClickListener AddTemplatesListener=new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Button vAddTemplates=(Button) v;
            switch (vAddTemplates.getId()){
                case R.id.enterSession1:// add one more
                    boolean checkStrokeNumber=false;
                    boolean checkTouchNumber=false;
                    try{
                        checkStrokeNumber=tempFileService.checkStrokeNumber(tempGesture)==1;
                        checkTouchNumber=tempFileService.checkTouchNumber(tempGesture)==1;
                    }
                    catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (checkStrokeNumber &&checkTouchNumber){
                        if (TPLNum<1){
                            touchView.setFileGesture(tempGesture);
                            TPLNum=TPLNum+1;
                            fileGesture=NewUsername+fileGestureType+" (" + TPLNum + ")";
                            try {
                                tempFileService.cleanGesture(tempGesture);
                                tempFileService.updateGesture(tempGesture, tempGesturePre);

                                tempFileService.saveTestGesture(tempGesture, fileGesture);
                                touchView.clear();
                                tempFileService.delete(filePathSession1, tempGesture);

                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            if (TPLNum<TotalGesture) {
                                SuccessAlert();
                            }
                            else{
                                finishAlert();
                            }
                        }
                        else{
                            try {
                                tempFileService.cleanGesture(tempGesture);
                                tempFileService.updateGesture(tempGesture, tempGesture2);
                                double tempSimi=simiTool.gestureSimilarity(tempGesture2,tempGesturePre);
                                if(tempSimi>=SimiThreshold){
                                    touchView.setFileGesture(tempGesture);
                                    TPLNum=TPLNum+1;
                                    fileGesture=NewUsername+fileGestureType+" (" + TPLNum + ")";
                                    try {
                                        tempFileService.cleanGesture(tempGesture);
                                        tempFileService.updateGesture(tempGesture, tempGesturePre);

                                        tempFileService.saveTestGesture(tempGesture, fileGesture);
                                        touchView.clear();
                                        tempFileService.delete(filePathSession1, tempGesture);

                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    if (TPLNum<TotalGesture) {
                                        SuccessAlert();
                                    }
                                    else{
                                        finishAlert();
                                    }
//                                Toast.makeText(Create_UniTCH_UniSTK.this, "The similairy is "+tempSimi, Toast.LENGTH_LONG).show();
                                }
                                else if (ErrorCount<ErrorCountThres){

                                    try {
                                        ErrorCount++;
//                                            System.out.println("RESET error count 1=" + ErrorCount);
                                        failAlert();
                                        touchView.clear();
                                        String fileGestureMisMatch = "MisMatch_" + NewUsername + fileGestureType + " (" + TPLNum + ")" + " (" + ErrorCount + ")";
                                        tempFileService.updateGesture(tempGesture, fileGestureMisMatch);

                                        tempFileService.delete(filePathSession1, tempGesture);
                                    }catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }
                                else{
                                    ErrorCount++;
                                    String fileGestureMisMatch = "MisMatch_" + NewUsername + fileGestureType + " (" + TPLNum + ")" + " (" + ErrorCount + ")";
                                    tempFileService.updateGesture(tempGesture, fileGestureMisMatch);
                                    fileGesture = NewUsername + fileGestureType + " (" + TPLNum + ")";

                                    tempFileService.updateGesture(fileGesture, "MisMatch_" + NewUsername + fileGestureType + " (" + TPLNum + ")" + " (0)");
                                    filePathSession1 = filePath + File.separator + "Session1" + File.separator;
                                    tempFileService.delete(filePathSession1, tempGesture);
                                    tempFileService.delete(filePathSession1, tempGesturePre);
                                    tempFileService.delete(filePathSession1, fileGesture);
                                    ErrorCount = 0;
                                    resetAlert();

                                    touchView.clear();
                                    TPLNum=TPLNum-1;
                                }

                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        failAlert2();
                    }
                    break;
                case R.id.clearSession1: // re-try
                    touchView.clear();
                    tempFileService.delete(filePathSession1,tempGesture);
                    Toast.makeText(Create_UniTCH_UniSTK.this, "Please re-input this gesture again!", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    };
    public void SuccessAlert(){
        Toast.makeText(Create_UniTCH_UniSTK.this, "Your gesture has been saved! Please enter another gesture!", Toast.LENGTH_LONG).show();
    }

    public void finishAlert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Notification");
        builder1.setMessage("Congratulations! \r\nYou have finished this task of free form gesture. \r\nPlease click OK for the next task!");
        builder1.setCancelable(true);
        builder1.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        try {
                            FileIO tempIO=new FileIO();
                            tempIO.setPath(filePath);
                            tempIO.updateProgress(getString(R.string.session2Progress_user), 301);
                            tempFileService.setPath(filePathSession1);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        tempFileService.delete(filePathSession1,tempGesturePre);
                        Intent intentUTUS=new Intent();
                        intentUTUS.putExtra("UserName",NewUsername);

                        //intentUTUS.setClass(Create_UniTCH_UniSTK.this, Create_UniTCH_MulSTK.class);
                        Create_UniTCH_UniSTK.this.startActivity(intentUTUS);
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public void welcomAlert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Session 1");
        builder1.setMessage("You need to input 4 types of gestures.\n" +
                "   1) Single finger and do not lift your finger during drawing;\n" +
                "   2) Single finger and lift your finger at least once during drawing;\n" +
                "   3) Multiple fingers and do not lift your fingers during drawing;\n" +
                "   4) Multiple fingers and lift your fingers at least once during drawing.\n" +
                "\nFor each type of gesture, you need to input twice.");
        builder1.setCancelable(true);
        builder1.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                        session1Alert();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };
    public void session1Alert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Session 1, Task 1");
        builder1.setMessage("Please input the gesture with ONE finger and NOT LIFT your finger during drawing.");
        builder1.setCancelable(true);
        builder1.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };
    public void failAlert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Sorry!");
        builder1.setMessage("Your gesture is not similar enough as your previous one!\r\n" +
                "Please try again!");
        builder1.setCancelable(true);
        builder1.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };
    public void failAlert2(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Sorry!");
        builder1.setMessage("Your gesture need to be drawn by ONE finger and DO NOT LIFT your finger during drawing!\r\n" +
                "Please try again!");
        builder1.setCancelable(true);
        builder1.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };
    public void resetAlert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Start Over!");
        builder1.setMessage("Your have input too many unsimilar trials as your previous one!\r\n" +
                "Please start over for this gesture name");
        builder1.setCancelable(true);
        builder1.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    };

}
