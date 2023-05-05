package cn.ly.common.datasource.service.query.build;

import cn.ly.common.datasource.service.query.AbstractQueryTree;
import cn.ly.common.datasource.service.query.IQueryTree;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/30 20:04
 * @Description:
 */
public class QueryTreeBuilder extends AbstractTreeBuilder implements ITreeBuilder {


    public QueryTreeBuilder(IQueryTree queryTree) {
        super.queryTree = (AbstractQueryTree) queryTree;
    }

    public QueryTreeBuilder(IQueryTree queryTree, Class clazz) {
        super.queryTree = (AbstractQueryTree) queryTree;
        super.clazz = clazz;
    }

    @Override
    public AbstractTreeBuilder build() {
        return build(false);
    }

    @Override
    public AbstractTreeBuilder rebuild() {
        return build(true);
    }


}
