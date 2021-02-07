package fr.epita.mob.service;

import fr.epita.mob.datamodel.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ImageCsvDAO {
    private String fileName;

    public ImageCsvDAO(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Image[] getAllImages(){
        String filePath = new String("D:\\EPITA\\Java\\mob-programming\\mnist_train.csv");
        List<String> Lines = null;

        try {
            Lines = Files.readAllLines(new File(filePath).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        for (int i = 0; i < 2; i++)
//        {
//            System.out.println(Lines.get(i));
//        }

//        String[] lineArray = new String[Lines.get(0).length()];
//        lineArray = Lines.get(0).split(",");
//
//        for (int i = 0; i<lineArray.length; i++){
//            System.out.print(lineArray[i] + ",");
//        }

//        String headers = Lines.get(0);
        Lines.remove(0);

        Image[] outputImages = new Image[Lines.size()];

//        Double[][] doubleLines = new Double[Lines.size()][Lines.get(0).split(",").length];

//        for (String line : Lines){
//            lineArray = line.split(",");
//            doubleLines
//        }

        String[] stringArray = Lines.get(0).split(",");
        Double[] doubleArray = new Double[stringArray.length];

        for (int i = 0; i < Lines.size(); i++){
            stringArray = Lines.get(i).split(",");
//            Double[] doubleArray = new Double[stringArray.length];

            for(int j=0; j<stringArray.length; j++){
                doubleArray[j] = Double.parseDouble(stringArray[j]);
            }
            outputImages[i] = new Image(doubleArray[0],reshapeLine(doubleArray));

//            System.arraycopy(doubleArray, 0, doubleLines[i],0,doubleArray.length);
        }

        return outputImages;


    }

    public Double[][] reshapeLine(Double[] inputLine){
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
}
