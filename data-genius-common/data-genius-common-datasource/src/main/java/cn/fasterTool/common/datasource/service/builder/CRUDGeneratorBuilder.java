package cn.fasterTool.common.datasource.service.builder;

import cn.fasterTool.common.datasource.constants.BaseCRUDConstants;
import cn.fasterTool.common.datasource.service.query.IQueryTree;
import cn.fasterTool.common.datasource.service.query.build.ITreeBuilder;
import cn.fasterTool.common.datasource.service.query.build.QueryTreeBuilder;
import cn.fasterTool.common.datasource.utils.CRUDUtils;
import com.baidu.fsg.uid.UidGenerator;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sql生成处理器
 *
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/25 17:46
 * @Description:
 */
public abstract class CRUDGeneratorBuilder extends AbstractSqlGeneratorBuilder implements ISqlGenerator {


    private final static Logger logger = LoggerFactory.getLogger(CRUDGeneratorBuilder.class);


    @Override
    public String insertSql(Object obj) {
        return insertSql(obj, null);
    }

    @Override
    public String insertSql(Object obj, String... ignoreFields) {
        return insertSql(obj, CRUDUtils.getTableName(obj), ignoreFields);
    }

    @Override
    public String insertSql(Object obj, String tableName, String... ignoreFields) {
        return insertSql(Arrays.asList(obj), tableName, false, null, ignoreFields);
    }


    @Override
    public String insertSql(List list,String...ignoreFields) {
        return insertSql(list,CRUDUtils.getTableName(list.get(0)),false,null,ignoreFields);
    }

    @Override
    public String insertSqlUseUid(List list, UidGenerator uidGenerator,String...ignoreFields) {
        return insertSql(list,CRUDUtils.getTableName(list.get(0)),true,uidGenerator,ignoreFields);
    }


    @Override
    public String insertSqlUseUid(Object obj,UidGenerator uidGenerator) {
        return insertSqlUseUid(obj,uidGenerator,null);
    }

    @Override
    public String insertSqlUseUid(Object obj,UidGenerator uidGenerator, String... ignoreFields) {
        return insertSql(Arrays.asList(obj), CRUDUtils.getTableName(obj), true,uidGenerator, false, ignoreFields);
    }

    @Override
    public String insertSqlExcludeId(Object obj) {
        return insertSqlExcludeId(obj,null);
    }

    @Override
    public String insertSqlExcludeId(Object obj, String... ignoreFields) {
        return insertSql(Arrays.asList(obj), CRUDUtils.getTableName(obj), false,null, true, ignoreFields);
    }

    @Override
    public String insertSql(List list, String tableName, boolean withUid, UidGenerator uidGenerator, String... ignoreFields) {
        return insertSql(list, tableName, withUid, uidGenerator, false, ignoreFields);
    }

    public String insertSql(List list, String tableName, boolean withUid, UidGenerator uidGenerator, boolean excludeId, String... ignoreFields) {
        if (list == null || list.size() == 0) return null;

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tableName);

        StringBuilder keySb = new StringBuilder();
        List<StringBuilder> valueSb = new ArrayList<>(list.size());
        list.forEach(item -> {
            valueSb.add(new StringBuilder());
        });

        for (Field field : list.get(0).getClass().getDeclaredFields()) {

            if (excludeId && field.getDeclaredAnnotation(Id.class) != null) continue;

            // filter the ignoreFields
            if (field.getAnnotation(Ignore.class) == null && !ignoreField(field.getName(), ignoreFields)) {
                keySb.append("`").append(generatorTableColumn(field)).append("`,");
                for (int index = 0; index < list.size(); index++) {
                    valueSb.get(index).append("'").append(generateFieldValue(field, list.get(index), withUid, uidGenerator)).append("',");
                }
            }
        }

        sb.append(BaseCRUDConstants.BRACKET_LEFT).append(keySb.substring(0, keySb.length() - 1)).append(BaseCRUDConstants.BRACKET_RIGHT).append("VALUES");
        valueSb.forEach(item -> {
            sb.append(BaseCRUDConstants.BRACKET_LEFT).append(item.substring(0, item.length() - 1)).append(BaseCRUDConstants.BRACKET_RIGHT).append(",");
        });

        return sb.toString().substring(0, sb.length() - 1);
    }


    @Override
    public String updateSqlNotNullSql(Object obj) {
        return updateSqlNotNullSql(obj, null);
    }


    @Override
    public String updateSqlNotNullSql(Object obj, String... ignoreFields) {
        return updateSqlNotNullSql(obj, CRUDUtils.getTableName(obj), ignoreFields);
    }


    @Override
    public String updateSqlNotNullSql(Object obj, String tableName, String... ignoreFields) {
        return updateSql(obj, tableName, false, ignoreFields);
    }


    @Override
    public String updateSqlAllSql(Object obj) {
        return updateSqlAllSql(obj, null);
    }


    @Override
    public String updateSqlAllSql(Object obj, String... ignoreFields) {
        return updateSqlAllSql(obj, CRUDUtils.getTableName(obj), ignoreFields);
    }


    @Override
    public String updateSqlAllSql(Object obj, String tableName, String... ignoreFields) {
        return updateSql(obj, tableName, true, ignoreFields);
    }


    @Override
    public String updateSql(Object obj, String tableName, boolean updateNullField, String... ignoreFields) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(tableName).append(" ");

        StringBuilder whereSb = new StringBuilder();
        StringBuilder setSb = new StringBuilder();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            generatorUpdateSetItem(obj, setSb, whereSb, fields[i], updateNullField, ignoreFields);
        }

        if (whereSb.length() == 0) throw new RuntimeException("Id value not be null");

        return sb.append(" ").append(" SET ").append(" ").append(setSb.substring(0, setSb.length() - 1))
                .append(" ").append(" WHERE ").append(" ").append(whereSb).toString();

    }


    @Override
    public String findByIdSql(Object id, Class clazz) {
        return findByIdSql(id, CRUDUtils.getTableName(clazz));
    }


    @Override
    public String findByIdSql(Object id, Class clazz, String... ignoreFields) {
        return findByIdSql(id, CRUDUtils.getTableName(clazz), clazz, ignoreFields);
    }


    @Override
    public String findByIdSql(Object id, String tableName) {
        return findByIdSql(id, tableName, null);
    }


    @Override
    public String findByIdSql(Object id, String tableName, Class clazz, String... ignoreFields) {
        if (ObjectUtils.isEmpty(id)) throw new RuntimeException("id can‘t be null");
        StringBuilder sb = new StringBuilder();

        StringBuilder columnSb = new StringBuilder();
        StringBuilder idSb = new StringBuilder();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Ignore.class) != null || ignoreField(field.getName(), ignoreFields)) continue;
            if (!ignoreField(field.getName(), ignoreFields))
                columnSb.append(" ").append(generatorTableColumn(field)).append(" AS ").append(field.getName()).append(",");

            if (isId(field))
                idSb.append(generatorTableColumn(field)).append(BaseCRUDConstants.EQUAL).append("'").append(id).append("'");
        }
        if (idSb.length() == 0) throw new RuntimeException("Please identify the ID annotation");

        return sb.append("SELECT ").append(columnSb.substring(0, columnSb.length() - 1)).append(" ")
                .append("FROM ").append(tableName).append(" ")
                .append("WHERE ").append(idSb).toString();
    }

    @Override
    public String deleteByIdSql(Object id, Class clazz) {
        return deleteByIdSql(Arrays.asList(id), clazz);
    }

    @Override
    public String deleteByIdSql(List<Object> ids, Class clazz) {
        return deleteByIdListSql(ids, CRUDUtils.getTableName(clazz), clazz);
    }

    @Override
    public String deleteByIdListSql(List<Object> ids, String tableName, Class clazz) {
        if (ObjectUtils.isEmpty(ids)) throw new RuntimeException("id can‘t be null");

        Field idField = null;
        for (Field field : clazz.getDeclaredFields()) {
            if (isId(field)) idField = field;
        }

        if (idField == null) throw new RuntimeException("can't find Id field");

        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(tableName).append(" WHERE ").append(idField.getName()).append(" ").append(BaseCRUDConstants.IN).append(BaseCRUDConstants.BRACKET_LEFT);

        ids.forEach(item -> sb.append(BaseCRUDConstants.VALUE_IDENTIFIER).append(item).append(BaseCRUDConstants.VALUE_IDENTIFIER).append(BaseCRUDConstants.COMMA));

        return sb.substring(0, sb.length() - 1) + BaseCRUDConstants.BRACKET_RIGHT;

    }


    @Override
    public String querySql(IQueryTree queryTree, Class clazz) {
        return treeBuilder(queryTree, clazz).build().generateNoPageSql();
    }

    @Override
    public ITreeBuilder queryBuilder(IQueryTree queryTree, Class clazz) {
        return treeBuilder(queryTree, clazz).build();
    }

    @Override
    public String queryPageSizeSql(IQueryTree queryTree, Class clazz) {
        return treeBuilder(queryTree, clazz).build().generatePageTotalSql();
    }

    @Override
    public String queryPageSizeSql(ITreeBuilder treeBuilder) {
        return treeBuilder.generatePageTotalSql();
    }

    @Override
    public String queryPageInfoSql(IQueryTree queryTree, Class clazz) {
        return treeBuilder(queryTree, clazz).build().generatePageInfoSql();
    }

    @Override
    public String queryPageInfoSql(ITreeBuilder treeBuilder) {
        return treeBuilder.generatePageInfoSql();
    }

    protected ITreeBuilder treeBuilder(IQueryTree queryTree) {
        return new QueryTreeBuilder(queryTree);
    }

    protected ITreeBuilder treeBuilder(IQueryTree queryTree, Class clazz) {
        return new QueryTreeBuilder(queryTree, clazz);
    }


}
