passwordAnalyse
=====

项目比较乱，以后有时间重构。

有些功没有测试可能存在bug，以后再说。
-----

项目有三个Package:<br>
1、Analyse：对密码进行组成、键盘模式、拼音、英语单词日期分析;<br>
2、database：对Analyse得到的数据进行处理，包括寻找Top10、计算概率，以及将数据存入数据库（未实现）;<br>
3、state：该Package都为一些枚举量，在某些分析中为了表述清楚就这样写了，不过在com.Analysis.Analyse和com.database.ResultOperator中比较乱，可能没有遵循里面的定义;<br>

todo:<br>
1、对英文单词词库（即english文件）进行更新;<br>
2、debug……<br>
3、Trie树的定义以及相关算法（例如找Top10）存在可以优化的点;<br>
