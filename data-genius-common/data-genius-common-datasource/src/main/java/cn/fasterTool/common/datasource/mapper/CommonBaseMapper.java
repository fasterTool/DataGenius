package cn.fasterTool.common.datasource.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998"> liu yun</a>
 * @date 2023/3/27 10:18
 * @Description:
 */
@Mapper
public interface CommonBaseMapper {

    public int insertMethod(String sql);

    public int delete(String sql);

    public int updateMethod(String sql);

    public Map findById(String sql);

    public int count(String sql);

    public List<Map> query(String sql);

}
