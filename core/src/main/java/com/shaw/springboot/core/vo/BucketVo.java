package com.shaw.springboot.core.vo;

import io.minio.messages.Bucket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
public class BucketVo {

  @Schema(description = "bucket名称")
  private String name;

  @Schema(description = "创建时间")
  private String creationDate;

  public BucketVo() {
  }

  public BucketVo(Bucket bucket) {
    this.name = bucket.name();
    this.creationDate = bucket.creationDate()
        .withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINESE));
  }
}
