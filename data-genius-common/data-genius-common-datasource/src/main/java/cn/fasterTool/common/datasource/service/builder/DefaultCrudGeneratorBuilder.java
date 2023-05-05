package cn.fasterTool.common.datasource.service.builder;

import cn.fasterTool.common.datasource.service.query.IQueryTree;
import cn.fasterTool.common.datasource.service.query.build.ITreeBuilder;
import cn.fasterTool.common.datasource.service.query.build.QueryTreeBuilder;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/28 16:31
 * @Description:
 */
public class DefaultCrudGeneratorBuilder extends CRUDGeneratorBuilder {


    /**
     * @param queryTree
     * @return
     */
    @Override
    public ITreeBuilder treeBuilder(IQueryTree queryTree) {
        return new QueryTreeBuilder(queryTree);
    }

    @Override
    public ITreeBuilder treeBuilder(IQueryTree queryTree, Class clazz) {
        return new QueryTreeBuilder(queryTree, clazz);
    }

    @Override
    protected String type() {
        return CRUDGeneratorFactory.DataType.MYSQL.getType();
    }
}
