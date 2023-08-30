package com.jbd.thymeleaf_start.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jbd.thymeleaf_start.domain.Media;
import com.jbd.thymeleaf_start.model.MediaDTO;
import com.jbd.thymeleaf_start.repos.MediaRepository;
import com.jbd.thymeleaf_start.util.NotFoundException;


@Service
public class MediaServiceImpl implements IMediaService {

    private final MediaRepository mediaRepository;

    public MediaServiceImpl(final MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Override
    public List<MediaDTO> findAll() {
        final List<Media> medias = (List<Media>) mediaRepository.findAll();
        return medias.stream()
                .map(media -> mapToDTO(media, new MediaDTO()))
                .toList();
    }

    @Override
    public MediaDTO get(final Long id) {
        return mediaRepository.findById(id)
                .map(media -> mapToDTO(media, new MediaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final MediaDTO mediaDTO) {
        final Media media = new Media();
        mapToEntity(mediaDTO, media);
        return mediaRepository.save(media).getId();
    }

    @Override
    public void update(final Long id, final MediaDTO mediaDTO) {
        final Media media = mediaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(mediaDTO, media);
        mediaRepository.save(media);
    }

    @Override
    public void delete(final Long id) {
    	mediaRepository.deleteById(id);
    }
    
    @Override
    public boolean nameExists(final String name) {
        return mediaRepository.existsByNameIgnoreCase(name);
    }

	@Override
	public MediaDTO findByName(String name) {
		return mediaRepository.findByName(name)
                .map(media -> mapToDTO(media, new MediaDTO()))
                .orElseThrow(NotFoundException::new);
	}

    private MediaDTO mapToDTO(final Media media, final MediaDTO mediaDTO) {
    	mediaDTO.setId(media.getId());
    	mediaDTO.setName(media.getName());
    	mediaDTO.setMediaLocation(media.getMediaLocation());
    	mediaDTO.setMediaType(media.getMediaType());
    	mediaDTO.setCreatedDate(media.getCreatedDate());
    	mediaDTO.setUpdatedDate(media.getUpdatedDate());
        return mediaDTO;
    }

    private Media mapToEntity(final MediaDTO mediaDto, final Media media) {
    	media.setName(mediaDto.getName());
    	media.setMediaLocation(mediaDto.getMediaLocation());
    	media.setMediaType(mediaDto.getMediaType());
    	media.setCreatedDate(mediaDto.getCreatedDate());
    	media.setUpdatedDate(mediaDto.getUpdatedDate());
        return media;
    }


}
