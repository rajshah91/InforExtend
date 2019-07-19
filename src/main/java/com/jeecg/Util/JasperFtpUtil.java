package com.jeecg.Util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.dom4j.Document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.SocketException;

public class JasperFtpUtil {
    private static FTPClient getFTPClient(String ip, String username, String password, String workdir) {
        FTPClient ftpClient = new FTPClient();
        try {

            // 连接FTP服务器 端口默认21
            ftpClient.connect(ip, 21);
            ftpClient.login(username, password);// 登陆FTP服务器
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
            }
        } catch (SocketException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        return ftpClient;
    }

    public static boolean setJasper(String ip, String username, String password, String workdir, Document doc, String _Printername) {
        FTPClient ftpClient = getFTPClient(ip,username,password,workdir);
        try {
            ftpClient.changeWorkingDirectory(workdir);
            ftpClient.storeFile(String.valueOf(System.currentTimeMillis())+"@"+_Printername+".xml",new ByteArrayInputStream((doc.asXML().getBytes())));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
