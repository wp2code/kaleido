package com.lzx.kaleido.spi.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.spi.db.model.ConnectionWrapper;
import com.lzx.kaleido.spi.db.model.DBConfig;
import com.lzx.kaleido.spi.db.utils.JdbcUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lwp
 * @date 2024-03-15
 **/
public final class ConnectionManager {
    
    private final Map<String, Map<String, ConnectionWrapper>> activeConnection = new HashMap<>();
    
    /**
     * 注册
     *
     * @param connectionWrapper
     */
    public void register(ConnectionWrapper connectionWrapper) {
        Assert.notNull(connectionWrapper);
        Assert.notNull(connectionWrapper.getId());
        Assert.notNull(connectionWrapper.getDatabaseName());
        if (activeConnection.containsKey(connectionWrapper.getId())) {
            final Map<String, ConnectionWrapper> connectionWrapperMap = getConnectionWrapperMap(connectionWrapper.getId());
            final ConnectionWrapper oldConnectionWrapper = connectionWrapperMap.get(connectionWrapper.getDatabaseName());
            if (oldConnectionWrapper != null) {
                JdbcUtil.closeConnection(oldConnectionWrapper.getConnection());
            }
            connectionWrapperMap.put(connectionWrapper.getDatabaseName(), connectionWrapper);
        } else {
            activeConnection.put(connectionWrapper.getId(), connectionWrapper.toMap());
        }
    }
    
    /**
     * 移除所有连接
     */
    public void removeAll() {
        if (activeConnection.size() > 0) {
            activeConnection.forEach((k, v) -> {
                v.forEach((kk, vv) -> JdbcUtil.closeConnection(vv.getConnection()));
            });
            activeConnection.clear();
        }
    }
    
    /**
     * @param connectionId
     * @param databaseName
     */
    public void remove(final String connectionId, final String databaseName) {
        Assert.notNull(connectionId);
        boolean isRemove = false;
        for (final Map.Entry<String, Map<String, ConnectionWrapper>> entry : activeConnection.entrySet()) {
            if (StrUtil.equals(entry.getKey(), connectionId)) {
                Map<String, ConnectionWrapper> value = entry.getValue();
                if (StrUtil.isBlank(databaseName)) {
                    value.forEach((kk, vv) -> JdbcUtil.closeConnection(vv.getConnection()));
                    isRemove = true;
                } else {
                    final ConnectionWrapper connectionWrapper = value.get(databaseName);
                    if (connectionWrapper != null) {
                        JdbcUtil.closeConnection(connectionWrapper.getConnection());
                        value.remove(databaseName);
                    }
                    if (value.isEmpty()) {
                        isRemove = true;
                    }
                }
            }
        }
        if (isRemove) {
            activeConnection.remove(connectionId);
        }
    }
    
    /**
     * @param connectionId
     */
    public void removeOther(final String connectionId) {
        Assert.notNull(connectionId);
        final List<String> removeKeys = new ArrayList<>();
        for (final Map.Entry<String, Map<String, ConnectionWrapper>> entry : activeConnection.entrySet()) {
            if (!StrUtil.equals(entry.getKey(), connectionId)) {
                entry.getValue().forEach((kk, vv) -> JdbcUtil.closeConnection(vv.getConnection()));
                removeKeys.add(entry.getKey());
            }
        }
        if (CollUtil.isNotEmpty(removeKeys)) {
            removeKeys.forEach(activeConnection::remove);
        }
    }
    
    /**
     * @param connectionId
     * @param databaseName
     * @return
     */
    public ConnectionWrapper getConnection(final String connectionId, final String databaseName) {
        Assert.notNull(connectionId);
        Assert.notNull(databaseName);
        return getConnectionWrapperMap(connectionId).get(databaseName);
    }
    
    /**
     * @param connectionId
     * @param databaseName
     * @return
     */
    public ConnectionWrapper getConnectionThrowException(final String connectionId, final String databaseName) {
        Assert.notNull(connectionId);
        Assert.notNull(databaseName);
        if (activeConnection.containsKey(connectionId)) {
            final ConnectionWrapper connectionWrapper = getConnectionWrapper(connectionId, databaseName);
            if (connectionWrapper != null) {
                return connectionWrapper;
            }
        }
        throw new CommonRuntimeException(ErrorCode.CONNECTION_IS_NULL);
    }
    
    /**
     * @param connectionId
     * @return
     */
    private Map<String, ConnectionWrapper> getConnectionWrapperMap(String connectionId) {
        return activeConnection.getOrDefault(connectionId, new HashMap<>());
    }
    
    /**
     * @param connectionId
     * @param databaseName
     * @return
     */
    public ConnectionWrapper getConnectionWrapper(String connectionId, String databaseName) {
        final Map<String, ConnectionWrapper> connectionWrapperMap = getConnectionWrapperMap(connectionId);
        if (connectionWrapperMap != null) {
            for (final Map.Entry<String, ConnectionWrapper> entry : connectionWrapperMap.entrySet()) {
                final ConnectionWrapper wrapper = entry.getValue();
                final DBConfig dbConfig = wrapper.getPropertiesConfig();
                if (dbConfig.isSupportSchema()) {
                    if (entry.getKey().equals(databaseName)) {
                        return wrapper;
                    }
                } else {
                    return wrapper;
                }
            }
        }
        return null;
    }
    
    private static class StaticHolder {
        
        private final static ConnectionManager INSTANCE = new ConnectionManager();
    }
    
    public static ConnectionManager getInstance() {
        return ConnectionManager.StaticHolder.INSTANCE;
    }
    
    private ConnectionManager() {
    }
    
}
