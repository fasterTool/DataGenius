package cn.ly.archive.study.test.service.impl;

import cn.ly.archive.study.test.domain.User;
import cn.ly.archive.study.test.domain.req.UserListReq;
import cn.ly.archive.study.test.domain.vo.UserVo;
import cn.ly.archive.study.test.service.IUserService;
import cn.fasterTool.common.datasource.service.impl.AbstractBaseServiceImpl;
import cn.fasterTool.common.datasource.service.query.IQueryTree;
import cn.fasterTool.common.datasource.service.query.ParamCondition;
import cn.fasterTool.common.datasource.service.query.QueryCondition;
import cn.fasterTool.common.datasource.service.query.QueryTree;
import cn.fasterTool.common.web.domain.TableDataInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-04-30
 */
@Service
public class UserServiceImpl extends AbstractBaseServiceImpl<User> implements IUserService {

    @Override
    public TableDataInfo<UserVo> searchPageInfo(UserListReq req) {


        List<ParamCondition> conditionList = new ArrayList<>();
        if (StringUtils.isNotBlank(req.getName())) {
            conditionList.add(ParamCondition.where("name").like(req.getName()));
        }
        if (StringUtils.isNotBlank(req.getAccount())) {
            conditionList.add(ParamCondition.where("account").is(req.getAccount()));
        }
        if (StringUtils.isNotBlank(req.getId())) {
            conditionList.add(ParamCondition.where("id").is(req.getId()));
        }

        IQueryTree queryTree = new QueryTree<User>()
                .query(QueryCondition.and(conditionList))
                .page(req.getPageNo(), req.getPageSize());

        return searchPageInfo(queryTree, UserVo.class);
    }
}
