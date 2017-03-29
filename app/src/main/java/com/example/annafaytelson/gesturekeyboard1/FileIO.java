package com.example.annafaytelson.gesturekeyboard1;


import android.content.Context;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileIO {

    private static final String DIR = "Attack";
    private static String Path=Environment.getExternalStorageDirectory().toString()+ File.separator+ DIR+ File.separator;
    Context context;
    public void setPath(String filePath){
        Path=filePath;
    }
    public void save(String fileName, String fileContent,boolean Save) throws IOException {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
        {
            File file = new File(Path+ fileName+".txt");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, Save);// ׷���ļ� :save is true
            fileOutputStream.write(fileContent.getBytes());
            fileOutputStream.close();
        } else {
        }
    }

    public String read(String fileName) throws IOException {
        byte []data = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(Path+ fileName+".txt");
            if(!file.getParentFile().exists())
            {
                file.getParentFile().mkdirs();
            }

            FileInputStream fileInputStream=new FileInputStream(file);
            int len=0;
            byte[] buffer=new byte[1024];
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            while((len=fileInputStream.read(buffer))!=-1)
            {
                baos.write(buffer, 0, len);
            }
            data=baos.toByteArray();
            baos.close();
            fileInputStream.close();
        }
        else {
        }
        return new String(data);
    }

    public String readFile(String fileName) throws IOException{
        String res="";
        try{
            res=read(fileName);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public void delete(String filePath, String fileName){

        File file = new File(filePath,fileName+".txt");
        if(file.exists()){
            file.delete();
        };
    }

    public void cleanGesture(String tempGestureName) throws IOException{
        String tempGesture=readFile(tempGestureName);
        String[] GestureList=tempGesture.split("\r\n");
        int GestureLength=GestureList.length;
        ArrayList<String> GestureList_update=new ArrayList<String>();
        for (int iSample=0; iSample<GestureLength; iSample++){
            String[] tempSample=GestureList[iSample].split(" ");
            int sampleLength=tempSample.length;
            int tempNan=0;
            for (int Item=0; Item<sampleLength; Item++){
                int t=Float.isNaN(Float.parseFloat(tempSample[Item]))? 1:0;
                tempNan=tempNan+t;
            }
            if (tempNan==0){
                GestureList_update.add(GestureList[iSample]);
            }
        }
        String WriteBack="";
        for(int iW=0; iW<GestureList_update.size();iW++){
            WriteBack=WriteBack+GestureList_update.get(iW)+"\r\n";
        }
        save(tempGestureName,WriteBack ,false);
    }

    public int checkStrokeNumber(String tempGestureName) throws IOException{
        int strokeNumber=0;
        String tempGesture=readFile(tempGestureName);
        String[] GestureList=tempGesture.split("\r\n");
        int GestureLength=GestureList.length;
        ArrayList<String> GestureList_update=new ArrayList<String>();
        for (int iSample=0; iSample<GestureLength; iSample++){
            String[] tempSample=GestureList[iSample].split(" ");
            int sampleLength=tempSample.length;
            int tempNan=0;
            for (int Item=0; Item<sampleLength; Item++){
                if (Float.parseFloat(tempSample[Item])==0){
                    tempNan++;
                };
            }
            if (tempNan==sampleLength){
                strokeNumber++;
            }
        }
        return strokeNumber;
    }

    public int checkTouchNumber(String tempGestureName) throws IOException{
        int touchNumber=0;
        String tempGesture=readFile(tempGestureName);
        String[] GestureList=tempGesture.split("\r\n");
        int GestureLength=GestureList.length;
        ArrayList<String> GestureList_update=new ArrayList<String>();

        String[] tempSample=GestureList[5].split(" ");
        touchNumber=tempSample.length/3;

        return touchNumber;
    }
    public void updateProgress(String progressFile, int startIndex) throws IOException{
        save(progressFile, ""+startIndex,false);
    }
    public int readProgress(String progressFile) throws IOException{
        String progress=readFile(progressFile);
        int startIndex=Integer.parseInt(progress);
        return startIndex;
    }

    public void removeFromUserList(String username) throws IOException{
        String tempUsernameList = readFile("userlist");
        String[] UsernameList=tempUsernameList.split("\r\n");
        int ListLength=UsernameList.length;
        String[] UsernameList_update=new String[ListLength-1];

        int iUpdate=0;
        for (int iList=0; iList<ListLength;iList++){
            if (!UsernameList[iList].equals(username)){
                UsernameList_update[iUpdate]=UsernameList[iList];
                iUpdate=iUpdate+1;
            };
        }

        String WriteBack="";
        for(int iW=0; iW<ListLength-1;iW++){
            WriteBack=WriteBack+UsernameList_update[iW]+"\r\n";
        }
        save("userlist",WriteBack ,false);
    }

    public void saveTestGesture(String tempGestureName, String TestName) throws IOException{
        String gestureString=read(tempGestureName);
        save(TestName, gestureString,false);
    }

    public void updateGesture( String TempName,String targetGestureName) throws IOException{
        File tempG=new File(Path+targetGestureName);

        tempG.delete();
        if (!tempG.exists()){
            tempG.createNewFile();
        }

        String gestureString=read(TempName);
        save(targetGestureName, gestureString,false);
    }




/*
    public double[][] StringtoMatrix2D(String X){
        String[] XGestureArray=X.split("\r\n");
        int XLength=XGestureArray.length;
        String[] XGestureWidth=XGestureArray[1].split(" ");
        int XWidth=XGestureWidth.length;
        double [][]XNum=new double[XLength][XWidth];
        for (int iX=0;iX<XLength;iX++){
            String[] tempGesture=XGestureArray[iX].split(" ");
            for (int iY=0;iY<XWidth;iY++){
                XNum[iX][iY]=Double.parseDouble(tempGesture[iY]);
            }
        }
        return XNum;

    }
    public double[] StringtoMatrix1D(String X){
        String[] XGestureArray=X.split("\r\n");
        int XLength=XGestureArray.length;
        double []XNum=new double[XLength];
        for (int iX=0;iX<XLength;iX++){
            String tempGesture=XGestureArray[iX];
            XNum[iX]=Double.parseDouble(tempGesture);
        }
        return XNum;
    }
    public String Matrix2DtoString(double[][] X){
        int x_d1=X.length;
        int x_d2=X[0].length;
        String tempMultiXYsFinal="";
        for (int d1=0; d1<x_d1; d1++){
            String temp1D="";
            String tempMultiXYsFinal_1d="";
            for (int d2=0; d2<x_d2; d2++){
                temp1D=X[d1][d2]+" ";
                tempMultiXYsFinal_1d=tempMultiXYsFinal_1d+temp1D;
            }
            tempMultiXYsFinal=tempMultiXYsFinal+tempMultiXYsFinal_1d+"\r\n";
        }
        return tempMultiXYsFinal;
    }
    public String Matrix1DtoString(double[] X){
        int x_d1=X.length;
        String tempMultiXYsFinal="";
        for (int d1=0; d1<x_d1; d1++){
            tempMultiXYsFinal=tempMultiXYsFinal+X[d1]+"\r\n";
        }
        return tempMultiXYsFinal;
    }
    public String Matrix1DtoString_String(String[] X){
        int x_d1=X.length;
        String tempMultiXYsFinal="";
        for (int d1=0; d1<x_d1; d1++){
            tempMultiXYsFinal=tempMultiXYsFinal+X[d1]+"\r\n";
        }
        return tempMultiXYsFinal;
    }
    */
}
