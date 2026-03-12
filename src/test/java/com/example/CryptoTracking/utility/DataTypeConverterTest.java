package com.example.CryptoTracking.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DataTypeConverterTest {

    private DataTypeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new DataTypeConverter();
    }

    // Big decimal testing
    @Test
    void convertToBigDecimal_WhenNull_ShouldReturnZero() {
        assertThat(converter.convertToBigDecimal(null)).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void convertToBigDecimal_WhenAlreadyBigDecimal_ShouldReturnSameInstance() {
        BigDecimal input = new BigDecimal("100.50");
        assertThat(converter.convertToBigDecimal(input)).isSameAs(input);
    }

    @Test
    void convertToBigDecimal_WhenValidString_ShouldReturnParsedValue() {
        assertThat(converter.convertToBigDecimal("100.50")).isEqualTo(new BigDecimal("100.50"));
        assertThat(converter.convertToBigDecimal(100)).isEqualTo(new BigDecimal("100"));
    }

    @Test
    void convertToBigDecimal_WhenInvalidString_ShouldReturnNull() {
        assertThat(converter.convertToBigDecimal("invalid")).isNull();
    }

    // Integer
    @Test
    void convertToInteger_WhenNull_ShouldReturnNull() {
        assertThat(converter.convertToInteger(null)).isNull();
    }

    @Test
    void convertToInteger_WhenAlreadyInteger_ShouldReturnSameValue() {
        assertThat(converter.convertToInteger(42)).isEqualTo(42);
    }

    @Test
    void convertToInteger_WhenValidString_ShouldReturnParsedValue() {
        assertThat(converter.convertToInteger("42")).isEqualTo(42);
    }

    @Test
    void convertToInteger_WhenInvalidString_ShouldReturnNull() {
        assertThat(converter.convertToInteger("invalid")).isNull();
        assertThat(converter.convertToInteger("42.5")).isNull(); // parseInt fails on decimals
    }

    // Long
    @Test
    void convertToLong_WhenNull_ShouldReturnNull() {
        assertThat(converter.convertToLong(null)).isNull();
    }

    @Test
    void convertToLong_WhenIsNumber_ShouldReturnLongValue() {
        assertThat(converter.convertToLong(42L)).isEqualTo(42L);
        assertThat(converter.convertToLong(42)).isEqualTo(42L); // Integer also passes instanceof Number
    }

    @Test
    void convertToLong_WhenValidString_ShouldReturnParsedValue() {
        assertThat(converter.convertToLong("1234567890")).isEqualTo(1234567890L);
    }

    @Test
    void convertToLong_WhenInvalidString_ShouldReturnNull() {
        assertThat(converter.convertToLong("invalid")).isNull();
    }

    // Double
    @Test
    void convertToDouble_WhenNull_ShouldReturnNull() {
        assertThat(converter.convertToDouble(null)).isNull();
    }

    @Test
    void convertToDouble_WhenIsNumber_ShouldReturnDoubleValue() {
        assertThat(converter.convertToDouble(42.5d)).isEqualTo(42.5d);
        assertThat(converter.convertToDouble(42)).isEqualTo(42.0d);
    }

    @Test
    void convertToDouble_WhenValidString_ShouldReturnParsedValue() {
        assertThat(converter.convertToDouble("42.5")).isEqualTo(42.5d);
    }

    @Test
    void convertToDouble_WhenInvalidString_ShouldReturnNull() {
        assertThat(converter.convertToDouble("invalid")).isNull();
    }


    // OffsetDateTime
    @Test
    void converToOffsetDateTime_WhenNull_ShouldReturnNull() {
        assertThat(converter.convertToOffsetDateTime(null)).isNull();
    }

    @Test
    void converToOffsetDateTime_WhenAlreadyOffsetDateTime_ShouldReturnSameInstance() {
        OffsetDateTime now = OffsetDateTime.now();
        assertThat(converter.convertToOffsetDateTime(now)).isSameAs(now);
    }

    @Test
    void converToOffsetDateTime_WhenValidString_ShouldReturnParsedValue() {
        String isoString = "2026-03-09T20:53:15+07:00";
        OffsetDateTime expected = OffsetDateTime.parse(isoString);
        assertThat(converter.convertToOffsetDateTime(isoString)).isEqualTo(expected);
    }

    @Test
    void converToOffsetDateTime_WhenInvalidString_ShouldReturnNull() {
        assertThat(converter.convertToOffsetDateTime("invalid-date")).isNull();
    }

    // Instant
    @Test
    void convertToInstant_WhenNull_ShouldReturnNull() {
        assertThat(converter.convertToInstant(null)).isNull();
    }

    @Test
    void convertToInstant_WhenAlreadyInstant_ShouldReturnSameInstance() {
        Instant now = Instant.now();
        assertThat(converter.convertToInstant(now)).isSameAs(now);
    }

    @Test
    void convertToInstant_WhenValidString_ShouldReturnParsedValue() {
        String isoString = "2026-03-09T13:53:15Z";
        Instant expected = Instant.parse(isoString);
        assertThat(converter.convertToInstant(isoString)).isEqualTo(expected);
    }

    @Test
    void convertToInstant_WhenInvalidString_ShouldReturnNull() {
        assertThat(converter.convertToInstant("invalid-instant")).isNull();
    }
}