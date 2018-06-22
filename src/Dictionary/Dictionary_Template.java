package Dictionary;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

abstract class Dictionary_Template {
    public static String path = "src/Dictionary/database/";
    abstract boolean addText(String src, String dest);
    abstract DefaultListModel<String> query(String query_name, DefaultListModel<String> defaultListModel) throws IOException;
    abstract boolean modify(String ModifiedValue,String ModifytoString,File FileNowEdited) throws IOException;
    abstract boolean delete(String DeleteValue);
    String Dictionary_Name;
    String src;//输入数据的自定义名称类型
    String dest;//输入数据对应的自定义名称类型
}
