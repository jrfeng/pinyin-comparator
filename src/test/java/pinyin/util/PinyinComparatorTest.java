package pinyin.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PinyinComparatorTest {

    @Test
    public void getPinyinFirstLetter() {
        final String s1 = "单元测试";
        final String firstLetter1 = "DYCS";

        final String s2 = "@单元测试";
        final String firstLetter2 = "~@DYCS";

        assertEquals(firstLetter1, PinyinComparator.getPinyinFirstLetter(s1));
        assertEquals(firstLetter2, PinyinComparator.getPinyinFirstLetter(s2));
    }

    @Test
    public void compare() {
        final String s1 = "单元测试";
        final String s2 = "单元测试";

        final String s3 = "1单元测试";
        final String s4 = "12单元测试";

        final String s5 = "单元测试1";
        final String s6 = "单元测试12";

        final String s7 = "单元1测试";
        final String s8 = "单元12测试";

        final String s9 = "单元1测试";
        final String s10 = "单元001测试";

        final String s11 = "兵临城下";  // 首字母：BLCX
        final String s12 = "海阔天空";  // 首字母：HKTK

        final PinyinComparator comparator = new PinyinComparator();

        assertEquals(0, comparator.compare(s1, s2));
        assertTrue(comparator.compare(s3, s4) < 0);
        assertTrue(comparator.compare(s5, s6) < 0);
        assertTrue(comparator.compare(s7, s8) < 0);
        assertEquals(0, comparator.compare(s9, s10));
        assertTrue(comparator.compare(s11, s12) < 0);
    }
}
