package com.shaw.moment.core.util;

import com.shaw.moment.core.config.MinioConfig;
import com.shaw.moment.core.vo.BucketVo;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class MinioUtil {

  private final Logger logger = LoggerFactory.getLogger(MinioUtil.class);

  private final MinioConfig minioConfig;

  private final MinioClient minioClient;

  private final String minioBaseUrl;

  MinioUtil(MinioConfig minioConfig, MinioClient minioClient) {
    this.minioConfig = minioConfig;
    this.minioClient = minioClient;
    this.minioBaseUrl = minioConfig.getBaseUrl();
  }

  /**
   * 对象名称转换为文件名称
   *
   * @param objectName 对象名称
   * @return 文件名称
   */
  private String objectName2Filename(String objectName) {
    return objectName.replaceFirst("/", "-");
  }

  /**
   * 文件名称转换为对象名称
   *
   * @param filename 文件名称
   * @return 对象名称
   */
  private String filename2ObjectName(String filename) {
    return filename.replaceFirst("-", "/");
  }

  /**
   * 文件url转换为对象名称
   *
   * @param fileUrl 文件url
   * @return 对象名称
   */
  private String fileUrl2ObjectName(String fileUrl) {
    String filename = fileUrl.substring(minioBaseUrl.length());
    return filename2ObjectName(filename);
  }

  /**
   * 上传文件（对象同名会被覆盖）
   *
   * @param file 文件
   * @return 文件地址
   */
  public String putObject(MultipartFile file) throws Exception {
    String originalFilename = file.getOriginalFilename();

    if (StringUtils.isBlank(originalFilename)) {
      throw new RuntimeException();
    }

    // 生成名称
    String filename = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
    String objectName = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + filename;

    PutObjectArgs putObjectArgs = PutObjectArgs.builder()
        .bucket(minioConfig.getBucketName())
        .object(objectName)
        .stream(file.getInputStream(), file.getSize(), -1)
        .contentType(file.getContentType())
        .build();
    minioClient.putObject(putObjectArgs);

    return minioBaseUrl + objectName2Filename(objectName);
  }

  /**
   * 查看文件
   *
   * @param filename 文件名称
   * @return 文件数据
   */
  public ResponseEntity<byte[]> getObject(String filename) throws Exception {
    ResponseEntity<byte[]> result = null;

    String objectName = filename2ObjectName(filename);

    GetObjectArgs getObjectArgs = GetObjectArgs.builder()
        .bucket(minioConfig.getBucketName())
        .object(objectName)
        .build();

    try (GetObjectResponse getObjectResponse = minioClient.getObject(getObjectArgs)) {
      byte[] bytes = new byte[1024];
      int len;


      try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
        while ((len = getObjectResponse.read(bytes)) != -1) {
          os.write(bytes, 0, len);
        }
        os.flush();
        byte[] byteArray = os.toByteArray();

        // 设置请求头
        StatObjectArgs statObjectArgs = StatObjectArgs.builder()
            .bucket(minioConfig.getBucketName())
            .object(objectName)
            .build();
        String contentType = minioClient.statObject(statObjectArgs)
            .contentType();
        MediaType mediaType = MediaType.parseMediaType(contentType);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        result = new ResponseEntity<>(byteArray, headers, HttpStatus.OK);
      }

    }

    return result;
  }

  /**
   * 分享文件
   *
   * @param fileUrl 文件url
   * @return 文件地址（临时）
   */
  public String shareObject(String fileUrl) {
    // TODO: 生成临时文件地址
    // 1. 生成临时文件名
    // 2. 将临时文件名与真实文件名放到redis中：set <临时文件名> <真实文件名> ex 604800
    // 3. 返回临时文件名组成的url

    String objectName = fileUrl2ObjectName(fileUrl);
    String filename = objectName2Filename(objectName);
    return minioBaseUrl + filename;
  }


  /**
   * 下载文件
   *
   * @param fileUrl 文件url
   * @param res     response对象
   */
  public void download(String fileUrl, HttpServletResponse res) throws Exception {
    String objectName = fileUrl2ObjectName(fileUrl);

    GetObjectArgs getObjectArgs = GetObjectArgs.builder()
        .bucket(minioConfig.getBucketName())
        .object(objectName)
        .build();

    try (GetObjectResponse response = minioClient.getObject(getObjectArgs)) {
      byte[] buf = new byte[1024];
      int len;

      try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
        while ((len = response.read(buf)) != -1) {
          os.write(buf, 0, len);
        }
        os.flush();
        byte[] bytes = os.toByteArray();

        res.setCharacterEncoding("UTF-8");
        res.addHeader("Content-Disposition", "attachment;filename=" + objectName);

        try (ServletOutputStream stream = res.getOutputStream()) {
          stream.write(bytes);
          stream.flush();
        }
      }
    }
  }


  /**
   * 删除文件
   *
   * @param fileUrl 文件url
   */
  public void remove(String fileUrl) throws Exception {
    String objectName = fileUrl2ObjectName(fileUrl);

    RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
        .bucket(minioConfig.getBucketName())
        .object(objectName)
        .build();
    minioClient.removeObject(removeObjectArgs);
  }

  /**
   * 查看bucket是否存在
   *
   * @param bucketName bucket名称
   * @return bucket是否存在
   */
  public boolean bucketExists(String bucketName) {
    try {
      return minioClient.bucketExists(BucketExistsArgs.builder()
          .bucket(bucketName)
          .build());
    } catch (Exception e) {
      logger.error("error message", e);
      return false;
    }
  }

  /**
   * 创建bucket
   *
   * @param bucketName bucket名称
   * @return bucket是否成功创建
   */
  public boolean makeBucket(String bucketName) {
    try {
      minioClient.makeBucket(MakeBucketArgs.builder()
          .bucket(bucketName)
          .build());
      return true;
    } catch (Exception e) {
      logger.error("error message", e);
      return false;
    }
  }

  /**
   * 移除bucket
   *
   * @param bucketName bucket名称
   * @return bucket是否成功移除
   */
  public boolean removeBucket(String bucketName) {
    try {
      minioClient.removeBucket(RemoveBucketArgs.builder()
          .bucket(bucketName)
          .build());
      return true;
    } catch (Exception e) {
      logger.error("error message", e);
      return false;
    }
  }

  /**
   * 获取全部bucket
   *
   * @return bucket列表
   */
  public List<BucketVo> getAllBuckets() {
    try {
      return minioClient.listBuckets()
          .stream()
          .map(BucketVo::new)
          .toList();
    } catch (Exception e) {
      logger.error("error message", e);
      return null;
    }
  }

  /**
   * 获取文件的分享路径
   *
   * @param objectName 对象名称
   * @return 文件地址
   */
  public String getPresignedObjectUrl(String objectName) {
    // 查看文件地址
    GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
        .bucket(minioConfig.getBucketName())
        .object(objectName)
        .method(Method.GET)
        .build();

    String fileUrl;

    try {
      fileUrl = minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
    } catch (Exception e) {
      logger.error("error message", e);
      fileUrl = null;
    }

    return fileUrl;
  }

  /**
   * 查看文件对象
   *
   * @return bucket内文件对象信息
   */
  public List<Item> listObjects(String bucketName) {
    ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
        .bucket(minioConfig.getBucketName())
        .build();
    Iterable<Result<Item>> results = minioClient.listObjects(listObjectsArgs);

    List<Item> items = new ArrayList<>();
    try {
      for (Result<Item> result : results) {
        items.add(result.get());
      }
    } catch (Exception e) {
      logger.error("error message", e);
      items = null;
    }

    return items;
  }
}
