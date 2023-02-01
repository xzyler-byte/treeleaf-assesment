package com.xzyler.microservices.blogservice.repository;

import com.xzyler.microservices.blogservice.entity.Blog;
import com.xzyler.microservices.blogservice.generic.api.GenericSoftDeleteRepository;

public interface BlogRepository extends GenericSoftDeleteRepository<Blog, Integer> {
}
