package cn.yk.util;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileUtil {

  /*  public static void exportWord(ArrayList<Object> tableNameList, ArrayList<Object> tableDescribeList, ArrayList<Object[][]> fields, Object[] header, String fileName) {
        //创建Word文档
        Document document = new Document();
        //添加一个section
        Section section = document.addSection();

        //数据
  *//*      String[] header = {"姓名", "性别", "部门", "工号"};
        String[][] data =
                {
                        new String[]{"Winny", "女", "综合", "0109"},
                        new String[]{"Lois", "女", "综合", "0111"},
                        new String[]{"Jois", "男", "技术", "0110"},
                        new String[]{"Moon", "女", "销售", "0112"},
                        new String[]{"Vinit", "女", "后勤", "0113"},
                };*//*

        for (int u = 0; u < tableNameList.size(); u++) {

            Object[][] data = fields.get(u);
            //Paragraph paragraph = section.addParagraph();
          //  paragraph.appendText("这是一段样式繁多的文字，我是\n描述");
            //添加表格
            Table table = section.addTable(true);
            //设置表格的行数和列数
            table.resetCells(data.length + 1, header.length);

            //设置第一行作为表格的表头并添加数据
            TableRow row = table.getRows().get(0);
            row.isHeader(true);
            row.setHeight(20);
            row.setHeightType(TableRowHeightType.Exactly);
            row.getRowFormat().setBackColor(Color.gray);
            for (int i = 0; i < header.length; i++) {
                row.getCells().get(i).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
                Paragraph p = row.getCells().get(i).addParagraph();
                p.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
                TextRange range1 = p.appendText(String.valueOf(header[i]));
                range1.getCharacterFormat().setFontName("Arial");
                range1.getCharacterFormat().setFontSize(12f);
                range1.getCharacterFormat().setBold(true);
            }

            //添加数据到剩余行
            for (int r = 0; r < data.length; r++) {
                TableRow dataRow = table.getRows().get(r + 1);
                dataRow.setHeight(25);
                dataRow.setHeightType(TableRowHeightType.Exactly);
                dataRow.getRowFormat().setBackColor(Color.white);
                for (int c = 0; c < data[r].length; c++) {
                    dataRow.getCells().get(c).getCellFormat().setVerticalAlignment(VerticalAlignment.Middle);
                    TextRange range2 = dataRow.getCells().get(c).addParagraph().appendText(String.valueOf(data[r][c]));
                    range2.getCharacterFormat().setFontName("Arial");
                    range2.getCharacterFormat().setFontSize(10f);
                }
            }

            //设置单元格背景颜色
         *//*   for (int j = 1; j < table.getRows().getCount(); j++) {
                if (j % 2 == 0) {
                    TableRow row2 = table.getRows().get(j);
                    for (int f = 0; f < row2.getCells().getCount(); f++) {
                        row2.getCells().get(f).getCellFormat().setBackColor(new Color(173, 216, 230));
                    }
                }
            }*//*
        }

        //保存文档
        document.saveToFile(fileName, FileFormat.Docx_2013);
    }
*/
    public static boolean exportFile(String mdContext, String fileName) {
        File file = new File(fileName);
        try {
            if (!file.exists() && !file.createNewFile()) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(mdContext.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void exportHtml(ArrayList<Object> tableNameList, ArrayList<Object> tableDescribeList, ArrayList<Object[][]> fields, Object[] header, String fileName) {
        StringBuilder data = new StringBuilder();
        data.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Lightly-HTML-Project</title>\n" +
                "    <link type=\"text/css\" rel=\"stylesheet\" href=\"css/style.css\"/>\n" +
                "    <script type=\"text/javascript\" src=\"js/script.js\"></script>\n" +
                "</head>\n" +
                "<body>\n");

        for (int i = 0; i < tableNameList.size(); i++) {
            data.append("<table>\n");
            data.append(getP("表名:"+tableNameList.get(i)));
            data.append(getP("\n说明:"+tableDescribeList.get(i)));
            //表头
            data.append("<tr>\n");
            for (Object head:header){
                data.append(getTh(String.valueOf(head)));
            }
            data.append("</tr>\n");
            //数据
            Object[][] field  = fields.get(i);
            for (Object[] row:field){
                data.append("<tr>\n");
                for (Object column:row){
                    data.append(getTd(String.valueOf(column)));
                }
                data.append("</tr>\n");
            }
            data.append("</table>\n");
            data.append("<p></p>\n").append("<p></p>\n");
        }

        data.append(
                "</body>\n" +
                "<style type=\"text/css\">\n" +
                "body\n" +
                "{\n" +
                "width:340px;\n" +
                "height :800px;\n" +
                "}\n" +
                "table\n" +
                "{\n" +
                "border-collapse :collapse ;\n" +
                "}\n" +
                "th,td\n" +
                "{\n" +
                "width:100px;\n" +
                "height:40px;\n" +
                "border :1px solid black;\n" +
                "font-size:12px;\n" +
                "text-align :center;\n" +
                "}     \n" +
                "</style>\n" +
                "</html>");

        exportFile(data.toString(),fileName);
    }

    private static String getP(String str) {
        return "<p style=\"line-height: 10px;\">" + str + "</p >\n";
    }

    private static String getTh(String str) {
        return "<th>" + str + "</th>\n";
    }

    private static String getTd(String str) {
        return "<td>" + str + "</td>\n";
    }
}
