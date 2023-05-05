package cn.ly.archive.study.test.domain;

import cn.ly.common.datasource.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 *
 * </p>
 *
 * @author author
 * @since 2023-04-30
 */
@Data
@Table(tableName = "db_user")
public class User implements Serializable {

    @Id
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "添加时间")
    private Timestamp addTime;

    @ApiModelProperty(value = "上次修改时间")
    private Timestamp lastModifyTime;

    @ApiModelProperty(value = "添加用户")
    private Long addUser;

    @ApiModelProperty(value = "上次修改用户")
    private Long lastModifyUser;


}
