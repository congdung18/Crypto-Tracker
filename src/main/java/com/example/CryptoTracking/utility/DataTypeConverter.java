package com.example.CryptoTracking.utility;

import org.springframework.stereotype.Component;

import java.math.*;
import java.time.Instant;
import java.time.OffsetDateTime;

@Component
// Utility for converting data types
public class DataTypeConverter {
    public BigDecimal convertToBigDecimal(Object o){
        // Null case
        if (o == null){
            // Return Zero so it won't block the main arithmetic logic
            return BigDecimal.ZERO;
        }

        // Avoid converting BigDecimal to String
        if (o instanceof BigDecimal){
            return (BigDecimal) o;
        }

        try{
            return new BigDecimal(o.toString());
        } catch(NumberFormatException nfe) {
            return null;
        }
    }

    public Integer convertToInteger(Object o){
        if (o == null){
            return null;
        }

        if (o instanceof Integer){
            return ((Number) o).intValue();
        }

        // Handle not valid Integer types
        try{
            return Integer.parseInt(o.toString());
        } catch(NumberFormatException numberFormatException){
            return null;
        }
    }

    public Long convertToLong(Object o){
        if (o == null){
            return null;
        }

        if (o instanceof Number){
            return ((Number) o).longValue();
        }

        try{
            return Long.parseLong(o.toString());
        } catch (NumberFormatException numberFormatException){
            return null;
        }
    }

    public Double convertToDouble(Object o){
        if (o == null){
            return null;
        }

        if (o instanceof Number){
            return ((Number) o).doubleValue();
        }

        try{
            return Double.parseDouble(o.toString());
        }catch (NumberFormatException numberFormatException){
            return null;
        }
    }

    public OffsetDateTime converToOffsetDateTime(Object o){
        if (o == null){
            return null;
        }

        if (o instanceof OffsetDateTime){
            return (OffsetDateTime) o;
        }

        try{
            return OffsetDateTime.parse(o.toString());
        } catch (Exception exception){
            return null;
        }
    }

    public Instant convertToInstant(Object o){
        if (o == null){
            return null;
        }

        if (o instanceof Instant){
            return (Instant) o;
        }

        try{
            return Instant.parse(o.toString());
        } catch(Exception exception){
            return null;
        }
    }
}
