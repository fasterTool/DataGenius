package cn.ly.common.datasource.service.handle;

import cn.ly.common.datasource.annotation.Column;
import cn.ly.common.datasource.constants.BaseCRUDConstants;
import com.baidu.fsg.uid.UidGenerator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/27 10:07
 * @Description:
 */
public abstract class AbstractSqlGeneratorBuilder implements ISqlGenerator {

    private final static Logger logger = LoggerFactory.getLogger(AbstractSqlGeneratorBuilder.class);

    @Resource
    private UidGenerator uidGenerator;

    protected abstract String type();


    boolean ignoreField(String targetField, String... sourceFields) {
        if (sourceFields == null || sourceFields.length == 0) return false;
        for (String str : sourceFields) if (StringUtils.equals(targetField, str)) return true;
        return false;
    }

    boolean isId(Field field) {
        return field.getDeclaredAnnotation(Id.class) != null;
    }


    String generatorTableColumn(Field field) {
        Column column = field.getAnnotation(Column.class);
        return column == null ? generatorColumnByName(field.getName()) : column.column();
    }

    String generatorColumnByName(String fileName) {
        StringBuilder sb = new StringBuilder();
        for (char c : fileName.toCharArray()) {
            if (Character.isUpperCase(c)) sb.append(BaseCRUDConstants.UNDERLINE);
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    String generatorGetMethod(Field field) {
        return generatorMethod(field, "get");
    }

    String generatorSetMethod(Field field) {
        return generatorMethod(field, "set");
    }

    String generatorSetMethod(String fieldName) {
        return generatorMethod(fieldName, "set");
    }

    String generatorMethod(Field field, String methodPrefix) {
        return generatorMethod(field.getName(), methodPrefix);
    }

    String generatorMethod(String fieldName, String methodPrefix) {
        return methodPrefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    String generateFieldValue(Field field, Object obj, boolean withUid) {
        if (field.getDeclaredAnnotation(Id.class) != null) return generateIdValue(field, obj, withUid);
        return earnFieldValue(field, obj);
    }

    String generateIdValue(Field field, Object obj, boolean withUid) {
        return withUid ? String.valueOf(uidGenerator.getUID()) : earnFieldValue(field, obj);
    }

    String earnFieldValue(Field field, Object obj) {
        String getMethodName = generatorGetMethod(field);
        try {
            Method getMethod = obj.getClass().getMethod(getMethodName);
            Object invoke = getMethod.invoke(obj);
            return invoke == null ? null : String.valueOf(invoke);

        } catch (NoSuchMethodException e) {
            logger.error("generatorGetMethod NoSuchMethodException error:", e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            logger.error("generatorGetMethod InvocationTargetException error:", e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            logger.error("generatorGetMethod IllegalAccessException error:", e);
            throw new RuntimeException(e);
        }

    }

    private static String getComparisonCondition(String property, String operation, String value) {
        String result = "";
        switch (operation) {
            case BaseCRUDConstants.NOT_EQUAL:
                result = property + " <> '" + value + "'";
                break;
            case BaseCRUDConstants.GREATER:
                result = property + " > '" + value + "'";
                break;
            case BaseCRUDConstants.GREATER_EQUAL:
                result = property + " >= '" + value + "'";
                break;
            case BaseCRUDConstants.LESS:
                result = property + " < '" + value + "'";
                break;
            case BaseCRUDConstants.LESS_EQUAL:
                result = property + " <= '" + value + "'";
                break;
            case BaseCRUDConstants.LIKE:
                result = property + " LIKE '%" + value + "%'";
                break;
            case BaseCRUDConstants.EQUAL:
            default:
                result = property + " = '" + value + "'";
        }
        return result;
    }

    void generatorUpdateSetItem(Object obj, StringBuilder setSb, StringBuilder whereSb, Field field, boolean updateNullField, String... ignoreFields) {
        if (ignoreField(field.getName(), ignoreFields)) return;
        final Id annotation = field.getDeclaredAnnotation(Id.class);
        final String value = earnFieldValue(field, obj);
        if (annotation != null) generatorIdWhereItem(whereSb, field, value);
        if (annotation == null && (value != null || updateNullField)) generatorUpdateItem(setSb, field, value);
    }

    void generatorUpdateItem(StringBuilder setSb, Field field, String value) {
        setSb.append(" ").append(generatorTableColumn(field)).append(BaseCRUDConstants.EQUAL).append("'").append(value).append("',");
    }

    void generatorIdWhereItem(StringBuilder whereSb, Field field, String value) {
        whereSb.append(generatorTableColumn(field)).append(BaseCRUDConstants.EQUAL).append("'").append(value).append("'");
    }


}
