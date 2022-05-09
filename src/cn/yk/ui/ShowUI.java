package cn.yk.ui;

import cn.yk.constant.TableHeaderConstant;
import cn.yk.util.FileUtil;
import cn.yk.util.StringUtils;
import com.intellij.database.model.DasColumn;
import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.ui.ColoredSideBorder;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.containers.JBIterable;
import org.jsoup.internal.StringUtil;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.util.*;
import java.util.List;

public class ShowUI extends JFrame {

    private Clipboard clipboard = getToolkit().getSystemClipboard();

    public static void main(String[] args) {
        new ShowUI();
    }

    public ShowUI() {
        AnActionEvent anActionEvent = null;
        StringBuilder mdContext = new StringBuilder();
        PsiElement[] psiElements = anActionEvent.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        Object[] tableHead = new Object[]{TableHeaderConstant.FIELD, TableHeaderConstant.TYPE, TableHeaderConstant.NOT_NULL,
                TableHeaderConstant.INDEX, TableHeaderConstant.PRIMARY, TableHeaderConstant.DEFAULT_VALUE, TableHeaderConstant.DESCRIPTION};
        ArrayList<Object> tableNameList = new ArrayList<>();
        ArrayList<Object> tableDescribeList = new ArrayList<>();
        ArrayList<Object[][]> fields = new ArrayList<>();

        ArrayList<JTable> jTableArrayList = new ArrayList<>();
        if (psiElements != null) {
            for (PsiElement psiElement : psiElements) {

                StringBuilder oneTable = new StringBuilder();
                DbTable dbTable = (DbTable) psiElement;

                tableNameList.add(dbTable.getName());
                tableDescribeList.add(dbTable.getComment() == null ? "" : dbTable.getComment());

                String[][] field = getField(tableHead, dbTable);
                fields.add(field);

                for (Object head : tableHead) {
                    oneTable.append("|").append(head);
                }
                oneTable.append("|\n");
                for (int i = 0; i < tableHead.length; i++) {
                    oneTable.append("|").append("----");
                }
                oneTable.append("|\n");

                for (int i = 0; i < field.length; i++) {
                    for (int u = 0; u < field[0].length; u++) {
                        oneTable.append("|").append(field[i][u] == null ? "" : field[i][u]);
                    }
                    oneTable.append("|\n");
                }
                //TODO
                jTableArrayList.add(getJTable(field, tableHead));
                String tableInfo = "table name:" + dbTable.getName() + "\n" + (dbTable.getComment() == null ? "" : dbTable.getComment());
                mdContext.append("\n\n").append(tableInfo).append("\n").append(oneTable);
            }
        }

        //---------------------------------------   内容分割线   ----------------------------------

        JButton buttonCopy = new JButton("copy");
        buttonCopy.setBounds(100, 0, 50, 30);

        //文本域
        JTextArea textArea = new JTextArea(40, 50);
        textArea.setText(mdContext.toString());


        //Scroll面板
        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel panel = new JPanel();
        for (JTable table : jTableArrayList) {
            panel.add(table);
        }
        JScrollPane scrollPane2 = new JScrollPane(panel);

        scrollPane.setBounds(0, 50, 200, 300);
        JPanel topPanel = new JPanel();

        topPanel.add(buttonCopy);
        topPanel.add(scrollPane2);
        // 添加面板
        add(topPanel);


        buttonCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //内容写到复制里
                clipboard.setContents(new StringSelection(textArea.getText()), null);
                Messages.showMessageDialog("copy success!", "Notice", Messages.getInformationIcon());
            }
        });

        setTitle("Table To Md");
        setSize(700, 1500);
        setPreferredSize(new Dimension(650, 1500));//设置大小
        setBackground(Color.black);
        setLocation(120, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }


    private JTable getJTable(String[][] data, Object[] head) {
     //   UIManager.put("Table.gridColor", new ColorUIResource(Color.gray));
        DefaultTableCellRenderer trc = new DefaultTableCellRenderer();
        trc.setHorizontalAlignment(JLabel.CENTER);
        JTable jTable = new JTable(data, head);
        jTable.setDefaultRenderer(Object.class, trc);

        DefaultTableCellHeaderRenderer thr = new DefaultTableCellHeaderRenderer();
        thr.setHorizontalAlignment(JLabel.CENTER);
        jTable.getTableHeader().setDefaultRenderer(thr);

        // tableModel.setValueAt();
        jTable.setRowHeight(50);
        Border border = new ColoredSideBorder(JBColor.BLACK, JBColor.BLACK, JBColor.BLACK, JBColor.BLACK, 3);
        jTable.setBorder(border);
        JTableHeader tableHeader = jTable.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 50));
        jTable.getTableHeader().setVisible(true);
        return jTable;
    }


    @NotNull
    private String[][] getField(Object[] tableHead, DbTable dbTable) {
        JBIterable<? extends DasColumn> columnsIter = DasUtil.getColumns(dbTable);
        List<? extends DasColumn> dasColumns = columnsIter.toList();

        Set<Object> headSet = new HashSet<>();
        headSet.addAll(Arrays.asList(tableHead));
        String[][] field = new String[dasColumns.size()][tableHead.length];
        for (int i = 0; i < dasColumns.size(); i++) {
            for (int u = 0; u < tableHead.length; u++)
                field[i][u] = getValue(String.valueOf(tableHead[u]), dasColumns.get(i));
           /* field[0][i] = dasColumns.get(i).getName();
            field[1][i] = dasColumns.get(i).getDataType().typeName;
            field[2][i] = dasColumns.get(i).isNotNull() ? "Y" : "";
            field[3][i] = DasUtil.isIndexColumn(dasColumns.get(i)) ? "Y" : "";
            field[4][i] = DasUtil.isPrimary(dasColumns.get(i)) ? "Y" : "";
            field[5][i] = dasColumns.get(i).getDefault();
            field[6][i] = dasColumns.get(i).getComment();*/
        }
        return field;
    }

    private String getValue(String headName, DasColumn dasColumn) {
        switch (headName) {
            case TableHeaderConstant
                    .FIELD:
                return dasColumn.getName();
            case TableHeaderConstant.TYPE:
                return String.valueOf(dasColumn.getDataType());
            case TableHeaderConstant.NOT_NULL:
                return dasColumn.isNotNull() ? "Y" : " ";
            case TableHeaderConstant.INDEX:
                return DasUtil.isIndexColumn(dasColumn) ? "Y" : " ";
            case TableHeaderConstant.PRIMARY:
                return DasUtil.isPrimary(dasColumn) ? "Y" : " ";
            case TableHeaderConstant.DEFAULT_VALUE:
                return dasColumn.getDefault();
            case TableHeaderConstant.DESCRIPTION:
                return dasColumn.getComment() == null ? " " : dasColumn.getComment();
            default:
                return " ";
        }
    }

    public ShowUI(AnActionEvent anActionEvent) {

        StringBuilder mdContext = new StringBuilder();
        PsiElement[] psiElements = anActionEvent.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        Object[] tableHead = new Object[]{TableHeaderConstant.FIELD, TableHeaderConstant.TYPE, TableHeaderConstant.NOT_NULL,
                TableHeaderConstant.INDEX, TableHeaderConstant.PRIMARY, TableHeaderConstant.DEFAULT_VALUE, TableHeaderConstant.DESCRIPTION};
        ArrayList<Object> tableNameList = new ArrayList<>();
        ArrayList<Object> tableDescribeList = new ArrayList<>();
        ArrayList<Object[][]> fields = new ArrayList<>();

        ArrayList<JTable> jTableArrayList = new ArrayList<>();
        if (psiElements != null) {
            for (PsiElement psiElement : psiElements) {

                StringBuilder oneTable = new StringBuilder();
                DbTable dbTable = (DbTable) psiElement;

                tableNameList.add(dbTable.getName());
                tableDescribeList.add(dbTable.getComment() == null ? "" : dbTable.getComment());

                String[][] field = getField(tableHead, dbTable);
                fields.add(field);

                for (Object head : tableHead) {
                    oneTable.append("|").append(head);
                }
                oneTable.append("|\n");
                for (int i = 0; i < tableHead.length; i++) {
                    oneTable.append("|").append("----");
                }
                oneTable.append("|\n");

                for (int i = 0; i < field.length; i++) {
                    for (int u = 0; u < field[i].length; u++) {
                        oneTable.append("|").append(field[i][u] == null ? " " : field[i][u]);
                    }
                    oneTable.append("|\n");
                }
                //TODO
                //jTableArrayList.add(getJTable(field, tableHead));
                String tableInfo = "table name:" + dbTable.getName() + "\n" + (dbTable.getComment() == null ? "" : dbTable.getComment());
                mdContext.append("\n\n").append(tableInfo).append("\n").append(oneTable);
            }
        }

        //---------------------------------------------------------------------//

        JBTabbedPane jTabbedpane = new JBTabbedPane();// 存放选项卡的组件
        String[] tabNames = {"table to document", "document to sql"};
        JPanel topPanel = new JPanel();
      //  topPanel.setBackground(Color.CYAN);
        topPanel.setLayout(new BorderLayout());
        //head 不显示
        Object[][] head = new Object[1][];
        head[0] = tableHead;
        Object[][] data1 = new Object[][]{
                {"表名", ComplexTable.mergeCellX, ComplexTable.mergeCellX},
                {"描述：大三大四的", ComplexTable.mergeCellX, ComplexTable.mergeCellX},
                {"列1", "列2", "列3"},
                {"7", "8", "9"},
                {"10dukhfaiudgfksjdgfkusygfyusdfvhu", "11", "12"},
                {"10", "11", "12"},
                {"10", "11", "12"},
                {"10", "11", "12"},
                {"10", "11", "12"},
                {"10", "11", "12"},
                {"10", "11", "12"},
                {"10", "11", "12"}, {"10", "11", "12"}, {"10", "11", "12"},
                {"10", "11", "12"},
                {"10", "11", "12"},
                {"10", "11", "12"},
                {"15", "16", "18"},
        };
        final String[] exportFileName = {null};

        for (int i = 0; i < fields.size(); i++) {
            Object[][] data = new Object[3 + fields.get(i).length][tableHead.length];
            data[0][0] = "table name：" + tableNameList.get(i);
            for (int u = 1; u < data[0].length; u++) {
                data[0][u] = ComplexTable.mergeCellX;
            }
            data[1][0] = "describe：" + tableDescribeList.get(i);
            for (int u = 1; u < data[1].length; u++) {
                data[1][u] = ComplexTable.mergeCellX;
            }
            data[2] = tableHead;
            for (int u = 3; u < data.length; u++) {
                //TODO 可能会有赋值问题
                data[u] = fields.get(i)[u - 3];
            }
            jTableArrayList.add(new ComplexTable(head, data));
        }


        JPanel headJPanel = new JPanel();
        headJPanel.setLayout(new GridLayout(2, 3, 50, 10));
        JButton buttonCopy = new JButton("copy");
        //  buttonCopy.setBounds(100, 0, 50, 30);

        buttonCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //内容写到复制里
                clipboard.setContents(new StringSelection(mdContext.toString()), null);
                Messages.showMessageDialog("Copy success!", "Notice", Messages.getInformationIcon());
            }
        });

        JButton selectButton = new JButton("Select an Export Directory");
        JButton exportButton = new JButton("export");
        final JLabel fileLabel = new JLabel("FilePath:");


        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int option = fileChooser.showOpenDialog(topPanel.getParent());
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    fileLabel.setText("FilePath:" + file.getPath());
                    exportFileName[0] = file.getPath();
                } else {
                    fileLabel.setText("Select the export folder～");
                }
            }
        });

        fileLabel.setVisible(true);

        JComboBox fileType = new JComboBox();
        fileType.addItem(".md");
        fileType.addItem(".html");
     //   fileType.addItem(".docx");

        fileType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取下拉框的值
                System.out.println(fileType.getSelectedItem().toString());
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //导出文件
                if (StringUtil.isBlank(exportFileName[0])) {
                    Messages.showMessageDialog("Select a path for exporting the file!", "Notice", Messages.getInformationIcon());
                } else {
                    switch (String.valueOf(fileType.getSelectedItem())) {
                        case ".md":
                            FileUtil.exportFile(mdContext.toString(), exportFileName[0] + "/" + tableNameList.get(0) + ".md");
                            break;
                        case ".html":
                            FileUtil.exportHtml(tableNameList, tableDescribeList, fields, tableHead, exportFileName[0] + "/" + tableNameList.get(0) + ".html");
                            break;
                        case ".docx":
                     //       FileUtil.exportWord(tableNameList, tableDescribeList, fields, tableHead, exportFileName[0] + "/" + tableNameList.get(0) + ".docx");
                            break;
                        default:
                            Messages.showMessageDialog("The file type is not supported!", "Notice", Messages.getInformationIcon());
                            break;

                    }
                    Messages.showMessageDialog("Export success!", "Notice", Messages.getInformationIcon());
                }
            }
        });

        //如果需要全选功能在数组值中加入全选即可

        Object[] values = new Object[]{"全选", TableHeaderConstant.FIELD, TableHeaderConstant.TYPE, TableHeaderConstant.NOT_NULL,
                TableHeaderConstant.INDEX, TableHeaderConstant.PRIMARY, TableHeaderConstant.DEFAULT_VALUE, TableHeaderConstant.DESCRIPTION};
        MultiComboBox comboxstatus = new MultiComboBox(values);
        comboxstatus.setSelectValues(new Object[]{TableHeaderConstant.FIELD, TableHeaderConstant.TYPE, TableHeaderConstant.NOT_NULL,
                TableHeaderConstant.PRIMARY, TableHeaderConstant.DEFAULT_VALUE, TableHeaderConstant.DESCRIPTION});

        headJPanel.add(selectButton);
        headJPanel.add(fileLabel);
        headJPanel.add(fileType);
        headJPanel.add(exportButton);
        headJPanel.add(buttonCopy);
        //  headJPanel.add(comboxstatus);

      //  headJPanel.setBackground(Color.green);
        topPanel.add(headJPanel, BorderLayout.NORTH);

        //文本域
        // JTextArea textArea = new JTextArea(40, 50);
        //   textArea.setText("result.toString()");

        //Scroll面板
        //  JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel panel = new JPanel();
        panel.setSize(500, 700);
        panel.setVisible(true);
        //   panel.setBounds(0,50,50,700);
       // panel.setBackground(Color.gray);
        panel.setLayout(new GridLayout(jTableArrayList.size(), 1, 50, 10));
        for (JTable table : jTableArrayList) {

           // table.setRowHeight(10);
            //  JScrollPane scroll = new JScrollPane(table);
            Panel childPanel = new Panel();
            // Label label = new Label("biaoming");
            //  childPanel.add(label,BorderLayout.NORTH);
            childPanel.add(table, BorderLayout.CENTER);
            //  scroll.setName("biaoming");
            panel.add(table);
        }

        JScrollPane scrollPane2 = new JScrollPane(panel);

        //  scrollPane.setBounds(0, 50, 200, 300);
        topPanel.add(scrollPane2, BorderLayout.CENTER);
        topPanel.setBounds(300, 200, 600, 300);

        JPanel topPanel2 = new JPanel();
        topPanel2.setLayout(new BorderLayout());
        JTextArea sqlArea = new JTextArea();
        sqlArea.setRows(20);
        JScrollPane sqlJScroll = new JScrollPane(sqlArea);
        JLabel tipsLabel = new JLabel("支持解析列名：字段、类型、非空、主键、默认值、描述");

        JButton toSqlButton = new JButton("get sql");
        topPanel2.add(toSqlButton,BorderLayout.NORTH);
        topPanel2.add(sqlJScroll,BorderLayout.CENTER);
        topPanel2.add(tipsLabel,BorderLayout.SOUTH);
        toSqlButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (sqlArea.getText() == null || "".equals(sqlArea.getText().trim())) {
                        Messages.showMessageDialog("Please enter the markdown statement!", "Notice", Messages.getInformationIcon());
                    } else {
                        Set<String> yes = new HashSet<>(Arrays.asList(new String[]{"y","Y","yes","YES","是"}));
                        Set<String> no = new HashSet<>(Arrays.asList(new String[]{"n","N","no","NO","不是","否"}));
                        String data = sqlArea.getText();

                        StringBuilder sb = new StringBuilder("CREATE TABLE `" + "` (\n");
                        String[] lines = data.split("\n");
                        String lastLines = "";
                        String tableName = "";
                        boolean flag = false;
                        Map<Integer, String> fieldMap = new HashMap<>();
                        // 按行读取字符串
                        for (String column : lines) {

                            if (column.contains("table name")) {
                                tableName = column.replace("table name:", "");
                                flag = false;
                            }

                            if (column.contains("----")) {
                                sb = new StringBuilder("CREATE TABLE `" + tableName + "` (\n");
                                String[] headers = lastLines.split("\\|");
                                for (int i = 0; i < headers.length; i++) {
                                    fieldMap.put(i, headers[i]);
                                }
                                flag = true;
                                continue;
                            }
                            if (flag) {
                                String[] columnPropertys = column.split("\\|");
                                boolean primaryKey = false;
                                for (int i = 0; i < columnPropertys.length; i++) {

                                    switch (fieldMap.get(i).trim()) {
                                        case TableHeaderConstant.FIELD:
                                            if (columnPropertys[i].trim().length() > 0){
                                                sb.append("`").append(columnPropertys[i].trim()).append("` ");
                                            }
                                            break;
                                        case TableHeaderConstant.TYPE:
                                            if (columnPropertys[i].trim().length() > 0){
                                                sb.append(columnPropertys[i].trim()).append(" ");
                                            }

                                            break;
                                        case TableHeaderConstant.NOT_NULL:
                                            if (yes.contains(columnPropertys[i].trim())){
                                                sb.append("NOT NULL ");
                                            }
                                            break;
                                        case TableHeaderConstant.PRIMARY:
                                            if (yes.contains(columnPropertys[i].trim())){
                                                primaryKey = true;
                                            }
                                            break;
                                        case TableHeaderConstant.DEFAULT_VALUE:
                                            if (columnPropertys[i].trim().length() > 0){
                                                sb.append("DEFAULT ").append(columnPropertys[i].trim()).append(" ");
                                            }

                                            break;
                                        case TableHeaderConstant.DESCRIPTION:
                                            if (columnPropertys[i].trim().length() > 0){
                                                sb.append("COMMENT '").append(columnPropertys[i].trim()).append("' ");
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                if (primaryKey) {
                                    sb.append("PRIMARY KEY ");
                                }

                                sb = sb.deleteCharAt(sb.lastIndexOf(" "));
                                sb.append(",\n");
                            }

                            lastLines = column;
                        }
                        sb = sb.deleteCharAt(sb.lastIndexOf(","));
                        sb.append(");");

                        sqlArea.setText(sb.toString());
                        System.out.printf(sb.toString());
                    }
                } catch (Exception ex) {
                    Messages.showMessageDialog(String.valueOf(ex), "Notice", Messages.getInformationIcon());

                }

            }
        });


        jTabbedpane.addTab(tabNames[0], null, topPanel, "Tab1");// 加入第一个页面
        jTabbedpane.setMnemonicAt(0, KeyEvent.VK_0);// 设置第一个位置的快捷键为0

        jTabbedpane.addTab(tabNames[1], null, topPanel2, "Tab2");// 加入第一个页面
        jTabbedpane.setMnemonicAt(1, KeyEvent.VK_1);// 设置第一个位置的快捷键为0
        add(jTabbedpane);
        // 添加面板
        // add(topPanel, BorderLayout.CENTER);

        setTitle("table-document");
        setSize(900, 1500);
        setPreferredSize(new Dimension(850, 1500));//设置大小
        setBackground(Color.black);
        setLocation(120, 100);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

    }

}
