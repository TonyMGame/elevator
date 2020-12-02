package com.soundai.elevator.cms.service;

import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.entity.Material;
import com.soundai.elevator.cms.vo.MaterialPageVo;
import com.soundai.elevator.cms.vo.PageList;
import com.soundai.elevator.cms.vo.UpdatePosterVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface MaterialService {

    void upload(MultipartFile file) throws BusinessException;

    PageList getMaterialList(MaterialPageVo materialVo) throws BusinessException;

    List<Material> materialDictionary(int type) throws BusinessException;

    void delMaterial(int id) throws BusinessException;

    void updatePoster(UpdatePosterVo updatePosterVo) throws BusinessException;

}
