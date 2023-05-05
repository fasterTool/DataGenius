package cn.ly.common.datasource.utils;

import cn.ly.common.datasource.annotation.Column;
import cn.ly.common.datasource.annotation.Table;
import cn.ly.common.datasource.constants.BaseCRUDConstants;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/29 15:21
 * @Description:
 */
public class CRUDUtils {


    public static String generatorTableColumn(Field field) {
        Column column = field.getAnnotation(Column.class);
        return column == null ? generatorColumnByName(field.getName()) : column.column();
    }

    public static String generatorColumnByName(String fileName) {
        StringBuilder sb = new StringBuilder();
        for (char c : fileName.toCharArray()) {
            if (Character.isUpperCase(c)) sb.append(BaseCRUDConstants.UNDERLINE);
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    public static String getTableName(Object obj) {
        return getTableName(obj.getClass());
    }

    public static String getTableName(Class clazz) {
        Table annotation = (Table) clazz.getDeclaredAnnotation(Table.class);
        return annotation == null ? clazz.getSimpleName() : annotation.tableName();
    }

    public static String getTableNameWithAlias(Class clazz) {
        Table annotation = (Table) clazz.getDeclaredAnnotation(Table.class);
        return annotation == null ? clazz.getSimpleName() : getTableNameWithAlias(annotation);
    }

    public static String getTableNameWithAlias(Table table) {
        return StringUtils.isBlank(table.alias()) ? table.tableName() : String.format(" %s as %s ", table.tableName(), table.alias());
    }


}
