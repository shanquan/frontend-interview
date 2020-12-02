# interview

Android pda interview project

Android开发任务请直接看[这里](#android-studio开发任务)

## Questions

### Java,Android基本问题
1. int与Integer的区别，HashMap与ArrayList区别
2. String、StringBuffer、StringBuilder区别和使用场景
3. 谈谈java泛型、抽象类、内部类、接口概念与用法
   - A
   ```java
    public class Main{
      private String data="data";
      class test{
        public void go(){
          System.out.println(data);
        }
      }
    }
   ```
   - B
   ```java
    abstract class Animal{
      private int age;
      public Animal(int age){
        this.age = age;
        System.out.println("初始化Animal");
      }
      public void move(){
        System.out.println("跑步数："+this.age);
      }
    }
    abstract class Dog extends Animal{
      public Dog(int age){
        super(age);
        System.out.println("初始化Dog");
      }
    }
    public class BigDog extends Dog{
      public BigDog(){
        super(10);
        System.out.println("初始化BigDog");
      }
      public static void main(String[] args){
        BigDog a = new BigDog();
        a.move();
      }
    }
   ```
   - C
   ```java
    class Test<T>
    {
        T obj;
        Test(T obj) {  this.obj = obj;  }
        public T getObject()  { return this.obj; }
    }
    class Main
    {
      public static void main (String[] args)
      {
        Test <Integer> iObj = new Test<Integer>(15);
        System.out.println(iObj.getObject());
        Test <String> sObj = new Test<String>("GeeksForGeeks"); 
        System.out.println(sObj.getObject());
      }
    }
   ```
   - D
   ```java
    interface in1
    {
      final int a = 10;
      void display();
    }
    class testClass implements in1
    {
      public void display()
      {
      System.out.println("test");
      }
      public static void main (String[] args)
      {
      testClass t = new testClass();
      t.display();
      System.out.println(a);
      }
    }
   ```
4. 熟悉哪些设计模式
   > 工厂模式`Factory`、单例模式`Singleton`、建造者模式`Builder`
   - A
   ```java
    public interface Sender {  
      public void Send();  
    }

    public class MailSender implements Sender {  
      @Override  
      public void Send() {  
          System.out.println("this is mailsender!");  
      }  
    }

    public class SmsSender implements Sender {  
      @Override  
      public void Send() {  
          System.out.println("this is sms sender!");  
      }  
    }

    public class SendCreater {  
      public Sender produce(String type) {  
          if ("mail".equals(type)) {  
              return new MailSender();  
          } else if ("sms".equals(type)) {  
              return new SmsSender();  
          } else {  
              System.out.println("请输入正确的类型!");  
              return null;  
          }  
      }  
    } 

    public class CreaterTest {  
      public static void main(String[] args) {  
          SendCreater creater = new SendCreater();  
          Sender sender = creater.produce("sms");  
          sender.Send();  
      }  
    }
   ```
   - B
   ```java
   public class Creater {
      static class Student{
          String name = null ;
          int number = -1 ;
          String sex = null ;
          int age = -1 ;
          String school = null ;
          static class StudentCreater{
              String name = null ;
              int number = -1 ;
              String sex = null ;
              int age = -1 ;
              String school = null ;
              public StudentCreater setName(String name) {
                  this.name = name;
                  return  this ;
              }

              public StudentCreater setNumber(int number) {
                  this.number = number;
                  return  this ;
              }

              public StudentCreater setSex(String sex) {
                  this.sex = sex;
                  return  this ;
              }

              public StudentCreater setAge(int age) {
                  this.age = age;
                  return  this ;
              }

              public StudentCreater setSchool(String school) {
                  this.school = school;
                  return  this ;
              }
              public Student create() {
                  return new Student(this);
              }
          }
          public Student(StudentCreater creater){
              this.age = creater.age;
              this.name = creater.name;
              this.number = creater.number;
              this.school = creater.school ;
              this.sex = creater.sex ;
          }
      }
      public static void main( String[] args ){
          Student a = new Student.StudentCreater().setAge(13).setName("LiHua").create();
          Student b = new Student.StudentCreater().setSchool("sc").setSex("Male").setName("ZhangSan").create();
      }
    }
   ```
   - C
   ```java
    class Teacher {
      private Teacher() {}
      private static Teacher t = null ;
      public synchronized static Teacher getTeacher() {
        if(t==null) {
          t = new Teacher() ;
        }
        return t ;
      }
    }
    
    public class TeacherDemo {
      public static void main(String[] args) {
        Teacher t1 = Teacher.getTeacher() ;
        Teacher t2 = Teacher.getTeacher() ;
        System.out.println(t1==t2);
        System.out.println(t1);
        System.out.println(t2);
      }
    }
   ```
5. 排序算法
   > 冒泡排序`BubbleSort`，选择排序`SelectSort`，插入排序`InsertSort`:`O(n2)`;快速排序`QuickSort`:`O(N*logN)`
   - A
   ```java
   public static void SortD(int a[],int l,int r){
        if(l>=r)
          return;
        int i = l; int j = r; int key = a[l];
        while(i<j){
            while(i<j && a[j]>=key)
                j--;
            if(i<j){
                a[i] = a[j];
                i++;
            }
            while(i<j && a[i]<key)
                i++;
            if(i<j){
                a[j] = a[i];
                j--;
            }
        }
        a[i] = key;
        SortD(a, l, i-1);
        SortD(a, i+1, r);
    }
   ```
   - B
   ```java
    public static void SortC(int array[],int lenth){
      int temp;  
      for(int i=0;i<lenth-1;i++){
          for(int j=i+1;j>0;j--){
              if(array[j] < array[j-1]){
                  temp = array[j-1];
                  array[j-1] = array[j];
                  array[j] = temp;
              }else{
                  break;
              }
          }
      }
    }
   ```
   - C
   ```java
    public static void SortB(int array[],int lenth){
      for(int i=0;i<lenth-1;i++){
          int minIndex = i;
          for(int j=i+1;j<lenth;j++){
             if(array[j]<array[minIndex]){
                 minIndex = j;
             }
          }
          if(minIndex != i){
              int temp = array[i];
              array[i] = array[minIndex];
              array[minIndex] = temp;
          }
      }
    }
   ```
   - D
   ```java
    public static void SortA(int [] arr){
        int temp;
        for(int i=0; i<arr.length-1; i++){
            for(int j=arr.length-1; j>i; j--){
                if(arr[j] < arr[j-1]){
                    temp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = temp;
                }
            }
        }
    }
   ```
6. Android四大组件包括以下哪些？（多选）
   - [ ] Activity
   - [ ] Controller
   - [ ] Service
   - [ ] Model
   - [ ] BroadcastReceiver
   - [ ] Intent
   - [ ] ContentProvider
   - [ ] View
7. 一个安卓界面即为一个`?`
8. `?`负责后台任务的运行
9.  `?`负责广播
10. `?`负责应用的对外分享数据功能
11. `?`充当四大组件信使，完成数据的组件传递及开启组件的功能
12. 使用XML文件对安卓应用界面进行布局，常用的布局(`layout`)有（多选）：
    - [ ] LinearLayout
    - [ ] TableLayout
    - [ ] FrameLayout
    - [ ] RelativeLayout
    - [ ] GridLayout
    - [ ] PercentLinearLayout
    - [ ] AbsoluteLayout
    - [ ] PositionLayout
    - [ ] ConstraintLayout
13. 接口数据一般采用`?`（数据格式）
14. JSON解析用`?`（包名或库名）
15. Android网络图片请求进行三级缓存，分别为？加载顺序？
16. 图片加载框架有哪些`?`他们之间的区别是什么？
17. 网络框架有哪些`?`他们之间的区别是什么？
18. Http请求中包含哪些内容？，上传文件和发起请求的post请求的区别？
19. Android异步消息处理机制？
20. ANR是什么？怎样避免和解决ANR？
21. RecyclerView和ListView的区别？
22. 如何在服务器接口未准备好的情况下调试接口？
23. padding和margin的区别。给固定大小及wrap_content的textView设置padding，它的可点击区域会变化吗？
24. 有没有了解过以下语言/包/技术/框架？（了解的请勾选）
    - [ ] Cordova
    - [ ] Reative Native
    - [ ] flutter
    - [ ] Kotlin
    - [ ] Android Jetpack
    - [ ] AndroidX
    - [ ] Dragger

### 学习/职业经历相关
1. Java语言用了多久？开发入门方式是怎样的？（大学课程、网络自学、培训机构）
2. 对自己的职业能力与方向大致判断下？感兴趣的有哪些技术方向（网页收藏、博客或公众号订阅）？职业规划是怎样的？
3. 自身经历中用过哪些开发工具？自学过或试用过哪些技术？分别是何种渠道自学的？
4. 在工作中做过的自认为比较有挑战的需求是什么，是怎么实现的？
5. 简单列举下几个工作中遇到的Bug是怎样的，及解决过程？
6. 在团队中担任怎样的角色，团队的开发流程是怎样的？
7. 有开源项目吗？参与的角色？
8. 平时怎么获取新知识？
9. 有没有自己独立负责技术架构与选型的项目？选择依据有哪些？
10. 有没有用过/熟悉的Linux系统命令行工具？哪些？
11. 有没有阅读过英文文档（README或documents），打开看看？

## Android Studio开发任务

请在此项目源码基础上完成SAMPLE页面的开发（用java，不能用html/js），实现与SAMPLE2页面相同的功能，组件与交互也需相同，主题外观不必完全一致。需求说明如下：
- 账号、密码可以任意输入
- 菜单页面，点击SAMPLE按钮，进入页面布局、组件、交互，与SAMPLE2一致
- 工段内容输入内容匹配`101`，按下回车键后，调用`GetLines`接口获取线体数据`assets/GetLines.json`
- 工段和线体选择后，点击启动按钮，提示“启动成功”，否则提示“XX为空”

```bash
# intranet
svn checkout http://10.6.78.246:8080/svn/repo1/interview/ --username yy --password 1234
git clone ssh://git@10.12.5.188:10022/appdev/interview.git

# internet
git clone -b android https://github.com/shanquan/interview.git
```