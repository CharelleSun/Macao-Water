package maintenance.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("maintenance_notice")



public class MaintenanceNotice {
    @TableId(type = IdType.AUTO)
    private Long id;

    //    @jakarta.validation.constraints.NotBlank(message = "标题不能为空")
//    @Size(max = 200, message = "标题长度不能超过200字符")
    private String title;

    //    @NotBlank(message = "内容不能为空")
    private String content;

    private String contentEng;
    private String contentPor;

//            //    @NotNull(message = "开始时间不能为空")
//        //    @FutureOrPresent(message = "开始时间必须大于当前时间")
//            @TableField("starttime")
//            private LocalDateTime startTime;
//
//            //    @NotNull(message = "结束时间不能为空")
//            @TableField("endtime")
//            private LocalDateTime endTime;

    @TableField("starttime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @TableField("endtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

//    @TableField(fill = FieldFill.INSERT)
//    private LocalDateTime createdAt;
//
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private LocalDateTime updatedAt;



    public MaintenanceNotice() {
        startTime = null;
    }

    //    @AssertTrue(message = "结束时间必须大于开始时间")
    public boolean isEndAfterStart() {
        return endTime.isAfter(startTime);
    }


}