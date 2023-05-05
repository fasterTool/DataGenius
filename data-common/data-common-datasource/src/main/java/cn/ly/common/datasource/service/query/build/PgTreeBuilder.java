package cn.ly.common.datasource.service.query.build;

import cn.ly.common.datasource.service.query.AbstractQueryTree;
import cn.ly.common.datasource.service.query.IQueryTree;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/4/4 19:58
 * @Description:
 */
public class PgTreeBuilder extends AbstractTreeBuilder implements ITreeBuilder {
    public PgTreeBuilder(IQueryTree queryTree) {
        super.queryTree = (AbstractQueryTree) queryTree;
    }

    public PgTreeBuilder(IQueryTree queryTree, Class clazz) {
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

    @Override
    protected void generatePageBuilder(boolean needRefresh) {
        if (pageBuilder != null && !needRefresh) return;
        if (!this.queryTree.isUsePage()) return;
        queryTree.checkPageCondition();
        pageBuilder = String.format("Limit %s offset %s", queryTree.getPageCondition().getPageSize(), (queryTree.getPageCondition().getPage() - 1) * queryTree.getPageCondition().getPageSize());
    }
}
