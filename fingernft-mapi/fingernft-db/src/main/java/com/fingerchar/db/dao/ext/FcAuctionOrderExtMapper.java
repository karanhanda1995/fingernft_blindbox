package com.fingerchar.db.dao.ext;

import com.fingerchar.db.domain.FcAuctionOrder;
import com.fingerchar.db.vo.AuctionParamVO;
import com.fingerchar.db.vo.FcAuctionJoinVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FcAuctionOrderExtMapper {

	public List<FcAuctionOrder> participation(@Param("address")String address, @Param("token")String token, @Param("tokenId")String tokenId);

	List<FcAuctionJoinVo> findHost(@Param("joinId")Long joinId, @Param("orderIds")List<Long> orderIds);

	List<FcAuctionOrder> listbymulti(List<AuctionParamVO> params);
}
