package cn.yk.ui;

import cn.yk.config.NameConfig;
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
import org.jetbrains.annotations.NotNull;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.*;

public class ShowUI extends JFrame {

    private Clipboard clipboard = getToolkit().getSystemClipboard();


    public static void main(String[] args) {
        new ShowUI();
    }

    public void init() {
        TableHeaderConstant.update();
    }

    public ShowUI() {

        init();

        JPanel topPanelSetting = new JPanel();

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        topPanelSetting.setLayout(null);

        // 创建 JLabel
        JLabel fieldLabel = new JLabel("field:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        fieldLabel.setBounds(10, 20, 80, 25);
        topPanelSetting.add(fieldLabel);

        /*
         * 创建文本域用于用户输入
         */
        JTextField fieldText = new JTextField(20);
        fieldText.setBounds(100, 20, 165, 25);
        topPanelSetting.add(fieldText);

        // 输入数据类型的文本域
        JLabel dataTypeLabel = new JLabel("dataType:");
        dataTypeLabel.setBounds(10, 50, 80, 25);
        topPanelSetting.add(dataTypeLabel);

        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        JTextField dataTypeText = new JTextField(20);
        dataTypeText.setBounds(100, 50, 165, 25);
        topPanelSetting.add(dataTypeText);

        // 输入不为空的文本域
        JLabel notNullLabel = new JLabel("not null:");
        notNullLabel.setBounds(10, 80, 80, 25);
        topPanelSetting.add(notNullLabel);

        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        JTextField notNullText = new JTextField(20);
        notNullText.setBounds(100, 80, 165, 25);
        topPanelSetting.add(notNullText);

        // 输入不为空的文本域
        JLabel indexLabel = new JLabel("index:");
        indexLabel.setBounds(10, 110, 80, 25);
        topPanelSetting.add(indexLabel);

        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        JTextField indexText = new JTextField(20);
        indexText.setBounds(100, 110, 165, 25);
        topPanelSetting.add(indexText);

        // 输入不为空的文本域
        JLabel primaryLabel = new JLabel("primary:");
        primaryLabel.setBounds(10, 140, 80, 25);
        topPanelSetting.add(primaryLabel);

        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        JTextField primaryText = new JTextField(20);
        primaryText.setBounds(100, 140, 165, 25);
        topPanelSetting.add(primaryText);

        // 输入不为空的文本域
        JLabel defaultValueLabel = new JLabel("defaultValue:");
        defaultValueLabel.setBounds(10, 170, 100, 25);
        topPanelSetting.add(defaultValueLabel);

        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        JTextField defaultValueText = new JTextField(20);
        defaultValueText.setBounds(100, 170, 165, 25);
        topPanelSetting.add(defaultValueText);

        // 输入不为空的文本域
        JLabel descriptionLabel = new JLabel("description:");
        descriptionLabel.setBounds(10, 200, 80, 25);
        topPanelSetting.add(descriptionLabel);

        /*
         *这个类似用于输入的文本域
         * 但是输入的信息会以点号代替，用于包含密码的安全性
         */
        JTextField descriptionText = new JTextField(20);
        descriptionText.setBounds(100, 200, 165, 25);
        topPanelSetting.add(descriptionText);

        // 创建登录按钮
        JButton saveButton = new JButton("save");
        saveButton.setBounds(350 / 2 - 40, 250 - 12, 80, 25);
        topPanelSetting.add(saveButton);
        add(topPanelSetting);

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

        }
        return field;
    }

    private String getValue(String headName, DasColumn dasColumn) {
        if (Objects.equals(TableHeaderConstant.FIELD, headName))
            return dasColumn.getName();
        if (Objects.equals(headName, TableHeaderConstant.TYPE))
            return String.valueOf(dasColumn.getDataType());
        if (Objects.equals(headName, TableHeaderConstant.NOT_NULL))
            return dasColumn.isNotNull() ? "Y" : " ";
        if (Objects.equals(headName, TableHeaderConstant.INDEX))
            return DasUtil.isIndexColumn(dasColumn) ? "Y" : " ";
        if (Objects.equals(headName, TableHeaderConstant.PRIMARY))
            return DasUtil.isPrimary(dasColumn) ? "Y" : " ";
        if (Objects.equals(headName, TableHeaderConstant.DEFAULT_VALUE))
            return dasColumn.getDefault();
        if (Objects.equals(headName, TableHeaderConstant.DESCRIPTION))
            return dasColumn.getComment() == null ? " " : dasColumn.getComment().replace("\n"," ");

        return " ";

    }

    public ShowUI(AnActionEvent anActionEvent) {
        init();
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
        String[] tabNames = {"table to document", "document to sql", "setting"};
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        //head 不显示
        Object[][] head = new Object[1][];
        head[0] = tableHead;

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
                if (StringUtils.isBlank(exportFileName[0])) {
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

        topPanel.add(headJPanel, BorderLayout.NORTH);

        //文本域
        // JTextArea textArea = new JTextArea(40, 50);
        //   textArea.setText("result.toString()");

        //Scroll面板
        //  JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel topPanel3 = new JPanel();
        topPanel3.setSize(500, 700);
        topPanel3.setVisible(true);
        //   panel.setBounds(0,50,50,700);
        // panel.setBackground(Color.gray);
        topPanel3.setLayout(new GridLayout(jTableArrayList.size(), 1, 50, 10));
        for (JTable table : jTableArrayList) {

            // table.setRowHeight(10);
            //  JScrollPane scroll = new JScrollPane(table);
            Panel childPanel = new Panel();
            childPanel.add(table, BorderLayout.CENTER);
            topPanel3.add(table);
        }

        JScrollPane scrollPane2 = new JScrollPane(topPanel3);

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
        topPanel2.add(toSqlButton, BorderLayout.NORTH);
        topPanel2.add(sqlJScroll, BorderLayout.CENTER);
        topPanel2.add(tipsLabel, BorderLayout.SOUTH);
        toSqlButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (sqlArea.getText() == null || "".equals(sqlArea.getText().trim())) {
                        Messages.showMessageDialog("Please enter the markdown statement!", "Notice", Messages.getInformationIcon());
                    } else {
                        Set<String> yes = new HashSet<>(Arrays.asList(new String[]{"y", "Y", "yes", "YES", "是"}));
                        Set<String> no = new HashSet<>(Arrays.asList(new String[]{"n", "N", "no", "NO", "不是", "否"}));
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

                                        if(Objects.equals(fieldMap.get(i).trim(), TableHeaderConstant.FIELD)){
                                            if (columnPropertys[i].trim().length() > 0) {
                                                sb.append("`").append(columnPropertys[i].trim()).append("` ");
                                            }
                                            continue;
                                        }


                                        if(Objects.equals(fieldMap.get(i).trim(), TableHeaderConstant.TYPE)){
                                            if (columnPropertys[i].trim().length() > 0) {
                                                sb.append(columnPropertys[i].trim()).append(" ");
                                            }
                                            continue;
                                        }

                                        if(Objects.equals(fieldMap.get(i).trim(), TableHeaderConstant.NOT_NULL)){
                                            if (yes.contains(columnPropertys[i].trim())) {
                                                sb.append("NOT NULL ");
                                            }
                                            continue;
                                        }


                                        if(Objects.equals(fieldMap.get(i).trim(), TableHeaderConstant.PRIMARY)){
                                            if (yes.contains(columnPropertys[i].trim())) {
                                                primaryKey = true;
                                            }
                                            continue;
                                        }

                                        if(Objects.equals(fieldMap.get(i).trim(), TableHeaderConstant.DEFAULT_VALUE)){
                                            if (columnPropertys[i].trim().length() > 0) {
                                                sb.append("DEFAULT ").append(columnPropertys[i].trim()).append(" ");
                                            }
                                            continue;
                                        }

                                        if(Objects.equals(fieldMap.get(i).trim(), TableHeaderConstant.DESCRIPTION)){
                                            if (columnPropertys[i].trim().length() > 0) {
                                                sb.append("COMMENT '").append(columnPropertys[i].trim()).append("' ");
                                            }
                                            continue;
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

        JPanel topPanelSetting = new JPanel();

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        topPanelSetting.setLayout(null);

        // 创建 JLabel
        JLabel fieldLabel = new JLabel("field:");
        fieldLabel.setBounds(10, 20, 80, 25);
        topPanelSetting.add(fieldLabel);

        JTextField fieldText = new JTextField(20);
        fieldText.setBounds(100, 20, 165, 25);
        fieldText.setText(TableHeaderConstant.FIELD);
        topPanelSetting.add(fieldText);

        JLabel dataTypeLabel = new JLabel("dataType:");
        dataTypeLabel.setBounds(10, 50, 80, 25);
        topPanelSetting.add(dataTypeLabel);

        JTextField dataTypeText = new JTextField(20);
        dataTypeText.setBounds(100, 50, 165, 25);
        dataTypeText.setText(TableHeaderConstant.TYPE);
        topPanelSetting.add(dataTypeText);

        JLabel notNullLabel = new JLabel("not null:");
        notNullLabel.setBounds(10, 80, 80, 25);
        topPanelSetting.add(notNullLabel);

        JTextField notNullText = new JTextField(20);
        notNullText.setBounds(100, 80, 165, 25);
        notNullText.setText(TableHeaderConstant.NOT_NULL);
        topPanelSetting.add(notNullText);

        JLabel indexLabel = new JLabel("index:");
        indexLabel.setBounds(10, 110, 80, 25);
        topPanelSetting.add(indexLabel);

        JTextField indexText = new JTextField(20);
        indexText.setBounds(100, 110, 165, 25);
        indexText.setText(TableHeaderConstant.INDEX);
        topPanelSetting.add(indexText);

        JLabel primaryLabel = new JLabel("primary:");
        primaryLabel.setBounds(10, 140, 80, 25);
        topPanelSetting.add(primaryLabel);

        JTextField primaryText = new JTextField(20);
        primaryText.setBounds(100, 140, 165, 25);
        primaryText.setText(TableHeaderConstant.PRIMARY);
        topPanelSetting.add(primaryText);

        JLabel defaultValueLabel = new JLabel("defaultValue:");
        defaultValueLabel.setBounds(10, 170, 100, 25);
        topPanelSetting.add(defaultValueLabel);

        JTextField defaultValueText = new JTextField(20);
        defaultValueText.setBounds(100, 170, 165, 25);
        defaultValueText.setText(TableHeaderConstant.DEFAULT_VALUE);
        topPanelSetting.add(defaultValueText);

        JLabel descriptionLabel = new JLabel("description:");
        descriptionLabel.setBounds(10, 200, 80, 25);
        topPanelSetting.add(descriptionLabel);

        JTextField descriptionText = new JTextField(20);
        descriptionText.setBounds(100, 200, 165, 25);
        descriptionText.setText(TableHeaderConstant.DESCRIPTION);
        topPanelSetting.add(descriptionText);

        JButton saveButton = new JButton("save");
        saveButton.setBounds(350 / 2 - 40, 250 - 12, 80, 25);
        topPanelSetting.add(saveButton);

        ActionListener saveListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NameConfig config = new NameConfig();
                config.setField(fieldText.getText());
                config.setDataType(dataTypeText.getText());
                config.setPrimary(primaryText.getText());
                config.setIsNotNull(notNullText.getText());
                config.setIsIndex(indexText.getText());
                config.setDefaultValue(defaultValueText.getText());
                config.setDescription(descriptionText.getText());
                FileUtil.saveNameConfig(config);
               // TableHeaderConstant.update(config);
                Messages.showMessageDialog("Save success!\nRe-open the plugin configuration to take effect", "Notice", Messages.getInformationIcon());
            }
        };

        saveButton.addActionListener(saveListener);

        /*----------------------------------------------------------------------------*/

        jTabbedpane.addTab(tabNames[0], null, topPanel, "Tab1");// 加入第一个页面
        jTabbedpane.setMnemonicAt(0, KeyEvent.VK_0);// 设置第一个位置的快捷键为0

        jTabbedpane.addTab(tabNames[1], null, topPanel2, "Tab2");// 加入第一个页面
        jTabbedpane.setMnemonicAt(1, KeyEvent.VK_1);// 设置第一个位置的快捷键为0

        jTabbedpane.addTab(tabNames[2], null, topPanelSetting, "Tab3");// 加入第一个页面
        jTabbedpane.setMnemonicAt(2, KeyEvent.VK_2);// 设置第一个位置的快捷键为0
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
