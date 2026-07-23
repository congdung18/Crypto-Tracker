import { useState, useEffect, useCallback } from 'react';
import { 
  TrendingUp, 
  TrendingDown, 
  Search, 
  RotateCw, 
  Coins, 
  X, 
  ChevronLeft, 
  ChevronRight, 
  Globe, 
  Info,
  AlertTriangle 
} from 'lucide-react';

// API Base URL config: uses proxy in production or localhost during local dev
const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8088';

interface GlobalMarketData {
  activeCryptocurrencies: number;
  markets: number;
  totalMarketCap: number;
  totalVolume: number;
}

interface CoinSummary {
  id: string;
  symbol: string;
  name: string;
  image: string;
  price: number;
  marketCapRank: number;
  marketCap: number;
  totalVolume: number;
  high24h: number;
  low24h: number;
  priceChangePercentage24h: number;
  circulatingSupply: number;
  totalSupply: number;
  maxSupply: number;
  fullyDilutedValuation: number;
  lastUpdated: string;
}

interface CoinPriceHistory {
  id: number;
  coinId: string;
  price: number;
  timestamp: string;
}

interface Ticker {
  base: string;
  target: string;
  market: { name: string; identifier: string };
  last: number;
  volume: number;
  spread: number;
}

function App() {
  // Global Market State
  const [globalData, setGlobalData] = useState<GlobalMarketData | null>(null);
  const [loadingGlobal, setLoadingGlobal] = useState(true);

  // Coins Table State
  const [coins, setCoins] = useState<CoinSummary[]>([]);
  const [loadingCoins, setLoadingCoins] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isOnline, setIsOnline] = useState(true);

  // Pagination State (Spring Boot uses 0-based indexing for pages)
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // Filters State
  const [searchName, setSearchName] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [minRank, setMinRank] = useState('');
  const [maxRank, setMaxRank] = useState('');

  // Details Modal State
  const [selectedCoin, setSelectedCoin] = useState<CoinSummary | null>(null);
  const [priceHistory, setPriceHistory] = useState<CoinPriceHistory[]>([]);
  const [loadingHistory, setLoadingHistory] = useState(false);
  const [tickers, setTickers] = useState<Ticker[]>([]);
  const [loadingTickers, setLoadingTickers] = useState(false);
  const [selectedCurrency, setSelectedCurrency] = useState<string>('USD');
  const [marketChart, setMarketChart] = useState<[number, number][]>([]);
  const [chartTab, setChartTab] = useState<'live' | 'local'>('live');
  const [loadingLiveChart, setLoadingLiveChart] = useState(false);

  // Fetch Global Stats
  const fetchGlobalData = useCallback(async () => {
    try {
      setLoadingGlobal(true);
      const res = await fetch(`${API_BASE}/api/v1/global`);
      if (!res.ok) throw new Error('Failed to fetch global market data');
      const data: GlobalMarketData = await res.json();
      setGlobalData(data);
      setIsOnline(true);
    } catch (err) {
      console.error(err);
      setIsOnline(false);
    } finally {
      setLoadingGlobal(false);
    }
  }, []);

  // Fetch Coins List
  const fetchCoins = useCallback(async () => {
    try {
      setLoadingCoins(true);
      setError(null);

      // Build query parameters dynamically to avoid querying empty strings
      const params = new URLSearchParams();
      params.append('page', page.toString());
      params.append('size', size.toString());
      
      // Default sorting: sort by rank ascending
      params.append('sort', 'marketCapRank,asc');

      if (searchName.trim()) params.append('name', searchName.trim());
      if (minPrice.trim()) params.append('minPrice', minPrice.trim());
      if (maxPrice.trim()) params.append('maxPrice', maxPrice.trim());
      if (minRank.trim()) params.append('minMarketCapRank', minRank.trim());
      if (maxRank.trim()) params.append('maxMarketCapRank', maxRank.trim());

      const res = await fetch(`${API_BASE}/api/v1/coins?${params.toString()}`);
      if (!res.ok) throw new Error('Failed to fetch coins list');
      
      const data = await res.json();
      setCoins(data.content || []);
      setTotalPages(data.totalPages || 0);
      setTotalElements(data.totalElements || 0);
      setIsOnline(true);
    } catch (err: any) {
      setError(err.message || 'Something went wrong while fetching coins.');
      setIsOnline(false);
    } finally {
      setLoadingCoins(false);
    }
  }, [page, size, searchName, minPrice, maxPrice, minRank, maxRank]);

  // Fetch Individual Coin Details
  const viewCoinDetails = async (coinId: string) => {
    try {
      const res = await fetch(`${API_BASE}/api/v1/coins/${coinId}`);
      if (!res.ok) throw new Error('Could not fetch coin details');
      const data: CoinSummary = await res.json();
      setSelectedCoin(data);
      setChartTab('live'); // Default to live 7D chart for instant rich detail!

      // Fetch live price history from CoinGecko
      setLoadingLiveChart(true);
      try {
        const liveChartRes = await fetch(`${API_BASE}/api/v1/coins/${coinId}/chart?days=7`);
        if (liveChartRes.ok) {
          const liveChartData = await liveChartRes.json();
          setMarketChart(liveChartData.prices || []);
        } else {
          setMarketChart([]);
        }
      } catch (lcErr) {
        console.error('Error fetching live chart:', lcErr);
        setMarketChart([]);
      } finally {
        setLoadingLiveChart(false);
      }

      // Fetch local price history
      setLoadingHistory(true);
      try {
        const historyRes = await fetch(`${API_BASE}/api/v1/coins/${coinId}/history`);
        if (historyRes.ok) {
          const historyData = await historyRes.json();
          setPriceHistory(historyData);
        } else {
          setPriceHistory([]);
        }
      } catch (hErr) {
        console.error('Error fetching price history:', hErr);
        setPriceHistory([]);
      } finally {
        setLoadingHistory(false);
      }

      // Fetch tickers
      setLoadingTickers(true);
      try {
        const tickersRes = await fetch(`${API_BASE}/api/v1/coins/${coinId}/tickers`);
        if (tickersRes.ok) {
          const tickersData = await tickersRes.json();
          setTickers(tickersData.tickers || []);
        } else {
          setTickers([]);
        }
      } catch (tErr) {
        console.error('Error fetching tickers:', tErr);
        setTickers([]);
      } finally {
        setLoadingTickers(false);
      }
    } catch (err) {
      console.error(err);
      alert('Error fetching coin details');
    }
  };

  // Initial and reactive data loading
  useEffect(() => {
    fetchGlobalData();
  }, [fetchGlobalData]);

  useEffect(() => {
    // Reset page to 0 if size/filters change to prevent page overflow
    fetchCoins();
  }, [fetchCoins]);

  // Reset Filters trigger
  const handleResetFilters = () => {
    setSearchName('');
    setMinPrice('');
    setMaxPrice('');
    setMinRank('');
    setMaxRank('');
    setPage(0);
  };

  // Helper formatter for currencies
  const formatCurrency = (val: number | null) => {
    if (val === null || val === undefined) return '$0.00';
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: val >= 1 ? 2 : 6,
      maximumFractionDigits: val >= 1 ? 2 : 8
    }).format(val);
  };

  // Helper formatter for compact currencies
  const formatCompactCurrency = (val: number | null) => {
    if (val === null || val === undefined) return '$0.00';
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      notation: 'compact',
      maximumFractionDigits: 2
    }).format(val);
  };

  // Currency configurations for conversion
  const btcPrice = coins.find(c => c.id === 'bitcoin')?.price || selectedCoin?.price || 65000;
  const currencies: Record<string, { code: string; symbol: string; rate: number; position: 'prefix' | 'suffix'; decimals: number }> = {
    USD: { code: 'USD', symbol: '$', rate: 1, position: 'prefix', decimals: 2 },
    VND: { code: 'VND', symbol: ' ₫', rate: 25400, position: 'suffix', decimals: 0 },
    EUR: { code: 'EUR', symbol: '€', rate: 0.92, position: 'prefix', decimals: 2 },
    JPY: { code: 'JPY', symbol: '¥', rate: 155, position: 'prefix', decimals: 0 },
    BTC: { code: 'BTC', symbol: '₿ ', rate: 1 / btcPrice, position: 'prefix', decimals: 6 },
  };

  const formatValInCurrency = (val: number | null, currCode: string) => {
    if (val === null || val === undefined) return 'N/A';
    const cfg = currencies[currCode] || currencies.USD;
    const converted = val * cfg.rate;
    
    // For small decimal prices, support extra fraction digits
    const isSmall = val < 1;
    const decs = isSmall ? Math.max(cfg.decimals, 6) : cfg.decimals;

    const formatted = new Intl.NumberFormat('en-US', {
      minimumFractionDigits: decs,
      maximumFractionDigits: isSmall ? Math.max(cfg.decimals, 8) : decs
    }).format(converted);

    return cfg.position === 'prefix' ? `${cfg.symbol}${formatted}` : `${formatted}${cfg.symbol}`;
  };

  const formatCompactValInCurrency = (val: number | null, currCode: string) => {
    if (val === null || val === undefined) return 'N/A';
    const cfg = currencies[currCode] || currencies.USD;
    const converted = val * cfg.rate;

    const formatted = new Intl.NumberFormat('en-US', {
      notation: 'compact',
      maximumFractionDigits: 2
    }).format(converted);

    return cfg.position === 'prefix' ? `${cfg.symbol}${formatted}` : `${formatted}${cfg.symbol}`;
  };

  const renderChart = () => {
    const isLive = chartTab === 'live';
    const isLoading = isLive ? loadingLiveChart : loadingHistory;
    const dataPoints = isLive ? marketChart : priceHistory;

    if (isLoading) {
      return (
        <div style={{ height: '180px', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'rgba(255,255,255,0.02)', border: '1px solid var(--border-color)', borderRadius: '12px', marginBottom: '1.5rem' }}>
          <div className="spinner" style={{ width: '24px', height: '24px' }} />
        </div>
      );
    }

    const conversionRate = currencies[selectedCurrency]?.rate || 1;

    // Prices mapping: marketChart points are [timestamp, price] arrays. priceHistory points are objects with price field.
    const prices = dataPoints.map(item => {
      const rawPrice = Array.isArray(item) ? item[1] : item.price;
      return rawPrice * conversionRate;
    });

    if (prices.length < 2) {
      return (
        <div style={{ background: 'rgba(255,255,255,0.02)', padding: '1rem', borderRadius: '12px', border: '1px solid var(--border-color)', marginBottom: '1.5rem' }}>
          {/* Tabs header */}
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.75rem' }}>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', textTransform: 'uppercase', letterSpacing: '0.05em', fontWeight: 600 }}>Trend Chart</span>
            <div style={{ display: 'flex', gap: '0.35rem', background: 'rgba(255,255,255,0.03)', padding: '0.2rem', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
              <button 
                onClick={() => setChartTab('live')} 
                className={`page-btn ${isLive ? 'active' : ''}`}
                style={{ fontSize: '0.65rem', height: '22px', padding: '0 0.5rem', width: 'auto', border: 'none', background: isLive ? 'var(--accent-blue)' : 'transparent', borderRadius: '6px', cursor: 'pointer', color: 'var(--text-primary)' }}
              >
                7D Live
              </button>
              <button 
                onClick={() => setChartTab('local')} 
                className={`page-btn ${!isLive ? 'active' : ''}`}
                style={{ fontSize: '0.65rem', height: '22px', padding: '0 0.5rem', width: 'auto', border: 'none', background: !isLive ? 'var(--accent-blue)' : 'transparent', borderRadius: '6px', cursor: 'pointer', color: 'var(--text-primary)' }}
              >
                Local DB
              </button>
            </div>
          </div>
          <div style={{ height: '140px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--text-muted)', fontSize: '0.85rem', textAlign: 'center', border: '1px dashed var(--border-color)', borderRadius: '12px', padding: '1rem' }}>
            {isLive 
              ? 'Failed to load 7D historical chart data from CoinGecko.' 
              : 'Insufficient local price history snapshots in database (Scheduler updates every 5 minutes).'}
          </div>
        </div>
      );
    }

    const min = Math.min(...prices);
    const max = Math.max(...prices);
    const range = max - min || 1;

    const width = 500;
    const height = 140;
    const padding = 10;

    const points = dataPoints.map((item, i) => {
      const rawPrice = Array.isArray(item) ? item[1] : item.price;
      const x = (i / (dataPoints.length - 1)) * (width - padding * 2) + padding;
      const y = height - (((rawPrice * conversionRate) - min) / range) * (height - padding * 2) - padding;
      return { x, y };
    });

    const linePath = points.map((p, i) => `${i === 0 ? 'M' : 'L'} ${p.x} ${p.y}`).join(' ');
    const areaPath = `${linePath} L ${points[points.length - 1].x} ${height} L ${points[0].x} ${height} Z`;

    const priceChange = prices[prices.length - 1] - prices[0];
    const strokeColor = priceChange >= 0 ? '#10B981' : '#EF4444';
    const fillGradient = priceChange >= 0 ? 'url(#greenGrad)' : 'url(#redGrad)';

    // Get time boundaries
    const startTime = isLive 
      ? (marketChart[0] ? marketChart[0][0] : Date.now())
      : (priceHistory[0] ? new Date(priceHistory[0].timestamp).getTime() : Date.now());
    const endTime = isLive 
      ? (marketChart[marketChart.length - 1] ? marketChart[marketChart.length - 1][0] : Date.now())
      : (priceHistory[priceHistory.length - 1] ? new Date(priceHistory[priceHistory.length - 1].timestamp).getTime() : Date.now());

    return (
      <div style={{ background: 'rgba(255,255,255,0.02)', padding: '1rem', borderRadius: '12px', border: '1px solid var(--border-color)', marginBottom: '1.5rem' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.75rem' }}>
          <div style={{ display: 'flex', flexDirection: 'column' }}>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', fontWeight: 600 }}>
              {isLive ? '7-Day Price Trend (CoinGecko)' : 'Local Price History (Database)'}
            </span>
            <span style={{ color: strokeColor, fontWeight: 700, fontSize: '1rem', marginTop: '0.15rem' }}>
              {priceChange >= 0 ? '+' : ''}{formatValInCurrency(priceChange / conversionRate, selectedCurrency)}
            </span>
          </div>

          {/* Selector Tabs */}
          <div style={{ display: 'flex', gap: '0.35rem', background: 'rgba(255,255,255,0.03)', padding: '0.2rem', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
            <button 
              onClick={() => setChartTab('live')} 
              className={`page-btn ${isLive ? 'active' : ''}`}
              style={{ fontSize: '0.65rem', height: '22px', padding: '0 0.5rem', width: 'auto', border: 'none', background: isLive ? 'var(--accent-blue)' : 'transparent', borderRadius: '6px', cursor: 'pointer', color: 'var(--text-primary)' }}
            >
              7D Live
            </button>
            <button 
              onClick={() => setChartTab('local')} 
              className={`page-btn ${!isLive ? 'active' : ''}`}
              style={{ fontSize: '0.65rem', height: '22px', padding: '0 0.5rem', width: 'auto', border: 'none', background: !isLive ? 'var(--accent-blue)' : 'transparent', borderRadius: '6px', cursor: 'pointer', color: 'var(--text-primary)' }}
            >
              Local DB
            </button>
          </div>
        </div>

        <svg viewBox={`0 0 ${width} ${height}`} width="100%" height={height} style={{ overflow: 'visible' }}>
          <defs>
            <linearGradient id="greenGrad" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stopColor="#10B981" stopOpacity="0.25" />
              <stop offset="100%" stopColor="#10B981" stopOpacity="0" />
            </linearGradient>
            <linearGradient id="redGrad" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stopColor="#EF4444" stopOpacity="0.25" />
              <stop offset="100%" stopColor="#EF4444" stopOpacity="0" />
            </linearGradient>
          </defs>
          <line x1={0} y1={padding} x2={width} y2={padding} stroke="rgba(255,255,255,0.04)" strokeDasharray="3" />
          <line x1={0} y1={height/2} x2={width} y2={height/2} stroke="rgba(255,255,255,0.04)" strokeDasharray="3" />
          <line x1={0} y1={height - padding} x2={width} y2={height - padding} stroke="rgba(255,255,255,0.04)" strokeDasharray="3" />
          
          <path d={areaPath} fill={fillGradient} />
          <path d={linePath} fill="none" stroke={strokeColor} strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" />
        </svg>

        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '0.5rem', fontSize: '0.7rem', color: 'var(--text-muted)' }}>
          <span>{new Date(startTime).toLocaleDateString()} {new Date(startTime).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}</span>
          <span>{new Date(endTime).toLocaleDateString()} {new Date(endTime).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}</span>
        </div>
      </div>
    );
  };

  const renderTickers = () => {
    if (loadingTickers) {
      return (
        <div style={{ padding: '2rem 0', display: 'flex', justifyContent: 'center' }}>
          <div className="spinner" style={{ width: '24px', height: '24px' }} />
        </div>
      );
    }
    if (tickers.length === 0) {
      return (
        <div style={{ color: 'var(--text-muted)', fontSize: '0.85rem', textAlign: 'center', padding: '1rem', border: '1px dashed var(--border-color)', borderRadius: '12px', marginTop: '1.5rem' }}>
          No active market listings found for this asset.
        </div>
      );
    }

    return (
      <div style={{ marginTop: '1.5rem' }}>
        <h4 style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.05em', fontWeight: 600 }}>
          Exchange Markets & Trading Pairs
        </h4>
        <div style={{ overflowX: 'auto', border: '1px solid var(--border-color)', borderRadius: '8px', maxHeight: '200px', overflowY: 'auto' }}>
          <table className="data-table" style={{ fontSize: '0.8rem' }}>
            <thead>
              <tr style={{ background: 'rgba(255,255,255,0.01)' }}>
                <th style={{ padding: '0.5rem 0.75rem', fontSize: '0.75rem' }}>Exchange</th>
                <th style={{ padding: '0.5rem 0.75rem', fontSize: '0.75rem' }}>Trading Pair</th>
                <th style={{ padding: '0.5rem 0.75rem', fontSize: '0.75rem', textAlign: 'right' }}>Price</th>
                <th style={{ padding: '0.5rem 0.75rem', fontSize: '0.75rem', textAlign: 'right' }}>24h Volume</th>
                <th style={{ padding: '0.5rem 0.75rem', fontSize: '0.75rem', textAlign: 'right' }}>Spread</th>
              </tr>
            </thead>
            <tbody>
              {tickers.slice(0, 10).map((t, idx) => (
                <tr key={idx} style={{ cursor: 'default' }}>
                  <td style={{ padding: '0.5rem 0.75rem', fontWeight: 600 }}>{t.market.name}</td>
                  <td style={{ padding: '0.5rem 0.75rem', color: 'var(--accent-cyan)' }}>{t.base}/{t.target}</td>
                  <td style={{ padding: '0.5rem 0.75rem', textAlign: 'right', fontWeight: 500 }}>{formatValInCurrency(t.last, selectedCurrency)}</td>
                  <td style={{ padding: '0.5rem 0.75rem', textAlign: 'right' }}>{formatValInCurrency(t.volume, selectedCurrency)}</td>
                  <td style={{ padding: '0.5rem 0.75rem', textAlign: 'right', color: 'var(--text-muted)' }}>
                    {t.spread ? `${(Number(t.spread) * 100).toFixed(2)}%` : '0.00%'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    );
  };

  return (
    <div className="app-container">
      {/* Header */}
      <header className="app-header">
        <div className="header-inner">
          <a href="#" className="brand" onClick={(e) => { e.preventDefault(); handleResetFilters(); }}>
            <Coins size={30} className="brand-logo" />
            <h1>Crypto Tracker</h1>
          </a>
          <div className="connection-status">
            <span className={`status-dot ${isOnline ? 'online' : 'offline'}`} />
            <span>Backend: {isOnline ? 'Connected' : 'Disconnected'}</span>
            <button 
              onClick={() => { fetchGlobalData(); fetchCoins(); }} 
              className="close-btn" 
              style={{ width: '24px', height: '24px', marginLeft: '0.5rem' }}
              title="Force Refresh Data"
            >
              <RotateCw size={14} />
            </button>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="main-content">
        {/* Connection Failure Alert */}
        {!isOnline && (
          <div className="error-container animate-fade-in">
            <AlertTriangle size={24} style={{ color: 'var(--accent-red)' }} />
            <div>
              <h4 style={{ fontWeight: 600 }}>Connection Error</h4>
              <p style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                Cannot establish a connection with the Spring Boot backend server ({API_BASE}). 
                Please ensure the server is running.
              </p>
            </div>
            <button onClick={() => { fetchGlobalData(); fetchCoins(); }} className="retry-btn">
              Retry Connection
            </button>
          </div>
        )}

        {/* Global Market Data Banner */}
        <section className="stats-grid">
          <div className="stat-card animate-fade-in" style={{ animationDelay: '0s' }}>
            <span className="stat-label">Active Cryptocurrencies</span>
            <span className="stat-value">
              {loadingGlobal ? '...' : (globalData?.activeCryptocurrencies?.toLocaleString() || 'N/A')}
              <Globe size={18} style={{ color: 'var(--accent-cyan)', marginLeft: 'auto' }} />
            </span>
            <span className="stat-sub">Tracking current market supply</span>
          </div>

          <div className="stat-card animate-fade-in" style={{ animationDelay: '0.1s' }}>
            <span className="stat-label">Active Markets</span>
            <span className="stat-value">
              {loadingGlobal ? '...' : (globalData?.markets?.toLocaleString() || 'N/A')}
              <Globe size={18} style={{ color: 'var(--accent-blue)', marginLeft: 'auto' }} />
            </span>
            <span className="stat-sub">Trading platforms and exchanges</span>
          </div>

          <div className="stat-card animate-fade-in" style={{ animationDelay: '0.2s' }}>
            <span className="stat-label">Global Market Cap</span>
            <span className="stat-value">
              {loadingGlobal ? '...' : formatCompactCurrency(globalData?.totalMarketCap || null)}
              <Globe size={18} style={{ color: 'var(--accent-green)', marginLeft: 'auto' }} />
            </span>
            <span className="stat-sub">Aggregated global valuation</span>
          </div>

          <div className="stat-card animate-fade-in" style={{ animationDelay: '0.3s' }}>
            <span className="stat-label">24h Global Volume</span>
            <span className="stat-value">
              {loadingGlobal ? '...' : formatCompactCurrency(globalData?.totalVolume || null)}
              <Globe size={18} style={{ color: 'var(--accent-yellow)', marginLeft: 'auto' }} />
            </span>
            <span className="stat-sub">Cumulative daily transactions</span>
          </div>
        </section>

        {/* Search & Filters Card */}
        <section className="filter-card animate-fade-in">
          <div className="filter-grid">
            {/* Search Input */}
            <div className="filter-group">
              <label htmlFor="search">Search Coin</label>
              <div className="input-container">
                <Search size={16} className="input-icon" />
                <input
                  id="search"
                  type="text"
                  placeholder="Bitcoin, ETH, btc..."
                  value={searchName}
                  onChange={(e) => { setSearchName(e.target.value); setPage(0); }}
                  className="filter-input"
                />
              </div>
            </div>

            {/* Price Filter range */}
            <div className="filter-group">
              <label>Price Range (USD)</label>
              <div className="filter-row">
                <input
                  type="number"
                  placeholder="Min"
                  value={minPrice}
                  onChange={(e) => { setMinPrice(e.target.value); setPage(0); }}
                  className="filter-input no-icon-input"
                />
                <input
                  type="number"
                  placeholder="Max"
                  value={maxPrice}
                  onChange={(e) => { setMaxPrice(e.target.value); setPage(0); }}
                  className="filter-input no-icon-input"
                />
              </div>
            </div>

            {/* Market Rank Filter range */}
            <div className="filter-group">
              <label>Market Cap Rank</label>
              <div className="filter-row">
                <input
                  type="number"
                  placeholder="Min (e.g. 1)"
                  value={minRank}
                  onChange={(e) => { setMinRank(e.target.value); setPage(0); }}
                  className="filter-input no-icon-input"
                />
                <input
                  type="number"
                  placeholder="Max (e.g. 100)"
                  value={maxRank}
                  onChange={(e) => { setMaxRank(e.target.value); setPage(0); }}
                  className="filter-input no-icon-input"
                />
              </div>
            </div>

            {/* Reset Button */}
            <div className="filter-group" style={{ justifyContent: 'flex-end' }}>
              <button 
                onClick={handleResetFilters} 
                className="retry-btn" 
                style={{ 
                  background: 'rgba(255,255,255,0.06)', 
                  border: '1px solid var(--border-color)',
                  width: '100%',
                  height: '42px',
                  borderRadius: '8px',
                  color: 'var(--text-primary)'
                }}
              >
                Reset Filters
              </button>
            </div>
          </div>
        </section>

        {/* Coins Table Card */}
        <section className="table-card animate-fade-in">
          {loadingCoins ? (
            <div className="loading-container">
              <div className="spinner" />
              <p style={{ color: 'var(--text-secondary)' }}>Loading live cryptocurrency market...</p>
            </div>
          ) : error ? (
            <div style={{ padding: '3rem 2rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
              <AlertTriangle size={32} style={{ color: 'var(--accent-red)', marginBottom: '1rem' }} />
              <p>{error}</p>
            </div>
          ) : coins.length === 0 ? (
            <div style={{ padding: '4rem 2rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
              <Info size={32} style={{ color: 'var(--accent-cyan)', marginBottom: '1rem' }} />
              <p>No cryptocurrencies found matching the specified filters.</p>
            </div>
          ) : (
            <>
              <div className="data-table-wrapper">
                <table className="data-table">
                  <thead>
                    <tr>
                      <th style={{ width: '80px' }}>Rank</th>
                      <th>Asset</th>
                      <th style={{ textAlign: 'right' }}>Price</th>
                      <th style={{ textAlign: 'right' }}>24h Change</th>
                      <th style={{ width: '80px', textAlign: 'center' }}>Details</th>
                    </tr>
                  </thead>
                  <tbody>
                    {coins.map((coin) => (
                      <tr key={coin.id || coin.symbol} onClick={() => coin.id && viewCoinDetails(coin.id)}>
                        <td>
                          <span className="coin-rank">{coin.marketCapRank || 'N/A'}</span>
                        </td>
                        <td>
                          <div className="coin-identity">
                            <img src={coin.image} alt={coin.name} className="coin-image" onError={(e) => {
                              (e.target as HTMLImageElement).src = 'https://assets.coingecko.com/coins/images/1/large/bitcoin.png';
                            }} />
                            <div className="coin-name-container">
                              <span className="coin-name">{coin.name}</span>
                              <span className="coin-symbol">{coin.symbol}</span>
                            </div>
                          </div>
                        </td>
                        <td style={{ textAlign: 'right' }}>
                          <span className="coin-price">{formatCurrency(coin.price)}</span>
                        </td>
                        <td style={{ textAlign: 'right' }}>
                          <span className={`percentage-badge ${coin.priceChangePercentage24h >= 0 ? 'up' : 'down'}`}>
                            {coin.priceChangePercentage24h >= 0 ? <TrendingUp size={14} /> : <TrendingDown size={14} />}
                            {coin.priceChangePercentage24h !== null 
                              ? `${coin.priceChangePercentage24h.toFixed(2)}%` 
                              : '0.00%'}
                          </span>
                        </td>
                        <td style={{ textAlign: 'center' }} onClick={(e) => { e.stopPropagation(); coin.id && viewCoinDetails(coin.id); }}>
                          <button className="close-btn" style={{ width: '28px', height: '28px' }}>
                            <Info size={16} style={{ color: 'var(--accent-cyan)' }} />
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              {/* Pagination Panel */}
              <div className="pagination-panel">
                <div className="size-selector">
                  <span>Show</span>
                  <select 
                    value={size} 
                    onChange={(e) => { setSize(Number(e.target.value)); setPage(0); }}
                    className="size-select"
                  >
                    <option value={10}>10 coins</option>
                    <option value={25}>25 coins</option>
                    <option value={50}>50 coins</option>
                  </select>
                  <span>of {totalElements} assets</span>
                </div>

                <div className="page-info">
                  Page <strong>{page + 1}</strong> of <strong>{totalPages || 1}</strong>
                </div>

                <div className="page-controls">
                  <button 
                    onClick={() => setPage((p) => Math.max(0, p - 1))} 
                    disabled={page === 0}
                    className="page-btn"
                  >
                    <ChevronLeft size={18} />
                  </button>
                  
                  {/* Dynamically render page buttons */}
                  {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                    // Show pages centered around the current page
                    let targetPage = page - 2 + i;
                    if (page < 2) targetPage = i;
                    if (page >= totalPages - 3) targetPage = totalPages - 5 + i;
                    
                    // Boundary checks
                    if (targetPage < 0 || targetPage >= totalPages) return null;

                    return (
                      <button
                        key={targetPage}
                        onClick={() => setPage(targetPage)}
                        className={`page-btn ${page === targetPage ? 'active' : ''}`}
                      >
                        {targetPage + 1}
                      </button>
                    );
                  })}

                  <button 
                    onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))} 
                    disabled={page >= totalPages - 1}
                    className="page-btn"
                  >
                    <ChevronRight size={18} />
                  </button>
                </div>
              </div>
            </>
          )}
        </section>
      </main>

      {/* Details Dialog Modal */}
      {selectedCoin && (
        <div className="modal-overlay" onClick={() => setSelectedCoin(null)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <div className="modal-title">
                <img src={selectedCoin.image} alt={selectedCoin.name} style={{ width: '28px', height: '28px', borderRadius: '50%' }} />
                <h3 style={{ fontSize: '1.25rem' }}>{selectedCoin.name} ({selectedCoin.symbol.toUpperCase()})</h3>
              </div>
              <div style={{ marginLeft: 'auto', marginRight: '1rem', display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
                <span style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>Currency:</span>
                <select 
                  value={selectedCurrency} 
                  onChange={(e) => setSelectedCurrency(e.target.value)}
                  className="size-select"
                  style={{ fontSize: '0.8rem', padding: '0.15rem 0.35rem' }}
                >
                  <option value="USD">USD ($)</option>
                  <option value="VND">VND (₫)</option>
                  <option value="EUR">EUR (€)</option>
                  <option value="JPY">JPY (¥)</option>
                  <option value="BTC">BTC (₿)</option>
                </select>
              </div>
              <button onClick={() => setSelectedCoin(null)} className="close-btn">
                <X size={18} />
              </button>
            </div>
            
            <div className="modal-body">
              <div className="detail-price-section">
                <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                  Current Market Price
                </span>
                <div className="detail-price">{formatValInCurrency(selectedCoin.price, selectedCurrency)}</div>
                <span className={`percentage-badge ${selectedCoin.priceChangePercentage24h >= 0 ? 'up' : 'down'}`}>
                  {selectedCoin.priceChangePercentage24h >= 0 ? <TrendingUp size={14} /> : <TrendingDown size={14} />}
                  {selectedCoin.priceChangePercentage24h !== null 
                    ? `${selectedCoin.priceChangePercentage24h.toFixed(2)}% (24h)` 
                    : '0.00%'}
                </span>
              </div>

              {renderChart()}

              <div className="detail-list">
                <div className="detail-item">
                  <span className="detail-label">Market Capitalization Rank</span>
                  <span className="detail-value">#{selectedCoin.marketCapRank || 'N/A'}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Market Capitalization</span>
                  <span className="detail-value">{selectedCoin.marketCap ? formatCompactValInCurrency(selectedCoin.marketCap, selectedCurrency) : 'N/A'}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Fully Diluted Valuation (FDV)</span>
                  <span className="detail-value">{selectedCoin.fullyDilutedValuation ? formatCompactValInCurrency(Number(selectedCoin.fullyDilutedValuation), selectedCurrency) : 'N/A'}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">24h Transaction Volume</span>
                  <span className="detail-value">{selectedCoin.totalVolume ? formatCompactValInCurrency(Number(selectedCoin.totalVolume), selectedCurrency) : 'N/A'}</span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Circulating Supply</span>
                  <span className="detail-value" style={{ textTransform: 'uppercase' }}>
                    {selectedCoin.circulatingSupply ? `${Number(selectedCoin.circulatingSupply).toLocaleString()} ${selectedCoin.symbol.toUpperCase()}` : 'N/A'}
                  </span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Total Supply</span>
                  <span className="detail-value" style={{ textTransform: 'uppercase' }}>
                    {selectedCoin.totalSupply ? `${Number(selectedCoin.totalSupply).toLocaleString()} ${selectedCoin.symbol.toUpperCase()}` : 'N/A'}
                  </span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Max Supply</span>
                  <span className="detail-value" style={{ textTransform: 'uppercase' }}>
                    {selectedCoin.maxSupply ? `${Number(selectedCoin.maxSupply).toLocaleString()} ${selectedCoin.symbol.toUpperCase()}` : 'N/A'}
                  </span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">24h High ({selectedCurrency})</span>
                  <span className="detail-value" style={{ color: 'var(--accent-green)' }}>
                    {selectedCoin.high24h ? formatValInCurrency(Number(selectedCoin.high24h), selectedCurrency) : 'N/A'}
                  </span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">24h Low ({selectedCurrency})</span>
                  <span className="detail-value" style={{ color: 'var(--accent-red)' }}>
                    {selectedCoin.low24h ? formatValInCurrency(Number(selectedCoin.low24h), selectedCurrency) : 'N/A'}
                  </span>
                </div>
                <div className="detail-item">
                  <span className="detail-label">Last Updated Timestamp</span>
                  <span className="detail-value" style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                    {selectedCoin.lastUpdated ? new Date(selectedCoin.lastUpdated).toLocaleString() : 'N/A'}
                  </span>
                </div>
              </div>

              {renderTickers()}
            </div>
          </div>
        </div>
      )}

      {/* Footer */}
      <footer className="app-footer">
        <p>&copy; {new Date().getFullYear()} Crypto Tracker API Dashboard. Developed as part of research purposes.</p>
      </footer>
    </div>
  );
}

export default App;
