package edu.appstate.cs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ReadCSVFile {
    private int naCount = 0;
    private ArrayList<String> didNotParticipate = new ArrayList<>();
    private List<ArrayList<String>> finalList = new ArrayList<>();

    private ArrayList<String> averageAttemptsIncludingIncorrect = new ArrayList<>();
    private ArrayList<String> averageAttemptsExcludingIncorrect = new ArrayList<>();
    private ArrayList<String> percentSuccessIncludingNoAnswer = new ArrayList<>();
    private ArrayList<String> percentSuccessExcludingNoAnswer = new ArrayList<>();

    public static void main(String[] args) {
        ReadCSVFile rcf = new ReadCSVFile();
        rcf.generateParticipants();

        rcf.averageAttemptsIncludingIncorrect();
        rcf.averageAttemptsExcludingIncorrect();
        rcf.percentSuccessIncludingNoAnswer();
        rcf.percentSuccessExcludingNoAnswer();
    }

    private void percentSuccessExcludingNoAnswer() {

    }

    private void percentSuccessIncludingNoAnswer() {
        int sum = 0;
        for(int i = 0; i < finalList.size();i++) {
            for (int j = 1; j < finalList.get(i).size();j++) {
                int tmp = Integer.parseInt(finalList.get(i).get(j));
                System.out.println(" "+tmp);
                if (tmp > 0) {
                    sum += tmp;
                }
                else {
                    sum += Math.abs(tmp);
                }
                System.out.println(""+sum);
            }

        }
        System.out.print("Sum: " + sum);
    }

    private void averageAttemptsIncludingIncorrect() {

    }

    private void averageAttemptsExcludingIncorrect() {

    }


    private void generateParticipants() {

        String csvFile = "/home/scott/Downloads/Esmailis16_gradeCheck.csv";
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] country = line.split(cvsSplitBy);
                naCount = 0;
                for (int i = 0; i < country.length; i++) {
                    if (country[i].equals("N/A")) {
                        naCount++;
                        country[i] = "0";
                    }
                    if (country[i].contains("Incorrect")) {
                        String[] temp = country[i].split(" ");
                        country[i] = temp[0].replace("\"","-");
                    }
                }

                if(naCount == (country.length - 1)) {
                    didNotParticipate.add(country[0]);
                }
                else {
                    finalList.add(new ArrayList<>());
                    int index = finalList.size() - 1;
                    for (String aCountry : country) {
                        finalList.get(index).add(aCountry);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Skipping " + didNotParticipate.size() + " students that did not participate.");
        System.out.println("Found " + finalList.size() + " students that did participate.");
        System.out.println("Negative numbers are incorrect and zeros are N/A's\n\n");
        for(int i = 0; i < finalList.size();i++) {
            for(int j = 0; j < finalList.get(i).size(); j ++) {
                System.out.print(finalList.get(i).get(j) + " ");
            }
            System.out.println("");
        }

    }
}