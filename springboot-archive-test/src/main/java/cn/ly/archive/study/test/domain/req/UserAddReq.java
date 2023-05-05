package cn.ly.archive.study.test.domain.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998">liu yun</a>
 * @classname UserAddReq
 * @create 2023/4/30 12:24
 */
@Data
public class UserAddReq {

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String name;

    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String account;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
