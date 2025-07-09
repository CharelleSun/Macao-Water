package maintenance.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import maintenance.entity.MaintenanceNotice;
import maintenance.model.PageResult;
import maintenance.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/maintenance")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class MaintenanceController {
    @Autowired
    MaintenanceService maintenanceService;

    // 发送验证码接口
    @PostMapping("/verification/send-code")
    public ResponseEntity<?> sendVerificationCode() {
        try {
            maintenanceService.sendVerificationCode();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("发送验证码失败: " + e.getMessage());
        }
    }

    @PostMapping("/getList")
    public PageResult<MaintenanceNotice> getNotices(
            @RequestBody Map<String, String> params
    ) {
        String status = "1";
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now();
        int page = Integer.parseInt(params.get("page"));
        int pageSize = Integer.parseInt(params.get("pageSize"));
        String title = params.get("title");

        return maintenanceService.getNotices(title, status, startTime, endTime, page, pageSize);
    }
    // 添加删除公告接口
    @PostMapping("/delete")
    public ResponseEntity<?> deleteNotice(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        if (id == null) {
            return ResponseEntity.badRequest().body("缺少公告ID");
        }

        try {
            maintenanceService.deleteNotice(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("删除公告失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除失败: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createNotice(
            @RequestBody Map<String, Object> requestData) {
        try {
            // 1. 检查请求数据是否包含必要的字段
            if (!requestData.containsKey("notice") || !(requestData.get("notice") instanceof Map)) {
                return ResponseEntity.badRequest().body("请求格式错误：缺少 notice 对象");
            }

            // 2. 获取 notice 对象
            Map<String, Object> noticeMap = (Map<String, Object>) requestData.get("notice");
            String token = (String) requestData.get("token");

            // 3. 检查必要字段是否存在
            if (!noticeMap.containsKey("title") ||
                    !noticeMap.containsKey("content") ||
                    !noticeMap.containsKey("startTime") ||
                    !noticeMap.containsKey("endTime")) {

                return ResponseEntity.badRequest().body("notice 对象缺少必要字段");
            }

            // 4. 创建公告对象
            MaintenanceNotice notice = new MaintenanceNotice();
            notice.setTitle((String) noticeMap.get("title"));
            notice.setContent((String) noticeMap.get("content"));

            // 5. 转换时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            notice.setStartTime(LocalDateTime.parse((String) noticeMap.get("startTime"), formatter));
            notice.setEndTime(LocalDateTime.parse((String) noticeMap.get("endTime"), formatter));

            // 6. 调用服务
            maintenanceService.createNotice(notice, token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // 7. 记录详细错误日志
            log.error("创建公告失败", e);
            return ResponseEntity.badRequest().body("请求处理失败: " + e.getMessage());
        }
    }
}