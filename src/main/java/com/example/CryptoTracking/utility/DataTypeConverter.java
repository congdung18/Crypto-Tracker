package com.example.CryptoTracking.ultility;

import java.math.*;

// Ultility for converting data types
public class DataTypeConverter {
    private BigDecimal convertToBigDecimal(Object o){
        // Null case
        if (o == null){
            // Return Zero so it won't block the main arithmetic logic
            return BigDecimal.ZERO;
        }

        // Avoid converting BigDecimal to String
        if (o instanceof BigDecimal){
            return (BigDecimal) o;
        }

        return new BigDecimal(o.toString());
    }

    private Integer convertToInteger(Object o){
        if (o == null){
            return null;
        }

        if (o instanceof Integer){
            return 
        }
    }
}
