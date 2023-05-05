package cn.ly.common.datasource.service.query;

import cn.ly.common.datasource.enumeration.JoinType;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/29 10:40
 * @Description:
 */
public class JoinCondition {

    private JoinType joinType;

    private String tableName;

    private String alias;

    private QueryCondition queryCondition;


    private JoinCondition() {

    }

    public JoinCondition(String tableName, JoinType joinType) {
        this.tableName = tableName;
        this.joinType = joinType;
    }

    public static JoinCondition leftJoin(String tableName) {
        return new JoinCondition(tableName, JoinType.LEFT);
    }

    public static JoinCondition rightJoin(String tableName) {
        return new JoinCondition(tableName, JoinType.RIGHT);
    }

    public static JoinCondition innerJoin(String tableName) {
        return new JoinCondition(tableName, JoinType.INNER);
    }


    public JoinCondition table(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public JoinCondition join(JoinType joinType) {
        this.joinType = joinType;
        return this;
    }

    public JoinCondition as(String alias) {
        this.alias = alias;
        return this;
    }

    public JoinCondition on(QueryCondition queryCondition) {
        this.queryCondition = queryCondition;
        return this;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public String getTableName() {
        return tableName;
    }

    public String getAlias() {
        return alias;
    }

    public QueryCondition getQueryCondition() {
        return queryCondition;
    }
}
