package com.lzx.kaleido.spi.db;

import cn.hutool.core.lang.Assert;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.spi.db.model.ConnectionWrapper;
import com.lzx.kaleido.spi.db.utils.JdbcUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lwp
 * @date 2024-03-15
 **/
public final class ConnectionManager {
    
    private static final Map<String, Map<String, ConnectionWrapper>> activeConnection = new HashMap<>();
    
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
     * 移除
     *
     * @param connectionId
     * @param databaseName
     */
    public static void remove(final String connectionId, final String databaseName) {
        Assert.notNull(connectionId);
        Assert.notNull(databaseName);
        activeConnection.computeIfPresent(connectionId, (k, v) -> {
            final ConnectionWrapper connectionWrapper = v.get(databaseName);
            if (connectionWrapper != null) {
                JdbcUtil.closeConnection(connectionWrapper.getConnection());
                v.remove(databaseName);
            }
            return v.isEmpty() ? null : v;
        });
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
        Map<String, ConnectionWrapper> wrapperMap = null;
        if (!activeConnection.containsKey(connectionId) || !(wrapperMap = getConnectionWrapperMap(connectionId)).containsKey(
                databaseName)) {
            throw new CommonRuntimeException(ErrorCode.CONNECTION_IS_NULL);
        }
        return wrapperMap.get(databaseName);
    }
    
    /**
     * @param connectionId
     * @return
     */
    private Map<String, ConnectionWrapper> getConnectionWrapperMap(String connectionId) {
        return activeConnection.getOrDefault(connectionId, new HashMap<>());
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
