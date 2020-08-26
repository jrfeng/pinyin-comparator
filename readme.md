**中文按首字母排序**

**例：**

```java
import pinyin.util.PinyinComparator;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> chineseArray = new ArrayList<>();
        chineseArray.add("抱薪救火");
        chineseArray.add("安然无恙");
        chineseArray.add("张冠李戴");
        chineseArray.add("天下无双");

        PinyinComparator comparator = new PinyinComparator();

        chineseArray.sort(comparator);

        System.out.println(chineseArray);   // 输出：[安然无恙, 抱薪救火, 天下无双, 张冠李戴]
    }
}
```

## LICENSE

MIT