/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Service.java to edit this template
 */
package group2.client.service;

import group2.client.entities.Casher;
import group2.client.repository.CasherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ann
 */
@Service
public class CasherService {
    @Autowired
    CasherRepository res;
    
    public Casher getCasherById(Integer id) {
        return res.findById(id).orElse(null);
    }
}
