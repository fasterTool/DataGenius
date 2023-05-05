package cn.ly.common.datasource.service.query;

import cn.ly.common.datasource.enumeration.SortType;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/29 13:37
 * @Description:
 */
public class SortCondition {

    private String tableName;
    private String columnName;

    private SortType sortType;

    private SortCondition() {

    }

    public SortCondition(String columnName, SortType sortType) {
        this.columnName = columnName;
        this.sortType = sortType;
    }

    public SortCondition(String tableName, String columnName, SortType sortType) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.sortType = sortType;
    }


    public static SortCondition sort(String columnName, SortType sortType) {
        return new SortCondition(columnName, sortType);
    }

    public static SortCondition sort(String tableName, String columnName, SortType sortType) {
        return new SortCondition(tableName, columnName, sortType);
    }

    public SortCondition tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SortCondition column(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public SortCondition type(SortType sortType) {
        this.sortType = sortType;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public SortType getSortType() {
        return sortType;
    }


}
