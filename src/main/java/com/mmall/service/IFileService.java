package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 唐孟廷
 * @desc 文件服务
 * @date 2020/5/4 - 14:03
 */
public interface IFileService {

    /**
     * 上传文件指定ftp路径下
     *
     * @param file 文件
     * @param path ftp服务器上的路径
     * @return 返回文件在ftp上的路径, 前端可以通过url访问:
     * http://img.tomcatBbzzzs.com/sz/4e3r-uiyg-1654846341354.jpg
     */
    String upload(MultipartFile file, String path);
}
