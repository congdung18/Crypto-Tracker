-- Drop tables if they exist to ensure a clean slate
DROP TABLE IF EXISTS coins;
DROP TABLE IF EXISTS global_market_data;

-- Create coins table
CREATE TABLE coins (
   id VARCHAR(255) PRIMARY KEY,
   symbol VARCHAR(20) NOT NULL,
   name VARCHAR(255) NOT NULL,
   image TEXT,
   current_price NUMERIC(19, 8),
   market_cap BIGINT,
   market_cap_rank INTEGER,
   total_volume NUMERIC(19, 2),
   high24h NUMERIC(19, 8),
   low24h NUMERIC(19, 8),
   price_change_percentage24h DOUBLE PRECISION,
   price_change_percentage7d DOUBLE PRECISION,
   price_change_percentage1h DOUBLE PRECISION,
   last_updated TIMESTAMP WITH TIME ZONE
);

-- Create global market data table
CREATE TABLE global_market_data (
    id BIGSERIAL PRIMARY KEY,
    active_cryptocurrencies INTEGER,
    markets INTEGER,
    total_market_cap_usd NUMERIC(30, 2),
    total_volume_usd NUMERIC(30, 2),
    market_cap_change_percentage_24h_usd DOUBLE PRECISION,
    btc_dominance DOUBLE PRECISION,
    updated_at TIMESTAMP WITH TIME ZONE
);