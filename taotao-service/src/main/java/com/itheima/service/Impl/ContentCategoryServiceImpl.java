package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.service.ContentCategoryService;
import com.itheima.mapper.ContentCategoryMapper;
import com.itheima.pojo.ContentCategory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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

        //为什么要返回一个对象
        //1.如果不返回的话，分类的条目无法绑定对象。也就是添加好的分类，根本就没有数据绑定
        //以后如果要对这个分类删除&更新，服务器是不知道的
        //2.如果不返回数据，那么在页面上再进行其他操作，光标会乱跑

        /*
            结论：要返回对象，返回的对象还是当前操作的添加对象contentCategory
         */
        //return parentCategory;
        return contentCategory;
    }

    @Override
    public int update(ContentCategory contentCategory) {//参数只有id和name，剩下的其他都是默认值
        int rows = mapper.updateByPrimaryKeySelective(contentCategory);//按主键
        System.out.println("rows=" + rows);
        return rows;
    }

    @Override
    public int delete(ContentCategory contentCategory) {
        //1.现在只删除子级分类
        //int result=mapper.deleteByPrimaryKey(contentCategory);

        /*
            2.上面针对的是删除一条 子级分类，现在考虑的是直接删除父级分类
            AAA  ---->>>直接删除AAA
              BBB
                CCC
                  DDD
         */
        //先定义一个集合，一边来装要删除的对象
        List<ContentCategory> list=new ArrayList<>();

        //根据当前的id，找到它的所有孩子
        //先往集合存放本来应该删除的分类
        list.add(contentCategory);
        //还有查询它的子级分类
        findAllChild(list,contentCategory.getId());
        //再去删除      到这里list集合已经装好了要删除的多少个分类了
        int result=deleteAll(list);

        /*
            考虑最后一个问题：把儿子都有删完，刷新后，父亲没有子级分类就要变成子级分类

            删除操作会传过来一个自己的id和一个父级id
            id&&parentId----》》ContentCategory
         */
        //按照parentId去查询总数
        ContentCategory c=new ContentCategory();
        c.setParentId(contentCategory.getParentId());
        int count = mapper.selectCount(c);

        //表示当前操作的父亲节点已经没有子节点了
        if (count<1){
            //由于这里还要执行一次更新操作，所以还需要创建一次对象
            c=new ContentCategory();
            c.setId(contentCategory.getParentId());
            c.setIsParent(false);
            mapper.updateByPrimaryKeySelective(c);
        }

        return result;
    }

    //删除的操作方法,删除一个集合
    private int deleteAll(List<ContentCategory> list) {
        int result=0;
        for (ContentCategory category:list){
            result+=mapper.delete(category);
        }
        return result;
    }

    /*
        查询给定的分类ID的所有子分类，包含多重的子级分类
        list  存储的集合
        id    当前要查询的ID
     */
    private void findAllChild(List<ContentCategory> list, Long id) {
        //找到当前节点的孩子
        List<ContentCategory> childList = getCategoryByParentId(id);
        if(childList!=null&&childList.size()>0){        //递归跳出循环的重要条件

            //遍历这些子级分类
            for (ContentCategory category:childList){
                //先往List集合添加这个分类
                list.add(category);
            /*
                List<ContentCategory> childList2 = getCategoryByParentId(category.getId());
                    for (ContentCategory contentCategory:childList2){
                        //先往List集合添加这个分类
                        list.add(contentCategory);
             */
                //执行递归，再调方法
                findAllChild(list,category.getId());
            }
        }
    }
}
