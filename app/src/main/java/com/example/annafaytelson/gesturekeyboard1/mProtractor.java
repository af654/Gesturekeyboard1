package com.example.annafaytelson.gesturekeyboard1;


import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 1/1/2016.
 */
public class mProtractor {
    FileIO tempFileService=new FileIO();
    static String Path;
    String fileGesture;
    int ReSampleRate;
    String Scale;
    String Location;
    String Rotation;
    String UserName;
    matlabTool tempMatlab=new matlabTool();

    public String SimiScoreTemplates(String combinationType, String filePath,String UserName){
        String[] templatesSet=thiftTemplates(combinationType,filePath,UserName);
//		System.out.println("Simi"+templatesSet[0]);
        double[] Output=new double[2];
        String SimiScoreFinal="0";

        String[] OutputString=new String[2];
        if (templatesSet[0].equals("templateIsEmpty")){
            OutputString[0]="-1";
            OutputString[1]="empty";
            SimiScoreFinal ="-1";
            return SimiScoreFinal;
        }
        else{
            int templateSetLength=templatesSet.length;
            double[] SimiScore= new double[templateSetLength];

            for (int i=0;i<templateSetLength;i++){
                String TSTcurGestureName=fileGesture;
                String TPLtempGestureName=templatesSet[i];
                int TSTcurWidth=0;
                int TPLtempWidth=0;

                try {
                    TSTcurWidth = getPathWidth(TSTcurGestureName);
                    TPLtempWidth=getPathWidth(TPLtempGestureName);

                    if (TSTcurWidth==TPLtempWidth){
                        double[][] TSTcurGesture= preProcessing(TSTcurGestureName, ReSampleRate, Scale, Location, Rotation);
                        double[][] TPLtempGesture= preProcessing(TPLtempGestureName, ReSampleRate, Scale, Location, Rotation);
                        SimiScore[i]=similarityScore(TPLtempGesture,TSTcurGesture, TPLtempWidth,TSTcurWidth, ReSampleRate, Rotation);
                    }
                    else{
                        SimiScore[i]=0;
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Output=tempMatlab.max(SimiScore);
            SimiScoreFinal=""+Output[0];
            return SimiScoreFinal;
        }
    }
    public void setPath(String filePath){
        Path=filePath;
        tempFileService.setPath(filePath);
    }
    public void setReSampleRate(int reSampleRate){
        ReSampleRate=reSampleRate;
    }

    public double gestureSimilarity(String gesture1, String gesture2){
        double simiScore=0;

        int TSTcurWidth=0;
        int TPLtempWidth=0;

        try {
            TSTcurWidth = getPathWidth(gesture1);
            TPLtempWidth=getPathWidth(gesture2);

            if (TSTcurWidth==TPLtempWidth){
                double[][] TSTcurGesture= preProcessing(gesture1, ReSampleRate, "0", "0", "0");
                double[][] TPLtempGesture= preProcessing(gesture2, ReSampleRate, "0", "0", "0");

                TPLtempWidth=TPLtempWidth/3*2;
                TSTcurWidth=TSTcurWidth/3*2;
                simiScore=similarityScore(TPLtempGesture,TSTcurGesture, TPLtempWidth,TSTcurWidth, ReSampleRate, "0");
            }
            else{
                simiScore=0;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return simiScore;
    }

    public double similarityScore(double[][] template,double[][] test, int width_Temp,int width_Test, int length, String isRotation){
        double similarityScore;

        if (width_Temp==width_Test){
            double [][] templateFinger=new double [length][2];
            double [][] testFinger=new double [length][2];
            double[] similarity=new double[width_Temp/2];

            for (int iSimiarity=0;iSimiarity<width_Temp/2;iSimiarity++){
                for (int iFinger=0; iFinger<2; iFinger++){
                    for (int iLength=0;iLength<length;iLength++){
                        templateFinger[iLength][iFinger]=template[iLength][iSimiarity*2+iFinger];
                    }
                }
                for (int iFinger=0; iFinger<2; iFinger++){
                    for (int iLength=0;iLength<length;iLength++){
                        testFinger[iLength][iFinger]=test[iLength][iSimiarity*2+iFinger];
                    }
                }
                similarity[iSimiarity]=similarityScorePerFinger(templateFinger,testFinger,width_Temp,length,isRotation);
            }
            similarityScore=tempMatlab.sum(similarity)/(width_Temp/2);
            return 	similarityScore;
        }
        else{
            similarityScore=0;
            return similarityScore;
        }
    }


    public double similarityScorePerFinger(double[][] templateFinger,double[][] testFinger,int width,int length,String isRotation){
        matlabTool matTool=new matlabTool();

        double[] x_t=matTool.matrix2Dto1D_col(testFinger, 0);
        double[] y_t=matTool.matrix2Dto1D_col(testFinger, 1);
        double[] x_g=matTool.matrix2Dto1D_col(templateFinger, 0);
        double[] y_g=matTool.matrix2Dto1D_col(templateFinger, 1);

        double[] v_tg=new double[length];
        double[] v_t=new double[length];
        double[] v_g=new double[length];
        double[] aOptimal=new double[length];
        double[] bOptimal=new double[length];
        double[] xOptimal=new double[length];
        double[] yOptimal=new double[length];

        if (isRotation.equals("0")){
            for (int iOptimal=0;iOptimal<length;iOptimal++){
                aOptimal[iOptimal]=x_t[iOptimal]*x_g[iOptimal]+y_t[iOptimal]*y_g[iOptimal];
                bOptimal[iOptimal]=x_t[iOptimal]*y_g[iOptimal]-y_t[iOptimal]*x_g[iOptimal];
            }
            double thetaOptimal_1=Math.atan2(matTool.sum(bOptimal), matTool.sum(aOptimal));
            for (int iOptimal_2=0;iOptimal_2<length;iOptimal_2++){
                xOptimal[iOptimal_2]=x_t[iOptimal_2]*Math.cos(thetaOptimal_1)-y_t[iOptimal_2]*Math.sin(thetaOptimal_1);
                yOptimal[iOptimal_2]=x_t[iOptimal_2]*Math.sin(thetaOptimal_1)+y_t[iOptimal_2]*Math.cos(thetaOptimal_1);
            }
        }
        else{
            xOptimal=x_t;
            yOptimal=y_t;
        }

        //similarity score
        for(int iSimilarity=0;iSimilarity< length;iSimilarity++){
            v_tg[iSimilarity]=xOptimal[iSimilarity]*x_g[iSimilarity]+yOptimal[iSimilarity]*y_g[iSimilarity];
            v_t[iSimilarity]=xOptimal[iSimilarity]*xOptimal[iSimilarity]+yOptimal[iSimilarity]*yOptimal[iSimilarity];
            v_g[iSimilarity]=x_g[iSimilarity]*x_g[iSimilarity]+y_g[iSimilarity]*y_g[iSimilarity];

        }
        double sum_1=matTool.sum(v_tg);
        //gets the length of the gesture
        double sum_2=Math.sqrt(matTool.sum(v_t))*Math.sqrt(matTool.sum(v_g));

        double sum1Osum2=sum_1/sum_2;
        double similarityScore;
        if (Math.acos(sum1Osum2)<=0.0001){
            similarityScore=8;
        }
        else{
            similarityScore=1/Math.acos(sum1Osum2);
        }
        return similarityScore;
    }


    public String[] thiftTemplates(String combinationType, String filePath, String UserName){
        //find the templates under the combination
        File file=new File(filePath);
        StringBuffer TemplateList= new StringBuffer();

//		System.out.println("filePath:"+filePath);
//		System.out.println("UserName:"+UserName);
        File[]fileList=file.listFiles();
        for(int i=0;i<fileList.length;i++){
            String tempName = fileList[i].getName();
            if (tempName.startsWith(combinationType+"TPL"+UserName)){
                TemplateList.append(tempName+"\r\n");
            }
        }
        String tempTemplateListString=TemplateList.toString();
        String [] TemplateListString=tempTemplateListString.split("\r\n");
        if (TemplateListString.length!=0){
            return TemplateListString;
        }
        else{
            String [] returnString= new String[1];
            returnString[0]="templateIsEmpty";
            return returnString;
        }
    }

    public String[] thiftByFinger(String[] dataList, String filePath, String UserName) throws IOException{
        //Assumed there always has the gestures in a specific finger number
        String fileName=Scale+Location+Rotation+"TPL"+UserName+"___"+1;

//		System.out.println("sameList:\r\n"+newIO.Matrix1DtoString_String(dataList_SameFinger));

        int ThresFingerNum=getPathWidth(fileName);


        StringBuffer Thifted=new StringBuffer();
        for (int iT=0;iT<dataList.length;iT++){
            String tempName=dataList[iT];
            int TempFingerNum=getPathWidth(tempName);
            if (TempFingerNum==ThresFingerNum){
                Thifted.append(tempName+"\r\n");
            }
        }
        String tempThiftedListString=Thifted.toString();
        String [] ThiftedListString=tempThiftedListString.split("\r\n");
        return ThiftedListString;
    }
    public void setFileGesture(String gestureFile){
        fileGesture=gestureFile;
    }

    public void setSLRresample(String tempScale,String tempLocation,String tempRotation, int resample, String tempUserName ){
        ReSampleRate=resample;
        Scale=tempScale;
        Location=tempLocation;
        Rotation=tempRotation;
        UserName=tempUserName;
    }
    public double[][] preProcessing(String newGesture, int ReSampleRate, String isScale, String isLocation, String isRotation) throws IOException{
        int pathWidth=getPathWidth(newGesture)/3*2;
//		matlabTool matTool=new matlabTool();
        // reSampling
        double[][] reSampledGesture=new double[ReSampleRate][pathWidth];
        reSampledGesture=reSample(newGesture,ReSampleRate);

//		System.out.println("preProcessing width_resample="+reSampledGesture[0].length);

        // If Location take into consideration
//		double[][] GestureLocation=new double[ReSampleRate][pathWidth];
        double[][] GestureLocation=reSampledGesture;
        if (isScale.equals("0")){
            GestureLocation=centered(reSampledGesture,pathWidth, ReSampleRate);
        }

//		System.out.println("preProcessing width_L="+GestureLocation[0].length);
        // If Rotation take into consideration
        double[][] GestureRotation=GestureLocation;
//		GestureRotation=GestureLocation;
        if (isRotation.equals("0")){
            GestureRotation=rotated(GestureLocation,pathWidth,ReSampleRate);
        }

//		System.out.println("preProcessing width_R="+GestureRotation[0].length);

        // If Scaling take into consideration
        double[][] GestureScale=GestureRotation;
//		GestureScale=GestureRotation;
        if(isScale.equals("0")){
            GestureScale=scaled(GestureRotation,pathWidth,ReSampleRate);
        }
//		System.out.println("preProcessing width_S="+GestureScale[0].length);
        return GestureScale;
    }

    public int getPathWidth(String newGesture) throws IOException{
        //obtain the fingerNum of the new gesture
        String pathGesture=tempFileService.read(newGesture);
        String[] pathGestureArray=pathGesture.split("\r\n");
        String[] pathGestureWidth=pathGestureArray[(int)(pathGestureArray.length*4/5)].split(" ");
        int pathWidth=pathGestureWidth.length;
        return pathWidth;
    }


    public double[][] reSample(String newGesture,int ReSampleRate) throws IOException{
        matlabTool tempMat=new matlabTool();

        String pathGesture=tempFileService.read(newGesture);

        String[] pathGestureArray=pathGesture.split("\r\n");
        int pathLength=pathGestureArray.length-2;
        String[] pathGestureWidth=pathGestureArray[pathGestureArray.length-2].split(" ");
        int pathWidth=pathGestureWidth.length;
        double [][]pathNum=new double[pathLength][pathWidth];
        double[][] pathNum_extend=tempMat.matrixInitial2(pathLength+ReSampleRate,pathWidth);


        int iCount=0;

        for (int iX=0;iX<pathLength;iX++){
            String[] tempGesture=pathGestureArray[iX+2].split(" ");

//    		System.out.println("tempGesture length="+tempGesture.length);
            int tempCount=0;
            for (int iY=0;iY<tempGesture.length;iY++){
                if (Double.parseDouble(tempGesture[iY])<2 && (iY+1)%3!=0){
//    				pathNum[iCount][iY]=Double.parseDouble(tempGesture[iY]);
//        			pathNum_extend[iCount][iY]=Double.parseDouble(tempGesture[iY]);
                    tempCount=tempCount+1;
                }

                if (tempGesture.length!=pathWidth){
                    tempCount=tempCount+1;
                }
            }
            if (tempCount==0){
                for (int iY=0;iY<pathWidth;iY++){

                    pathNum[iCount][iY]=Double.parseDouble(tempGesture[iY]);
                    pathNum_extend[iCount][iY]=Double.parseDouble(tempGesture[iY]);
                }
                iCount=iCount+1;
            }
        }
//        System.out.println("pathLength 1:"+pathLength);
        pathLength=iCount-1;

//        System.out.println("pathWidth:"+pathWidth);
//        System.out.println("pathLength 2:"+pathLength);

        double[][] pathNum_cut=new double[pathLength][pathWidth/3*2];
        double[][] pathNum_extend_cut=tempMat.matrixInitial2(pathLength+ReSampleRate,pathWidth/3*2);
        ///cut matrix
        for (int iCut=0; iCut<pathLength; iCut++){
            for (int jCut=0; jCut<pathWidth/3;jCut++){
                pathNum_cut[iCut][jCut*2]=pathNum[iCut][jCut*3];
                pathNum_cut[iCut][jCut*2+1]=pathNum[iCut][jCut*3+1];
                pathNum_extend_cut[iCut][jCut*2]=pathNum_extend[iCut][jCut*3];
                pathNum_extend_cut[iCut][jCut*2+1]=pathNum_extend[iCut][jCut*3+1];
            }
        }

        // resample

        pathWidth=pathWidth/3*2;
/*
        System.out.println("pathNum_extend_cut pathWidth:"+pathNum_extend_cut[0].length);
        System.out.println("pathNum_extend_cut pathLength:"+pathNum_extend_cut.length);
        System.out.println("pathNum_cut pathWidth:"+pathNum_cut[0].length);
        System.out.println("pathNum_cut pathLength:"+pathNum_cut.length);
*/
        double[][]pathResample=new double[ReSampleRate][pathWidth];
        for (int iFingerXY=0;iFingerXY<pathWidth/2;iFingerXY++){
            double[] gXtemp=tempMat.matrix2Dto1D_col(pathNum_cut, iFingerXY*2);
            double[] gYtemp=tempMat.matrix2Dto1D_col(pathNum_cut, iFingerXY*2+1);
            double thres=lengthGesture(gXtemp,gYtemp)/(ReSampleRate-1);
/*
            System.out.println("pathNum_extend_cut pathWidth:"+pathNum_extend_cut[0].length);
            System.out.println("iFingerXY:"+iFingerXY);
            System.out.println("pathNum_extend_cut value:"+pathNum_extend_cut[0][iFingerXY*2]);
*/
            pathResample[0][iFingerXY*2]=pathNum_extend_cut[0][iFingerXY*2];
            pathResample[0][iFingerXY*2+1]=pathNum_extend_cut[0][iFingerXY*2+1];

            double D=0;
            int iR=1;
            for (int iResample=1;iResample<pathNum_extend_cut.length-1;iResample++){
                double d=distance(pathNum_extend_cut[iResample-1][iFingerXY*2],pathNum_extend_cut[iResample-1][iFingerXY*2+1],
                        pathNum_extend_cut[iResample][iFingerXY*2],pathNum_extend_cut[iResample][iFingerXY*2+1]);

                if (D+d>=thres){
                    pathResample[iR][iFingerXY*2]=pathNum_extend_cut[iResample-1][iFingerXY*2]
                            +((thres-D)/d)*(pathNum_extend_cut[iResample][iFingerXY*2]-pathNum_extend_cut[iResample-1][iFingerXY*2]);
                    pathResample[iR][iFingerXY*2+1]=pathNum_extend_cut[iResample-1][iFingerXY*2+1]
                            +((thres-D)/d)*(pathNum_extend_cut[iResample][iFingerXY*2+1]-pathNum_extend_cut[iResample-1][iFingerXY*2+1]);

                    for (int iRevalue=iR+pathNum_cut.length;iRevalue>=iResample+1;iRevalue--){
                        pathNum_extend_cut[iRevalue][iFingerXY*2]=pathNum_extend_cut[iRevalue-1][iFingerXY*2];
                        pathNum_extend_cut[iRevalue][iFingerXY*2+1]=pathNum_extend_cut[iRevalue-1][iFingerXY*2+1];
                    }
                    pathNum_extend_cut[iResample][iFingerXY*2]=pathResample[iR][iFingerXY*2];
                    pathNum_extend_cut[iResample][iFingerXY*2+1]=pathResample[iR][iFingerXY*2+1];

                    iR=iR+1;
                    D=0;
                }
                else{
                    D=D+d;
                }
            }
            pathResample[ReSampleRate-1][iFingerXY*2]=pathNum_cut[pathLength-1][iFingerXY*2];
            pathResample[ReSampleRate-1][iFingerXY*2+1]=pathNum_cut[pathLength-1][iFingerXY*2+1];
        }

        double[][] newResample;

        if (pathResample.length>pathResample[0].length){
            newResample=pathResample;
        }
        else{
            newResample=tempMat.matrixTran(pathResample);
        }


        return newResample;
    }
    private double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));

    }

    private double lengthGesture(double[] gX, double[] gY){
        int x_d1=gX.length;
        double total=0;
        for (int i=1;i<x_d1; i++){
            total=total+Math.sqrt((gX[i]-gX[i-1])*(gX[i]-gX[i-1])+(gY[i]-gY[i-1])*(gY[i]-gY[i-1]));
        }
        return total;
    }


    private double[][] centered(double[][] preCenterPath, int pathWidth, int pathLength){
        //centered
        matlabTool matTool=new matlabTool();

        double[] Centroid=new double[pathWidth];
        double[][] centered=new double[pathLength][pathWidth];
        Centroid=matTool.matrixMean2D(preCenterPath);

//		System.out.println("preCenterPath="+preCenterPath.length+" ;"+preCenterPath[0].length);
//		System.out.println("centered:"+Centroid.length);

        for (int iCol=0;iCol<pathWidth;iCol++){
//			double centeroid1=Centroid[iCol];
            for (int iRow=0;iRow<pathLength;iRow++){
                centered[iRow][iCol]=preCenterPath[iRow][iCol]-Centroid[iCol];
            }

        }
        return centered;
    }

    private double[][] rotated(double[][] preRotation, int pathWidth, int pathLength){
        // centered
        matlabTool matTool=new matlabTool();
        // centroid
        double[] Centroid=matTool.matrixMean2D(preRotation);

        double[][] centeredGesture=centered(preRotation,pathWidth,pathLength);
        double[][] retatedGesture=new double[pathLength][pathWidth];


        // rotate & decentered
        for (int iFinger=0; iFinger<pathWidth/2;iFinger++){
//			System.out.println("Rotate width="+pathWidth+" pathLength="+pathLength);

            double[] tempX=matTool.matrix2Dto1D_col(centeredGesture, iFinger*2);
            double[] tempY=matTool.matrix2Dto1D_col(centeredGesture, iFinger*2+1);

//			System.out.println("tempX length="+tempY.length);

            double alignAngle=-Math.atan2(tempX[0], tempY[0]);
            double[] xRotated=tempX;
            double[] yRotated=tempY;
            for (int i_align=0;i_align<pathLength;i_align++){
                xRotated[i_align]=tempX[i_align]*Math.cos(alignAngle)-tempY[i_align]*Math.sin(alignAngle); // rotate
                yRotated[i_align]=tempX[i_align]*Math.sin(alignAngle)+tempY[i_align]*Math.cos(alignAngle);

//				System.out.println("retatedGesture length="+retatedGesture.length+"  width="+retatedGesture[0].length);
//				System.out.println("xRotated length="+xRotated.length+"  width=");
//				System.out.println("Centroid length="+Centroid.length+"  width=");
                retatedGesture[i_align][iFinger*2]=xRotated[i_align]+Centroid[iFinger*2]; // decentered
                retatedGesture[i_align][iFinger*2+1]=yRotated[i_align]+Centroid[iFinger*2+1];
            }
        }

        return retatedGesture;
    }
    private double[][] scaled(double[][] preScaling, int pathWidth, int pathLength){
        // centered
        matlabTool matTool=new matlabTool();
        // centroid
        double[] Centroid=matTool.matrixMean2D(preScaling);

        double[][] centeredGesture=centered(preScaling,pathWidth,pathLength);
        double[][] scaledGesture=new double[pathLength][pathWidth];

        for (int iFinger=0; iFinger<pathWidth/2;iFinger++){
            double[] tempX=matTool.matrix2Dto1D_col(centeredGesture, iFinger*2);
            double[] tempY=matTool.matrix2Dto1D_col(centeredGesture, iFinger*2+1);

            double sumX=Math.sqrt(matTool.sum(matTool.matrixSquare1(tempX)));
            double sumY=Math.sqrt(matTool.sum(matTool.matrixSquare1(tempY)));

            double[] tempXscaled=matTool.matrixDivide(tempX, sumX);
            double[] tempYscaled=matTool.matrixDivide(tempY, sumY);

            for (int i_align=0;i_align<pathLength;i_align++){      // decentered
                scaledGesture[i_align][iFinger*2]=tempXscaled[i_align]+Centroid[iFinger*2];
                scaledGesture[i_align][iFinger*2+1]=tempYscaled[i_align]+Centroid[iFinger*2+1];
            }
        }

        return scaledGesture;
    }

    public boolean compareSimilarity(double similarityScore,double criterion){
        if(similarityScore>=criterion){
            return true;
        }
        else{
            return false;
        }
    }


    public double criterionSLR(String isScale, String isLocation, String isRotation){
        double criterion;
        if(isScale.equals("0")){
            if (isLocation.equals("0")){
                if(isRotation.equals("0")){
                    criterion=1.0385; // S=0  L=0  R=0
                    return criterion;
                }
                else{
                    criterion=1.0013; // S=0  L=0  R=1
                    return criterion;
                }
            }
            else{
                if(isRotation.equals("0")){
                    criterion=0.001; // S=0  L=1  R=0
                    return criterion;
                }
                else{
                    criterion=22.5236; // S=0  L=1  R=1
                    return criterion;
                }
            }
        }
        else{
            if (isLocation.equals("0")){
                if(isRotation.equals("0")){
                    criterion=1.1329; // S=1  L=0  R=0
                    return criterion;
                }
                else{
                    criterion=8; // S=1  L=0  R=1
                    return criterion;
                }
            }
            else{
                if(isRotation.equals("0")){
                    criterion=3.4330; // S=1  L=1  R=0
                    return criterion;
                }
                else{
                    criterion=3.5627; // S=1  L=1  R=1
                    return criterion;
                }
            }
        }
    }

    public double criterionSLRComb(String isScale, String isLocation, String isRotation){
        double criterion;
        if(isScale.equals("0")){
            if (isLocation.equals("0")){
                if(isRotation.equals("0")){
                    criterion=1.0385; // S=0  L=0  R=0
                    return criterion;
                }
                else{
                    criterion=1.0013; // S=0  L=0  R=1
                    return criterion;
                }
            }
            else{
                if(isRotation.equals("0")){
                    criterion=0.001; // S=0  L=1  R=0
                    return criterion;
                }
                else{
                    criterion=22.5236; // S=0  L=1  R=1
                    return criterion;
                }
            }
        }
        else{
            if (isLocation.equals("0")){
                if(isRotation.equals("0")){
                    criterion=1.1329; // S=1  L=0  R=0
                    return criterion;
                }
                else{
                    criterion=9.5; // S=1  L=0  R=1
                    return criterion;
                }
            }
            else{
                if(isRotation.equals("0")){
                    criterion=3.4330; // S=1  L=1  R=0
                    return criterion;
                }
                else{
                    criterion=3.5627; // S=1  L=1  R=1
                    return criterion;
                }
            }
        }
    }

}
