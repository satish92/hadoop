package com.jaromin.hbase.matchers;

/**
 * Copyright 2013 Patrick K. Jaromin <patrick@jaromin.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Patrick
 *
 */
public class KeyValueMatcherTest {

	private byte[] rowKey =  Bytes.toBytes("rowKey");

	private byte[] columnFamilyA = Bytes.toBytes("a");
	private byte[] columnFamilyB = Bytes.toBytes("b");
	
	@Before
	public void setUp() {	
	}

	@Test
	public void testAllMatchers_Put() {
		Put put = new Put(rowKey);
		put.add(columnFamilyA, "column1".getBytes(), "avalue1".getBytes());
		put.add(columnFamilyA, "column2".getBytes(), "avalue2".getBytes());
		put.add(columnFamilyA, "column3".getBytes(), "avalue3".getBytes());
		put.add(columnFamilyB, "lastColumn".getBytes(), "bvalue".getBytes());
	
		assertThat(put, Matchers.hasKeyValue(Matchers.hasColumn("a:column1"), "avalue1"));
		assertThat(put, Matchers.hasKeyValue(Matchers.hasColumn(startsWith("a:col")), is("avalue1")));
		
		assertThat(put, Matchers.hasKeyValue(Matchers.hasColumn(startsWith("a:col")), is("avalue1")));
		
		assertThat(put, Matchers.hasKeyValue(is("avalue1")));
		assertThat(put, Matchers.hasKeyValue(Matchers.hasColumn(startsWith("b:last")), is(not(startsWith("av")))));
		assertThat(put, Matchers.hasKeyValue(is(not(startsWith("cv")))));
		
		
		assertThat(put, Matchers.hasKeyValue(Matchers.hasColumn(startsWith("a:col")), is(not("avalue"))));
		
		assertThat(put, Matchers.hasKeyValue(Matchers.hasColumn(is("a:column2")), is("avalue2")));
		
		assertThat(put, Matchers.hasKeyValue(Matchers.hasColumn(is("a:column3")), is(not(startsWith("value")))));
		
		assertThat(put, Matchers.hasKeyValue(Matchers.hasColumn(startsWith("a:col")), startsWith("avalue")));

		assertThat(put, Matchers.hasKeyValue(Matchers.hasColumn(startsWith("a:col")), containsString("avalue")));
		
		assertThat(put, Matchers.hasKeyValue(Matchers.hasColumn(startsWith("b:lastColumn")), is("bvalue")));		
	}	

	@Test
	public void testNumericValues() {
		Put put = new Put(rowKey);
		put.add(columnFamilyA, "column1".getBytes(), Bytes.toBytes(8888L));
		put.add(columnFamilyB, "column2".getBytes(), Bytes.toBytes(9999L));
	
		assertThat(put, Matchers.hasKeyValue(
				Matchers.hasColumn(startsWith("a:col")), 
				is((not("avalue1")))));
		
		assertThat(put, Matchers.hasLongKeyValue(
				Matchers.hasColumn(startsWith("a:col")), 
				is(8888L)));
		assertThat(put, Matchers.hasLongKeyValue(
				Matchers.hasColumn(startsWith("a:col")), 
				is(lessThan(8889L))));
		assertThat(put, Matchers.hasLongKeyValue(
				Matchers.hasColumn(startsWith("a:col")), 
				is(greaterThan(8887L))));
		assertThat(put, Matchers.hasLongKeyValue(
				Matchers.hasColumn(startsWith("a:col")), 
				is(not(8887L))));
		
		assertThat(put, Matchers.hasLongKeyValue(
				Matchers.hasColumn(startsWith("b:col")), 
				is(9999L)));
		assertThat(put, Matchers.hasLongKeyValue(
				Matchers.hasColumn(startsWith("b:col")), 
				is(not(8888L))));
		
	}
}
