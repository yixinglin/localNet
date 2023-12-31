package org.hsgt.core.config;

import org.hsgt.order.config.CarrierConfig;
import org.hsgt.order.config.MetroOrderConfig;
import org.hsgt.pricing.config.MetroPricingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Order(2)
public class StartUpComponent implements CommandLineRunner {
    Logger logger = Logger.loggerBuilder(StartUpComponent.class);

    @Autowired
    MetroOrderConfig metroOrderConfig;
    @Autowired
    CarrierConfig carrierConfig;
    @Autowired
    MetroPricingConfig metroPricingConfig;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("StartUpComponent");
        createDirectory(metroOrderConfig.getCachePath());
        createDirectory(metroOrderConfig.getPdfOutPath());
        File f = new File(metroOrderConfig.getTemplateDocFile());
        createDirectory(f.getParent());
        createDirectory(carrierConfig.getCachePath());
    }

    private Path createDirectory(String path) {
        Path p = Paths.get(path);
        Path ans = null;
        try {
            ans = Files.createDirectories(p);
        } catch (IOException e) {
            logger.error("StartUp:createDirectory:ComponentIOException");
        }
        return ans;
    }
}
