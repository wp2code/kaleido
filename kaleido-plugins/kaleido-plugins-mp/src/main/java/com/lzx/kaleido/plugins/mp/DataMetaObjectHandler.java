package com.lzx.kaleido.plugins.mp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lzx.kaleido.infra.base.constant.Constants;
import com.lzx.kaleido.infra.base.utils.ContextAuthUtil;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author lwp
 * @date 2023-11-25
 **/
public class DataMetaObjectHandler implements MetaObjectHandler {
    
    /**
     * 创建时间
     */
    public static final String CREATE_TIME = "createTime";
    
    /**
     * 更新时间
     */
    public static final String UPDATE_TIME = "updateTime";
    
    
    /**
     * 创建人ID
     */
    public static final String CREATOR_ID = "creatorId";
    
    /**
     * 删除标识
     */
    public static final String DELETED = "deleted";
    
    @Override
    public void insertFill(final MetaObject metaObject) {
        Object createTime = getFieldValByName(CREATE_TIME, metaObject);
        Object updateTime = getFieldValByName(UPDATE_TIME, metaObject);
        Object creatorId = getFieldValByName(CREATOR_ID, metaObject);
        Object deleted = getFieldValByName(DELETED, metaObject);
        final LocalDateTime date = LocalDateTime.now();
        if (createTime == null) {
            setFieldValByName(CREATE_TIME, date, metaObject);
        }
        if (updateTime == null) {
            setFieldValByName(UPDATE_TIME, date, metaObject);
        }
        if (creatorId == null) {
            if (metaObject.getGetterType(CREATOR_ID).equals(Long.class)) {
                setFieldValByName(CREATOR_ID, Long.valueOf(ContextAuthUtil.getUserIdIfAbsent(Constants.DEFAULT_USER_ID)), metaObject);
            } else {
                setFieldValByName(CREATOR_ID, ContextAuthUtil.getUserIdIfAbsent(Constants.DEFAULT_USER_ID), metaObject);
            }
        }
        if (deleted == null) {
            setFieldValByName(DELETED, 0, metaObject);
        }
    }
    
    @Override
    public void updateFill(final MetaObject metaObject) {
        setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);
    }
}
