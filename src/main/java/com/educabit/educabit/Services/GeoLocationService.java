package com.educabit.educabit.Services;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;

@Service
public class GeoLocationService {

    private static final Logger logger = LoggerFactory.getLogger(GeoLocationService.class);

    private DatabaseReader dbReader;

    @Value("classpath:GeoLite2-City.mmdb")
    private Resource geoLiteDb;

    @PostConstruct
    public void init() {
        try {
            // Check if file exists to avoid startup crash if user hasn't downloaded it yet
            if (geoLiteDb.exists()) {
                dbReader = new DatabaseReader.Builder(geoLiteDb.getInputStream()).build();
            } else {
                logger.warn("GeoLite2-City.mmdb not found in classpath. GeoIP features will be disabled.");
            }
        } catch (IOException e) {
            logger.error("Erro ao carregar banco GeoIP", e);
        }
    }

    public String getUserLocation(String ipAddress) {
        if (dbReader == null)
            return "GeoIP Unavailable";

        try {
            InetAddress ipAddressNode = InetAddress.getByName(ipAddress);
            CityResponse response = dbReader.city(ipAddressNode);

            String cidade = (response.getCity() != null && response.getCity().getName() != null)
                    ? response.getCity().getName()
                    : "Unknown City";
            String estado = (response.getMostSpecificSubdivision() != null
                    && response.getMostSpecificSubdivision().getName() != null)
                            ? response.getMostSpecificSubdivision().getName()
                            : "Unknown State";

            return cidade + ", " + estado;
        } catch (IOException | GeoIp2Exception e) {
            // IP might be reserved (localhost) or not found
            return "Location Unknown";
        }
    }
}
