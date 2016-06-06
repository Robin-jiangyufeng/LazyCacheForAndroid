package com.robin.lazy.sample;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * 文 件 名:  CalculatorTest.java
 * 版    权:  Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  江钰锋 00501
 * 修改时间:  16/6/6
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

public class CalculatorTest {
    private Calculator calculator;

    @Before
    public void setUp() throws Exception {
        calculator=new Calculator();
    }

    @Test
    public void testPlus() throws Exception {
        Assert.assertEquals(11d,calculator.plus(5d,6d));
    }

    @Test
    public void testSubstract() throws Exception {
        Assert.assertEquals(-1d,calculator.substract(5d, 6d));
    }

    @Test
    public void testMultiply() throws Exception {
        Assert.assertEquals(30d,calculator.multiply(5d, 6d));
    }

    @Test
    public void testDivide() throws Exception {
        Assert.assertEquals(4d,calculator.divide(5d, 6d));
    }

    @Test
    public void testArea(){
        System.out.printf("aaaaaaa");
    }
}