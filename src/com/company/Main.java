package com.company;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        showInfo();
        boolean fileOrConsole = isFileOrConsole(input);
        int matrixOrder = findInputData(fileOrConsole, input);
        int[][] matrix = getMatrix(matrixOrder, input);
        showMatrix(matrix);
        int determinant = calcDeterminant(matrix);
        showResult(determinant, "Определитель матрицы равен: ");
        if (isSaveToFile(input)) {
            writeToFile(findFilePath(input), determinant);
        }
        input.close();
    }

    public static void showInfo(){
        System.out.println("Эта программа считает определитель матрицы.");
    }

    public static boolean isSaveToFile(Scanner input){
        System.out.println("Сохранить результат в файл?");
        System.out.println("1. Да.");
        System.out.println("2. Нет.");
        int temp = 0;
        boolean isInCorrect;
        do{
            isInCorrect = false;
            try {
                temp = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e){
                System.out.print("Введите данные корректно: ");
                isInCorrect = true;
            }
            if (!(temp == 1 || temp == 2) && !isInCorrect){
                System.out.print("Введите данные корректно: ");
            }
        } while (isInCorrect);
        return temp == 1;
    }

    public static void writeToFile(String filePath, int determinant){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write("Определитель матрицы равен " + determinant);
            writer.close();
        } catch (IOException e){
            System.err.println("I/O error.");
        }
        System.out.println("Успешно сохранено!");
    }

    public static String findFilePath(Scanner input){
        String filePath;
        FileReader reader = null;
        boolean isIncorrect;
        do {
            System.out.print("Введите путь к файлу: ");
            filePath = input.nextLine();
            isIncorrect = false;
            try{
                reader = new FileReader(filePath);
            } catch (FileNotFoundException e){
                System.out.println("Файл не найден.");
                isIncorrect = true;
            }
        } while (isIncorrect);
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("I/O error.");
        }
        return filePath;
    }

    public static boolean isValidData(String data){
        boolean isValid = true;
        try{
            Integer.parseInt(data);
        } catch (NumberFormatException e) {
            isValid = false;
            System.out.println("Введите корректно данные:");
        }
        if (isValid && (Integer.parseInt(data) > 5 || Integer.parseInt(data) < 1)){
            System.out.println("Порядок матрицы должен быть больше 0 и не больше 5 и являться числом:");
            isValid = false;
        }
        return isValid;
    }

    public static String inputDataFromConsole(Scanner input){
        String data;
        boolean isInCorrect;
        System.out.print("Введите порядок матрицы: ");
        do {
            data = input.nextLine();
            isInCorrect = !isValidData(data);
        } while (isInCorrect);
        return data;
    }

    public static String inputDataFromFile(Scanner input, String filePath){
        String inputData = null;
        boolean isCorrect = true;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            inputData = reader.readLine();
            reader.close();
        } catch (IOException e){
            System.err.println("Input error.");
        }
        try {
            Integer.parseInt(inputData);
        } catch (NumberFormatException e){
            isCorrect = false;
        }
        if (Objects.isNull(inputData) || (!isCorrect)){
            inputData = inputDataFromConsole(input);
        }
        return inputData;
    }

    public static int findInputData(boolean fileOrConsole, Scanner input){
        if (fileOrConsole){
            return Integer.parseInt(inputDataFromConsole(input));
        } else {
            return Integer.parseInt(inputDataFromFile(input, findFilePath(input)));
        }
    }

    public static boolean isFileOrConsole(Scanner input){
        boolean fileOrConsole;
        boolean isInCorrect;
        int temp = 0;
        System.out.println("1. Ввести порядок матрицы вручную.");
        System.out.println("2. Ввести порядок матрицы с файла.");
        do {
            isInCorrect = false;
            try {
                temp = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e){
                System.out.println("Введены некорректные данные.");
                isInCorrect = true;
            }
            if ( !isInCorrect && (!(temp == 1 || temp == 2))){
                System.err.println("Введены некорректные данные.");
                isInCorrect = true;
            }
        } while (isInCorrect);
        fileOrConsole = temp == 1;
        return fileOrConsole;
    }

    public static int findNumber(Scanner input){
        boolean isInCorrect;
        int temp = 0;
        do {
            isInCorrect = false;
            try {
                temp = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введены некорректные данные.");
                isInCorrect = true;
            }
        } while (isInCorrect);
        return temp;
    }

    public static int[][] getMatrix(int matrixOrder, Scanner input){
        System.out.println("Введите матрицу");
        int [][] matrix = new int[matrixOrder][matrixOrder];
        for (int i = 0; i < matrixOrder; i++){
            for (int j = 0; j < matrixOrder; j++){
                matrix[i][j] = (findNumber(input));
            }
        }
        return matrix;
    }

    public static void showMatrix(int[][] matrix){
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                System.out.format("%3d ", anInt);
            }
            System.out.println("\n");
        }
    }

    public static int[][] createSubArray(int[][] srcMatrix, int srcJ){
        int[][] subArray = new int[srcMatrix.length-1][srcMatrix.length-1];
        for (int i = 1; i < srcMatrix.length; i++){
            int j2 = 0;
            for (int j = 0; j < srcMatrix.length; j++){
                if (j != srcJ) {
                    subArray[i-1][j2] = srcMatrix[i][j];
                    j2++;
                }
            }
        }
        return subArray;
    }

    public static int calcDeterminant(int[][] matrix){
        return determinant(matrix, matrix.length);
    }

    public static int determinant(int[][] matrix, int matrixOrder) {
        int res;
        if (matrixOrder == 1) { res = matrix[0][0]; }
        else {
            res = 0;
            for (int i = 0; i < matrixOrder; i++) {
                int[][] subArray = createSubArray(matrix, i);
                res += Math.pow(-1.0, 1.0 + i + 1.0) * matrix[0][i] * determinant(subArray, matrixOrder - 1);
            }
        }
        return res;
    }

    public static void showResult(int result, String str){
        System.out.println(str + result + ".");
    }
}
