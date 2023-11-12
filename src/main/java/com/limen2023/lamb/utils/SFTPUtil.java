package com.limen2023.lamb.utils;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * 类描述：
 * 上传文件到服务器的 工具类
 *
 * @ClassName SFTPUtil
 * @Author msi
 * @Date 2020/9/2 23:29
 * @Version 1.0
 */
@Component
public class SFTPUtil {

    /**
     * 返回公网访问的地址前缀
     */
    @Value("${customize.remoteServer.sftp.SFTP_httpBaseUrl}")
    protected String baseUrl;
    /**
     * 公网访问的端口
     */
    @Value("${customize.remoteServer.sftp.SFTP_httpPort}")
    protected int port;
    /**
     * 主机保存的目录
     */
    @Value("${customize.remoteServer.sftp.SFTP_directory}")
    protected String directory;
    /**
     * 主机的IP
     */
    @Value("${customize.remoteServer.sftp.SFTP_host}")
    protected String host;
    /**
     * ssh端口
     */
    @Value("${customize.remoteServer.sftp.SFTP_port}")
    protected int sshPort;
    /**
     * 用户名
     */
    @Value("${customize.remoteServer.sftp.SFTP_username}")
    protected String username;
    /**
     * 密码
     */
    @Value("${customize.remoteServer.sftp.SFTP_password}")
    protected String password;

    /**
     * 上传多文件到指定远程主机
     * @param file
     * @return list
     */
    public List<String> uploadMultipartFilesToServer(MultipartFile file) throws SftpException, JSchException, IOException {
        List<String> list = new ArrayList<>();
        ChannelSftp sftp = null;
        Session session = null;
        sftp = this.connect(this.host, this.sshPort, this.username, this.password);
        session = sftp.getSession();
//        for (int i = 0; i < files.length; i++) {
//
//        }
        String originalFilename = file.getOriginalFilename();
        // 生成文件夹名 yyyy-mm
        String relativePath = new StringBuilder().append(LocalDate.now().getYear())
                .append("-").append(LocalDate.now().getMonthValue()).toString();

        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();

        int lastIndex = originalFilename.lastIndexOf(".");
        String fileSuffix = originalFilename.substring(lastIndex);
        String filePrefix = originalFilename.substring(0, lastIndex);
        String fileName = new StringBuilder().append(filePrefix).append(uuid).append(fileSuffix).toString();

        // 文件上层目录
        String directory = this.directory + relativePath;
        // 创建文件夹
        this.createDir(directory, sftp);
        // 进入文件夹内
        sftp.cd(directory);
        // 创建文件
        sftp.put(file.getInputStream(), fileName);
        // 拼接返回格式
        String s = new StringBuilder("http://").append(this.host).append(":").append(this.port)
                .append(this.baseUrl).append(relativePath).append("/").append(fileName).toString();

        list.add(s);
        // 关掉连接
        sftp.disconnect();
        sftp.getSession().disconnect();

        return list;
    }

    /**
     * 建立连接
     * @param host  主机
     * @param port  端口
     * @param username  用户名
     * @param password  密码
     * @return
     */
    public ChannelSftp connect(String host, int port, String username,
                               String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sftp;
    }

    /**
     * 创建目录
     *
     */
    public void createDir(String createpath, ChannelSftp sftp) {
        try {
            if (isDirExist(sftp, createpath)) {
                sftp.cd(createpath);
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            // 循环创建目录
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(sftp, filePath.toString())) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                }
            }
            sftp.cd(createpath);
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断目录是否存在
     */
    public boolean isDirExist(ChannelSftp sftp, String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }
}