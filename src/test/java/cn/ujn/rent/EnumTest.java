package cn.ujn.rent;

import java.util.Arrays;

public enum EnumTest {
    A,B,C;

    public static void main(String[] args) {
        System.out.println(Arrays.toString(EnumTest.values()));

    }
}
