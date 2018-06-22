package Dictionary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class Window_header extends JFrame {
    Scanner scanner = new Scanner(System.in);
    DefaultListModel listModel1 = new DefaultListModel();
    public static String path = "src/Dictionary/database/";
    private Window_Process process;


    Window_header(String title) {
        init(title);
    }
    void init(String title)//判断确定按钮的问题
    {
        //windows_process_create
        //
        setBackground(Color.black);
        setTitle(title);
        setBounds(500, 300, 500, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        JPanel p1 = new JPanel(new FlowLayout());
        JLabel  label_start= new JLabel("欢迎来到字典小程序~\n\n请输入你要使用/修改的字典",SwingConstants.CENTER);
        label_start.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 18));
        JList list = new JList();
        Vector vt = get_data_node();
        list.setListData(vt);
        p1.add(label_start);
        p1.setBounds(0, 60, 500, 100);
        JScrollPane pane = new JScrollPane(list);
        pane.setBounds(100, 100, 300, 250);
        //button用jpanel p3，采用flowlayout排版
        JPanel p3 = new JPanel(new FlowLayout());
        JButton[] button = new JButton[3];
        button[0] = new JButton("确定");
        button[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //得到选定的文件，进行下一步操作
                //System.out.print(list.getSelectedValue()); //DEBUG
                if (list.getSelectedIndex()==-1) return;
                File process_file=new File(path+list.getSelectedValue());
                process = new Window_Process(process_file);
                process.init(process_file);
            }
        });
        button[1] = new JButton("新建");
        button[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //添加文件名，创建文件数据库
                String inputValue = JOptionPane.showInputDialog("请输入你想创建的字典名");
                if(inputValue==null)
                {
                    return;
                }
                else if(inputValue.equals(""))
                {
                    JOptionPane.showMessageDialog(null,"你没输入任何数据哦~","错误",JOptionPane.ERROR_MESSAGE);
                }
                File newfile = new File(path + inputValue);
                if (newfile.exists()) {
                    JOptionPane.showMessageDialog(null, "已经存在的文件名不允许创建哦~", "Oops", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                newfile.createNewFile();
                listModel1.addElement(inputValue);
                list.setModel(listModel1);
                //写入字典的类型
                new Dictionary_Factory(newfile);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
            }
        });
        button[2] = new JButton("删除");
        button[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //得到选定的文件，从数据库里删除
                if (list.getSelectedIndex() != -1) {
                    File detf = new File(path + list.getSelectedValue());
                    if (detf.exists() && detf.isFile()) {
                        detf.delete();
                        if(detf.exists())
                        {
                            JOptionPane.showMessageDialog(null, "删除失败", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    listModel1.remove(list.getSelectedIndex());
                    list.setModel(listModel1);
                }
                else{
                    JOptionPane.showMessageDialog(null, "请选择要删除的字典", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        for (int i = 0; i < 3; ++i)
            p3.add(button[i]);
        p3.setBounds(100, 370, 300, 100);
        //button
        add(pane);
        add(p1);
        add(p3);
        //add(field);
        //
        setVisible(true);
    }

    Vector get_data_node() {
        listModel1.clear();
        Vector vt = new Vector();
        File file = new File(path);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getName();
            vt.add(files[i].getName());
            listModel1.addElement(name);
        }
        return vt;
    }

}
