package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author 唐孟廷
 * @desc 文件实现类
 * @date 2020/5/4 - 16:28
 */
@Service("com.mmall.service.impl.FileServiceImpl")
public class FileServiceImpl implements IFileService {
    private Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 上传文件指定ftp路径下
     *
     * @param file 文件
     * @param path ftp服务器上的路径
     * @return 返回文件在ftp上的路径, 前端可以通过url访问:
     * http://img.tomcatBbzzzs.com/sz/4e3r-uiyg-1654846341354.jpg
     */
    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();

        // 扩展名 abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);

        // 上传到ftp的文件名称
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        log.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);


        try {
            file.transferTo(targetFile);
            // 文件已经上传成功了
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // 已经上传到ftp服务器上

            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常",e);
            return null;
        }
        // A:abc.jpg
        // B:abc.jpg
        return targetFile.getName();
    }
}
