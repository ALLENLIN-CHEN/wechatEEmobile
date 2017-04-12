package com.service;

import com.dao.impl.ProjectDao;
import com.entity.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zengqin on 2017/3/21.
 */
@Service
public class ProjectServiceTest {
    @Autowired
    private ProjectDao projectDao;
    public Pager findAllProject(String scope,Pager pagerModel){
        return pagerModel;
    }
}
