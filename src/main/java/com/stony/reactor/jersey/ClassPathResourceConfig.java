package com.stony.reactor.jersey;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.core.ScanningResourceConfig;
import com.sun.jersey.core.spi.scanning.PackageNamesScanner;

import java.util.Collection;

/**
 * <p>reactor-netty-ext
 * <p>com.stony.reactor.jersey
 *
 * @author stony
 * @version 下午7:03
 * @since 2018/1/19
 */
public class ClassPathResourceConfig extends ScanningResourceConfig {
    final String pkgNamesStr;

    /**
     * 分隔符 ,;
     * @param pkgNamesStr
     */
    public ClassPathResourceConfig(String pkgNamesStr) {
        this.pkgNamesStr = pkgNamesStr;
        String[] pkgNames = getElements(new String[]{pkgNamesStr}, ResourceConfig.COMMON_DELIMITERS);
        init(new PackageNamesScanner(pkgNames));
    }
    public void addValueProviderClass(Collection<Class<?>> providers) {
        getClasses().addAll(providers);
    }
}