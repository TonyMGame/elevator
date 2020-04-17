package com.soundai.elevator.cms.api;


import com.soundai.elevator.cms.common.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(value="素材")
@RestController
@RequestMapping("/material")
public class MaterialController {

    @ApiOperation(value = "上传素材")
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Object token(@RequestParam(value = "file") MultipartFile file) throws BusinessException {
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        return null;
    }


}
