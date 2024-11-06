package com.teenet.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.teenet.entity.database.Dic;
import com.teenet.mapper.DicMapper;
import com.teenet.service.DicService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author threedong
 * @since 2022-06-14
 */
@Service
public class DicServiceImpl extends ServiceImpl<DicMapper, Dic> implements DicService {


    public Map<String, List<String>> type1 = new HashMap<>();

    @Override
    public Map<String, List<String>> typeIs1() {
        if (type1.isEmpty()) {
            List<Dic> dicList = this.list(Wrappers.<Dic>lambdaQuery().eq(Dic::getType, 1));
            List<Dic> parent = dicList.stream().filter(item -> ObjectUtils.isNull(item.getParentId())).collect(Collectors.toList());
            eachChild(parent, dicList);
            parent.forEach(item -> {
                String name = item.getName();
                List<Dic> child = item.getChild();
                List<String> collect = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(child)) {
                    collect = child.stream().map(Dic::getName).collect(Collectors.toList());
                }
                type1.put(name, collect);
            });
        }
        return type1;
    }

    @Override
    public void clearType1() {
        type1.clear();
        typeIs1();
    }

    private void eachChild(List<Dic> parent, List<Dic> all) {
        parent.forEach(item -> {
            Integer id = item.getId();
            List<Dic> child = all.stream().filter(item2 -> {
                Integer parentId = item2.getParentId();
                return ObjectUtils.isNotNull(parentId) && parentId.equals(id);
            }).collect(Collectors.toList());
            item.setChild(child);
        });
    }
}
