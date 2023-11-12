package com.limen2023.lamb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limen2023.lamb.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hulakimir
 * @since 2023-09-23
 */
@Component
public interface UserMapper extends BaseMapper<User> {

    /**
     * 删除用户角色关系
     * @param userId
     * @return
     */
    @Delete("delete from sys_user_role where user_id=#{userId}")
    int deleteUserRole(Long userId);

    /**
     * 保存用户角色关系
     * @param userId
     * @param roleIds
     * @return
     */
    int saveUserRole(Long userId, List<Long> roleIds);
}
