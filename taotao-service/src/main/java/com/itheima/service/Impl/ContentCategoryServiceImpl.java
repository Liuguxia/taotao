package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.service.ContentCategoryService;
import com.itheima.mapper.ContentCategoryMapper;
import com.itheima.pojo.ContentCategory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private ContentCategoryMapper mapper;
    @Override
    public List<ContentCategory> getCategoryByParentId(Long id) {
        ContentCategory category=new ContentCategory();
        category.setParentId(id);

        return mapper.select(category);
    }

    @Override
    public ContentCategory add(ContentCategory contentCategory) {
        //1.直接添加这个分类到表里  contentCategory：parentId,name
        contentCategory.setStatus(1);//正常使用
        contentCategory.setIsParent(false);//false表示添加的都不是父亲
        contentCategory.setCreated(new Date());//创建时间
        contentCategory.setUpdated(new Date());//更新时间

        mapper.insertSelective(contentCategory);

        //2.上面的代码针对的场景是：在父级分类下创建子分类，如果是在子分类下创建子分类。
        //那么上面的代码仅仅只能添加子分类，并不会把子分类变成父级分类

        /*
            判断当前这个分类的父亲是不是子分类，如果是子分类，那么要把这个父亲变成父级分类。
         */
        Long parentId=contentCategory.getParentId();
        ContentCategory parentCategory=mapper.selectByPrimaryKey(parentId);

        //判断它的父亲是不是子级分类
        if(!parentCategory.getIsParent()){

            //让它的父亲是父级分类
            parentCategory.setIsParent(true);

        }
        mapper.updateByPrimaryKeySelective(parentCategory);
        return parentCategory;
    }
}
