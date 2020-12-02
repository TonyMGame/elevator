package com.soundai.elevator.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.common.ProException;
import com.soundai.elevator.cms.common.ResultEnum;
import com.soundai.elevator.cms.entity.Material;
import com.soundai.elevator.cms.entity.User;
import com.soundai.elevator.cms.mapper.MaterialMapper;
import com.soundai.elevator.cms.service.MaterialService;
import com.soundai.elevator.cms.task.CachePool;
import com.soundai.elevator.cms.task.ThreadRepertory;
import com.soundai.elevator.cms.util.*;
import com.soundai.elevator.cms.vo.MaterialPageVo;
import com.soundai.elevator.cms.vo.PageList;
import com.soundai.elevator.cms.vo.UpdatePosterVo;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * @author 常磊
 * @date 4.18
 * @describe 素材管理
 */
@Log4j2
@Service
public class MaterialServiceImpl implements MaterialService {
    
    @Value("${material.upload-url}")
    private String uploadUrl;
    @Resource
    private OkHttp3 okHttp3;
    @Resource
    private MaterialMapper materMapper;
    @Value("${azero.appId}")
    private String appId;
    @Value("${azero.secretKey}")
    private String secretKey;


    /**
     * 素材上传
     *
     * @param file
     * @throws BusinessException
     */
    @Override
    public void upload(MultipartFile file) throws BusinessException {
        String fileName = file.getOriginalFilename();
        int type = FileTypeUtil.getType(fileName);
        String md5 = FileMd5.getMd5(file);
        User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
        int num = materMapper.selectMaterialByMd5(new HashMap() {{
            put("md5", md5);
            put("updateUser", user.getId());
        }});
        if (num > 0) {
            throw new BusinessException(ResultEnum.UPLOAD_ALREADY_ERROR);
        }
        if (type == 0) {
            throw new BusinessException(ResultEnum.TYPE_ERROR);
        }
        log.info("upload fileName {}", fileName);
        String upload = getRequest(file, fileName);
        JSONObject uploadJson = JSON.parseObject(upload);
        log.info("uploadJson {}", uploadJson);
        int code = Integer.parseInt(uploadJson.getString("code"));
        if (code != 200) {
            throw new ProException(code, uploadJson.getString("message"));
        }
        JSONObject data = uploadJson.getJSONObject("data");
        //入库
        try {
            Material material = new Material();
            material.setMaterialId(data.getString("id"));
            material.setUrl(data.getString("url"));
            material.setName(fileName);
            material.setIsUsed(0);
            material.setUpdateUser(user.getId());
            material.setPoster("未知");
            material.setType(type);
            material.setMd5(md5);
            material.setRunNum(0);
            materMapper.insertMaterial(material);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.INSERT_DATA_ERROR);
        }
    }


    /**
     * 获取素材列表
     *
     * @param materialVo
     * @return
     * @throws BusinessException
     */
    @Override
    public PageList getMaterialList(MaterialPageVo materialVo) throws BusinessException {
        User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
        if (user.getLevel() != 2) {
            materialVo.setUserId(user.getId());
        }
        Page page = PageHelper.startPage(materialVo.getPageNum(), materialVo.getPageSize());
        List<Material> materialList = this.materMapper.getMaterialList(materialVo);
        return new PageList(materialVo.getPageNum(),
                materialVo.getPageSize(),
                page.getTotal(), page.getPages(),
                materialList);
    }

    /**
     * 素材字典
     *
     * @param type
     * @return
     * @throws BusinessException
     */
    @Override
    public List<Material> materialDictionary(int type) throws BusinessException {
        User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
        HashMap map = new HashMap();
        if (type != 0) {
            map.put("type", type);
        }
        if (user != null) {
            if (user.getLevel() != 2) {
                map.put("userId", user.getId());
            }
        }
        return this.materMapper.materialDictionary(map);
    }


    /**
     * 删除素材
     *
     * @param id
     * @throws BusinessException
     */
    @Override
    public void delMaterial(int id) throws BusinessException {
        this.materMapper.delMaterial(id);
    }

    @Override
    public void updatePoster(UpdatePosterVo updatePosterVo) throws BusinessException {
        this.materMapper.updatePoster(updatePosterVo);
    }


    /**
     * http请求方法
     *
     * @param multipartFile
     * @return
     * @throws BusinessException
     */
    public String getRequest(MultipartFile multipartFile, String fileName) throws BusinessException {
        try {
            File file = MultipartFileToFile.multipartFileToFile(multipartFile);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", fileName,
                            RequestBody.create(MediaType.parse("multipart/form-data"), file))
                    .build();
            Request request = new Request.Builder()
                    .header("Authorization", "SaiApp " + appId + ":" + Signature.signatureStr(appId, secretKey))
                    .url(uploadUrl)
                    .post(requestBody)
                    .build();
            Response response = okHttp3.getOkHttpClient().newCall(request).execute();
            MultipartFileToFile.delteTempFile(file);
            String res = response.body().string();
            log.info(res);
            return res;
        } catch (Exception e) {
            log.info("getRequest upload err");
            e.printStackTrace();
            throw new BusinessException(ResultEnum.UPLOAD_ERROR);
        }
    }
}
