package pinyin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.promeg.pinyinhelper.Pinyin;

public class PinyinComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        String s1 = getPinyinFirstLetter(o1).toLowerCase();
        String s2 = getPinyinFirstLetter(o2).toLowerCase();

        List<Element> s1ElementList = asElementList(s1);
        List<Element> s2ElementList = asElementList(s2);

        return compare(s1ElementList, s2ElementList);
    }

    private int compare(List<Element> listA, List<Element> listB) {
        if (listA.isEmpty() && listB.isEmpty()) {
            return 0;
        }

        if (listA.isEmpty()) {
            return -1;
        }

        if (listB.isEmpty()) {
            return 1;
        }

        int size = Math.min(listA.size(), listB.size());
        for (int i = 0; i < size; i++) {
            int result = listA.get(i).compareTo(listB.get(i));
            if (result != 0) {
                return result;
            }
        }

        return Integer.compare(listA.size(), listB.size());
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

    private List<Element> asElementList(String string) {
        if (string.isEmpty()) {
            return Collections.emptyList();
        }

        List<Element> elementList = new ArrayList<>();

        StringBuilder stringBuffer = new StringBuilder();
        StringBuilder intBuffer = new StringBuilder();

        char c = string.charAt(0);
        int lastType = Element.getType(c);

        appendToBuffer(c, lastType, stringBuffer, intBuffer);

        for (int i = 1; i < string.length(); i++) {
            c = string.charAt(i);
            int type = Element.getType(c);

            if (type != lastType) {
                elementList.add(asElement(lastType, stringBuffer, intBuffer));
            }

            lastType = type;
            appendToBuffer(c, lastType, stringBuffer, intBuffer);
        }

        elementList.add(asElement(lastType, stringBuffer, intBuffer));

        return elementList;
    }

    private Element asElement(int type, StringBuilder stringBuffer, StringBuilder intBuffer) {
        if (type == Element.TYPE_STRING) {
            Element element = new Element(stringBuffer.toString());
            clear(stringBuffer);
            return element;
        }

        Element element = new Element(Integer.parseInt(intBuffer.toString()));
        clear(intBuffer);
        return element;
    }

    private void appendToBuffer(char c, int type, StringBuilder stringBuffer, StringBuilder intBuffer) {
        if (type == Element.TYPE_STRING) {
            stringBuffer.append(c);
        } else {
            intBuffer.append(c);
        }
    }

    private static class Element implements Comparable<Element> {
        public static final int TYPE_STRING = 0;
        public static final int TYPE_INTEGER = 1;

        int type;
        String stringValue;
        int intValue;

        Element(String value) {
            this.type = TYPE_STRING;
            stringValue = value;
        }

        Element(int value) {
            this.type = TYPE_INTEGER;
            intValue = value;
        }

        @Override
        public int compareTo(Element o) {
            if (o.type != this.type) {
                return compareType(o);
            }

            if (this.type == TYPE_STRING) {
                return this.stringValue.compareToIgnoreCase(o.stringValue);
            }

            return Integer.compare(this.intValue, o.intValue);
        }

        // 数字类型小于字符串类型
        private int compareType(Element o) {
            if (o.type == TYPE_INTEGER) {
                return -1;
            }

            return 1;
        }

        public static int getType(char c) {
            return isNumber(c) ? Element.TYPE_INTEGER : Element.TYPE_STRING;
        }

        @Override
        public String toString() {
            return "Element{" +
                    "type=" + type +
                    ", stringValue='" + stringValue + '\'' +
                    ", intValue=" + intValue +
                    '}';
        }
    }
}
