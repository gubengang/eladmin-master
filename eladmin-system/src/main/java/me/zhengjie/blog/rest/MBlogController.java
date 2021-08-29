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
package me.zhengjie.blog.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.blog.domain.MBlog;
import me.zhengjie.blog.service.MBlogService;
import me.zhengjie.blog.service.dto.MBlogQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author gubengang
* @date 2021-08-29
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "博客管理")
@RequestMapping("/api/mBlog")
public class MBlogController {

    private final MBlogService mBlogService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('mBlog:list')")
    public void download(HttpServletResponse response, MBlogQueryCriteria criteria) throws IOException {
        mBlogService.download(mBlogService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询博客")
    @ApiOperation("查询博客")
    @PreAuthorize("@el.check('mBlog:list')")
    public ResponseEntity<Object> query(MBlogQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(mBlogService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增博客")
    @ApiOperation("新增博客")
    @PreAuthorize("@el.check('mBlog:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody MBlog resources){
        return new ResponseEntity<>(mBlogService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改博客")
    @ApiOperation("修改博客")
    @PreAuthorize("@el.check('mBlog:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody MBlog resources){
        mBlogService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除博客")
    @ApiOperation("删除博客")
    @PreAuthorize("@el.check('mBlog:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        mBlogService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}