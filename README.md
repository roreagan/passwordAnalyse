passwordAnalyse
=====
程序使用说明<br>
-----
1.	开发语言与开发工具<br>
--------
本项目采用Java编写，使用的IDE为Intellij Idea 2016.2.4版本，项目为.idea项目。<br>
本项目放在github中，https://github.com/roreagan/passwordAnalyse，可以随时下载。<br>
<br>
2.	代码结构<br>
---------
###Analysis:<br>
该package用于完成第一问，即密码分析，Analyse.java为核心。Analyse会处理读取的每一行数据，分别在DateFormat、KeyboardClass、WordsMatch中进行日期分析、键盘模式分析和拼音/单词分析。WordNode类用于实现前缀树以提高效率。<br><br>
###datebase<br>
虽然称作Database但是该package只用于将分析结果打印出来，生成分析的“文件名+.log”的形式，Analysis.analyse得到的所有数据都会传递到其所有的ResultOperator类中进而对每一行密码进行汇总，最终通过output打印出结果。<br><br>
###generate<br>
Generate包用于生成密码库，其中的两个类分别代表了两种生成密码的策略。GenerateByInfo是通过将用户输入的数据进行处理后枚举生成密码库，GenerateLibrary通过读取之前的分析数据生成密码库。<br><br>
###state<br>
state包中的类都为枚举类，分别代表着密码分析阶段的各个状态，DatePattern用于表示日期分析的格式，DateState代表日期分析时密码的构成，KeyboardState代表密码所属的键盘模式。<br><br>
###strength<br>
strength包中只有一个Estimation类，用于分析密码强度，核心为checkStrength()函数，通过Markov算法给密码打分。<br><br>
###ui<br>
顾名思义，ui包主要用于显示界面，其中的Test类综合了分析和利用分析结果生成密码库的Analysis.Analyse类和GenerateLibrary类。FileClean用于密码文件的整理（项目里面单独拿出来用的，并没有纳入软件中），MainUI表示最终软件的界面。<br>
<br>
3.	程序的使用
-----------
本程序提供了界面用于各个问题。
 <br>

###密码分析： <br>
输入文件名，点击密码分析，则程序会根据搜索在同一目录下的文件，若不存在则会有弹窗提示文件不存在，否则会进行分析，结束后由弹窗提示分析完成。<br>
【注意】：输入的文件必须每行都为密码，由于并没有对密码的内容和格式进行检测，如果文件不符合格式或包含键盘上没有的字符，则程序会错误运行。<br>
<br>
####生成密码库：<br>
不根据输入信息只能根据分析结果生成密码库，如果之前没有密码库，则无法点击。<br>
若根据输入信息生成密码库，则会检验日期格式，最终会根据输入的各个信息进行枚举进而生成密码库。<br>

####密码强度检测：<br>
密码强度检测只用输入密码后点击密码强度检测，则右侧会以0-100分的标准为密码强度打分。<br>
<br>
【注意】：本程序还有一些依赖文件，拼音和密码检测需要的pinyin和english以及密码强度检测需要的lib文件，这些文件都不能删除以保证程序的正常运行。


