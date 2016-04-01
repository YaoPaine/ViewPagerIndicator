# ViewPagerIndicator
A ViewPager Indicator for study

该项目是在观看幕课网关于自定义ViewPagerIndicator的教学视频后的练习。具体内容可以通过观看幕课网视频学习。这里主要讲一下区别：

  1. 在视频中，没有对在调用ViewPager的setCurrentItem()方法时设置indicator的相应滑动，这是一个bug.
  2. 视频中的项目在当前条目为当前屏幕最大可现实条目的左侧条目时，再滑动条目indicator就会滑动到最大可显示条目的下一个条目,这样会导致当indicator的最大条目为可显示条目时，再次向左滑动ViewPager时会导致indicator继续向左滑动，造成indicator最右侧存在空白区域。
  
存在的问题：

  indicator没有滑动功能，不能滑动indicator后再选择相应的viewpager(待改善)
