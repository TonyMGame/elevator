package com.soundai.elevator.cms.mapper;

import com.soundai.elevator.cms.entity.Material;
import com.soundai.elevator.cms.vo.MaterialPageVo;
import com.soundai.elevator.cms.vo.UpdatePosterVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MaterialMapper {
    /**
     * 查询素材列表
     *
     * @param materialVo
     * @return
     */
    List<Material> getMaterialList(MaterialPageVo materialVo);

    /**
     * 插入素材
     *
     * @param material
     */
    void insertMaterial(Material material);

    /**
     * 删除素材
     *
     * @param id
     */
    void delMaterial(int id);

    /**
     * 获取素材字典
     *
     * @return
     */
    List<Material> materialDictionary(Map map);

    /**
     * 使用素材
     *
     * @return
     */
    void updateMaterialUsed(List list);

    /**
     * 更新广告主
     * @param updatePosterVo
     */
    void updatePoster(UpdatePosterVo updatePosterVo);

    /**
     * 查询是否有重复的图片
     * @param map
     * @return
     */
    Integer selectMaterialByMd5(Map map);

}
