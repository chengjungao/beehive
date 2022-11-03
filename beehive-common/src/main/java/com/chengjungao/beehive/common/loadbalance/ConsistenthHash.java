package com.chengjungao.beehive.common.loadbalance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConsistenthHash {
	
  public List<Range> partitionRange(int partitions, Range range) {
	    int min = range.min;
	    int max = range.max;

	    assert max >= min;
	    if (partitions == 0) return Collections.emptyList();
	    long rangeSize = (long)max - (long)min;
	    long rangeStep = Math.max(1, rangeSize / partitions);

	    List<Range> ranges = new ArrayList<>(partitions);

	    long start = min;
	    long end = start;

	    while (end < max) {
	      end = start + rangeStep;
	      // make last range always end exactly on MAX_VALUE
	      if (ranges.size() == partitions - 1) {
	        end = max;
	      }
	      ranges.add(new Range((int)start, (int)end));
	      start = end + 1L;
	    }

	    return ranges;
	}
  
    static class  Range{
    	private int min;
	    private int max;
	    
	    public Range(int min, int max) {
	        assert min <= max;
	        this.min = min;
	        this.max = max;
	      }

	      public boolean includes(int hash) {
	        return hash >= min && hash <= max;
	      }

	      public boolean isSubsetOf(Range superset) {
	        return superset.min <= min && superset.max >= max;
	      }

	      public boolean overlaps(Range other) {
	        return includes(other.min) || includes(other.max) || isSubsetOf(other);
	      }

	      @Override
	      public String toString() {
	        return Integer.toHexString(min) + '-' + Integer.toHexString(max);
	      }
    }
    
}
