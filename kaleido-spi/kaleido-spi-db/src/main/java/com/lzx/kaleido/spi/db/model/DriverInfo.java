package com.lzx.kaleido.spi.db.model;

import lombok.Data;

import java.sql.Driver;

/**
 * @author lwp
 * @date 2023-11-10
 **/
@Data
public class DriverInfo {
    
    private Driver driver;
    
    private DriverProperties driverProperties;
    
    /**
     * @param driver
     * @param driverProperties
     * @return
     */
    public static DriverInfo of(Driver driver, DriverProperties driverProperties) {
        final DriverInfo driverInfo = new DriverInfo();
        driverInfo.setDriver(driver);
        driverInfo.setDriverProperties(driverProperties);
        return driverInfo;
    }
}
