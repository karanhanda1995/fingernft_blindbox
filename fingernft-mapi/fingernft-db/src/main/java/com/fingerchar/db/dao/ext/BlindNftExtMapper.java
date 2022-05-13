package com.fingerchar.db.dao.ext;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BlindNftExtMapper {

	@Select(value = "select max(token_id) from `blind_nft`  where `address` = #{address}")
	String getMaxTokenId(@Param("address") String address);
}
