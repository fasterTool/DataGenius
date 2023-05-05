package cn.ly.common.datasource.service.handle;

import cn.ly.common.datasource.service.query.IQueryTree;
import cn.ly.common.datasource.service.query.build.ITreeBuilder;

import java.util.List;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/28 16:32
 * @Description:
 */
public interface ISqlGenerator {

    String insertSql(Object obj);

    String insertSqlUseUid(Object obj);

    String insertSqlExcludeId(Object obj);

    String insertSqlUseUid(Object obj, String... ignoreFields);

    String insertSqlExcludeId(Object obj, String... ignoreFields);

    String insertSql(Object obj, String... ignoreFields);

    String insertSql(Object obj, String tableName, String... ignoreFields);

    String insertSql(List list, String tableName, boolean withUid, String... ignoreFields);

    String updateSqlNotNullSql(Object obj);

    String updateSqlNotNullSql(Object obj, String... ignoreFields);

    String updateSqlNotNullSql(Object obj, String tableName, String... ignoreFields);

    String updateSqlAllSql(Object obj);

    String updateSqlAllSql(Object obj, String... ignoreFields);

    String updateSqlAllSql(Object obj, String tableName, String... ignoreFields);

    String updateSql(Object obj, String tableName, boolean updateNullField, String... ignoreFields);

    String findByIdSql(Object id, Class clazz);

    String findByIdSql(Object id, Class clazz, String... ignoreFields);

    String findByIdSql(Object id, String tableName);


    String findByIdSql(Object id, String tableName, Class clazz, String... ignoreFields);


    String deleteByIdSql(Object id, Class clazz);

    String deleteByIdSql(List<Object> ids, Class clazz);

    String deleteByIdListSql(List<Object> ids, String tableName, Class clazz);

    String querySql(IQueryTree queryTree, Class clazz);

    ITreeBuilder queryBuilder(IQueryTree queryTree, Class clazz);

    String queryPageSizeSql(IQueryTree queryTree, Class clazz);

    String queryPageSizeSql(ITreeBuilder treeBuilder);

    String queryPageInfoSql(IQueryTree queryTree, Class clazz);

    String queryPageInfoSql(ITreeBuilder treeBuilder);
}
