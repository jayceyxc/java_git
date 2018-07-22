package com.linus.tools;

import org.apache.commons.cli.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.*;

public class CsvUtils {
    private static final SimpleDateFormat birthFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static Map<String, String> sexMapping = new HashMap<>();
    private static Map<String, String> occupationMapping = new HashMap<>();
    private static Map<String, String> maritalStatusMapping = new HashMap<>();
    private static Map<String, String> paymentOptionMapping = new HashMap<>();
    private static Map<String, String> relationshipMapping = new HashMap<>();
    static {
        sexMapping.put("0", "未知");
        sexMapping.put("1", "男");
        sexMapping.put("2", "女");
        occupationMapping.put("11", "国家公务员");
        occupationMapping.put("13", "专业技术人员");
        occupationMapping.put("17", "职员");
        occupationMapping.put("21", "企业管理人员");
        occupationMapping.put("24", "工人");
        occupationMapping.put("27", "农民");
        occupationMapping.put("31", "学生");
        occupationMapping.put("37", "现役军人");
        occupationMapping.put("51", "自由职业者");
        occupationMapping.put("54", "个体经营者");
        occupationMapping.put("70", "无业人员");
        occupationMapping.put("80", "退（离）休人员");
        occupationMapping.put("90", "其他");
        maritalStatusMapping.put("1", "未婚");
        maritalStatusMapping.put("2", "已婚");
        maritalStatusMapping.put("3", "丧婚");
        maritalStatusMapping.put("4", "离婚");
        maritalStatusMapping.put("9", "其他");
        paymentOptionMapping.put("1", "城镇职工基本医疗保险");
        paymentOptionMapping.put("2", "城镇居民基本医疗保险");
        paymentOptionMapping.put("3", "新型农村合作医疗");
        paymentOptionMapping.put("4", "贫困救助");
        paymentOptionMapping.put("5", "商业医疗保险");
        paymentOptionMapping.put("6", "全公费");
        paymentOptionMapping.put("7", "全自费");
        paymentOptionMapping.put("8", "其他社会保险");
        paymentOptionMapping.put("9", "其他");
        relationshipMapping.put("1", "配偶");
        relationshipMapping.put("2", "子");
        relationshipMapping.put("3", "女");
        relationshipMapping.put("4", "孙子、孙女或外孙子、外孙女");
        relationshipMapping.put("5", "父母");
        relationshipMapping.put("6", "祖父母或外祖父母");
        relationshipMapping.put("7", "兄、弟、姐、妹");
        relationshipMapping.put("8", "其他");
    }


    public static void main(String[] args) {
        final Options options = new Options();
        final Option option = new Option("f", true, "csv file path");
        options.addOption(option);

        final CommandLineParser parser = new DefaultParser();
        String csvFilePath = null;
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("f")) {
                csvFilePath = cmd.getOptionValue("f");
            } else {
                System.err.println("please input the configuration file path by -f option");
                System.exit(1);
            }
            if (StringUtils.isBlank(csvFilePath)) {
                System.err.println("Blank file path");
            }
            Reader in = null;
            System.out.println("csv file path: " + csvFilePath);
            try {
                in = new FileReader(csvFilePath);
                CSVParser csvParser = CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreEmptyLines().parse(in);
                List<CSVRecord> records = csvParser.getRecords();
                System.out.println("total records number: " + records.size());
                int afcNumber = 0;
                for (CSVRecord record : records) {
                    String diagnoseNumber = record.get("P28").trim();
                    if (diagnoseNumber.startsWith("I48")) {
                        afcNumber++;
                        String inPatientID = record.get("P3").trim();
                        String name = record.get("P4").trim();
                        String sex = record.get("P5").trim();
                        String birthTime = record.get("P6").trim();
                        try {
                            Date date = birthFormat.parse(birthTime);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH) + 1;
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            System.out.printf("%4d-%02d-%02d\n", year, month, day);
                        } catch (java.text.ParseException pe) {
                            System.out.println(pe.getMessage());
                        }
                        String IDCardNumber = record.get("P13").trim();
                        String address = record.get("P801").trim();
                        String phoneNumber = record.get("P802").trim();
                        String diagnose = record.get("P281").trim();
                        String checkinTime = record.get("P22").trim();
                        String checkoutTime = record.get("P25").trim();
                        String nation = record.get("P11").trim();
                        String contactName= record.get("P18").trim();
                        String contactRelationship = record.get("P19").trim();
                        String contactPhoneNumber = record.get("P21").trim();
                        String occupation = record.get("P9").trim();
                        String maritalStatus = record.get("P8").trim();
                        String paymentOption= record.get("P1").trim();
                        System.out.printf("住院号：%s,\t姓名：%s,\t性别：%s,\t民族：%s\t,出生时间：%s,\t身份证号：%s\t地址：%s\t手机号：%s\t诊断疾病号：%s\t诊断：%s\n", inPatientID, name, sexMapping.getOrDefault(sex, "未知"), nation, birthTime, IDCardNumber, address, phoneNumber, diagnoseNumber, diagnose);
                        System.out.printf("入院时间：%s,\t出院时间：%s\t从事职业：%s\t婚姻状况：%s\t医疗付费方式：%s\t联系人姓名：%s\t与患者关系：%s\t联系人电话：%s\n", checkinTime, checkoutTime, occupationMapping.getOrDefault(occupation, "未知"), maritalStatusMapping.getOrDefault(maritalStatus,"未知"), paymentOptionMapping.getOrDefault(paymentOption, "其他"), contactName, relationshipMapping.getOrDefault(contactRelationship, "未知"), contactPhoneNumber);
                    }
                }
                System.out.println("AFC number: " + afcNumber);
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        } catch (final ParseException pe) {
            System.err.println("parser command line error");
            pe.printStackTrace();
        }
    }
}
