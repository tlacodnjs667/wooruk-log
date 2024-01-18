package org.itwill.springboot4.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FileUploader {

    private String host = "192.168.20.11";
    private Integer port = 21;
    private String user = "admin";
    private String password = "123123";

    public FileUploader() {

    }

    /**
     * DB 서버 내에 파일 저장하여 저장된 경로를 반환하는 메서드
     *
     * @param file        업로드할 MultipartFile
     * @param servletPath HttpServletRequest 타입의 request
     * @return 업로드된 이미지의 경로
     * @throws IOException 아마자 업로드 시 문제 발생 시 IOException qkftod
     */
    public String upload(MultipartFile file, String servletPath) throws IOException {

        FTPClient ftpClient = new FTPClient();

        connect(ftpClient);

        String result = null;

        List<String> filePath = mkDirByRequestUri(servletPath, ftpClient);
        InputStream inputStream = file.getInputStream();

        setFtpClientConfig(ftpClient);

        String fileName =
            UUID.randomUUID().toString() + "." + file.getOriginalFilename().split("\\.")[1];

        boolean uploadResult = ftpClient.storeFile(fileName, inputStream);

        if (uploadResult) {
            filePath.add(fileName);
            log.debug("파일 업로드 완료");
            result = String.join("/", filePath);
        }

        disconnect(ftpClient);
        return result;
    }


    /**
     * 경로를 주면 file 정보가 담겨있는 Resource 타입의 객체 반환
     *
     * @param imgUrl upload 메서드 시 제공된 경로
     * @return 해당 파일의 binary 데이터가 담긴 Resource 객체
     * @throws IOException
     */
    public Resource download(String imgUrl) throws IOException {
        FTPClient ftpClient = new FTPClient();

        connect(ftpClient);
        setFtpClientConfig(ftpClient);
        InputStream imgStream = ftpClient.retrieveFileStream(imgUrl);
        byte[] result = IOUtils.toByteArray(imgStream);

        if (result.length == 0) {
            log.error("::DB에 데이터 조회 실패");
            return null;
        }

        Resource resource = new ByteArrayResource(result);
        imgStream.close();
        disconnect(ftpClient);
        return resource;
    }

    /**
     * 해당 경로의 파일 삭제하는 메서드
     *
     * @param imgUrl 삭제할 파일의 경로
     * @return 성공 시 true, 실패 시 false 반환
     */
    public boolean delete(String imgUrl) throws IOException {
        boolean result = false;
        FTPClient ftpClient = new FTPClient();

        connect(ftpClient);
        result = ftpClient.deleteFile(imgUrl);
        disconnect(ftpClient);

        return result;
    }

    public boolean connect(FTPClient ftpClient) throws IOException {
        log.debug("connecting to... {}", host);

        ftpClient.addProtocolCommandListener(
            new PrintCommandListener(new PrintWriter(System.out), true));

        ftpClient.connect(host, port);
        int reply = ftpClient.getReplyCode();

        if (!FTPReply.isPositiveCompletion(reply)) {
            try {
                disconnect(ftpClient);
            } catch (IOException e) {
                log.error("연결에 실패하였습니다. {}", e.getMessage());
                return false;
            }
        }

        return ftpClient.login(user, password);
    }

    public void disconnect(FTPClient ftpClient) throws IOException {
        log.debug("disconnecting from {}", host);

        ftpClient.logout();
        ftpClient.disconnect();
    }

    public void setFtpClientConfig(FTPClient ftpClient) throws IOException {
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
        ftpClient.setAutodetectUTF8(true);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    }

    public List<String> mkDirByRequestUri(String servletPath, FTPClient ftpClient)
        throws RuntimeException, IOException {
        List<String> result = new ArrayList<>();

        List<String> paths = Arrays.stream(servletPath.split("/")).toList();

        log.debug("paths={}", paths);
        for (String path : paths) {
            if (path.isEmpty()) {
                continue;
            }
            result.add(path);

            if (!ftpClient.changeWorkingDirectory(path)) {
                ftpClient.makeDirectory(path);
                ftpClient.changeWorkingDirectory(path);
            }
        }

        return result;
    }

}
