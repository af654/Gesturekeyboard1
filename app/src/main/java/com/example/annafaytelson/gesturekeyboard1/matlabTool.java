package com.example.annafaytelson.gesturekeyboard1;

/**
 * Created by annafaytelson on 3/24/17.
 */

public class matlabTool {

    public double sum (double[] X){
        int sizeSum=X.length;
        double total=0;
        for (int iSum=0;iSum<sizeSum;iSum++){
            total=total+X[iSum];
        }
        return total;
    }

    public double sumsum (double[][] X){
        int sizeSum=X.length;
        int size_2=X[0].length;
        double total=0;
        for (int iSum=0;iSum<sizeSum;iSum++){
            for (int iSum_2=0;iSum_2<size_2;iSum_2++){
                total=total+X[iSum][iSum_2];
            }
        }
        return total;
    }
    public double[] sumsum2 (double[][] X){
        // equals matlab sum 1;
//		 	int sizeSum=X.length;
        int size_2=X[0].length;
        double[] totalSet=new double[size_2];
        for (int iSum=0;iSum<size_2;iSum++){
            totalSet[iSum]=sum(matrix2Dto1D_col(X, iSum));
        }
        return totalSet;
    }
    public double[] sumsum1 (double[][] X){
        // equals matlab sum 2;
        int sizeSum=X.length;
        int size_2=X[0].length;
        double[] totalSet=new double[sizeSum];
        for (int iSum_2=0;iSum_2<sizeSum;iSum_2++){
            double totaltemp=0;
            for (int iSum=0;iSum<size_2;iSum++){
                totaltemp=totaltemp+X[iSum_2][iSum];
            }
            totalSet[iSum_2]=totaltemp;
        }
        return totalSet;
    }

    public double[] matrixNormalize(double[] X){
        int x_d1=X.length;
        double tempMean=matrixMean1(X);
        double tempSTD=MatrixStdDev1(X);
        double[] result=new double[x_d1];
        for (int i=0; i<x_d1;i++){
            result[i]=(X[i]-tempMean)/tempSTD;
        }
        return result;

    }


    public double matrixMean1(double[] X){
        int sizeSum=X.length;
        double total=0;
        for (int iSum=0;iSum<sizeSum;iSum++){
            total=total+X[iSum];
        }
        double mean=total/sizeSum;
        return mean;
    }
    public double matrixMean2(double[][] X){
        int x_d1=X.length;
        int x_d2=X[0].length;
        double mean=sumsum(X)/(x_d1*x_d2);
        return mean;
    }

    public double[] matrixMean2D(double[][] X){
        int x_d1=X.length;
        int x_d2=X[0].length;
        double[] meanSet=new double[x_d2];
        for (int i=0;i<x_d2;i++){
            double[] tempX=matrix2Dto1D_col(X,i);
            meanSet[i]=sum(tempX)/x_d1;
        }
        return meanSet;
    }




    public double[] matrix2Dto1D_col(double[][]X, int colNum){
        int x_d1=X.length;
//		 int x_d2=X[0].length;
        double[] result=new double[x_d1];
        for (int i=0;i<x_d1;i++){
            result[i]=X[i][colNum];
        }
        return result;
    }

    public double[][] matrix3Dto2D_2(double[][][]X, int Num2D){
        int x_d1=X.length;
//		 int x_d2=X[0].length;
        int x_d3=X[0][0].length;
        double[][] result=new double[x_d1][x_d3];
        for (int i=0;i<x_d1;i++){
            for (int i3=0;i3<x_d3;i3++){
                result[i][i3]=X[i][Num2D][i3];
            }
        }
        return result;
    }




    public double[][] matrix2D_col_Assign(double[][]X, double[]Y,int colNum){

        double[][] result=X;
        if (X.length==Y.length){
            int x_d1=X.length;

            for (int i=0;i<x_d1; i++){
                result[i][colNum]=Y[i];
            }
        }
        else{
            System.out.println("matrix2D_col_Assign NOT taken");
        }


        return result;
    }



    public double[][] matrixMultiple(double[][] X,double[][]Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        int Y_d1=Y.length;
        int Y_d2=Y[0].length;

        double[][] result=new double[X_d1][Y_d2];

        if (X_d2==Y_d1){
            for (int iMul_d1=0; iMul_d1<X_d1;iMul_d1++){
                for (int iMul_d2=0; iMul_d2<Y_d2;iMul_d2++){
                    double[] tempR=new double[X_d2];
                    for (int iDim=0; iDim<X_d2; iDim++){
                        tempR[iDim]=X[iMul_d1][iDim]*Y[iDim][iMul_d2];
                    }
                    result[iMul_d1][iMul_d2]=sum(tempR);
                }
            }
        }
        else{
            System.out.println("matrixMultiple NOT taken");
        }

        return result;
    }
    public double[] matrixMultiple1(double[][] X,double[]Y){
        int X_d1=X.length;
        int X_d2=X[0].length;

        double[] result=new double[X_d1];


        if (X_d2==Y.length){
            for (int iMul_d1=0; iMul_d1<X_d1;iMul_d1++){
                double[] tempR=new double[X_d2];
                for (int iDim=0; iDim<X_d2; iDim++){
                    tempR[iDim]=X[iMul_d1][iDim]*Y[iDim];
                }
                result[iMul_d1]=sum(tempR);
            }
        }
        else{
            System.out.println("matrixMultiple1 NOT taken");
        }

        return result;
    }






    public double[][] matrixMultiple_point(double[][] X, double[][] Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        int Y_d1=Y.length;
        int Y_d2=Y[0].length;

        double[][] result=new double[X_d1][Y_d2];
        if ((X_d1==Y_d1)&&(X_d2==Y_d2)){
            for (int iMul_d1=0; iMul_d1<X_d1;iMul_d1++){
                for (int iMul_d2=0; iMul_d2<Y_d2;iMul_d2++){
                    result[iMul_d1][iMul_d2]=X[iMul_d1][iMul_d2]*Y[iMul_d1][iMul_d2];
                }
            }
        }
        else{
            System.out.println("matrixMultiple_point NOT taken");
        }

        return result;
    }

    public double[][] matrixMultiple_Element(double[][] X, double Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        double[][] result=new double[X_d1][X_d2];
        for (int iMul_d1=0; iMul_d1<X_d1;iMul_d1++){
            for (int iMul_d2=0; iMul_d2<X_d2;iMul_d2++){
                result[iMul_d1][iMul_d2]=X[iMul_d1][iMul_d2]*Y;
            }
        }
        return result;
    }

    public double[] matrixMultiple_Element1(double[] X, double Y){
        int X_d1=X.length;

        double[] result=new double[X_d1];
        for (int iMul_d1=0; iMul_d1<X_d1;iMul_d1++){
            result[iMul_d1]=X[iMul_d1]*Y;
        }
        return result;
    }


    public double[] matrixDivide(double[]X,double Y){
        int X_d1=X.length;
        double [] result=new double[X_d1];
        for (int iX=0;iX<X_d1;iX++){
            result[iX]=X[iX]/Y;
        }
        return result;
    }
    public double[] matrixDivide1_point(double[]X,double[] Y){
        int X_d1=X.length;
        double [] result=new double[X_d1];


        if (X_d1==Y.length){
            for (int iX=0;iX<X_d1;iX++){
                result[iX]=X[iX]/Y[iX];
            }
        }
        else{
            System.out.println("matrixDivide1_point NOT taken");
        }
        return result;

    }
    public double[][] matrixDivide2_point(double[][]X,double[][] Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        double [][] result=new double[X_d1][X_d2];

        if ((X_d1==Y.length)&&(X_d2==Y[0].length)){
            for (int iX=0;iX<X_d1;iX++){
                for (int iX2=0;iX2<X_d2;iX2++){
                    result[iX][iX2]=X[iX][iX2]/Y[iX][iX2];
                }
            }
        }
        else{
            System.out.println("matrixDivide2_point NOT taken");
        }

        return result;
    }
    public double[][] matrixDivideElement_2(double X,double[][] Y){
        int Y_d1=Y.length;
        int Y_d2=Y[0].length;
        double [][] result=new double[Y_d1][Y_d2];
        for (int iY=0;iY<Y_d1;iY++){
            for (int iY2=0;iY2<Y_d2;iY2++){
                result[iY][iY2]=X/Y[iY][iY2];
            }
        }
        return result;
    }




    public double[][] matrixTran(double[][] X){
        int X_d1=X.length;
        int X_d2=X[0].length;
        double[][] XT=new double[X_d2][X_d1];
        for (int x1=0;x1<X_d1; x1++){
            for (int x2=0;x2<X_d2; x2++){
                XT[x2][x1]=X[x1][x2];
            }
        }
        return XT;
    }

    public double[][] bsxfunPlus1(double[][] X,double[]Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        int Y_d1=Y.length;

        double [][] result=new double[X_d1][X_d2];
        if (X_d1==Y_d1){

            for (int i_1=0;i_1<X_d1;i_1++){
                for (int i_2=0;i_2<X_d2;i_2++){
                    result[i_1][i_2]=X[i_1][i_2]+Y[i_1];
                }
            }
        }
        else{
            System.out.println("bxs Plus NOT taken");
        }

        return result;
    }

    public double[][] bsxfunPlus2(double[] X,double[][] Y){
        int X_d1=X.length;
        int Y_d1=Y.length;
        int Y_d2=Y[0].length;

        double [][] result=new double[Y_d1][Y_d2];
        if (X_d1==Y_d1){
            for (int i_1=0;i_1<X_d1;i_1++){
                for (int i_2=0;i_2<Y_d2;i_2++){
                    result[i_1][i_2]=X[i_1]+Y[i_1][i_2];
                }
            }
        }
        else{
            System.out.println("bxs Plus2 NOT taken");
        }

        return result;
    }


    public double[][] bsxfunMinus1(double[][] X,double[]Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        int Y_d1=Y.length;

        double [][] result=new double[X_d1][X_d2];
        if (X_d2==Y_d1){
            for (int i_1=0;i_1<X_d1;i_1++){
                for (int i_2=0;i_2<X_d2;i_2++){
                    result[i_1][i_2]=X[i_1][i_2]-Y[i_2];
                }
            }
        }
        else{
            System.out.println("bxs Minus1 NOT taken");
        }
        return result;
    }

    public double[][] bsxfunRDivide1(double[][] X,double[]Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        int Y_d1=Y.length;

        double [][] result=new double[X_d1][X_d2];
        if (X_d2==Y_d1){
//			 System.out.println("bxs RDivide1 taken");
            for (int i_1=0;i_1<X_d1;i_1++){
                for (int i_2=0;i_2<X_d2;i_2++){
                    result[i_1][i_2]=X[i_1][i_2]/Y[i_2];
                }
            }
        }
        else{
            System.out.println("bxs RDivide1 NOT taken");
        }

        return result;
    }

    public double[] bsxfunTimes1(double[] X,double[]Y){
        int X_d1=X.length;

        double [] result=new double[X_d1];

        if (X_d1==Y.length){
            for (int i_1=0;i_1<X_d1;i_1++){
                result[i_1]=X[i_1]*Y[i_1];
            }
        }

        else{
            System.out.println("bsxfunTimes1 NOT taken");
        }
        return result;
    }

    public double[][] bsxfunTimes2(double[][] X,double[]Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        int Y_d1=Y.length;

        double [][] result=new double[X_d1][X_d2];
        if (X_d2==Y_d1){
//			 System.out.println("bxs Times2 taken");
            for (int i_1=0;i_1<X_d1;i_1++){
                for (int i_2=0;i_2<X_d2;i_2++){
                    result[i_1][i_2]=X[i_1][i_2]*Y[i_2];
                }
            }
        }
        else{
            System.out.println("bxs Times2 NOT taken");
        }
        return result;
    }

    public double[][] bsxfunMax1(double[][] X,double[]Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        int Y_d1=Y.length;

        double [][] result=new double[X_d1][X_d2];
        if (X_d1==Y_d1){
//			 System.out.println("bxs Max1 taken");
            for (int i_1=0;i_1<X_d1;i_1++){
                for (int i_2=0;i_2<X_d2;i_2++){
                    result[i_1][i_2]=Math.max(X[i_1][i_2], Y[i_1]);
                }
            }
        }
        else{
            System.out.println("bxs Max1 NOT taken");
        }


        return result;
    }






    public double[][] matrixExp(double[][]X){
        int X_d1=X.length;
        int X_d2=X[0].length;
        double[][] result=new double[X_d1][X_d2];
        for (int i_1=0;i_1< X_d1;i_1++){
            for (int i_2=0;i_2< X_d2;i_2++){
                result[i_1][i_2]=Math.exp(X[i_1][i_2]);
            }
        }
        return result;
    }

    public double[][] matrixLog(double[][]X){
        int X_d1=X.length;
        int X_d2=X[0].length;
        double[][] result=new double[X_d1][X_d2];
        for (int i_1=0;i_1< X_d1;i_1++){
            for (int i_2=0;i_2< X_d2;i_2++){
                result[i_1][i_2]=Math.log(X[i_1][i_2]);
            }
        }
        return result;
    }
    public double[] matrixLog1(double[]X){
        int X_d1=X.length;
        double[] result=new double[X_d1];
        for (int i_1=0;i_1< X_d1;i_1++){
            result[i_1]=Math.log(X[i_1]);
        }
        return result;
    }
    public double[] matrixSquare1(double[]X){
        int X_d1=X.length;
        double[] result=new double[X_d1];
        for (int i_1=0;i_1< X_d1;i_1++){
            result[i_1]=X[i_1]*X[i_1];
        }
        return result;
    }



    public double[] matrixAddElement1(double[]X,double Y){
        int X_d1=X.length;
        double[] result=new double[X_d1];
        for (int i_1=0; i_1<X_d1;i_1++){
            result[i_1]=X[i_1]+Y;
        }
        return result;
    }

    public double[] matrixAdd1(double[]X, double[]Y){
        int X_d1=X.length;
        double[] result=new double[X_d1];

        if (X_d1==Y.length){
            for(int i_1=0;i_1< X_d1;i_1++){
                result[i_1]=X[i_1]+Y[i_1];
            }
        }
        else{
            System.out.println("matrixAdd1 NOT taken");
        }
        return result;
    }
    public double[][] matrixAdd2(double[][]X, double[][]Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        double[][] result=new double[X_d1][X_d2];
//		 if ((X_d1==Y.length)&&(X_d2==Y[0].length))
        {
            for(int i_1=0;i_1< X_d1;i_1++){
                for(int i_2=0;i_2< X_d2;i_2++){
                    result[i_1][i_2]=X[i_1][i_2]+Y[i_1][i_2];
                }
            }
        }
//		 else{
//			 System.out.println("matrixAdd2 NOT taken");
//		 }
        return result;
    }
    public double[][] matrixAddElement2(double[][]X, double Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        double[][] result=new double[X_d1][X_d2];
        for(int i_1=0;i_1< X_d1;i_1++){
            for(int i_2=0;i_2< X_d2;i_2++){
                result[i_1][i_2]=X[i_1][i_2]+Y;
            }
        }
        return result;
    }

    public double[][] matrixMinus2(double[][]X, double[][]Y){
        int X_d1=X.length;
        int X_d2=X[0].length;
        double[][] result=new double[X_d1][X_d2];

        if ((X_d1==Y.length)&&(X_d2==Y[0].length)){
            for(int i_1=0;i_1< X_d1;i_1++){
                for(int i_2=0;i_2< X_d2;i_2++){
                    result[i_1][i_2]=X[i_1][i_2]-Y[i_1][i_2];
                }
            }
        }
        else{
            System.out.println("matrixMinus2 NOT taken");
        }
        return result;
    }

    public double[] matrixMinus1(double[] X, double[]Y){
        int Y_d1=Y.length;
        double[] result=new double[Y_d1];

        if (X.length==Y.length){
            for(int i_1=0;i_1< Y_d1;i_1++){
                result[i_1]=X[i_1]-Y[i_1];
            }
        }
        else{
            System.out.println("matrixMinus1 NOT taken");
        }
        return result;
    }

    public double[] matrixMinusElement1(double X, double[]Y){
        int Y_d1=Y.length;
        double[] result=new double[Y_d1];
        for(int i_1=0;i_1< Y_d1;i_1++){
            result[i_1]=X-Y[i_1];
        }
        return result;
    }




    public double[] matrixInitial(int dimSize){
        double[] result=new double[dimSize];
        for (int i=0;i<dimSize;i++){
            result[i]=0;
        }
        return result;
    }
    public double[][] matrixInitial2(int dimSize1,int dimSize2){
        double[][] result=new double[dimSize1][dimSize2];
        for (int i=0;i<dimSize1;i++){
            for (int i2=0;i2<dimSize2;i2++){
                result[i][i2]=0;
            }
        }
        return result;
    }





/*	 public double[] findInf(double[]X){
		 double [] index=new double[1];
		 int indexLength=X.length;
		 int indexID=0;
		 for (int iX=0; iX<indexLength; iX++){
			 if (X[iX]>100000){
				 index[indexID]=iX;
				 indexID=indexID+1;
			 }
		 }
		 index[indexID]=indexID; // the size of inf numbers
		 return index;
	 }
*/


    public double MatrixVariance (double[][] X){
        int size_1=X.length;
        int size_2=X[0].length;
        double total=0;
        for (int iSum_1=0;iSum_1<size_1;iSum_1++){
            for (int iSum_2=0;iSum_2<size_2;iSum_2++){
                total=total+X[iSum_1][iSum_2];
            }
        }
        double mean=total/(size_1*size_2);
        double total_2=0;
        for (int iSum_1=0;iSum_1<size_1;iSum_1++){
            for (int iSum_2=0;iSum_2<size_2;iSum_2++){
                total_2=total_2+(X[iSum_1][iSum_2]-mean)*(X[iSum_1][iSum_2]-mean);
            }
        }
        double variance=total_2/(size_1*size_2);

        return variance;
    }
    public double MatrixStdDev1 (double[]X){
        int size_1=X.length;
//		 int size_2=X[0].length;
        double total=0;
        for (int iSum_1=0;iSum_1<size_1;iSum_1++){

            total=total+X[iSum_1];

        }
        double mean=total/(size_1);
        double total_2=0;
        for (int iSum_1=0;iSum_1<size_1;iSum_1++){

            total_2=total_2+(X[iSum_1]-mean)*(X[iSum_1]-mean);

        }
        double variance=total_2/(size_1);
        double std=Math.sqrt(variance);
        return std;
    }






    public double[] max(double[] X){
        int sizeMax=X.length;
        double Max=X[0];
        double index=0;
        for (int iMax=0; iMax<sizeMax; iMax++){
            if (Max<X[iMax]){
                Max=X[iMax];
                index=iMax;
            }
        }
        double[] combol= new double[2];
        combol[0]=Max;
        combol[1]=index;
        return combol;
    }

    public double[] maxMatrix_d2(double[][]X){
        int dimMax=X.length;
        int sizeMax=X[0].length;
        double[] maxSet=new double[sizeMax];
        for (int iS=0; iS<sizeMax;iS++){
            double[] tempMax=new double[dimMax];
            for (int iDim=0; iDim<dimMax;iDim++){
                tempMax[iDim]=X[iDim][iS];
            }
            maxSet[iS]=max(tempMax)[0];
        }
        return maxSet;
    }

    public double[][] maxMatrix1(double[][] X){
        int d1=X.length;
        int d2=X[0].length;
        double[][] result=new double[d1][2];
        for(int i1=0;i1<d1;i1++){
            double[] temp=new double[d2];
            for (int i2=0;i2<d2;i2++){
                temp[i2]=X[i1][i2];
            }
            double[] combo=max(temp);
            result[i1][0]=combo[0];
            result[i1][1]=combo[1];
        }
        return result;
    }

    public double[][] epsFinal(double[][] sig_max_Loc, int nmix,int ndim){
        double[][] epsResult=matrixInitial2(ndim,nmix);
        for (int idmix=0; idmix<ndim;idmix++){
            int max=(int) sig_max_Loc[idmix][1];
            epsResult[idmix][max]=Math.sqrt(sig_max_Loc[idmix][0]);
        }
        return epsResult;
    }
    public double[][] MatrixMerge2(double[][]X,double[][]Y){
        int x_d1=X.length;
        int x_d2=X[0].length;
        int y_d2=Y[0].length;
        double[][] result=new double[x_d1][x_d2+y_d2];
        for (int d1=0; d1<x_d1; d1++){
            for (int d2=0; d2<x_d2; d2++){
                result[d1][d2]=X[d1][d2];
            }
            for (int d3=0; d3<y_d2;d3++){
                result[d1][x_d2+d3]=Y[d1][d3];
            }
        }
        return result;
    }
    public double[] MatrixMerge1(double[]X,double[]Y){
        int x_d1=X.length;
//		 int y_d1=Y.length;

        double[]result=new double[x_d1*2];
        for (int d1=0; d1<x_d1; d1++){
            result[d1]=X[d1];
            result[x_d1+d1]=Y[d1];
        }
        return result;
    }

    public double[][] ModifyNtoP(double[][] X){
        int x_d1=X.length;
        int x_d2=X[0].length;
        double[][] result=X;
        for (int id1=0; id1<x_d1; id1++){
            for (int id2=0; id2<x_d2; id2++){
                if (X[id1][id2]<0){
                    result[id1][id2]=-X[id1][id2];
                }
            }
        }
        return result;
    }




}
