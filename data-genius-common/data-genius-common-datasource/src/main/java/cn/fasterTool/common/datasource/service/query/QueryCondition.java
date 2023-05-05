package cn.fasterTool.common.datasource.service.query;

import cn.fasterTool.common.datasource.enumeration.OperateQueryType;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/29 10:39
 * @Description:
 */
public class QueryCondition {

    private LinkedList<OperateQueryType> queryTypes;

    private LinkedList<ParamCondition> paramConditions;

    public QueryCondition() {
        init();
    }

    public QueryCondition(Collection<ParamCondition> paramConditions, OperateQueryType type) {
        init(paramConditions, type);
    }

    private void init() {
        this.queryTypes = new LinkedList<>();
        this.paramConditions = new LinkedList<>();
    }

    private void init(Collection<ParamCondition> paramConditions, OperateQueryType type) {
        if (paramConditions != null && paramConditions.size() > 0) {
            init();
            this.paramConditions.addAll(paramConditions);
            this.queryTypes.addAll(Collections.nCopies(paramConditions.size(), type));
        }
    }


    public static QueryCondition where(ParamCondition paramCondition) {
        QueryCondition condition = new QueryCondition();
        condition.queryTypes.addFirst(OperateQueryType.AND);
        condition.paramConditions.addFirst(paramCondition);
        return condition;
    }

    public static QueryCondition and(Collection<ParamCondition> paramConditions) {
        return new QueryCondition(paramConditions, OperateQueryType.AND);
    }

    public static QueryCondition or(Collection<ParamCondition> paramConditions) {
        return new QueryCondition(paramConditions, OperateQueryType.OR);
    }


    public boolean checkNull() {
        return queryTypes == null || queryTypes.size() == 0;
    }


    public QueryCondition and(ParamCondition paramCondition) {
        queryTypes.addLast(OperateQueryType.AND);
        paramConditions.addLast(paramCondition);
        return this;
    }

    public QueryCondition or(ParamCondition paramCondition) {
        queryTypes.addLast(OperateQueryType.OR);
        paramConditions.addLast(paramCondition);
        return this;
    }

    public LinkedList<OperateQueryType> getQueryTypes() {
        return queryTypes;
    }

    public LinkedList<ParamCondition> getParamConditions() {
        return paramConditions;
    }
}
