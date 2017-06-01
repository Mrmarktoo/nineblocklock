# nineblocklock
九宫格 解锁 控件
默认规则：不少于四个连接才可以显示密码

1.0.3功能介绍：
1、该控件涉及到的颜色，线条宽度、北京图像都可以配置
2、九个点位默认值为0到8，可以配置对应的值（string，值与值之间逗号隔开）
3、九点连接过程中，选中的点不可以再选择，也就是最多可以产生九位的密码
4、样式分为两种，一种实心点，一种空心点（类似支付宝解锁）
5、可以开启辅助线显示，xml中配置
6、增加回调接口，返回生成密码和错误信息
7、gravity配置不再起作用，九宫格自动正方形显示，取决于最小边长

gradle引用：
compile 'com.marktoo.widget:nineblocklock:1.0.3'
maven引用：
"
<dependency> <groupId>com.marktoo.widget</groupId> <artifactId>nineblocklock</artifactId> <version>1.0.3</version> <type>pom</type> </dependency>
"

第一次bintray上传，准备不足，如有纰漏请见谅！
发现问题和我联系哦！
18910103220@189.cn