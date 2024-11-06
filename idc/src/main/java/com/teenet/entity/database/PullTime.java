package com.teenet.entity.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 从客户那拉取的时间
 * @Author threedong
 * @Date: 2022/7/14 9:21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PullTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime inTime;

    private Boolean successIs;

    /**
     * 1客户数据，2高频上送数据
     */
    private Integer type;

    private String content;

    /**
     * 数据重新上传的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime uploadAgainTime;

    private String pueM;
    private String pueD;
}
