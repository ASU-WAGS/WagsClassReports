/**
Author: Scott Shuffler
Date: 6-13-16
Purpose: Automation of end of semester grading for WAGS sections
How to use: See README in the home folder
**/
package edu.appstate.cs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class ReadCSVFile {
    private String filePath;

    private ArrayList<String> didNotParticipate = new ArrayList<>();
    private List<ArrayList<String>> finalList = new ArrayList<>();
    private ArrayList<String> individualColumn = new ArrayList<>();
    private ArrayList<String> problemNames = new ArrayList<>();

    private ArrayList<Double> averageAttemptsIncludingIncorrect = new ArrayList<>();
    private ArrayList<Double> averageAttemptsExcludingIncorrect = new ArrayList<>();
    private ArrayList<Double> percentSuccessIncludingNoAnswer = new ArrayList<>();
    private ArrayList<Double> percentSuccessExcludingNoAnswer = new ArrayList<>();

    public static void main(String[] args) {
        ReadCSVFile rcf = new ReadCSVFile();
        rcf.generateParticipants();
        rcf.getColumn();
        rcf.generateCSVFile();
    }

    private void generateCSVFile() {

        Scanner scanner = new Scanner( System.in );
        System.out.print( "What would you like to title this CSV file? (ex: Scott Spring 2016) " );
        String title = scanner.nextLine();

        //String csvFile = "/home/scott/Downloads/Esmailis16_gradeCheckCalculated.csv";

        String[] explodedFilePath = filePath.split("\\.");
        String tmpFilePath = explodedFilePath[0] + "_Calculated." + explodedFilePath[1];
        System.out.println("Saving file: " + tmpFilePath);

        try
        {
            FileWriter writer = new FileWriter(tmpFilePath);
            String dnp = String.valueOf(didNotParticipate.size());
            String fl = String.valueOf(finalList.size());
            String size = String.valueOf(didNotParticipate.size() + finalList.size());
            writer.append(title);
            writer.append('\n');
            writer.append('\n');
            writer.append("Class size: ");
            writer.append(size);
            writer.append('\n');
            writer.append("Number of students who had all NA answers: ");
            writer.append(dnp);
            writer.append('\n');
            writer.append("Number of students who participated: ");
            writer.append(fl);
            writer.append('\n');
            writer.append("Students who had no answers were excluded from all calculations");
            writer.append('\n');
            writer.append('\n');
            writer.append('\n');

            for (String problemName : problemNames) {
                if (problemName.contains("Username")){
                    writer.append(',');
                }
                else {
                    writer.append(problemName);
                    writer.append(',');
                }
            }
            writer.append('\n');
            writer.append("Average Attempts Including Incorrect");
            writer.append(',');
            for (Double anAverageAttemptsIncludingIncorrect : averageAttemptsIncludingIncorrect) {
                writer.append(String.valueOf(anAverageAttemptsIncludingIncorrect));
                writer.append(',');
            }
            writer.append('\n');
            writer.append("Average Attempts Excluding Incorrect");
            writer.append(',');
            for (Double anAverageAttemptsExcludingIncorrect : averageAttemptsExcludingIncorrect) {
                writer.append(String.valueOf(anAverageAttemptsExcludingIncorrect));
                writer.append(',');
            }
            writer.append('\n');

            writer.append("Percent Success Including No Answer");
            writer.append(',');
            for (Double aPercentSuccessIncludingNoAnswer : percentSuccessIncludingNoAnswer) {
                writer.append(String.valueOf(aPercentSuccessIncludingNoAnswer));
                writer.append('%');
                writer.append(',');
            }
            writer.append('\n');

            writer.append("Percent Success Excluding No Answer");
            writer.append(',');
            for (Double aPercentSuccessExcludingNoAnswer : percentSuccessExcludingNoAnswer) {
                writer.append(String.valueOf(aPercentSuccessExcludingNoAnswer));
                writer.append('%');
                writer.append(',');
            }
            writer.append('\n');


            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    private void getColumn() {
        for(int i = 1; i < finalList.get(0).size(); i++) {
            for (ArrayList<String> aFinalList : finalList) {
                individualColumn.add(aFinalList.get(i));
            }

            System.out.println(problemNames.get(i));
            averageAttemptsExcludingIncorrect(individualColumn);
            averageAttemptsIncludingIncorrect(individualColumn);
            percentSuccessExcludingNoAnswer(individualColumn);
            percentSuccessIncludingNoAnswer(individualColumn);

            individualColumn.clear();
            System.out.println("\r\n");
        }
    }

    private void percentSuccessExcludingNoAnswer(ArrayList<String> individualColumn) {
        int tmp = 0;
        int total = finalList.size();
        int correct = 0;
        for(int i = 0; i < finalList.size(); i++) {
            try {
                tmp = Integer.parseInt(individualColumn.get(i));
            }
            catch (Exception e) {
                //System.out.println(e);
            }
            if(tmp == 0) {
                total--;
            }
            else if(tmp > 0){
                correct++;
            }
        }
        double percentCorrect = ((double)correct/total) * 100;
        DecimalFormat df = new DecimalFormat("#.##");
        try {
            percentCorrect = Double.valueOf(df.format(percentCorrect));
            percentSuccessExcludingNoAnswer.add(percentCorrect);
            System.out.printf("Percent correct without NA: %.2f%%\n", percentCorrect);
        }
        catch (Exception ignored) {

        }

    }

    private void percentSuccessIncludingNoAnswer(ArrayList<String> individualColumn) {
        int tmp = 0;
        int total = finalList.size();
        int correct = 0;
        for(int i = 0; i < finalList.size(); i++) {
            try {
                tmp = Integer.parseInt(individualColumn.get(i));
            }
            catch (Exception e) {
                //System.out.println(e);
            }
            if(tmp > 0){
                correct++;
            }
        }
        double percentCorrect = ((double)correct/total) * 100;
        DecimalFormat df = new DecimalFormat("#.##");
        percentCorrect = Double.valueOf(df.format(percentCorrect));
        percentSuccessIncludingNoAnswer.add(percentCorrect);
        System.out.printf("Percent correct with NA: %.2f%%\n", percentCorrect);
    }

    private void averageAttemptsIncludingIncorrect(ArrayList<String> individualColumn) {
        int sum = 0;
        int tmp = 0;
        for (String anIndividualColumn : individualColumn) {
            try {
                tmp = Integer.parseInt(anIndividualColumn);
            } catch (Exception e) {
                //System.out.println(e);
            }
            sum += Math.abs(tmp);
        }
        double avg = (double)sum/finalList.size();
        DecimalFormat df = new DecimalFormat("#.##");
        avg = Double.valueOf(df.format(avg));
        averageAttemptsIncludingIncorrect.add(avg);
        System.out.printf("Average Attempt with incorrect: %.2f\n", avg);
    }

    private void averageAttemptsExcludingIncorrect(ArrayList<String> individualColumn) {
        int sum = 0;
        int tmp = 0;
        for (String anIndividualColumn : individualColumn) {
            try {
                tmp = Integer.parseInt(anIndividualColumn);
            } catch (Exception e) {
                //System.out.println(e);
            }
            if (tmp > 0) {
                sum += tmp;
            }
        }
        double avg = (double)sum/finalList.size();
        DecimalFormat df = new DecimalFormat("#.##");
        avg = Double.valueOf(df.format(avg));
        averageAttemptsExcludingIncorrect.add(avg);
        System.out.printf("Average Attempt without incorrect: %.2f\n", avg);
    }


    private void generateParticipants() {
        Scanner scanner = new Scanner( System.in );
        System.out.print( "Enter full path to file(ex: /home/scott/grade.csv): " );
        String csvFile = scanner.nextLine();
        filePath = csvFile;
        //String csvFile = "/home/scott/Downloads/Esmailis16_gradeCheck.csv";
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";

        try {
            int lineCount = 0;
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                String[] country = line.split(cvsSplitBy);

                if (lineCount == 4) {
                    Collections.addAll(problemNames, country);
                }
                else if (lineCount >= 5) {
                    int naCount = 0;
                    for (int i = 0; i < country.length; i++) {
                        if (country[i].equals("N/A")) {
                            naCount++;
                            country[i] = "0";
                        }
                        if (country[i].contains("Incorrect")) {
                            String[] temp = country[i].split(" ");
                            country[i] = temp[0].replace("\"", "-");
                        }
                    }

                    if (naCount == (country.length - 1)) {
                        didNotParticipate.add(country[0]);
                    } else {
                        finalList.add(new ArrayList<>());
                        int index = finalList.size() - 1;
                        for (String aCountry : country) {
                            finalList.get(index).add(aCountry);
                        }
                    }

                }

                lineCount++;
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
        System.out.println("Negative numbers are incorrect and zeros are N/A's\r\n");
        for (ArrayList<String> aFinalList : finalList) {
            for (String anAFinalList : aFinalList) {
                System.out.print(anAFinalList + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
}
