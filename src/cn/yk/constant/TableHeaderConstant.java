package cn.yk.constant;


import cn.yk.config.NameConfig;
import cn.yk.util.FileUtil;

public final class TableHeaderConstant {

    static NameConfig nameConfig = FileUtil.getNameConfig();

    public static String FIELD = nameConfig.getField() == null ? "字段" : nameConfig.getField();
    public static String TYPE = nameConfig.getDataType() == null ? "类型" : nameConfig.getDataType();
    public static String NOT_NULL = nameConfig.getIsNotNull() == null ? "非空" : nameConfig.getIsNotNull();
    public static String INDEX = nameConfig.getIsIndex() == null ? "索引" : nameConfig.getIsIndex();
    public static String PRIMARY = nameConfig.getPrimary() == null ? "主键" : nameConfig.getPrimary();
    public static String DEFAULT_VALUE = nameConfig.getDefaultValue() == null ? "默认值" : nameConfig.getDefaultValue();
    public static String DESCRIPTION = nameConfig.getDescription() == null ? "说明" : nameConfig.getDescription();

    public static void update(NameConfig config) {
        FIELD = config.getField();
        TYPE = config.getDataType();
        NOT_NULL = config.getIsNotNull();
        INDEX = config.getIsIndex();
        PRIMARY = config.getPrimary();
        DEFAULT_VALUE = config.getDefaultValue();
        DESCRIPTION = config.getDescription();
    }

    public static void update() {
        NameConfig config = FileUtil.getNameConfig();
        FIELD = config.getField() == null ? "字段" : config.getField();
        TYPE = config.getDataType() == null ? "类型" : config.getDataType();
        NOT_NULL = config.getIsNotNull() == null ? "非空" : config.getIsNotNull();
        INDEX = config.getIsIndex() == null ? "索引" : config.getIsIndex();
        PRIMARY = config.getPrimary() == null ? "主键" : config.getPrimary();
        DEFAULT_VALUE = config.getDefaultValue() == null ? "默认值" : config.getDefaultValue();
        DESCRIPTION = config.getDescription() == null ? "说明" : config.getDescription();

    }


}
