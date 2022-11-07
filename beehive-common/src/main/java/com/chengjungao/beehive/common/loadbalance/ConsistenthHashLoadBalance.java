package com.chengjungao.beehive.common.loadbalance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.chengjungao.beehive.common.hash.Hash;

/**
 * 一致性环状hash实现的负载均衡
 * @author wolf
 */
public class ConsistenthHashLoadBalance implements LoadBalanceById {
	
	private List<Node> allNodes = new ArrayList<>();
	
	private volatile List<Node> aliveNodes = new ArrayList<>();
	
	private volatile Map<Range,Node> rangeNodeMapping = new ConcurrentHashMap<>();
	
	private Map<Node, List<Range>> nodeRangesMapping = new HashMap<>();

	private List<Range> ranges;
	
	
	private NodeChangeListener listener;
	
	public ConsistenthHashLoadBalance(List<Node> nodes) {
		this(nodes, 10, new DefaultNodeChangeListener());
	}
	
	public ConsistenthHashLoadBalance(List<Node> nodes,int shardsPerNode,NodeChangeListener nodeChangeListener) {
		this.allNodes.addAll(nodes);
		this.ranges = partitionRange(nodes.size() * shardsPerNode , new Range(Integer.MIN_VALUE, Integer.MAX_VALUE));
		
		List<Range> tempRanges = new ArrayList<>(); //待分配的ranges
		
		//将rangs分配给存活的节点
		for (int i = 0; i < nodes.size(); i++) {
			List<Range> subRanges = ranges.subList(i * shardsPerNode , (i + 1) * shardsPerNode);
			nodeRangesMapping.put(nodes.get(i), subRanges);
			if (nodes.get(i).healthCheck()) {
				aliveNodes.add(nodes.get(i));
				for (Range range : subRanges) {
					rangeNodeMapping.put(range, nodes.get(i));
				}
			}else {
				tempRanges.addAll(subRanges);
			}
		}
		if (aliveNodes.isEmpty()) {
			throw new RuntimeException("No Alive Node, Please check!");
		}
		
		//将坏掉的节点所需要分配的节点，分配给其他节点
		for (int i = 0; i < tempRanges.size(); i++) {
			rangeNodeMapping.put(tempRanges.get(i), aliveNodes.get(i % aliveNodes.size()));
		}
		
		this.listener = nodeChangeListener;
		//start listen all nodes
		this.listener.start(this);
	}

	@Override
	public Node selectNode(String identify) {
		if (identify == null || "".equals(identify)) {
			throw new RuntimeException("identify is empty!");
		}
		int hash = Hash.murmurhash3_x86_32(identify, 0, identify.length(), 0);
		Range targetRange = searchByHash(hash,0,ranges.size());
		
		return rangeNodeMapping.get(targetRange);
	}
	
	@Override
	public void nodeFail(Node node) {
		aliveNodes.remove(node);
		//重新分配此节点的ranges
		List<Range> ranges = nodeRangesMapping.get(node);
		for (int i = 0; i < ranges.size(); i++) {
			rangeNodeMapping.put(ranges.get(i), aliveNodes.get(i % aliveNodes.size()));
		}
	}

	@Override
	public void nodeRecover(Node node) {
		aliveNodes.add(node);
		//恢复此节点原来所属的ranges
		List<Range> ranges = nodeRangesMapping.get(node);
		for (int i = 0; i < ranges.size(); i++) {
			rangeNodeMapping.put(ranges.get(i), node);
		}
	}
	
	@Override
	public List<Node> getAllNodes() {
		return this.allNodes;
	}
	
	@Override
	public List<Node> getAliveNodes() {
		return this.aliveNodes;
	}

	
	private Range searchByHash(int hash,int min,int max) {
		int mid = (max + min) / 2;
		Range midRange = ranges.get(mid);
		RangeResult rangeResult =  midRange.include(hash);
		switch (rangeResult) {
		case SUCCESS: {
			return midRange;
			}
		case OUT_OF_RANGE_LEFT: {
			return searchByHash(hash, min, mid);
			}
			
		case OUT_OF_RANGE_RIGHT:{
			return searchByHash(hash, mid, max);
			}
		 default :
			 throw new RuntimeException("It is impossible!");
		}
	}

	@Override
	public void shutdown() {
		this.listener.shutdown();
		for (Node node : allNodes) {
			try {
				node.close();
			} catch (Exception e) {
				// ignore
			}
		}
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
