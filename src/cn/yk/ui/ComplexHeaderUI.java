package cn.yk.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * 实现表头渲染UI，主要功能时画出表头的单元格
 * @author 雷锋
 * @Date 2019年5月27日
 */
public class ComplexHeaderUI extends BasicTableHeaderUI {
    private Object[][] headerRows;
    private JTable table;
    private int singleRowHeight;
    public ComplexHeaderUI(Object[][] headerRows,JTable table){
        this.headerRows = headerRows;
        this.table = table;
//获取单行的高度，不能使用header.getHeight()获取高度，因为此时表头还没初始化完毕，获取出来的高度是0
        this.singleRowHeight = table.getRowHeight();
// System.out.println(table.getRowHeight());
        JTableHeader tableHeader = table.getTableHeader();
//设置表头不允许拖动 、由于合并了单元格，拖动之后会乱
        tableHeader.setReorderingAllowed(false);
//设置表头整体高度、宽度
        tableHeader.setPreferredSize(new Dimension(table.getWidth(), singleRowHeight * headerRows.length ));
    }
    /**
     * 重写BasicTableHeaderUI.paint的方法是最重要的部分
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        for( int row = 0 ; row < headerRows.length ; row++ ){
            Object[] headerRow = headerRows[row];
            for( int col = 0 ; col < headerRow.length ; col++ ){
                Object cell = headerRow[col];
//如果单元格为合并类单元格、获取其上方是X合并类单元格 + 左边是Y合并类单元格，那么该单元格不需要在窗口展示
                if( cell == ComplexTable.mergeCellX || cell == ComplexTable.mergeCellY || ( col > 0 && row > 0 && headerRow[col - 1] == ComplexTable.mergeCellY &&
                        headerRows[row-1][col] == ComplexTable.mergeCellX ) )
                    continue;
                Rectangle rect = this.getCellRect(row, col);
                String text = cell == null ? "" : cell.toString();
                paintCell(g, rect, text);
            }
        }
    }
    /**
     * 获取当前单元格需要占多少个单位，比如此时的row+1行col列的值=mergeCell，那么说明当前单元格需要占2行
     * @param row
     * @param col
     * @return
     */
    private Rectangle getCellRect(int row , int col){
        int mergeRowNum = 1;
        int nextRow = row;
//判断出y轴方向合并了几行
        while( ++nextRow < headerRows.length ){
            Object nextRowCell = headerRows[nextRow][col];
            if( nextRowCell == ComplexTable.mergeCellY )
                mergeRowNum++;
            else
                break;
        }
        int mergeCellNum = 1;
        int nextCol = col;
        Object[] headerRow = headerRows[row];
//判断x轴方向合并了几列
        while( ++nextCol < headerRow.length ){
            Object nextCell = headerRow[nextCol];
            if( nextCell == ComplexTable.mergeCellX )
                mergeCellNum++;
            else
                break;
        }
//得到一个单元格，起点坐标、宽度、高度
        Rectangle rect = new Rectangle();
        rect.height = this.getCellHeight(mergeRowNum);
        rect.width = this.getCellWidth( col , mergeCellNum);
        rect.y = this.getCellY(row);
        rect.x = this.getCellX( col );
        return rect;
    }
    //根据合并行数得到单元格的高度
    private int getCellHeight( int mergeRowNum ){
        int height = 0;
        for( int i = 0 ; i < mergeRowNum ; i++ )
            height += singleRowHeight;
        return height;
    }
    //根据合并列数得到单元格宽度
    private int getCellWidth( int column , int mergeCellNum ){
        int width = 0;
        TableColumnModel colModel = header.getColumnModel();
        for( int i = 0 ; i < mergeCellNum ; i++ ){
            width += colModel.getColumn( column + i ).getWidth();
        }
        return width;
    }
    //根据单元格所在列得到x轴坐标
    private int getCellX( int column ){
        int width = 0;
        TableColumnModel colModel = header.getColumnModel();
        for( int i = 0 ; i < column ; i++ ){
            width += colModel.getColumn( i ).getWidth();
        }
        return width;
    }
    //根据单元格所在行得到y轴坐标
    private int getCellY( int row ){
        int height = 0;
        for( int i = 0 ; i < row ; i++ ){
            height += singleRowHeight;
        }
        return height;
    }
    //得到具有指定文本的标签
    private JLabel getComponent(String text){
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Dialog", Font.PLAIN, 12));
        label.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        return label;
    }
    /**
     * 画出单元格
     * @param g 画笔
     * @param cellRect 坐标 宽度 高度

     */
    private void paintCell(Graphics g, Rectangle cellRect , String text) {
        Component component = this.getComponent(text);
        rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y,
                        cellRect.width, cellRect.height, true);
    }
}

