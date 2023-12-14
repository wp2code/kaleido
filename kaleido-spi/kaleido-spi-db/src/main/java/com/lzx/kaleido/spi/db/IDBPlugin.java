package com.lzx.kaleido.spi.db;

import com.lzx.kaleido.infra.base.annotations.SpiSingleton;
import com.lzx.kaleido.spi.db.model.DBConfig;

/**
 * @author lwp
 * @date 2023-11-16
 **/
@SpiSingleton
public interface IDBPlugin {
    
    /**
     * @return
     */
    String getDbType();
    
    /**
     * @return
     */
    DBConfig getDBConfig();
    
    
    /**
     * @return
     */
    IMetaData getMetaData();
    
    
    /**
     * @return
     */
    IDBManager getDBManager();
}
