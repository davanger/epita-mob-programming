package fr.epita.mob.datamodel;

public class Image {

    private Double label;
    private Double[][] dataMatrix;


    public Image(Double label, Double[][] dataMatrix) {
        this.label = label;
        this.dataMatrix = dataMatrix;
    }

    public Double getLabel() {
        return label;
    }

    public Double[][] getDataMatrix() {
        return dataMatrix;
    }

    public void setLabel(Double label) {
        this.label = label;
    }

    public void setDataMatrix(Double[][] dataMatrix) {
        this.dataMatrix = dataMatrix;
    }

    public void showMatrix (){
        for(Double[] line : this.dataMatrix){
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
