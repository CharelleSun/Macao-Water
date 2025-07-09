package maintenance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import maintenance.entity.MaintenanceNotice;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaintenanceNoticeMapper extends BaseMapper<MaintenanceNotice> {
}