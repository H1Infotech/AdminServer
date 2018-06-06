package com.h1infotech.smarthive.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.h1infotech.smarthive.domain.BeeBox;
import com.h1infotech.smarthive.common.BizCodeEnum;
import com.h1infotech.smarthive.common.BusinessException;
import com.h1infotech.smarthive.repository.BeeBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.h1infotech.smarthive.web.response.BeeBoxPageRetrievalResponse;

@Service(value = "beeBoxService")
public class BeeBoxServiceImpl implements BeeBoxService {

	@Autowired
	private BeeBoxRepository beeBoxRepository;
	
	@Override
    public List<BeeBox> getAllBeeBoxes() {
		return beeBoxRepository.findAll();
    }
	
	@Override 
	public BeeBoxPageRetrievalResponse getAllBeeBoxes(int pageNo, int pageSize) {
		if(pageNo < 1 || pageSize < 0) {
			throw new BusinessException(BizCodeEnum.ILLEGAL_INPUT);
		}
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<BeeBox> pageBeeBox = beeBoxRepository.findAll(page);
		BeeBoxPageRetrievalResponse response = new BeeBoxPageRetrievalResponse();
		if(pageBeeBox==null || pageBeeBox.getContent()==null) {
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(0);
		}else {
			response.setBeeBoxes(pageBeeBox.getContent());
			response.setCurrentPageNo(pageNo);
			response.setTotalPageNo(pageBeeBox.getTotalPages());
		}
		return response;
	}
}
