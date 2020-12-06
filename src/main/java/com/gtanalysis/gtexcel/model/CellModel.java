/*
 */
package com.gtanalysis.gtexcel.model;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nkabiliravi
 */
@XmlRootElement
public class CellModel {
    
    private int columnIndex;
    private int rowIndex;
    private Object value;
    private int valueType;
    
    public CellModel() {
    }
    
    public CellModel(int columnIndex, int rowIndex, Object value, int valueType) {
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.value = value;
        this.valueType = valueType;
    }
    
    public int getColumnIndex() {
        return columnIndex;
    }
    
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }
    
    public int getRowIndex() {
        return rowIndex;
    }
    
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
    
    public Object getValue() {
        return value;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
    
    public int getValueType() {
        return valueType;
    }
    
    public void setValueType(int valueType) {
        this.valueType = valueType;
    }
    
    public String toXml() throws IOException {
        StringWriter sw = new StringWriter();
        JAXB.marshal(this, sw);
        String result = sw.toString();
        sw.close();
        return result;
    }
    
    public void setXml(String xml) {
        CellModel cellModel;
        try (StringReader sr = new StringReader(xml)) {
            cellModel = JAXB.unmarshal(sr, CellModel.class);
        }
        setColumnIndex(cellModel.getColumnIndex());
        setRowIndex(cellModel.getRowIndex());
        setValue(cellModel.getValue());
        setValueType(cellModel.getValueType());
    }    
    
}
