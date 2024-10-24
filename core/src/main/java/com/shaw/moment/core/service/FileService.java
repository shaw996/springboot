package com.shaw.moment.core.service;

import com.shaw.moment.common.util.Result;
import com.shaw.moment.core.vo.BucketVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

  Result<String> upload(MultipartFile file);

  ResponseEntity<byte[]> preview(String filename);

  Result<String> share(String fileUrl);

  Result<Void> download(String fileUrl, HttpServletResponse response);

  Result<Boolean> delete(String fileUrl);

  Result<Boolean> bucketExists(String bucketName);

  Result<Boolean> makeBucket(String bucketName);

  Result<Boolean> removeBucket(String bucketName);

  Result<List<BucketVo>> listAllBuckets();

}
