package cn.ly.archive.study.test.domain.req;

import cn.fasterTool.common.web.domain.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998">liu yun</a>
 * @classname UserListReq
 * @create 2023/5/3 21:27
 */
@Data
public class UserListReq extends PageReq {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "账号")
    private String account;


}
