package com.lzx.kaleido.spi.db.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * @author lwp
 * @date 2023-11-11
 **/
@Slf4j
@UtilityClass
public class JdbcJarUtil {
    
    public static final String PATH =
            System.getProperty("user.home") + File.separator + ".kaleido" + File.separator + "jdbc-lib" + File.separator;
    
    static {
        final File file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    
    
    /**
     * 下载路径
     *
     * @param downloadUrls
     * @param type
     * @param version
     * @return
     * @throws CommonException
     */
    public String getFullPath(List<String> downloadUrls, String type, String version) throws CommonException {
        final String path = appendFileDir(PATH, type, version);
        File file = new File(path);
        final boolean exists = file.exists();
        //判断是否已经存在
        if (exists && file.isDirectory()) {
            final File[] files = file.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar"));
            if (ArrayUtil.isNotEmpty(files)) {
                return files[0].getPath();
            }
        }
        file = sortDownLoad(downloadUrls, path);
        return file.getPath();
    }
    
    
    /**
     * 按照顺序下载
     *
     * @param downloadUrls
     * @param path
     */
    public File sortDownLoad(List<String> downloadUrls, String path) throws CommonException {
        File file = null;
        if (CollUtil.isNotEmpty(downloadUrls)) {
            for (final String downloadUrl : downloadUrls) {
                if (StrUtil.isBlank(downloadUrl)) {
                    continue;
                }
                try {
                    file = download(downloadUrl, path, -1);
                    if (file != null && file.exists()) {
                        log.info("下载驱动成功！{}", downloadUrl);
                        break;
                    }
                } catch (Exception ex) {
                    log.error("下载失败！地址【{}】", downloadUrl);
                    //下载文件存在了，判断一下并且删除
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                }
            }
        }
        if (file == null || !file.exists()) {
            throw new CommonException(ErrorCode.CONNECTION_JDBC_LOAD_FAILED);
        }
        return file;
    }
    
    /**
     * 下载
     *
     * @param url
     * @throws CommonException
     */
    public File download(String url, String path, int timeout) {
        File destFile = new File(appendFileDir(path, getDriverName(url)));
        if (destFile.exists()) {
            destFile.delete();
        }
        return HttpUtil.downloadFileFromUrl(url, destFile, timeout);
    }
    
    /**
     * @param url
     * @return
     */
    public String getDriverName(String url) {
        return StrUtil.isNotBlank(url) ? url.substring(url.lastIndexOf("/") + 1) : "";
    }
    
    /**
     * @param paths
     * @return
     */
    private String appendFileDir(String... paths) {
        final StringBuilder sb = new StringBuilder();
        int size;
        if (paths != null && (size = paths.length) > 0) {
            for (int i = 0; i < size; i++) {
                final String path = paths[i];
                if (StrUtil.isNotBlank(path)) {
                    sb.append(path);
                    if (i != size - 1 || !path.endsWith(File.separator)) {
                        sb.append(File.separator);
                    }
                }
            }
        }
        return sb.toString();
    }
    
}
