package cn.yk.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ComplexTable extends JTable {
    public final static Object mergeCellX = "mergeCellX";//标识单元格是否要被横向合并
    public final static Object mergeCellY = "mergeCellY";//标识单元格是否要被纵向合并
    public ComplexTable(Object[][] headerRows , Object[][] body){
        super( new DefaultTableModel(body, headerRows[0]) );
// super( 0 , headerRows[0].length );
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setEnabled(false);
//设置table内容居中
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(JLabel.CENTER);// 这句和上句作用一样
        this.setDefaultRenderer(Object.class, tcr);
//设置表头UI
        this.getTableHeader().setUI(new ComplexHeaderUI(headerRows , this));
        this.setUI(new ComplexTableUI(body,this));
    }
}
