package maintenance.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maintenance.entity.MaintenanceNotice;
import maintenance.mapper.MaintenanceNoticeMapper;
import maintenance.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Data
@Slf4j
public class MaintenanceService {

    @Autowired
    MaintenanceNoticeMapper noticeMapper;

    @Autowired
    VerificationService verificationService;

    @Value("${app.verification.enabled:false}")
    private boolean verificationEnabled;

    public PageResult<MaintenanceNotice> getNotices(
            String title, String status, LocalDateTime startTime,
            LocalDateTime endTime, int page, int pageSize) {

        Page<MaintenanceNotice> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<MaintenanceNotice> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(title)) {
            wrapper.like(MaintenanceNotice::getTitle, title);
        }

        wrapper.orderByDesc(MaintenanceNotice::getStartTime);

        Page<MaintenanceNotice> result = noticeMapper.selectPage(pageInfo, wrapper);
        return new PageResult<>(result.getTotal(), result.getRecords());
    }

    // 修改创建公告方法 - 添加验证码验证
    public void createNotice(MaintenanceNotice notice, String token) {
        // 如果启用了验证功能
        if (verificationEnabled) {
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("验证码不能为空");
            }

            // 这里只需要传递 token 参数
            if (!verificationService.verifyCode(token)) {
                throw new RuntimeException("验证码无效或已过期");
            }
        }

        // 时间验证
        if (notice.getEndTime().isBefore(notice.getStartTime())) {
            throw new RuntimeException("结束时间必须大于开始时间");
        }
        noticeMapper.insert(notice);
    }

    // 发送验证码方法 - 不需要参数
    public void sendVerificationCode() {
        verificationService.sendVerificationCode();
    }

    public void deleteNotice(Long id) {
        log.info("Deleting notice with ID: {}", id);
        MaintenanceNotice notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw new RuntimeException("Notice not found");
        }
        noticeMapper.deleteById(id);
    }
}