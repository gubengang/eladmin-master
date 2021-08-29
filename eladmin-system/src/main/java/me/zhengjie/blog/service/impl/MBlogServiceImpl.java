/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.blog.service.impl;

import me.zhengjie.blog.domain.MBlog;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.blog.repository.MBlogRepository;
import me.zhengjie.blog.service.MBlogService;
import me.zhengjie.blog.service.dto.MBlogDto;
import me.zhengjie.blog.service.dto.MBlogQueryCriteria;
import me.zhengjie.blog.service.mapstruct.MBlogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author gubengang
* @date 2021-08-29
**/
@Service
@RequiredArgsConstructor
public class MBlogServiceImpl implements MBlogService {

    private final MBlogRepository mBlogRepository;
    private final MBlogMapper mBlogMapper;

    @Override
    public Map<String,Object> queryAll(MBlogQueryCriteria criteria, Pageable pageable){
        Page<MBlog> page = mBlogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(mBlogMapper::toDto));
    }

    @Override
    public List<MBlogDto> queryAll(MBlogQueryCriteria criteria){
        return mBlogMapper.toDto(mBlogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MBlogDto findById(Long id) {
        MBlog mBlog = mBlogRepository.findById(id).orElseGet(MBlog::new);
        ValidationUtil.isNull(mBlog.getId(),"MBlog","id",id);
        return mBlogMapper.toDto(mBlog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MBlogDto create(MBlog resources) {
        return mBlogMapper.toDto(mBlogRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MBlog resources) {
        MBlog mBlog = mBlogRepository.findById(resources.getId()).orElseGet(MBlog::new);
        ValidationUtil.isNull( mBlog.getId(),"MBlog","id",resources.getId());
        mBlog.copy(resources);
        mBlogRepository.save(mBlog);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            mBlogRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MBlogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MBlogDto mBlog : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", mBlog.getUserId());
            map.put("文章标题", mBlog.getTitle());
            map.put("描述", mBlog.getDescription());
            map.put("内容", mBlog.getContent());
            map.put(" created",  mBlog.getCreated());
            map.put(" status",  mBlog.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}