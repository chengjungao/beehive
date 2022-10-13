package com.chengjungao.beehive.cache;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class CacheStats {
  private final AtomicLong hitCount;
  private final AtomicLong missCount;
  private final AtomicLong loadSuccessCount;
  private final AtomicLong loadExceptionCount;
  private final AtomicLong totalLoadTime;
  private final AtomicLong evictionCount;

  /**
   * Constructs a new {@code CacheStats} instance.
   *
   * <p>Five parameters of the same type in a row is a bad thing, but this class is not constructed
   * by end users and is too fine-grained for a builder.
   */
  public CacheStats() {

    this.hitCount = new AtomicLong(0);
    this.missCount = new AtomicLong(0);
    this.loadSuccessCount = new AtomicLong(0);
    this.loadExceptionCount = new AtomicLong(0);
    this.totalLoadTime = new AtomicLong(0);
    this.evictionCount = new AtomicLong(0);
  }

  /**
   * Returns the number of times {@link Cache} lookup methods have returned either a cached or
   * uncached value. This is defined as {@code hitCount + missCount}.
   */
  public long requestCount() {
    return hitCount.get() + missCount.get();
  }

  /**
   * Returns the number of times {@link Cache} lookup methods have returned a cached value.
   */
  public long hitCount() {
    return hitCount.get();
  }

  /**
   * Returns the ratio of cache requests which were hits. This is defined as
   * {@code hitCount / requestCount}, or {@code 1.0} when {@code requestCount == 0}.
   * Note that {@code hitRate + missRate =~ 1.0}.
   */
  public double hitRate() {
    long requestCount = requestCount();
    return (requestCount == 0) ? 1.0 : (double) hitCount.get() / requestCount;
  }

  /**
   * Returns the number of times {@link Cache} lookup methods have returned an uncached (newly
   * loaded) value, or null. Multiple concurrent calls to {@link Cache} lookup methods on an absent
   * value can result in multiple misses, all returning the results of a single cache load
   * operation.
   */
  public long missCount() {
    return missCount.get();
  }

  /**
   * Returns the ratio of cache requests which were misses. This is defined as
   * {@code missCount / requestCount}, or {@code 0.0} when {@code requestCount == 0}.
   * Note that {@code hitRate + missRate =~ 1.0}. Cache misses include all requests which
   * weren't cache hits, including requests which resulted in either successful or failed loading
   * attempts, and requests which waited for other threads to finish loading. It is thus the case
   * that {@code missCount &gt;= loadSuccessCount + loadExceptionCount}. Multiple
   * concurrent misses for the same key will result in a single load operation.
   */
  public double missRate() {
    long requestCount = requestCount();
    return (requestCount == 0) ? 0.0 : (double) missCount.get() / requestCount;
  }

  /**
   * Returns the total number of times that {@link Cache} lookup methods attempted to load new
   * values. This includes both successful load operations, as well as those that threw
   * exceptions. This is defined as {@code loadSuccessCount + loadExceptionCount}.
   */
  public long loadCount() {
    return loadSuccessCount.get() + loadExceptionCount.get();
  }

  /**
   * Returns the number of times {@link Cache} lookup methods have successfully loaded a new value.
   * This is always incremented in conjunction with {@link #missCount}, though {@code missCount}
   * is also incremented when an exception is encountered during cache loading (see
   * {@link #loadExceptionCount}). Multiple concurrent misses for the same key will result in a
   * single load operation.
   */
  public long loadSuccessCount() {
    return loadSuccessCount.get();
  }

  /**
   * Returns the number of times {@link Cache} lookup methods threw an exception while loading a
   * new value. This is always incremented in conjunction with {@code missCount}, though
   * {@code missCount} is also incremented when cache loading completes successfully (see
   * {@link #loadSuccessCount}). Multiple concurrent misses for the same key will result in a
   * single load operation.
   */
  public long loadExceptionCount() {
    return loadExceptionCount.get();
  }

  /**
   * Returns the ratio of cache loading attempts which threw exceptions. This is defined as
   * {@code loadExceptionCount / (loadSuccessCount + loadExceptionCount)}, or
   * {@code 0.0} when {@code loadSuccessCount + loadExceptionCount == 0}.
   */
  public double loadExceptionRate() {
    long totalLoadCount = loadSuccessCount.get() + loadExceptionCount.get();
    return (totalLoadCount == 0)
        ? 0.0
        : (double) loadExceptionCount.get() / totalLoadCount;
  }

  /**
   * Returns the total number of nanoseconds the cache has spent loading new values. This can be
   * used to calculate the miss penalty. This value is increased every time
   * {@code loadSuccessCount} or {@code loadExceptionCount} is incremented.
   */
  public long totalLoadTime() {
    return totalLoadTime.get();
  }

  /**
   * Returns the average time spent loading new values. This is defined as
   * {@code totalLoadTime / (loadSuccessCount + loadExceptionCount)}.
   */
  public double averageLoadPenalty() {
    long totalLoadCount = loadSuccessCount.get() + loadExceptionCount.get();
    return (totalLoadCount == 0)
        ? 0.0
        : (double) totalLoadTime.get() / totalLoadCount;
  }

  /**
   * Returns the number of times an entry has been evicted. This count does not include manual
   * {@linkplain Cache#invalidate invalidations}.
   */
  public long evictionCount() {
    return evictionCount.get();
  }


  @Override
  public String toString() {
	Map<String, String> map = convertToMap();
    return map.toString();
  }

  private Map<String, String> convertToMap() {
	  Map<String, String> map = new HashMap<>();
	  map.put("hitCount", hitCount.toString());
	  map.put("missCount", missCount.toString());
	  map.put("loadSuccessCount", loadSuccessCount.toString());
	  map.put("loadExceptionCount", loadExceptionCount.toString());
	  map.put("totalLoadTime", totalLoadTime.toString());
	  map.put("evictionCount", evictionCount.toString());
	return map;
  }
}
