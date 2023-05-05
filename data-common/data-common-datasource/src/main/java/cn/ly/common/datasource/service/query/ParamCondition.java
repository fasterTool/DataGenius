package cn.ly.common.datasource.service.query;

import cn.ly.common.datasource.enumeration.OperateParamType;

/**
 * 条件参数
 *
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/29 10:47
 * @Description:
 */
public class ParamCondition {


    private String tableName;

    private String columnName;

    private OperateParamType operateType;

    private ValueType valueType = ValueType.value;

    private String valueTable;
    private Object value;

    private QueryCondition sonCondition;

    private boolean useSonCondition = false;

    public ParamCondition() {
    }

    public ParamCondition(String columnName) {
        this.columnName = columnName;
    }

    public ParamCondition(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public ParamCondition(QueryCondition queryCondition) {
        this.sonCondition = queryCondition;
        this.useSonCondition = true;
    }

    public static ParamCondition where(String columnName) {
        return new ParamCondition(columnName);
    }

    public static ParamCondition where(String tableName, String columnName) {
        return new ParamCondition(tableName, columnName);
    }

    public static ParamCondition son(QueryCondition queryCondition) {
        return new ParamCondition(queryCondition);
    }

    public ParamCondition is(Object value) {
        return equal(value);
    }

    public ParamCondition equal(Object value) {
        this.value = value;
        this.operateType = OperateParamType.EQUAL;
        return this;
    }

    public ParamCondition equal(String valueTable, Object value) {
        this.valueTable = valueTable;
        this.value = value;
        this.operateType = OperateParamType.EQUAL;
        this.valueType = ValueType.table_column;
        return this;
    }

    public ParamCondition notEqual(Object value) {
        this.value = value;
        this.operateType = OperateParamType.NOT_EQUAL;
        return this;
    }

    public ParamCondition notEqual(String valueTable, Object value) {
        this.value = value;
        this.operateType = OperateParamType.NOT_EQUAL;
        this.valueTable = valueTable;
        this.valueType = ValueType.table_column;
        return this;
    }


    public ParamCondition gt(Object value) {
        return greater(value);
    }

    public ParamCondition greater(Object value) {
        this.value = value;
        this.operateType = OperateParamType.GREATER;
        return this;
    }

    public ParamCondition gt(String valueTable, Object value) {
        return greater(valueTable, value);
    }

    public ParamCondition greater(String valueTable, Object value) {
        this.value = value;
        this.operateType = OperateParamType.GREATER;
        this.valueTable = valueTable;
        this.valueType = ValueType.table_column;
        return this;
    }

    public ParamCondition gtEqual(Object value) {
        return greaterEqual(value);
    }

    public ParamCondition greaterEqual(Object value) {
        this.value = value;
        this.operateType = OperateParamType.GREATER_EQUAL;
        return this;
    }

    public ParamCondition gtEqual(String valueTable, Object value) {
        return greaterEqual(valueTable, value);
    }

    public ParamCondition greaterEqual(String valueTable, Object value) {
        this.value = value;
        this.operateType = OperateParamType.GREATER_EQUAL;
        this.valueTable = valueTable;
        this.valueType = ValueType.table_column;
        return this;
    }

    public ParamCondition lt(Object value) {
        return less(value);
    }

    public ParamCondition less(Object value) {
        this.value = value;
        this.operateType = OperateParamType.LESS;
        return this;
    }

    public ParamCondition lt(String valueTable, Object value) {
        return less(valueTable, value);
    }

    public ParamCondition less(String valueTable, Object value) {
        this.value = value;
        this.operateType = OperateParamType.LESS;
        this.valueTable = valueTable;
        this.valueType = ValueType.table_column;
        return this;
    }


    public ParamCondition ltEqual(Object value) {
        return lessEqual(value);
    }

    public ParamCondition lessEqual(Object value) {
        this.value = value;
        this.operateType = OperateParamType.LESS_EQUAL;
        return this;
    }

    public ParamCondition ltEqual(String valueTable, Object value) {
        return lessEqual(valueTable, value);
    }

    public ParamCondition lessEqual(String valueTable, Object value) {
        this.value = value;
        this.operateType = OperateParamType.LESS_EQUAL;
        this.valueTable = valueTable;
        this.valueType = ValueType.table_column;
        return this;
    }

    public ParamCondition like(Object value) {
        this.value = value + "%";
        this.operateType = OperateParamType.LIKE;
        return this;
    }

    public ParamCondition in(Object value) {
        this.value = value;
        this.operateType = OperateParamType.IN;
        return this;
    }

    public ParamCondition notIn(Object value) {
        this.value = value;
        this.operateType = OperateParamType.NOT_IN;
        return this;
    }

    public String getColumnName() {
        return columnName;
    }


    public OperateParamType getOperateType() {
        return operateType;
    }

    public Object getValue() {
        return value;
    }


    public QueryCondition getSonCondition() {
        return sonCondition;
    }

    public boolean isUseSonCondition() {
        return useSonCondition;
    }

    public String getTableName() {
        return tableName;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public String getValueTable() {
        return valueTable;
    }

    public enum ValueType {
        value,
        table_column;
    }

}
