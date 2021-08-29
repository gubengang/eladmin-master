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
package me.zhengjie.blog.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author gubengang
* @date 2021-08-29
**/
@Entity
@Data
@Table(name="m_blog")
public class MBlog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "user_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @Column(name = "title",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "文章标题")
    private String title;

    @Column(name = "description",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "描述")
    private String description;

    @Column(name = "content")
    @ApiModelProperty(value = "内容")
    private String content;

    @Column(name = "created",nullable = false)
    @NotNull
    @CreationTimestamp
    @ApiModelProperty(value = "created")
    private Timestamp created;

    @Column(name = "status")
    @ApiModelProperty(value = "status")
    private Integer status;

    public void copy(MBlog source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}