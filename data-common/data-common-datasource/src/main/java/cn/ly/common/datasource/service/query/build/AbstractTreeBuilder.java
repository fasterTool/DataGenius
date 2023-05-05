package cn.ly.common.datasource.service.query.build;

import cn.ly.common.datasource.constants.BaseCRUDConstants;
import cn.ly.common.datasource.enumeration.JoinType;
import cn.ly.common.datasource.enumeration.OperateParamType;
import cn.ly.common.datasource.enumeration.OperateQueryType;
import cn.ly.common.datasource.enumeration.SortType;
import cn.ly.common.datasource.service.query.*;
import cn.ly.common.datasource.utils.CRUDUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/30 14:50
 * @Description:
 */
public abstract class AbstractTreeBuilder implements ITreeBuilder {

    protected Class clazz;
    protected AbstractQueryTree queryTree;

    protected String resultBuilder;

    protected String fromBuilder;
    protected String joinBuilder;
    protected String queryBuilder;
    protected String sortBuilder;
    protected String groupBuilder;

    protected String havingBuilder;
    protected String pageBuilder;


    protected AbstractTreeBuilder build(boolean needRefresh) {
        generateResultParam(needRefresh);
        generateFromBuilder(needRefresh);
        generateJoinBuilder(needRefresh);
        generateQueryBuilder(needRefresh);
        generateGroupBuilder(needRefresh);
        generateHavingBuilder(needRefresh);
        generateSortBuilder(needRefresh);
        generatePageBuilder(needRefresh);
        return this;
    }


    @Override
    public String generatePageTotalSql() {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(1)").append(" ")
                .append("From").append(" ").append(fromBuilder).append(" ");

        if (StringUtils.isNotBlank(joinBuilder)) {
            sb.append(joinBuilder).append(" ");
        }

        if (StringUtils.isNotBlank(queryBuilder)) {
            sb.append("WHERE").append(" ").append(queryBuilder).append(" ");
        }

        if (StringUtils.isNotBlank(groupBuilder)) {
            sb.append("GROUP BY").append(" ").append(groupBuilder).append(" ");
        }

        if (StringUtils.isNotBlank(havingBuilder)) {
            sb.append("HAVING").append(" ").append(havingBuilder).append(" ");
        }

        return sb.toString();
    }

    @Override
    public String generatePageInfoSql() {
        String sql = generateNoPageSql();
        return sql + " " + pageBuilder;
    }

    @Override
    public String generateNoPageSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT").append(" ").append(resultBuilder).append(" ")
                .append("From").append(" ").append(fromBuilder).append(" ");

        if (StringUtils.isNotBlank(joinBuilder)) {
            sb.append(joinBuilder).append(" ");
        }

        if (StringUtils.isNotBlank(queryBuilder)) {
            sb.append("WHERE").append(" ").append(queryBuilder).append(" ");
        }

        if (StringUtils.isNotBlank(groupBuilder)) {
            sb.append("GROUP BY").append(" ").append(groupBuilder).append(" ");
        }

        if (StringUtils.isNotBlank(havingBuilder)) {
            sb.append("HAVING").append(" ").append(havingBuilder).append(" ");
        }

        if (StringUtils.isNotBlank(sortBuilder)) {
            sb.append("ORDER BY").append(" ").append(sortBuilder).append(" ");
        }

        return sb.toString();
    }

    protected void generateResultParam(boolean needRefresh) {
        if (resultBuilder != null && !needRefresh) return;

        if (queryTree.getResultFields() != null && queryTree.getResultFields().size() != 0) {
            resultBuilder = String.join(",", queryTree.getResultFields());
            return;
        }

        if (clazz == null) {
            resultBuilder = "*";
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Field field : clazz.getDeclaredFields()) {
            sb.append(" ").append(CRUDUtils.generatorColumnByName(field.getName())).append(" as ").append(field.getName()).append(",");
        }
        resultBuilder = sb.substring(0, sb.length() - 1);
    }


    protected void generateJoinBuilder(boolean needRefresh) {
        if (joinBuilder != null && !needRefresh) return;
        final List<JoinCondition> joinConditions = queryTree.getJoinConditions();
        if (joinConditions == null || joinConditions.size() == 0) return;

        StringBuilder sb = new StringBuilder();
        joinConditions.forEach(item -> {
            sb.append(getJoinType(item.getJoinType())).append(" ").append(item.getTableName()).append(" ");
            if (StringUtils.isNotBlank(item.getAlias())) {
                sb.append(BaseCRUDConstants.AS).append(" ").append(item.getAlias()).append(" ");
            }
            if (!item.getQueryCondition().checkNull()) {
                sb.append(BaseCRUDConstants.ON).append(" ").append(explainQueryCondition(item.getQueryCondition()));
            }
        });

        joinBuilder = sb.toString();
    }

    protected void generateFromBuilder(boolean needRefresh) {
        if (joinBuilder != null && !needRefresh) return;
        if (StringUtils.isNotBlank(queryTree.getFromTable())) {
            fromBuilder = queryTree.getFromTable();
            return;
        }

        if (clazz == null) {
            throw new RuntimeException("Please ensure use table by com.linkapp.common.datasource.annotation.Table or set by QueryTree");
        }

        fromBuilder = CRUDUtils.getTableNameWithAlias(clazz);
    }


    protected void generateGroupBuilder(boolean needRefresh) {
        if (groupBuilder != null && !needRefresh) return;
        GroupCondition condition = queryTree.getGroupCondition();
        if (condition == null || condition.getFields() == null || condition.getFields().size() == 0) return;

        StringBuilder sb = new StringBuilder();
        condition.getFields().forEach(item -> sb.append(item).append(BaseCRUDConstants.COMMA));
        groupBuilder = sb.substring(0, sb.length() - 1);
    }

    protected void generateHavingBuilder(boolean needRefresh) {
        if (havingBuilder != null && !needRefresh) return;
        StringBuilder sb = explainQueryCondition(queryTree.getQueryCondition());
        havingBuilder = sb == null ? null : sb.toString();
    }

    protected void generateQueryBuilder(boolean needRefresh) {
        if (queryBuilder != null && !needRefresh) return;
        StringBuilder sb = explainQueryCondition(queryTree.getQueryCondition());
        queryBuilder = sb == null ? null : sb.toString();
    }


    protected void generateSortBuilder(boolean needRefresh) {
        if (sortBuilder != null && !needRefresh) return;
        if (queryTree.getSortConditions() == null || queryTree.getSortConditions().size() == 0) {
            sortBuilder = null;
            return;
        }

        StringBuilder sb = new StringBuilder();
        SortCondition sortCondition = null;
        while ((sortCondition = (SortCondition) queryTree.getSortConditions().poll()) != null) {
            if (StringUtils.isNotBlank(sortCondition.getTableName())) {
                sb.append(BaseCRUDConstants.COLUMN_IDENTIFIER).append(sortCondition.getTableName()).append(BaseCRUDConstants.COLUMN_IDENTIFIER);
            }
            sb.append(BaseCRUDConstants.COLUMN_IDENTIFIER).append(sortCondition.getColumnName()).append(BaseCRUDConstants.COLUMN_IDENTIFIER).append(" ")
                    .append(getSortType(sortCondition.getSortType())).append(",");
        }
        sortBuilder = sb == null ? null : sb.substring(0, sb.length() - 1) + " ";
    }

    protected void generatePageBuilder(boolean needRefresh) {
        if (pageBuilder != null && !needRefresh) return;
        if (!this.queryTree.isUsePage()) return;
        queryTree.checkPageCondition();
        pageBuilder = "Limit " + (queryTree.getPageCondition().getPage() - 1) * queryTree.getPageCondition().getPageSize() + " , " + queryTree.getPageCondition().getPageSize();
    }


    private StringBuilder explainParamCondition(ParamCondition paramCondition) {
        if (paramCondition == null) return null;
        return paramCondition.isUseSonCondition() ? explainParamSonCondition(paramCondition) : explainParamConditionNormal(paramCondition);
    }

    private StringBuilder explainParamConditionNormal(ParamCondition paramCondition) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(paramCondition.getTableName())) {
            sb.append(BaseCRUDConstants.COLUMN_IDENTIFIER).append(paramCondition.getTableName()).append(BaseCRUDConstants.COLUMN_IDENTIFIER)
                    .append(BaseCRUDConstants.POINT);
        }

        return sb.append(BaseCRUDConstants.COLUMN_IDENTIFIER).append(paramCondition.getColumnName()).append(BaseCRUDConstants.COLUMN_IDENTIFIER)
                .append(" ").append(getParamType(paramCondition.getOperateType())).append(" ")
                .append(explainParamValue(paramCondition));
    }

    private String explainParamValue(ParamCondition paramCondition) {

        if (paramCondition.getValueType() == ParamCondition.ValueType.table_column) {
            return String.format("`%s`.`%s`", paramCondition.getValueTable(), paramCondition.getValue());
        }
        Object value = paramCondition.getValue();
        if (value == null) throw new NullPointerException();
        if (value instanceof Collection) {
            StringBuilder sb = new StringBuilder(BaseCRUDConstants.BRACKET_LEFT);
            Collection collection = (Collection) value;
            collection.forEach(item -> {
                sb.append(BaseCRUDConstants.VALUE_IDENTIFIER).append(String.valueOf(item)).append(BaseCRUDConstants.VALUE_IDENTIFIER).append(BaseCRUDConstants.COMMA);
            });
            return sb.substring(0, sb.length() - 1) + BaseCRUDConstants.BRACKET_RIGHT;
        }
        return BaseCRUDConstants.VALUE_IDENTIFIER + String.valueOf(value) + BaseCRUDConstants.VALUE_IDENTIFIER;

    }

    private StringBuilder explainParamSonCondition(ParamCondition paramCondition) {
        StringBuilder explainQueryCondition = explainQueryCondition(paramCondition.getSonCondition());
        if (explainQueryCondition == null) return null;
        return new StringBuilder(BaseCRUDConstants.BRACKET_LEFT).append(explainQueryCondition).append(BaseCRUDConstants.BRACKET_RIGHT);
    }

    private StringBuilder explainQueryCondition(QueryCondition queryCondition) {
        if (queryCondition == null || queryCondition.getQueryTypes() == null || queryCondition.getQueryTypes().size() == 0)
            return null;
        if (queryCondition.getParamConditions().size() != queryCondition.getQueryTypes().size())
            throw new RuntimeException(String.format("query condition data in mix,type:{%s},condition:{%s}", queryCondition.getQueryTypes(), queryCondition.getParamConditions()));

        StringBuilder sb = new StringBuilder("1 = 1").append(" ");
        final LinkedList<OperateQueryType> queryTypes = queryCondition.getQueryTypes();

        OperateQueryType queryType;
        while ((queryType = queryTypes.poll()) != null) {
            ParamCondition paramCondition = queryCondition.getParamConditions().poll();
            sb.append(getQueryType(queryType)).append(" ").append(explainParamCondition(paramCondition)).append(" ");
        }

        return sb;
    }


    String getJoinType(JoinType joinType) {
        switch (joinType) {
            case LEFT:
                return BaseCRUDConstants.LEFT_JOIN;
            case RIGHT:
                return BaseCRUDConstants.RIGHT_JOIN;
            case INNER:
            default:
                return BaseCRUDConstants.INNER_JOIN;
        }
    }

    String getSortType(SortType sortType) {
        switch (sortType) {
            case DESC:
                return BaseCRUDConstants.DESC_SORT;
            case ASC:
            default:
                return BaseCRUDConstants.ASC_SORT;
        }
    }

    String getQueryType(OperateQueryType queryType) {
        switch (queryType) {
            case OR:
                return BaseCRUDConstants.OR_QUERY;
            case AND:
            default:
                return BaseCRUDConstants.AND_QUERY;
        }
    }

    String getParamType(OperateParamType operateParamType) {
        switch (operateParamType) {

            case NOT_EQUAL:
                return BaseCRUDConstants.NOT_EQUAL;
            case IN:
                return BaseCRUDConstants.IN;
            case NOT_IN:
                return BaseCRUDConstants.NOT_IN;
            case LESS:
                return BaseCRUDConstants.LESS;
            case LESS_EQUAL:
                return BaseCRUDConstants.LESS_EQUAL;
            case GREATER:
                return BaseCRUDConstants.GREATER;
            case GREATER_EQUAL:
                return BaseCRUDConstants.GREATER_EQUAL;
            case LIKE:
                return BaseCRUDConstants.LIKE;
            case EQUAL:
            default:
                return BaseCRUDConstants.EQUAL;
        }
    }


}
