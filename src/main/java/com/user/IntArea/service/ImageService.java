package com.user.IntArea.service;

import com.user.IntArea.common.utils.ImageUtil;
import com.user.IntArea.dto.image.ImageDto;
import com.user.IntArea.entity.Image;
import com.user.IntArea.entity.Portfolio;
import com.user.IntArea.entity.Quotation;
import com.user.IntArea.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageUtil imageUtil;
    private final ImageRepository imageRepository;

    private void saveSingleImage(MultipartFile image, UUID refId, int index) {
        ImageDto imageDto = imageUtil.uploadS3(image, refId, index)
                .orElseThrow(() -> new NoSuchElementException("S3 오류"));
        imageRepository.save(imageDto.toImage());
    }

    public void saveImage(MultipartFile image, UUID refId) {
        saveSingleImage(image, refId, 0);
    }

    @Transactional
    public void saveMultiImages(List<MultipartFile> images, UUID refId) {
        for (int i=0; i<images.size(); i++) {
            saveSingleImage(images.get(i), refId, i);
        }
    }

    public List<Image> getImagesFrom(Portfolio portfolio) {
        return imageRepository.findAllByRefId(portfolio.getId());
    }

    public List<Image> getImagesFrom(Quotation quotation) {
        return imageRepository.findAllByRefId(quotation.getId());
    }

    public void deleteImageByImageId(UUID id) {
        imageRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllImagesByRefId(UUID refId) {
        imageRepository.deleteByRefId(refId);
    }
}
