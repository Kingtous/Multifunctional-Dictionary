package Dictionary;

import javax.swing.*;

        public class launcher {
            public static String path = "src/Dictionary/database/";

            public static void main(String[] args) throws InterruptedException {
                //welcome in terminal
                System.out.println("欢迎来到我的程序，我是作者\"金韬\"");
                //Thread.sleep(1000);
                System.out.println("我的学号是20178401,来自计算机类1702班");
                //Thread.sleep(1000);
                System.out.println("加载中...");
                //Thread.sleep(1000);
                //open GUI and start the program
                    Window_header window=new Window_header("多功能字典 v1.0 作者：金韬");
            }
}
