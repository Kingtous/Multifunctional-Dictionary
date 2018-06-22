package Dictionary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

import static java.lang.Thread.sleep;

public class Window_Process extends JFrame{

    private Dictionary dictionary;
    private String[] input=new String[2];
    public JTextArea return_text=new JTextArea();
    public JList<String> query_list=new JList<>();
    Window_Process(File file){
        this.dictionary=new Dictionary(file);
    }

    void init(File file)
    {
        //thread 2 to show translation
        Show_Translation translation = new Show_Translation(file);
        Thread thread2=new Thread(translation);
        ////
        setBackground(Color.black);
        setTitle("多功能字典v1.0---"+file.getName());
        setBounds(500, 300, 500, 500);
        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        JTextArea area_query=new JTextArea();
        //label_state
        JLabel label_state=new JLabel("您正在使用的字典是：\n"+file.getName(),SwingConstants.CENTER);
        label_state.setBounds(0,20,500,20);
        add(label_state);
        //textarea_get_text
        JLabel label_get_text=new JLabel("查询结果",SwingConstants.CENTER);
        label_get_text.setBounds(30,70,200,20);
        label_get_text.setFont(new java.awt.Font("微软雅黑",java.awt.Font.PLAIN,18));


        //JTextarea->Jlist

        DefaultListModel<String> query_list_module= new DefaultListModel<>();  //to edit the query_list
        ////JTextArea get_text=new JTextArea();
        JScrollPane p1=new JScrollPane(query_list);
        p1.setBounds(30,100,200,340);
        add(p1);
        add(label_get_text);
        //textarea_return_text

        JLabel label_return_text=new JLabel("字典释义",SwingConstants.CENTER);
        label_return_text.setFont(new java.awt.Font("微软雅黑",java.awt.Font.PLAIN,18));
        label_return_text.setBounds(250,70,200,20);
        JScrollPane p2=new JScrollPane(return_text);
        p2.setBounds(250,100,200,100);
        add(p2);
        add(label_return_text);
        //textfield_input
        JTextField field = new JTextField("请输入您要查询的单词或单词的部分",20);
        field.addMouseListener(new MouseListener() {//鼠标点击清除提示信息
            @Override
            public void mouseClicked(MouseEvent e) {
                field.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        field.setBounds(250,240,200,30);
        add(field);
        //buttons
        JPanel p3 = new JPanel(null);
        p3.setBounds(260,290,200,200);
        JButton button[]=new JButton[5];
        button[0]=new JButton("查询");
        button[0].setLocation(10,10);
        button[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query_name=field.getText();
                if(query_name!=null || query_name!="" || query_name!="请输入您要查询的单词或单词的部分") {
                    try {
                        query_list.setModel(dictionary.query(query_name,query_list_module));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });


        button[1]=new JButton("修改");
        button[1].setLocation(100,10);
        button[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    synchronized (thread2)
                    {

                    }
                if(query_list.getSelectedIndex()!=-1) {
                    input[0]=JOptionPane.showInputDialog("请输入你修改的释义");
                    try {
                        dictionary.modify(query_list.getSelectedValue(),input[0],file);
                        JOptionPane.showMessageDialog(null,"成功！","修改元素",JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null,"失败！","修改元素",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        button[2]=new JButton("关闭");
        button[2].setLocation(100,60);
        button[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        button[3]=new JButton("添加");
        button[3].setLocation(10,110);
        button[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (thread2)
                {

                }
                input[0]=JOptionPane.showInputDialog("请输入所对应的源，例如happy-高兴中的happy");
                input[1]=JOptionPane.showInputDialog("请输入所对应的源，例如happy-高兴中的高兴");
                if (dictionary.addText(input[0],input[1]))
                {
                    JOptionPane.showMessageDialog(null,"成功！","添加元素",JOptionPane.INFORMATION_MESSAGE);
                }
                else JOptionPane.showMessageDialog(null,"处理过程中出现了错误！","添加元素",JOptionPane.ERROR_MESSAGE);

            }
        });


        button[4]=new JButton("删除");//modify+delete
        button[4].setLocation(100,110);
        button[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    thread2.wait();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if(query_list.getSelectedIndex()!=-1)
                {
                    dictionary.delete(query_list.getSelectedValue());
                    thread2.notifyAll();
                }
            }
        });

        //p1 specific
        button[0].setSize(80,90);
        p3.add(button[0]);
        for(int i=1;i<5;++i){
            button[i].setSize(80,40);
            p3.add(button[i]);
        }
        add(p3);
        setVisible(true);
        //Thread 2 get started
        thread2.start();
    }

    public class Show_Translation implements Runnable {
        private BufferedReader reader;
        private File file;
        private InputStreamReader inputStreamReader;
        private String[] message=new String[2];
        Show_Translation(File file)
        {
            this.file=file;
            try {
                FileInputStream stream=new FileInputStream(this.file);
                this.inputStreamReader= new InputStreamReader(stream,"UTF-8");//read
                this.reader=new BufferedReader(this.inputStreamReader);
                this.reader.mark((int)this.file.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {//it can throw InterruptedException
            while (true)
            {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!query_list.isSelectionEmpty())//already selected
                {
                    try {
                        reader.reset();
                        reader.mark((int) file.length());
                        reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    while (true)
                    {
                        try {
                            message=reader.readLine().split("\\s+");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (message==null) break;
                        if (message[0].equals(query_list.getSelectedValue()))
                        {
                            return_text.setText(message[1]);
                            break;
                        }
                    }
                }
                else continue;
            }
        }
    }
}
