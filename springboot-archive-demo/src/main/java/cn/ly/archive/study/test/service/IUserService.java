package cn.ly.archive.study.test.service;


import cn.ly.archive.study.test.domain.User;
import cn.ly.archive.study.test.domain.req.UserListReq;
import cn.ly.archive.study.test.domain.vo.UserVo;
import cn.fasterTool.common.datasource.service.IBaseService;
import cn.fasterTool.common.web.domain.TableDataInfo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2023-04-30
 */
public interface IUserService extends IBaseService<User> {

    TableDataInfo<UserVo> searchPageInfo(UserListReq req);
}
