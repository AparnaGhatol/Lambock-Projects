package com.jbd.thymeleaf_start.service;

import java.util.List;

import com.jbd.thymeleaf_start.model.MediaDTO;


public interface IMediaService {

    List<MediaDTO> findAll();

    MediaDTO get(Long id);
    
    MediaDTO findByName(String name);

    Long create(MediaDTO mediaDTO);

    void update(Long id, MediaDTO mdiaDTO);

    void delete(Long id);

    boolean nameExists(String name);

}
