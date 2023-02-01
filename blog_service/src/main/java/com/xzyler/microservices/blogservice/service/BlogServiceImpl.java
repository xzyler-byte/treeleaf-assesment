package com.xzyler.microservices.blogservice.service;

import com.xzyler.microservices.blogservice.entity.Blog;
import com.xzyler.microservices.blogservice.generic.service.GenericServiceImpl;
import com.xzyler.microservices.blogservice.repository.BlogRepository;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl extends GenericServiceImpl<Blog, Integer> implements BlogService {

    private final BlogRepository blogRepository;

    public BlogServiceImpl(BlogRepository blogRepository) {
        super(blogRepository);
        this.blogRepository = blogRepository;
    }
}
