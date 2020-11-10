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
    public void compareTest1() {
        final String s1 = "单元测试";
        final String s2 = "单元测试";

        final PinyinComparator comparator = new PinyinComparator();
        assertEquals(0, comparator.compare(s1, s2));
    }

    @Test
    public void compareTest2() {
        final String s1 = "1单元测试";
        final String s2 = "12单元测试";

        final PinyinComparator comparator = new PinyinComparator();
        assertTrue(comparator.compare(s1, s2) < 0);
    }

    @Test
    public void compareTest3() {
        final String s1 = "单元测试1";
        final String s2 = "单元测试12";

        final PinyinComparator comparator = new PinyinComparator();
        assertTrue(comparator.compare(s1, s2) < 0);
    }

    @Test
    public void compareTest4() {
        final String s1 = "单元1测试";
        final String s2 = "单元12测试";

        final PinyinComparator comparator = new PinyinComparator();
        assertTrue(comparator.compare(s1, s2) < 0);
    }

    @Test
    public void compareTest5() {
        final String s1 = "单元1测试";
        final String s2 = "单元001测试";

        final PinyinComparator comparator = new PinyinComparator();
        assertEquals(0, comparator.compare(s1, s2));
    }

    @Test
    public void compareTest6() {
        final String s1 = "兵临城下";  // 首字母：BLCX
        final String s2 = "海阔天空";  // 首字母：HKTK

        final PinyinComparator comparator = new PinyinComparator();
        assertTrue(comparator.compare(s1, s2) < 0);
    }

    @Test
    public void compareTest7() {
        final String s1 = "";
        final String s2 = "";

        final String s3 = "";
        final String s4 = "你好hello";

        final String s5 = "你好hello";
        final String s6 = "";

        final PinyinComparator comparator = new PinyinComparator();
        assertEquals(0, comparator.compare(s1, s2));
        assertEquals(-1, comparator.compare(s3, s4));
        assertEquals(1, comparator.compare(s5, s6));
    }

    @Test
    public void compareTest8() {
        final String s1 = "hello123world";
        final String s2 = "helloworld";

        final PinyinComparator comparator = new PinyinComparator();
        assertTrue(comparator.compare(s1, s2) < 0);
    }
}
