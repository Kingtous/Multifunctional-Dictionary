package Dictionary;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Dictionary_Factory extends Dictionary_Template{
    public Dictionary_Factory(File file) throws IOException {
        this.Dictionary_Name=file.getName();
        if(init_types()) {
            //write into the file accordingly
            if (file.canWrite()) {
                write_src_dest(file);
                JOptionPane.showMessageDialog(null, "创建成功，你可以在字典里写入数据了~", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "文件没有写权限~", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            file.delete();
            JOptionPane.showMessageDialog(null, "创建失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    boolean init_types()
    {
        try {
            Thread.sleep(400);//precaution the user's action is too fast and press "Enter" more than one times
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        src= JOptionPane.showInputDialog("你的翻译源的总称是？例如：“英语-中文”的中的英语");
        if(src==null) return false;
        try {
            Thread.sleep(400);//precaution the user's action is too fast and press "Enter" more than one times
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dest=JOptionPane.showInputDialog("你的翻译目标源的总称是？例如：“英语-中文”的中的中文");
        if(dest==null) return false;
        if(src.equals("") || dest.equals(""))
        {
            JOptionPane.showMessageDialog(null,"你有一项或多项没输入任何数据哦~","错误",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    //write types of the data
    void write_src_dest(File file) throws IOException {
        FileOutputStream outputStream=new FileOutputStream(file,false);//重写
        StringBuffer sb=new StringBuffer(src+" "+dest+"\n");
        outputStream.write(sb.toString().getBytes("utf-8"));
        outputStream.close();
    }

    @Override
    boolean addText(String src, String dest) {
        return false;
    }

    @Override
    DefaultListModel<String> query(String query_name, DefaultListModel<String> defaultListModel) throws IOException {
        return null;
    }

    @Override
    boolean modify(String ModifiedValue, String ModifytoString, File FileNowEdited) throws IOException {
        return false;
    }


    @Override
    boolean delete(String DeleteValue) {
        return false;
    }

    //of no use now

}
