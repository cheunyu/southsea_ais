package com.scyb.aisbroadcast.common.util;

import java.math.BigDecimal;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/8/4
 * Time:12:53
 */
public class ConvertUtil {

    public String decConvertDfm(int dec) {
        int d = dec/1000000;
        double f = Math.floor((ArithUtil.div(dec, 1000000, 6) - d)*60);
        double m = ((ArithUtil.div(dec, 1000000, 6)-d)*60-f)*60;
        BigDecimal b = new   BigDecimal(m);
        double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.valueOf(d)+" "+String.valueOf(f).replace(".0","")+"'"+String.valueOf(f1)+"\"";
    }

    public static void main(String args[]) {
        ConvertUtil cu = new ConvertUtil();
        System.out.println(cu.decConvertDfm(34746285));
        System.out.println(cu.decConvertDfm(123746285));
    }
}
