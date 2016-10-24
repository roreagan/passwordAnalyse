passwordAnalyse
=====

项目比较乱，以后有时间重构。

项目有三个Package:<br>
1. Analyse：对密码进行组成、键盘模式、拼音、英语单词日期分析;<br>
2. database：对Analyse得到的数据进行处理，包括寻找Top10、计算概率，以及将数据存入数据库（未实现）;<br>
3. state：该Package都为一些枚举量，在某些分析中为了表述清楚就这样写了，不过在com.Analysis.Analyse和com.database.ResultOperator中比较乱，可能没有遵循里面的定义;<br>

已经实现的功能:<br>
1. 密码构成元素分析：所有字符的出现次数、密码的结构（例如“DDDDDD”）、常用密码Top10；<br>
2. 键盘密码的模式分析：Same Row、Zig Zag、Sanke，还有一项 为Same Row中纯数字的比例；<br>
3. 日期密码及格式分析：日期密码的出现次数、模式（例如“YYMMDD”）、日期密码的构成（纯数字、有符号、有字母/）；<br>
4. 拼音和英文单词的统计：拼音和英文单词的Top10、出现次数、拼音/单词密码的构成（纯字母或者混合结构）；<br>


todo:<br>
1. 对英文单词词库（即english文件）进行更新;<br>
2. Trie树的定义以及相关算法（例如找Top10）存在可以优化的点;<br>

密码强度分析：<br>
1. 暂时采用了Markov算法，就一个com.strngth.estimation.java；<br>
2. Markov算法学习后得到的数据需要保存在一个文件中，下一次启动就直接读文件，暂时还没有存下来；<br>
3. 可能需要更改文件中的Estimation.minP；
