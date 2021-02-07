package fr.epita.mob.launcher;
import fr.epita.mob.datamodel.Image;
import fr.epita.mob.service.ImageCsvDAO;

import javax.sound.sampled.Line;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.Math;

public class Main {

    public static void main(String[] args) throws IOException{
        String filePath = new String("D:\\EPITA\\Java\\mob-programming\\mnist_train.csv");

        ImageCsvDAO trainSet = new ImageCsvDAO(filePath);

        Image[] trainImages = trainSet.getAllImages();
//        List<String> Lines = null;
//
//        Lines = Files.readAllLines(new File(filePath).toPath());
//
//        for (int i = 0; i < 2; i++)
//        {
//            System.out.println(Lines.get(i));
//        }
//
//        String[] lineArray = new String[Lines.get(0).length()];
//        lineArray = Lines.get(0).split(",");
//
//        for (int i = 0; i<lineArray.length; i++){
//            System.out.print(lineArray[i] + ",");
//        }
//
//        String headers = Lines.get(0);
//        Lines.remove(0);
//
//        Double[][] doubleLines = new Double[Lines.size()][Lines.get(0).split(",").length];
//
////        for (String line : Lines){
////            lineArray = line.split(",");
////            doubleLines
////        }
//
//        String[] stringArray = Lines.get(0).split(",");
//        Double[] doubleArray = new Double[stringArray.length];
//
//        for (int i = 0; i < Lines.size(); i++){
////            String[] stringArray = Lines.get(i).split(",");
////            Double[] doubleArray = new Double[stringArray.length];
//
//            for(int j=0; j<stringArray.length; j++){
//                doubleArray[j] = Double.parseDouble(stringArray[j]);
//            }
//
//            System.arraycopy(doubleArray, 0, doubleLines[i],0,doubleArray.length);
//        }
//
//        System.out.println("");
//        System.out.println("parsed doubles:");
//        for (int i = 0; i < 2; i++)
//        {
//            for (int j = 0; j<doubleLines[i].length; j++){
//                System.out.print(doubleLines[i][j] + ",");
//            }
//            System.out.println("");
//        }



        for (int i = 0; i < 2; i++)
        {
            System.out.println(trainImages[i].getLabel());
            trainImages[i].showMatrix();
//            showMatrix(images[i].getDataMatrix());

//            System.out.println(doubleLines[i][0]);
//            showMatrix(reshapeLine(doubleLines[i]));
//            for (int j = 0; j<doubleLines[i].length; j++){
//                System.out.print(doubleLines[i][j] + ",");
//            }
//            System.out.println("");
        }

//        System.out.println(lineArray);

//        System.out.println(Lines.get(0));

        HashMap<Double, Double> distribution = calculateDistribution(trainImages);

        for(Double key: distribution.keySet()){
            System.out.println(key + ": " + distribution.get(key));
        }

        HashMap<Double, Double[][]> centroids = trainCentroids(trainImages);

        for (Double key: centroids.keySet()){
            System.out.println(key);
            showMatrix(centroids.get(key));
        }


        String testFilePath = new String("D:\\EPITA\\Java\\mob-programming\\mnist_test.csv");

        ImageCsvDAO testSet = new ImageCsvDAO(testFilePath);

        Image[] testImages = testSet.getAllImages();

        for(Double digit: centroids.keySet()){
            System.out.println("For key " + digit.toString() + ":");
            int found = 0;
            int counter = 0;
            Image[] firstZeroes = new Image[10];
            while(found<10 && counter<testImages.length){
                if (testImages[counter].getLabel() == digit.doubleValue()){
                    firstZeroes[found] = testImages[counter];
                    found++;
                }
                counter++;
            }

            for (Image image : firstZeroes){
                System.out.println(clasify(image.getDataMatrix(),centroids));
            }
        }


    }

    public static HashMap<Double, Double> calculateDistribution(Image[] images){
        HashMap<Double, Double> outputMap = new HashMap<>();

        for(Image image : images){
            if(outputMap.containsKey(image.getLabel())){
                outputMap.put(image.getLabel(),outputMap.get(image.getLabel()) + 1.0);
            }
            else
            {
                outputMap.put(image.getLabel(),1.0);
            }
        }

        return outputMap;
    }

    public static HashMap<Double, Double[][]>trainCentroids(Image[] images) {
        HashMap<Double, Double[][]> centroids= new HashMap<>();
        HashMap<Double, Double> distribution = calculateDistribution(images);
        for (Double key : distribution.keySet()){

        }
        for(Image image : images){
            Double[][] currentSum = centroids.get(image.getLabel());
            if(currentSum == null){
                currentSum = new Double[28][28];
                for(int i =0;i<28;i++){
                    for(int j=0;j<28;j++){
                        currentSum[i][j] = 0.0;
                    }
                }
            }
            Double[][] imageData = image.getDataMatrix();

            for(int i =0;i<28;i++){
                for(int j=0;j<28;j++){
                    currentSum[i][j] = currentSum[i][j] + imageData[i][j];
                }
            }
            centroids.put(image.getLabel(),currentSum);
        }

        for(Double key: centroids.keySet()){
            Double[][] currentCentroid = centroids.get(key);
            Double keyTotal = distribution.get(key);
            for(int i =0;i<28;i++){
                for(int j=0;j<28;j++){
                    currentCentroid[i][j] = currentCentroid[i][j]/keyTotal;
                }
            }
        }
        return centroids;
    }

    public static Double calculateDistance(Double[][] array1, Double[][] array2){
        //Hint : the distance can be defined as the square root of the sum of each module (absolute difference)
        //of index-to-index values of the 2 considered matrix. Once you have that value, you have to take the
        //minimum.

        Double sum = 0.0;

        for(int i =0;i<28;i++){
            for(int j=0;j<28;j++){
                sum = sum + Math.abs(array1[i][j] - array2[i][j]);
            }
        }
        return Math.sqrt(sum);

    }

    public static Double clasify(Double[][] entryArray, HashMap<Double, Double[][]> centroids){
        Double candidate = 0.0;

        Double minDistance = 0.0;
        Double currentDistance = 0.0;

        for(Double key: centroids.keySet()){
            currentDistance = calculateDistance(entryArray, centroids.get(key));
            if(minDistance == 0.0){
                minDistance = currentDistance;
            } else if(currentDistance<minDistance){
                minDistance = currentDistance;
                candidate = key.doubleValue();
            }
        }

        return candidate;

    }

    public static Double[][] reshapeLine(Double[] inputLine){
        Double[][] outputMatrix = new Double[28][28];
//        System.out.println(inputLine.length);
        int j = 0;
        for(int i = 1; i<inputLine.length; i++){
            if(((i-1) % 28 == 0) && i != 1){
                j++;
            }
//            System.out.println(j + "," + i );
            outputMatrix[j][(i-1)%28] = inputLine[i];
        }
        return outputMatrix;
    }

    public static void showMatrix (Double[][] inputMatrix){
        for(Double[] line : inputMatrix){
            for(Double element : line){
                if (element > 100){
                    System.out.print("xx");
                }
                else {
                    System.out.print("..");
                }
            }
            System.out.println("");
        }
    }
}
