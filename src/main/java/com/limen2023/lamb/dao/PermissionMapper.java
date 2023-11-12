package com.limen2023.lamb.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limen2023.lamb.entity.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
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
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据用户ID查询权限列表
     * @param userId
     * @return
     */
    List<Permission> findPermissionListByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询权限列表
     * @param roleId
     * @return
     */
    List<Permission> findPermissionListByRoleId(Long roleId);

    @Delete("delete from sys_permission where id = #{id}")
    void delById(Long id);
}
