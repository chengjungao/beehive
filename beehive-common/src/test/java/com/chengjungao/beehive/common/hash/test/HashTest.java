package com.chengjungao.beehive.common.hash.test;

import org.junit.Test;

import com.chengjungao.beehive.common.hash.Hash;

public class HashTest {

	@Test
	public void test() {
		String str = "tttttt12123124qfasgarghsh";
		int hash = Hash.murmurhash3_x86_32(str, 0,str.length(), 0);
		System.err.println(Integer.toHexString(hash));
	}
}
