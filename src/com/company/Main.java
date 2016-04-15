package com.company;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String path = "/home/zelle/Playground/selected_topics/eclipse-issues-2001-1009/2002-2003/";

        File bugList = new File(path + "critical.bugs");


        HashMap<String, List<Long>> severityCommentsList = new HashMap<>();
        HashMap<String, List<Long>> priorityCommentsList = new HashMap<>();
        HashMap<String, List<Long>> blocksCommentsList = new HashMap<>();

        HashMap<String, HashMap<Date, Long>> dateMap = new HashMap<>();

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(bugList))) {
            String line;
            while ((line = br.readLine()) != null) {
                File bugFile = new File(path+line);
                if (bugFile.exists()) {
                    try (BufferedReader bufferedReaderBugFile = new BufferedReader(new FileReader(bugFile))) {
                        String bugFileLines;
                        String priority = null;
                        String severity = null;
                        String blocks = null;
                        String nrComments = null;
                        String lastDays = null;
                        String day = null;
                        while ((bugFileLines = bufferedReaderBugFile.readLine()) != null) {
                            if (bugFileLines.startsWith("#priority:")) {
                                priority = bugFileLines.replace("#priority:", "").trim();
                            }
                            if (bugFileLines.startsWith("#severity:")) {
                                severity = bugFileLines.replace("#severity:", "").trim();
                            }
                            if (bugFileLines.startsWith("#blocks:")) {
                                blocks = bugFileLines.replace("#blocks:", "").trim();
                            }
                            if (bugFileLines.startsWith("#nbr-comments:")) {
                                nrComments = bugFileLines.replace("#nbr-comments:", "").trim();
                            }
                            if (bugFileLines.startsWith("#last-days:")) {
                                lastDays = bugFileLines.replace("#last-days:", "").trim();

                            }
                            if (bugFileLines.startsWith("#closing-email:")) {
                                String dateClosing = bugFileLines.replace("#closing-email:", "").trim();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
                                try {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(dateFormat.parse(dateClosing));
                                    calendar.set(Calendar.HOUR, 0);
                                    calendar.set(Calendar.MINUTE, 0);
                                    calendar.set(Calendar.SECOND, 0);
                                    calendar.set(Calendar.MILLISECOND, 0);
                                    day = calendar.get(Calendar.DAY_OF_WEEK) + "";
                                    if(!dateMap.containsKey(severity)){
                                        dateMap.put(severity, new HashMap<Date, Long>());
                                    }

                                    HashMap<Date, Long> dateLongHashMap = dateMap.get(severity);
                                    if(!dateMap.get(severity).containsKey(calendar.getTime())) {
                                        dateMap.get(severity).put(calendar.getTime(), 0L);
                                    }
                                    dateLongHashMap.put(calendar.getTime(), dateLongHashMap.get(calendar.getTime())+1);
                                } catch (ParseException | NullPointerException e) {
                                    e.printStackTrace();
                                }


                            }


                        }
                        stringBuilder.append(bugFile.getName() + "," + priority + "," + severity + "," + nrComments +","+lastDays+","+ day +"\n");
//                        }
//                        if(priority != null){
//                            if(priorityCommentsList.get(priority) == null){
//                                priorityCommentsList.put(priority, new ArrayList<Long>());
//                            }
//                            priorityCommentsList.get(priority).add(Long.parseLong(nrComments));
//                        }
//                        if(severity != null){
//                            if(severityCommentsList.get(severity) == null){
//                                severityCommentsList.put(severity, new ArrayList<Long>());
//                            }
//                            severityCommentsList.get(severity).add(Long.parseLong(nrComments));
//                        }
//                        if(blocks != null && !blocks.isEmpty()){
//                            if(blocksCommentsList.get(blocks) == null){
//                                blocksCommentsList.put(blocks, new ArrayList<Long>());
//                            }
//                            priorityCommentsList.get(blocks).add(Long.parseLong(nrComments));
//                        }
                    }
                } else {
                    System.err.println("file " + line + " not found");
                }
            }
        }
        StringBuilder stringBuilderDate = new StringBuilder();
        for (Map.Entry<String, HashMap<Date, Long>> dateLongEntry : dateMap.entrySet()) {

            String severity = dateLongEntry.getKey();
            for (Map.Entry<Date, Long> entry : dateLongEntry.getValue().entrySet()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(entry.getKey());
                stringBuilderDate.append(severity+","+calendar.get(Calendar.DAY_OF_WEEK) + ","+ entry.getValue()+"\n");
            }

        }
        File outFileDate = new File(path + "results_date.out");
        try (PrintWriter out = new PrintWriter(outFileDate)) {
            out.print(stringBuilderDate.toString());
        }

        File outFile = new File(path + "results.out");
        try (PrintWriter out = new PrintWriter(outFile)) {
            out.print(stringBuilder.toString());
        }
    }
}
