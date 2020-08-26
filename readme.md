**中文按首字母排序**

## 项目配置

将以下代码添加到项目根目录下的 `build.gradle` 文件中：

```gradle
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

添加依赖： [![](https://jitpack.io/v/jrfeng/pinyin-comparator.svg)](https://jitpack.io/#jrfeng/pinyin-comparator)


```gradle
dependencies {
    implementation 'com.github.jrfeng:pinyin-comparator:0.1'
}
```

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

## 自定义字典

本项目依赖 [TinyPinyin](https://github.com/promeG/TinyPinyin) 来获取中文的拼音，如需使用自定义字典，还需添加对 [TinyPinyin](https://github.com/promeG/TinyPinyin) 的依赖。

将以下代码添加到项目根目录下的 `build.gradle` 文件中：

```gradle
repositories {
    ...
    jcenter()
}
```

添加 TinyPinyin 依赖：

```gradle
implementation 'com.github.promeg:tinypinyin:2.0.3'

implementation 'com.github.promeg:tinypinyin-lexicons-android-cncity:2.0.3' // 可选，适用于 Android 的中国地区词典
implementation 'com.github.promeg:tinypinyin-lexicons-java-cncity:2.0.3'    // 可选，适用于 Java 的中国地区词典
```

使用自定义字典：

```java
Pinyin.init(Pinyin.newConfig()
            .with(new PinyinMapDict() {
                @Override
                public Map<String, String[]> mapping() {
                    HashMap<String, String[]> map = new HashMap<String, String[]>();
                    map.put("重庆",  new String[]{"CHONG", "QING"});
                    return map;
                }
            }));
```

更多内容，请参考 [TinyPinyin](https://github.com/promeG/TinyPinyin)。

## LICENSE

Apache-2.0 License