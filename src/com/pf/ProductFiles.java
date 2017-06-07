package com.pf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

public class ProductFiles {
    //生成文件路径
    private static String path = "D:\\file\\";
    
    //文件路径+名称
    private static String filenameTemp;
    
    private static int lineNum=30000;
    /**
     * 创建文件
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
    public static boolean createFile(String fileName,String filecontent){
        Boolean bool = false;
        filenameTemp = path+fileName+".txt";//文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is "+filenameTemp);
                //创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bool;
    }

    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr;
        String temp  = "";
        
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();
            
            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);
            
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }
    public static void main(String[] args) throws IOException {
    	FileOutputStream out = null; 
    	String filename=null;
       while(true) {
    	      UUID uuid = UUID.randomUUID();
        	  StringBuffer pline =new StringBuffer();
              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
              
              for (int i =1; i <=lineNum; i++) {
              	
      		    if(i<lineNum){
      		    	pline.append(i+"|91.106.76.32|192.170.76.32|1234|1239|1|"+dateFormat.format(new Date()) +"|0|78542ef64e9d|0"+System.getProperty("line.separator"));
      		    }else{
      		    	pline.append(i+"|91.106.76.32|192.170.76.32|1234|1239|1|"+dateFormat.format(new Date()) +"|0|78542ef64e9d|0");
      		    }
              }
              
              String line =pline.toString();
              createFile("aaa_"+dateFormat.format(new Date())+uuid+"", line);
              
              filename="aaa_"+dateFormat.format(new Date()).substring(0,dateFormat.format(new Date()).length()-6)+uuid;
              File newfile=new File(path+"/aaa_"+dateFormat.format(new Date())+uuid+".zip");
              File file = ZipUtil.zip(filenameTemp);
              file.renameTo(newfile);
              
              FileInputStream fis= new FileInputStream(filenameTemp);  
      		  String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));  
      		  IOUtils.closeQuietly(fis);  
              
              File sgnfile = new File(path+"/aaa_"+dateFormat.format(new Date())+uuid+".sgn");
              out = new FileOutputStream(path+"/aaa_"+dateFormat.format(new Date())+uuid+".sgn");
              String sgnOut="filename:"+filename+System.getProperty("line.separator")
            		        +"file size:"+filenameTemp.length()+System.getProperty("line.separator")
                            +"record number："+lineNum+System.getProperty("line.separator")
                            +"MD5 "+md5;

              out.write(sgnOut.getBytes());
              sgnfile.createNewFile(); 
		}
    }
}

