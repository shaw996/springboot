package com.shaw.springboot.core.service.impl;

import com.shaw.springboot.common.util.Result;
import com.shaw.springboot.core.service.FileService;
import com.shaw.springboot.core.util.MinioUtil;
import com.shaw.springboot.core.vo.BucketVo;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

  private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

  private final MinioUtil minioUtil;

  FileServiceImpl(MinioUtil minioUtil) {
    this.minioUtil = minioUtil;
  }

  /**
   * 上传文件
   *
   * @param file 文件
   * @return 响应结果
   */
  @Override
  public Result<String> upload(MultipartFile file) {
    Result<String> result = null;

    try {
      String fileUrl = minioUtil.putObject(file);
      result = Result.success(fileUrl);
    } catch (Exception e) {
      logger.error("error message", e);
      result = Result.failed(e.getMessage());
    }

    return result;
  }

  /**
   * 预览文件
   *
   * @param filename 文件名称
   * @return 文件数据
   */
  @Override
  public ResponseEntity<byte[]> preview(String filename) {
    ResponseEntity<byte[]> result = null;

    try {
      result = minioUtil.getObject(filename);
    } catch (Exception e) {
      logger.error("error message", e);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.TEXT_PLAIN);

      result = new ResponseEntity<>(e.getMessage()
          .getBytes(StandardCharsets.UTF_8), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return result;
  }

  /**
   * 分享文件
   *
   * @param fileUrl 文件地址
   * @return 分享文件地址
   */
  @Override
  public Result<String> share(String fileUrl) {
    Result<String> result = null;

    try {
      String sharedFileUrl = minioUtil.shareObject(fileUrl);
      result = Result.success(sharedFileUrl);
    } catch (Exception e) {
      logger.error("error message", e);
      result = Result.failed(e.getMessage());
    }

    return result;
  }

  /**
   * 下载文件
   *
   * @param fileUrl 文件url
   * @param response 响应对象
   */
  @Override
  public Result<Void> download(String fileUrl, HttpServletResponse response) {
    Result<Void> result = null;

    try {
      minioUtil.download(fileUrl, response);
      result = Result.success();
    } catch (Exception e) {
      logger.error("error message", e);
      result = Result.failed(e.getMessage());
    }

    return result;
  }

  /**
   * 删除文件
   *
   * @param fileUrl 文件url
   * @return 响应结果
   */
  @Override
  public Result<Boolean> delete(String fileUrl) {
    Result<Boolean> result = null;

    try {
      minioUtil.remove(fileUrl);
      result = Result.success(true);
    } catch (Exception e) {
      logger.error("error message", e);
      result = Result.failed(e.getMessage());
    }

    return result;
  }

  /**
   * 判断桶是否存在
   *
   * @param bucketName bucket名称
   * @return 相应结果
   */
  @Override
  public Result<Boolean> bucketExists(String bucketName) {
    Result<Boolean> result = null;
    result = Result.success(true);
    return result;
  }

  /**
   * 创建桶
   *
   * @param bucketName bucket名称
   * @return 相应结果
   */
  @Override
  public Result<Boolean> makeBucket(String bucketName) {
    Result<Boolean> result = null;
    result = Result.success(true);
    return result;
  }

  /**
   * 删除桶
   *
   * @param bucketName bucket名称
   * @return 相应结果
   */
  @Override
  public Result<Boolean> removeBucket(String bucketName) {
    Result<Boolean> result = null;
    result = Result.success(true);
    return result;
  }

  /**
   * 桶列表
   *
   * @return 相应结果
   */
  @Override
  public Result<List<BucketVo>> listAllBuckets() {
    Result<List<BucketVo>> result = null;
    result = Result.success(List.of());
    return result;
  }
}
