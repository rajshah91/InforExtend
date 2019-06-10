package com.jeecg.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.CharBuffer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    
    private ZipUtils(){
    }
    
    public static void doCompress(String srcFile, String zipFile) throws IOException {
        doCompress(new File(srcFile), new File(zipFile));
    }
    
    /**
     * 文件压缩
     * @param srcFile 目录或者单个文件
     * @param zipFile 压缩后的ZIP文件
     */
    public static void doCompress(File srcFile, File zipFile) throws IOException {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            doCompress(srcFile, out);
        } catch (Exception e) {
            throw e;
        } finally {
            out.close();//记得关闭资源
        }
    }
    
    public static void doCompress(String filelName, ZipOutputStream out) throws IOException{
        doCompress(new File(filelName), out);
    }
    
    public static void doCompressByUrl(String filelName, ZipOutputStream out,String name) throws IOException{
    	doZipByUrl(filelName,out,name);
        //doCompress(new File(url.openStream()), out);
    }
    
    public static void doCompress(File file, ZipOutputStream out) throws IOException{
        doCompress(file, out, "");
    }
    
    public static void doCompress(File inFile, ZipOutputStream out, String dir) throws IOException {
        if ( inFile.isDirectory() ) {
            File[] files = inFile.listFiles();
            if (files!=null && files.length>0) {
                for (File file : files) {
                    String name = inFile.getName();
                    if (!"".equals(dir)) {
                        name = dir + "/" + name;
                    }
                    ZipUtils.doCompress(file, out, name);
                }
            }
        } else {
             ZipUtils.doZip(inFile, out, dir);
        }
    }
    
    public static void doZipByUrl(String filelName, ZipOutputStream out, String name) throws IOException {
    	URL url = new URL(filelName);
        URLConnection c = url.openConnection();
        c.connect();
    	ZipEntry entry = new ZipEntry(name);
        out.putNextEntry(entry);
       
        InputStream is = c.getInputStream();
        
        int len = 0 ;
        /*FileInputStream fis = new FileInputStream(inFile);*/
        byte[] buffer=new byte[2048];
        while((len=is.read(buffer))!=-1){
        	out.write(buffer,0,len);
        }
        out.closeEntry();
        is.close();
    }
    
    public static void doZip(File inFile, ZipOutputStream out, String dir) throws IOException {
        String entryName = null;
        if (!"".equals(dir)) {
            entryName = dir + "/" + inFile.getName();
        } else {
            entryName = inFile.getName();
        }
        ZipEntry entry = new ZipEntry(entryName);
        out.putNextEntry(entry);
        
        int len = 0 ;
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(inFile);
        while ((len = fis.read(buffer)) > 0) {
            out.write(buffer, 0, len);
            out.flush();
        }
        out.closeEntry();
        fis.close();
    }
    
    public static void main(String[] args) throws IOException {
        //doCompress("E:/杂物/", "E:/杂物.zip");
       /* for(int i=1;i<5;i++) {
        	doCompress("E:/TEST/"+i+".jpg", "E:/TEST.zip");
        }*/
        //System.exit(0);
    }
    
}