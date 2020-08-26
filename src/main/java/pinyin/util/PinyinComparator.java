package pinyin.util;

import java.util.Comparator;

import com.github.promeg.pinyinhelper.Pinyin;

public class PinyinComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        String s1 = getPinyinFirstLetter(o1).toLowerCase();
        String s2 = getPinyinFirstLetter(o2).toLowerCase();

        IndexWrapper index1 = new IndexWrapper(0);
        IndexWrapper index2 = new IndexWrapper(0);

        while (index1.get() < s1.length() && index2.get() < s2.length()) {
            char ch1 = s1.charAt(index1.get());
            char ch2 = s2.charAt(index2.get());

            int result = ch1 - ch2;
            if (isNumber(ch1) && isNumber(ch2)) {
                result = getNumber(s1, index1) - getNumber(s2, index2);
            }

            if (result != 0) {
                return result;
            }

            index1.inc();
            index2.inc();
        }

        return 0;
    }

    private Integer getNumber(String s, IndexWrapper index) {
        int end = index.get() + 1;

        for (int i = index.get(); i < s.length(); i++) {
            if (isNumber(s.charAt(i))) {
                end = i;
            }
        }

        Integer result = Integer.valueOf(s.substring(index.get(), end + 1));

        index.set(end);

        return result;
    }

    private static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    public static String getPinyinFirstLetter(String str) {
        StringBuilder buff = new StringBuilder();
        StringBuilder chineseBuff = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (Pinyin.isChinese(c)) {
                chineseBuff.append(c);
                continue;
            }

            buff.append(chineseFirstLetter(chineseBuff.toString()));
            if (i == 0 && notLetterOrNumber(c)) {
                // 如果第一个字符不是字母或数字，则为其增加一个波浪线 ~ 前缀。
                // 波浪线 ~ 字符是 ASCII 码表中倒数第二个字符，这样的话，在排序时，这类非字母或数字开头，也不是中文的字符串会被排到最后面
                buff.append('~')
                        .append(c);
            } else {
                buff.append(c);
            }

            clear(chineseBuff);
        }

        if (chineseBuff.length() > 0) {
            buff.append(chineseFirstLetter(chineseBuff.toString()));
        }

        return buff.toString();
    }

    private static void clear(StringBuilder stringBuilder) {
        stringBuilder.delete(0, stringBuilder.length());
    }

    private static boolean notLetterOrNumber(char c) {
        return !((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || isNumber(c));
    }

    private static String chineseFirstLetter(String chinese) {
        if (chinese.trim().length() == 0) {
            return "";
        }

        String separator = ",";
        String[] pinyin = Pinyin.toPinyin(chinese, separator)
                .trim()
                .split(separator);

        StringBuilder firstLetter = new StringBuilder();
        for (String s : pinyin) {
            firstLetter.append(s.charAt(0));
        }

        return firstLetter.toString();
    }

    private static class IndexWrapper {
        private int mIndex;

        IndexWrapper(int initValue) {
            mIndex = initValue;
        }

        int get() {
            return mIndex;
        }

        void set(int index) {
            mIndex = index;
        }

        void inc() {
            mIndex += 1;
        }
    }
}
