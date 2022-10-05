package com.alexvar.springboot_rest.services.Impl;

//import com.alexvar.springboot_rest.repositories.RoleRepository;
//import com.alexvar.springboot_rest.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Service
//public class RoleServiceImpl implements RoleService {

//    private final RoleRepository roleRepository;
//
//    @Autowired
//    public RoleServiceImpl(RoleRepository roleRepository){
//        this.roleRepository = roleRepository;
//    }
//
//    @Override
//    public Role create(Role role) {
//        return roleRepository.save(role);
//    }
//
//    @Override
//    public Role readById(long id) {
//        return roleRepository.findById(id).get();
//    }
//
//    @Override
//    public Role update(Role role) {
//        Role newRole = roleRepository.getReferenceById(role.getId());
//
//        return role;
//    }
//
//    @Override
//    public void delete(long id) {
//        roleRepository.deleteById(id);
//    }
//
//    @Override
//    public List<Role> getAll() {
//        List<Role> list = roleRepository.findAll();
//        return list.isEmpty() ? new ArrayList<>() : list;
//    }
//}
