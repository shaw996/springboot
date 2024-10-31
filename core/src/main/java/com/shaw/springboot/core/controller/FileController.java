package com.shaw.springboot.core.controller;

import com.shaw.springboot.common.util.Result;
import com.shaw.springboot.core.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件模块")
@Slf4j
@RestController
@RequestMapping("file")
public class FileController {

  @Autowired
  private FileService fileService;

  @Operation(summary = "上传文件")
  @PostMapping("upload")
  public Result<String> upload(@Parameter(description = "文件对象") @RequestParam("file") MultipartFile file) {
    return fileService.upload(file);
  }

  @Operation(summary = "预览文件")
  @GetMapping("preview/{filename}")
  public ResponseEntity<byte[]> getSource(@Parameter(description = "文件名称") @PathVariable("filename") String filename) {
    return fileService.preview(filename);
  }


  @Operation(summary = "分享文件")
  @GetMapping("share")
  public Result<String> share(@Parameter(description = "文件url", example = "/share?fileUrl=<URL>") @RequestParam(
      "fileUrl") String fileUrl) {
    return fileService.share(fileUrl);
  }

  @Operation(summary = "下载文件")
  @GetMapping("download")
  public Result<Void> download(@Parameter(description = "文件url", example = "/download?fileUrl=<URL>") @RequestParam(
      "fileUrl") String fileUrl,
                               HttpServletResponse response) {
    return fileService.download(fileUrl, response);
  }

  @Operation(summary = "删除文件")
  @PostMapping("delete")
  public Result<Boolean> delete(@Parameter(description = "文件url", example = "{ fileUrl: <URL> }") @RequestParam(
      "fileUrl") String fileUrl) {
    return fileService.delete(fileUrl);
  }

  //  @GetMapping("/bucketExists")
  //  public ResultVo<Boolean> bucketExists(@RequestParam("bucketName") String bucketName) {
  //    return fileService.bucketExists(bucketName);
  //  }
  //
  //  @GetMapping("/makeBucket")
  //  public ResultVo<Boolean> makeBucket(@RequestParam("bucketName") String bucketName) {
  //    return fileService.makeBucket(bucketName);
  //  }
  //
  //  @GetMapping("/removeBucket")
  //  public ResultVo<Boolean> removeBucket(@RequestParam("bucketName") String bucketName) {
  //    return fileService.removeBucket(bucketName);
  //  }
  //
  //  @GetMapping("/getAllBuckets")
  //  public ResultVo<List<BucketVo>> getAllBuckets() {
  //    return fileService.listAllBuckets();
  //  }
}
