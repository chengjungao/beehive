package com.chengjungao.beehive.common.loadbalance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.chengjungao.beehive.common.hash.Hash;

public class ConsistenthHashLoadBalance implements HashLoadBalance{
	
	private List<Node> allNodes = new ArrayList<>();
	
	private List<Node> aliveNodes = new ArrayList<>();
	
	private List<Range> ranges = new ArrayList<>();
	
	private Map<Range,Node> rangeNodeMapping = new HashMap<>();
	
	private Map<Node, List<Range>> nodeRangesMapping = new HashMap<>();

	private int midIndex;
	
	public ConsistenthHashLoadBalance(List<Node> nodes) {
		this.allNodes.addAll(nodes);
		this.midIndex = ranges.size() / 2;
	}

	@Override
	public Node selectNode(String identify) {
		if (identify == null || "".equals(identify)) {
			throw new RuntimeException("identify is empty!");
		}
		int hash = Hash.murmurhash3_x86_32(identify, 0, identify.length(), 0);
		Range targetRange = searchByHash(hash,ranges.get(midIndex),midIndex);
		
		return rangeNodeMapping.get(targetRange);
	}
	
	private Range searchByHash(int hash,Range midRange,int midIndex) {
		RangeResult rangeResult =  midRange.include(hash);
		switch (rangeResult) {
		case SUCCESS: {
			return midRange;
			}
		case OUT_OF_RANGE_LEFT: {
			midIndex = midIndex / 2;
			Range midRangeLeft = ranges.get(midIndex);
			return searchByHash(hash, midRangeLeft, midIndex);
			}
			
		case OUT_OF_RANGE_RIGHT:{
			midIndex = (midIndex + ranges.size()) / 2;
			Range midRangeRight = ranges.get(midIndex);
			return searchByHash(hash, midRangeRight, midIndex);
			}
		 default :
			 throw new RuntimeException("It is impossible!");
		}
	}

	@Override
	public void shutdown() {
		
	}
	
    private List<Range> partitionRange(int partitions, Range range) {
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
	      
	      public RangeResult include(int hash) {
	    	  if (hash < min) {
				return RangeResult.OUT_OF_RANGE_LEFT;
			  }else if (hash > max) {
				return RangeResult.OUT_OF_RANGE_RIGHT;
			  }else {
				return RangeResult.SUCCESS;
			  }
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

		@Override
		public int hashCode() {
			return Objects.hash(max, min);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Range other = (Range) obj;
			return max == other.max && min == other.min;
		}
    }
    
    enum RangeResult{
    	/**
    	 * 超出左边界
    	 */
    	OUT_OF_RANGE_LEFT, 
    	
    	/**
    	 * 超出右边界
    	 */
    	OUT_OF_RANGE_RIGHT,
    	
    	/**
    	 * 包含在范围中
    	 */
    	SUCCESS
    }

    
}
