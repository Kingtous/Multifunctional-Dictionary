package Dictionary;

import javax.swing.*;
import java.io.*;

public class Dictionary extends Dictionary_Template{
    private static String path = "src/Dictionary/database/";
    private File file;//ensure this file cannot be causually edited
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    String[] split_name=new String[2];
    String GetLine;


    public Dictionary(File file)
    {
        this.file=file;
        try {
            fileInputStream = new FileInputStream(file);
            fileOutputStream=new FileOutputStream(file,true);//append true
            InputStreamReader reader= null;
            reader = new InputStreamReader(fileInputStream,"UTF-8");//read
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(fileOutputStream));//write
            bufferedReader=new BufferedReader(reader);
            bufferedReader.mark((int) file.length()+1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //ensure the first line is exist
            String[] src_dest=bufferedReader.readLine().split("\\s+");
            src=src_dest[0];
            dest=src_dest[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    boolean addText(String src_txt,String dest_txt) {
        if (file.exists())
        {
            try {
                bufferedWriter.write(src_txt+" "+dest_txt+"\n");
                //must have the statement bufferedwriter.close(),otherwise it won't do anything on your data
                bufferedWriter.flush();
                //bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));//write
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else return false;
        return true;
    }
    @Override
    DefaultListModel<String> query(String query_name, DefaultListModel<String> defaultListModel) throws IOException {
        defaultListModel.clear();
        bufferedReader.mark((int) file.length());//MUST DO IT!!!
        bufferedReader.reset();
        //first line is the types,input into src,dest
        while ((GetLine=bufferedReader.readLine())!=null)
        {
            split_name=GetLine.split("\\s+");
            if (split_name[0].startsWith(query_name,0))
                defaultListModel.addElement(split_name[0]);
        }
        bufferedReader.reset();
        return  defaultListModel;//get_regex_name
    }

    @Override
    boolean modify(String ModifiedValue,String ModifytoString,File FileNowEdited) throws IOException {
        File tmp_file=new File(path+"tmp_file");
        if (!tmp_file.createNewFile()) return false;
        bufferedReader.mark((int) file.length());
        bufferedReader.reset();
        bufferedReader.readLine();
        BufferedWriter modify_writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmp_file,true)));
        while ((GetLine=bufferedReader.readLine())!=null)
        {
            split_name=GetLine.split("\\s+");
            if (split_name[0].equals(ModifiedValue))
                modify_writer.write(split_name[0]+" "+ModifytoString+"\n");
            else modify_writer.write(split_name[0]+" "+split_name[1]+"\n");
        }
        modify_writer.flush();
        File name=new File(path+FileNowEdited.getName());
        FileNowEdited.delete();
        tmp_file.renameTo(name);
        bufferedReader.mark((int) file.length());
        bufferedReader.reset();
        return true;
    }

    @Override
    boolean delete(String DeleteValue) {
        return false;
    }


}
