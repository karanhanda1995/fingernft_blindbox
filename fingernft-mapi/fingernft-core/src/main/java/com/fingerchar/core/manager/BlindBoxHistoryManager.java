package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.BlindBlindBox;
import com.fingerchar.db.domain.BlindBlindBoxOrder;
import com.fingerchar.db.domain.BlindBoxHistory;
import com.fingerchar.db.dto.BlindOpenLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author： Zjm
 * @Date：2022/3/22 11:55
 */
@Service
public class BlindBoxHistoryManager {
    @Autowired
    IBaseService baseService;

    public List<BlindBoxHistory> list(BlindBlindBox box){
        QueryWrapper<BlindBoxHistory> wrapper = new QueryWrapper<>();
        wrapper.eq(BlindBoxHistory.BLIND_BOX_ID, box.getId())
                .eq(BlindBoxHistory.DELETED, false);
        return this.baseService.findByCondition(BlindBoxHistory.class, wrapper);
    }

    public Integer add(BlindOpenLog log, BlindBlindBoxOrder order){
        Long time = Long.valueOf(log.getBlockTimestamp().toString());
        BlindBoxHistory history = new BlindBoxHistory();
        history.setBlindBoxId(order.getBlindBoxId());
        history.setAmount(log.getAmount().toString());
        history.setBuyer(log.getBuyer());
        history.setBuyToken(order.getBuyerToken());
        history.setBuyTokenId(order.getBuyerTokenId());
        history.setBuyValue(order.getBuying());
        history.setOwner(order.getOwner());
        history.setSalt(order.getSalt());
        history.setTxHash(log.getTxHash());
        history.setPaytokenName(order.getPaytokenName());
        history.setPaytokenSymbol(order.getPaytokenSymbol());
        history.setPaytokenDecimals(order.getPaytokenDecimals());
        history.setPaytokenMetadataContent(order.getPaytokenMetadataContent());
        history.setUpdateTime(time);
        history.setCreateTime(time);
        return this.save(history);
    }

    public Integer update(BlindBoxHistory history){
        return this.baseService.update(history);
    }

    public Integer save(BlindBoxHistory history){
        return this.baseService.save(history);
    }
}
