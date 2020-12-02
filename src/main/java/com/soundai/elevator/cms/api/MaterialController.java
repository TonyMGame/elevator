package com.soundai.elevator.cms.api;

import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.service.MaterialService;
import com.soundai.elevator.cms.vo.MaterialPageVo;
import com.soundai.elevator.cms.vo.UpdatePosterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@Api(tags = "素材管理")
@RestController
@RequestMapping("/material")
@CrossOrigin
public class MaterialController {

    @Resource
    private MaterialService materialService;

    @ApiOperation(value = "上传素材")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void token(@RequestParam(value = "file") MultipartFile file) throws BusinessException {
        this.materialService.upload(file);
    }

    @ApiOperation(value = "素材列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Object list(@RequestBody MaterialPageVo materialVo) throws BusinessException {
        return this.materialService.getMaterialList(materialVo);
    }

    @ApiOperation(value = "素材列表字典")
    @RequestMapping(value = "/material_dictionary", method = RequestMethod.GET)
    public Object materialDictionary(@RequestParam(value = "type") int type) throws BusinessException {
        return this.materialService.materialDictionary(type);
    }

    @ApiOperation(value = "删除素材")
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    public void list(@RequestParam int id) throws BusinessException {
        this.materialService.delMaterial(id);
    }

    @ApiOperation(value = "更新广告主")
    @RequestMapping(value = "/update_poster", method = RequestMethod.POST)
    public void updatePoster(@RequestBody UpdatePosterVo updatePosterVo) throws BusinessException {
        this.materialService.updatePoster(updatePosterVo);
    }


}
