package cn.yk.config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class NameConfig {

    private String field;

    private String dataType;

    private String isNotNull;

    private String isIndex;

    private String primary;

    private String defaultValue;

    private String description;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIsNotNull() {
        return isNotNull;
    }

    public void setIsNotNull(String isNotNull) {
        this.isNotNull = isNotNull;
    }

    public String getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(String isIndex) {
        this.isIndex = isIndex;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "NameConfig{" +
                "field='" + field + '\'' +
                ", dataType='" + dataType + '\'' +
                ", isNotNull='" + isNotNull + '\'' +
                ", isIndex='" + isIndex + '\'' +
                ", primary='" + primary + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public NameConfig(){}

    public NameConfig(Map<String,String> map) throws IllegalAccessException {
        if (map!=null){
            Class clazz = NameConfig.class;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field:fields){
                field.setAccessible(true);
                field.set(this,map.get(field.getName()));
            }
        }
    }

    public Map<String,String> toMap() throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        Class clazz = NameConfig.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields){
            if (field.get(this)!=null){
                map.put(field.getName(), String.valueOf(field.get(this)));
            }
        }
        return map;
    }
}
