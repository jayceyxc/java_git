package com.linus.tools;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MySqlDBUtil {

    private static final Logger logger = Logger.getLogger(MySqlDBUtil.class);

    public static void exportHospitalData(String table, List<String> columns, int limit, String fileName) {
        // 创建Excel表。
        XSSFWorkbook book = new XSSFWorkbook();
        // 在当前Excel创建一个子表
        XSSFSheet sheet = book.createSheet(table);
        try {
            Connection connection = DBCPMysqlPool.getConnection();
            String sql = "SELECT " + StringUtils.join(columns, ',') + " FROM " + table + " LIMIT " + limit;
            logger.info("sql: " + sql);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet != null) {
                // 设置表头信息（写入Excel左上角是从(0,0)开始的）
                XSSFRow row1 = sheet.createRow(0);
                for (int i = 1; i <= columns.size(); i++) {
                    String name = columns.get(i - 1);
                    // 单元格
                    XSSFCell cell = row1.createCell(i - 1);
                    // 写入数据
                    cell.setCellValue(name);
                }

                // 设置表格信息
                int idx = 1;
                while (!resultSet.isClosed() && resultSet.next()) {
                    // 行
                    XSSFRow row = sheet.createRow(idx);
                    boolean finished = true;
                    for (int i = 1; i <= columns.size(); i++) {
                        String value = resultSet.getString(columns.get(i - 1));
                        if (StringUtils.isEmpty(value)) {
                            logger.warn("值为空的");
                            finished = false;
                            continue;
                        }
                        if (value.length() > SpreadsheetVersion.EXCEL2007.getMaxTextLength()) {
                            logger.error("The maximum length of cell contents (text) is 32,767 characters. content is: " + value);
                            finished = false;
                            continue;
                        }
                        // 创建单元格
                        XSSFCell cell = row.createCell(i - 1);
                        // 写入数据
                        cell.setCellValue(value);
                    }
                    if (finished) {
                        idx++;
                    }
                }
            }
        } catch (SQLException sqle) {
            logger.error(sqle.getMessage());
            sqle.printStackTrace();
            return;
        }

        try {
            // 保存到文件
            book.write(new FileOutputStream(fileName));
        } catch (FileNotFoundException fnfe) {
            logger.error("文件没找到");
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            logger.error("文件IO异常");
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) throws ParseException {
        PropertyConfigurator.configure ("conf/log4j.properties");
        // Create a Parser
        CommandLineParser parser = new DefaultParser();
        Options options = new Options( );
        Option helpOpt = new Option("h", "help", false, "Print this usage information");
        Option verboseOpt = new Option("v", "verbose", false, "Print out VERBOSE information");
        Option fileOpt = Option
                .builder("f")
                .longOpt("file")
                .hasArg()
                .argName("XXX.xlxs")
                .required()
                .desc("the file to save the result, file suffix is xlxs")
                .build();
        Option tableOpt = Option
                .builder("t")
                .longOpt("table")
                .hasArg()
                .argName("TABLE_FILE")
                .required()
                .desc("the table of the data to be exported")
                .build();
        Option limitOpt = Option
                .builder("l")
                .longOpt("limit")
                .hasArg()
                .argName("LIMIT")
                .required()
                .desc("the number of the data to be exported")
                .build();
        Option columnOpt = Option.builder("c")
                .longOpt("columns")
                .hasArgs()
                .argName("column1,column2...")
                .valueSeparator(',')
                .required()
                .desc("A columns list with ',' separate to handle")
                .build();

        options.addOption(helpOpt);
        options.addOption(verboseOpt);
        options.addOption(fileOpt);
        options.addOption(tableOpt);
        options.addOption(limitOpt);
        options.addOption(columnOpt);


        CommandLine commandLine = null;
        try {
            // Parse the program arguments
            commandLine = parser.parse(options, args);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return;
        }
        // Set the appropriate variables based on supplied options
        boolean verbose = false;
        String fileName = "";
        String tableName = "";
        int limit = 100;
        List<String> columns = new ArrayList<>();

        if(args == null || args.length ==0 || commandLine.hasOption('h') ) {
            HelpFormatter help = new HelpFormatter();
            help.printHelp("export data", options);
            System.exit(0);
        }
        if( commandLine.hasOption('v') ) {
            verbose = true;
        }

        if (commandLine.hasOption('f')) {
            fileName = commandLine.getOptionValue('f');
            logger.info("文件名：" + fileName);
        } else {
            logger.error("请指定输出文件名");
            return;
        }

        if (commandLine.hasOption('t')) {
            tableName = commandLine.getOptionValue('t');
            logger.info("表名：" + tableName);
        } else {
            logger.error("请指定表名");
            return;
        }

        if (commandLine.hasOption('l')) {
            limit = Integer.valueOf(commandLine.getOptionValue('l'));
            logger.info("导出的记录数：" + limit);
        } else {
            logger.error("请指定导出记录数");
            return;
        }


        if( commandLine.hasOption('c') ) {
            String[] columnArg = commandLine.getOptionValues('c');
            columns = Arrays.asList(columnArg);
        }

        MySqlDBUtil.exportHospitalData(tableName, columns, limit, fileName);
    }
}
