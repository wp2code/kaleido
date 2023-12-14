package com.lzx.kaleido.spi.db.sql;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.spi.db.model.DriverInfo;
import com.lzx.kaleido.spi.db.model.DriverProperties;
import com.lzx.kaleido.spi.db.utils.JdbcJarUtil;
import com.lzx.kaleido.spi.db.utils.JdbcUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lwp
 * @date 2023-11-10
 **/
public class DriverManager {
    
    /**
     *
     */
    private static final Map<String, DriverInfo> DRIVER_ENTRY_MAP = new ConcurrentHashMap();
    
    /**
     *
     */
    private static final Map<String, ClassLoader> CLASS_LOADER_MAP = new ConcurrentHashMap();
    
    /**
     * @param url
     * @param userName
     * @param password
     * @param driver
     * @return
     * @throws CommonException
     */
    public static Connection getConnection(String url, String userName, String password, DriverProperties driver) throws CommonException {
        final Properties info = new Properties();
        if (StrUtil.isNotBlank(userName)) {
            info.setProperty("user", userName);
        }
        if (StrUtil.isNotBlank(password)) {
            info.setProperty("password", password);
        }
        return getConnection(url, info, driver);
    }
    
    /**
     * @param connection
     * @throws SQLException
     */
    public static void close(Connection connection) {
        JdbcUtil.closeConnection(connection);
    }
    
    /**
     * @param url
     * @param info
     * @param driverProperties
     * @return
     * @throws CommonException
     */
    public static Connection getConnection(String url, Properties info, DriverProperties driverProperties) throws CommonException {
        DriverInfo driverInfo = DRIVER_ENTRY_MAP.get(driverProperties.getUniqueId());
        if (driverInfo == null) {
            driverInfo = getJDBCDriver(driverProperties);
        }
        Connection connection;
        try {
            connection = driverInfo.getDriver().connect(url, info);
            return connection;
        } catch (SQLException sqlException) {
            //TODO 再试一次
            return null;
        }
    }
    
    /**
     * @param driverProperties
     * @return
     * @throws CommonException
     */
    public static DriverInfo getJDBCDriver(DriverProperties driverProperties) throws CommonException {
        if (DRIVER_ENTRY_MAP.containsKey(driverProperties.getUniqueId())) {
            return DRIVER_ENTRY_MAP.get(driverProperties.getUniqueId());
        }
        synchronized (driverProperties) {
            try {
                ClassLoader classLoader = getClassLoader(driverProperties);
                final Class<?> cls = classLoader.loadClass(driverProperties.getJdbcDriverClass());
                final Driver driver = (Driver) ReflectUtil.newInstance(cls);
                final DriverInfo driverInfo = DriverInfo.of(driver, driverProperties);
                DRIVER_ENTRY_MAP.put(driverProperties.getUniqueId(), driverInfo);
                return driverInfo;
            } catch (Exception e) {
                if (e instanceof CommonException ex) {
                    throw ex;
                }
                throw new CommonException(ErrorCode.CONNECTION_JDBC_LOAD_FAILED, e);
            }
        }
        
    }
    
    /**
     *
     */
    public static void clear() {
        CLASS_LOADER_MAP.clear();
        DRIVER_ENTRY_MAP.clear();
    }
    
    /**
     * @param driverProperties
     * @return
     * @throws CommonException
     */
    public static ClassLoader getClassLoader(DriverProperties driverProperties) throws CommonException {
        final String uniqueId = driverProperties.getUniqueId();
        if (CLASS_LOADER_MAP.containsKey(uniqueId)) {
            return CLASS_LOADER_MAP.get(uniqueId);
        } else {
            synchronized (uniqueId) {
                if (CLASS_LOADER_MAP.containsKey(uniqueId)) {
                    return CLASS_LOADER_MAP.get(uniqueId);
                }
                URLClassLoader classLoader;
                try {
                    final URL[] urls = new URL[1];
                    File driverFile = new File(JdbcJarUtil.getFullPath(driverProperties.getDownloadUrls(), driverProperties.getType(),
                            driverProperties.getVersion()));
                    urls[0] = driverFile.toURI().toURL();
                    classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
                    classLoader.loadClass(driverProperties.getJdbcDriverClass());
                } catch (Exception e) {
                    throw new CommonException(ErrorCode.CONNECTION_JDBC_LOAD_FAILED, e);
                }
                CLASS_LOADER_MAP.put(uniqueId, classLoader);
                return classLoader;
            }
        }
    }
}
