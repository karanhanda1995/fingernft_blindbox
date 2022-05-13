package com.fingerchar.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.manager.FcNoticeManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcNotice;
import com.fingerchar.db.vo.notice.NoticeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class FcNoticeService {
	
	@Autowired
	IBaseService baseService;

	@Autowired
	FcNoticeManager noticeManager;

	public IPage<NoticeInfoVo> page(Integer isRead, String address, IPage<FcNotice> pageInfo) {
		QueryWrapper<FcNotice> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNotice.OWNER, address)
			.eq(BaseEntity.DELETED, false);
		if(null != isRead) {
			if(isRead == 1) {
				wrapper.eq(FcNotice.IS_READ, true);
			} else {
				wrapper.eq(FcNotice.IS_READ, false);
			}
		}
		wrapper.orderByDesc(BaseEntity.CREATE_TIME);
		IPage<FcNotice> noticeIpage = this.baseService.findByPage(FcNotice.class, wrapper, pageInfo);
		List<FcNotice> noticeList = noticeIpage.getRecords();
		List<NoticeInfoVo> infoVoList = this.noticeManager.getNoticeInfoList(noticeList);
		IPage<NoticeInfoVo> infoVoIPage = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
		infoVoIPage.setRecords(infoVoList);
		infoVoIPage.setPages(pageInfo.getPages());
		return infoVoIPage;
	}

	public Integer read(Long id) {
		FcNotice notice = this.noticeManager.get(id);
		if(null == notice){
			return 0;
		}
		notice.setIsRead(true);
		return this.noticeManager.update(notice);
	}

	public Integer readAll(String address){
		return this.noticeManager.readAll(address);
	}

	public Integer findCountUnRead(String address) {
		return this.noticeManager.countUnread(address);
	}

	/**
	 * @param address
	 * @return
	 */
	public Object getCount(String address) {
		Integer total = this.noticeManager.countTotal(address);
		Integer unread = this.noticeManager.countUnread(address);

		Map<String, Integer> map = new HashMap<>();
		map.put("unreadCount", unread);
		map.put("totalCount", total);
		return ResponseUtil.ok(map);
	}
}
