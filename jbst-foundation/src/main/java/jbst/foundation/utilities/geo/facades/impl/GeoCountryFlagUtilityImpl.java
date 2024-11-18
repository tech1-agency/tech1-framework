package jbst.foundation.utilities.geo.facades.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.enums.Toggle;
import jbst.foundation.domain.geo.GeoCountryFlag;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.utilities.geo.facades.GeoCountryFlagUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static jbst.foundation.domain.enums.Status.FAILURE;
import static jbst.foundation.domain.enums.Status.SUCCESS;

@Slf4j
public class GeoCountryFlagUtilityImpl implements GeoCountryFlagUtility {
    private static final String COUNTRIES_FLAGS_JSON = "geo-countries-flags.json";

    private final Map<String, GeoCountryFlag> mappedByCountryName;
    private final Map<String, GeoCountryFlag> mappedByCountryCode;

    // Properties
    private final JbstProperties jbstProperties;

    public GeoCountryFlagUtilityImpl(
            ResourceLoader resourceLoader,
            JbstProperties jbstProperties
    ) {
        this.jbstProperties = jbstProperties;
        LOGGER.info(JbstConstants.Symbols.LINE_SEPARATOR_INTERPUNCT);
        var geoCountryFlagsConfigs = this.jbstProperties.getUtilitiesConfigs().getGeoCountryFlagsConfigs();
        LOGGER.info("{} Geo country flags {} json — {}", JbstConstants.Logs.FRAMEWORK_UTILITIES_PREFIX, COUNTRIES_FLAGS_JSON, Toggle.of(geoCountryFlagsConfigs.isEnabled()));
        if (geoCountryFlagsConfigs.isEnabled()) {
            try {
                var resource = resourceLoader.getResource("classpath:" + COUNTRIES_FLAGS_JSON);
                var typeReference = new TypeReference<List<GeoCountryFlag>>() {};
                var objectMapper = new ObjectMapper();
                var geoCountryFlags = objectMapper.readValue(resource.getInputStream(), typeReference);
                this.mappedByCountryName = geoCountryFlags.stream()
                        .collect(
                                Collectors.toMap(
                                        item -> item.name().toLowerCase(),
                                        Function.identity()
                                )
                        );
                this.mappedByCountryCode = geoCountryFlags.stream()
                        .collect(
                                Collectors.toMap(
                                        item -> item.code().toLowerCase(),
                                        Function.identity()
                                )
                        );
                LOGGER.info("{} Geo country flags {} json configuration status: {}", JbstConstants.Logs.FRAMEWORK_UTILITIES_PREFIX, COUNTRIES_FLAGS_JSON, SUCCESS);
            } catch (IOException | RuntimeException ex) {
                LOGGER.error("%s Geo country flags %s json configuration status: %s".formatted(JbstConstants.Logs.FRAMEWORK_UTILITIES_PREFIX, COUNTRIES_FLAGS_JSON, FAILURE));
                LOGGER.error("Please verify `{}` is present in classpath", COUNTRIES_FLAGS_JSON);
                throw new IllegalArgumentException(ex.getMessage());
            }
        } else {
            this.mappedByCountryName = new HashMap<>();
            this.mappedByCountryCode = new HashMap<>();
        }
        LOGGER.info(JbstConstants.Symbols.LINE_SEPARATOR_INTERPUNCT);
    }

    @Override
    public String getFlagEmojiByCountry(String country) {
        return this.getEmoji(
                this.mappedByCountryName,
                country
        );
    }

    @Override
    public String getFlagEmojiByCountryCode(String countryCode) {
        return this.getEmoji(
                this.mappedByCountryCode,
                countryCode
        );
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private String getEmoji(Map<String, GeoCountryFlag> mappedBy, String searchKey) {
        if (!this.jbstProperties.getUtilitiesConfigs().getGeoCountryFlagsConfigs().isEnabled()) {
            return GeoCountryFlag.unknown().emoji();
        }
        if (isNull(searchKey)) {
            searchKey = JbstConstants.Strings.UNKNOWN.toLowerCase();
        }
        return mappedBy.getOrDefault(searchKey.toLowerCase(), GeoCountryFlag.unknown()).emoji();
    }
}
