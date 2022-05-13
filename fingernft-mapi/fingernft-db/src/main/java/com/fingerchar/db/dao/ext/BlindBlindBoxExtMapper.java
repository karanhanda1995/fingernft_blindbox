package com.fingerchar.db.dao.ext;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.db.domain.BlindBlindBox;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlindBlindBoxExtMapper {

    IPage<BlindBlindBox> findActive(@Param("currentTime")Long currentTime, @Param("blindTypes")List<Long> blindTypes, @Param("sort")String sort, @Param("order")String order, IPage<BlindBlindBox> page);
}
