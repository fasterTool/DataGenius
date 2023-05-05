package cn.ly.archive.study.test.config.datasource;

import cn.fasterTool.common.datasource.service.builder.CRUDGeneratorBuilder;
import cn.fasterTool.common.datasource.service.query.IQueryTree;
import cn.fasterTool.common.datasource.service.query.build.ITreeBuilder;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998">liu yun</a>
 * @classname OtherBuilder
 * @create 2023/5/3 21:07
 */
public class OtherCRUDGeneratorBuilder extends CRUDGeneratorBuilder {
    public String type() {
        return "other";
    }

    @Override
    protected ITreeBuilder treeBuilder(IQueryTree queryTree) {
        return new OtherTreeBuilder(queryTree);
    }

    @Override
    protected ITreeBuilder treeBuilder(IQueryTree queryTree, Class clazz) {
        return new OtherTreeBuilder(queryTree, clazz);
    }
}
