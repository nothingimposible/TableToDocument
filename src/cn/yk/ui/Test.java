/*
package cn.yk.ui;

import com.spire.doc.*;
import com.spire.doc.documents.HorizontalAlignment;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.TableRowHeightType;
import com.spire.doc.documents.VerticalAlignment;
import com.spire.doc.fields.TextRange;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class Test extends JFrame {
    private JPanel contentPane;

    private JTable table;

    public static void main(String[] args){
        String s = "qwe \n"+"1232 \n";
        System.out.println(Arrays.asList(s.split("\n")));
    }

    private static void complex() {
        Object[][] headerRows = new Object[2][6];
        headerRows[0] = new Object[]{"1-1",ComplexTable.mergeCellX,ComplexTable.mergeCellX,ComplexTable.mergeCellX,ComplexTable.mergeCellX,ComplexTable.mergeCellX};

        // headerRows[0] = new Object[]{"1-1",ComplexTable.mergeCellX,"1-3","1-4",ComplexTable.mergeCellX,"1-6"};
//此处2-5是不会显示出来的，因为1-4向下合并了一行 + 向右合并了一列 ， 而2-5被这个矩形范围包括了
        headerRows[1] = new Object[]{"2-1", "2-2" ,"ComplexTable.mergeCellY","ComplexTable.mergeCellY","2-5","2-6"};
        Object[][] body = headerRows;
        JFrame frame=new JFrame();
        frame.setTitle("复杂表头demo");
        frame.getContentPane().add(new JScrollPane(new ComplexTable(headerRows , body)));
        frame.setSize(800,500);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
    }


    public static void word(){
        //创建Word文档
        Document document = new Document();
        //添加一个section
        Section section = document.addSection();

        //数据
        String[] header = {"姓名", "性别", "部门", "工号"};
        String[][] data =
                {
                        new String[]{"Winny", "女", "综合", "0109"},
                        new String[]{"Lois", "女", "综合", "0111"},
                        new String[]{"Jois", "男", "技术", "0110"},
                        new String[]{"Moon", "女", "销售", "0112"},
                        new String[]{"Vinit", "女", "后勤", "0113"},
                };

        for (int u=0;u<3;u++){

            Paragraph paragraph = section.addParagraph();
            paragraph.appendText("这是一段样式繁多的文字，我是\n描述");
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
                TextRange range1 = p.appendText(header[i]);
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
                    TextRange range2 = dataRow.getCells().get(c).addParagraph().appendText(data[r][c]);
                    range2.getCharacterFormat().setFontName("Arial");
                    range2.getCharacterFormat().setFontSize(10f);
                }
            }

            //设置单元格背景颜色
         */
/*   for (int j = 1; j < table.getRows().getCount(); j++) {
                if (j % 2 == 0) {
                    TableRow row2 = table.getRows().get(j);
                    for (int f = 0; f < row2.getCells().getCount(); f++) {
                        row2.getCells().get(f).getCellFormat().setBackColor(new Color(173, 216, 230));
                    }
                }
            }*//*

        }



        //保存文档
        document.saveToFile("CreateTable.docx", FileFormat.Docx_2013);
    }

    public Test() {

        addWindowListener(new WindowAdapter(){

            @Override

            public void windowActivated(WindowEvent e){

                do_this_windowActivated(e);

            }

        });

        setTitle("图书信息表");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtils.setCenter(this);//设置窗体大小600*800并居中

        contentPane=new JPanel();

        contentPane.setBorder(new EmptyBorder(5,5,5,5));

        contentPane.setLayout(new BorderLayout(0,0));

        setContentPane(contentPane);

        JPanel panel=new JPanel();

        contentPane.add(panel,BorderLayout.SOUTH);

        JButton button=new JButton("删除");

        button.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){

                do_button_actionPerformed(e);

            }

        });

        panel.add(button);

        JScrollPane scrollPane=new JScrollPane();

        contentPane.add(scrollPane,BorderLayout.CENTER);

        table=new JTable();

        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        scrollPane.setViewportView(table);

        setVisible(true);

    }

    protected void do_this_windowActivated(WindowEvent e){

        DefaultTableModel tableModel=(DefaultTableModel) table.getModel();    //获得表格模型

        tableModel.setRowCount(0);    //清空表格中的数据

        tableModel.setColumnIdentifiers(new Object[]{"书名","出版社","出版时间","丛书类别","定价"});    //设置表头

        tableModel.addRow(new Object[]{"Java从入门到精通（第2版）","清华大学出版社","2010-07-01","软件工程师入门丛书","59.8元"});    //增加列

        tableModel.addRow(new Object[]{"PHP从入门到精通（第2版）","清华大学出版社","2010-07-01","软件工程师入门丛书","69.8元"});

        tableModel.addRow(new Object[]{"Visual Basic从入门到精通（第2版）","清华大学出版社","2010-07-01","软件工程师入门丛书","69.8元"});

        tableModel.addRow(new Object[]{"Visual C++从入门到精通（第2版）","清华大学出版社","2010-07-01","软件工程师入门丛书","69.8元" });

        table.setRowHeight(30);

        table.setModel(tableModel);    //应用表格模型

    }

    protected void do_button_actionPerformed(ActionEvent e){

        DefaultTableModel model=(DefaultTableModel) table.getModel();    //获得表格模型

        int[] selectedRows=table.getSelectedRows();

        for(int i=0;i<selectedRows.length;i++){

            model.removeRow(selectedRows[0]);

            System.out.println();

        }

        table.setModel(model);

    }
}
*/
