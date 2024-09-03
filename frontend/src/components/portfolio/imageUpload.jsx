import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import ImageGallerySlider from "./imageGallerySlider.jsx";

const MAX_IMAGES = 32; // 최대 이미지 갯수 설정

const ImageUpload = () => {
    const [otherImages, setOtherImages] = useState([]);

    const handleOtherImagesChange = (event) => {
        const files = Array.from(event.target.files);
        const remainingSlots = MAX_IMAGES - otherImages.length;

        // 새로운 이미지가 최대 갯수를 초과하지 않도록 제한
        if (files.length > remainingSlots) {
            alert(`최대 ${MAX_IMAGES}개의 이미지만 등록할 수 있습니다.`);
            return;
        }

        const imageUrls = files.map(file => ({
            id: URL.createObjectURL(file),
            url: URL.createObjectURL(file),
            file: file
        }));
        setOtherImages([...otherImages, ...imageUrls]);
    };

    const moveImage = (dragIndex, hoverIndex) => {
        const draggedImages = [...otherImages];
        const [draggedImage] = draggedImages.splice(dragIndex, 1);
        draggedImages.splice(hoverIndex, 0, draggedImage);
        setOtherImages(draggedImages);
    };

    const removeImage = (index) => {
        const filteredImages = otherImages.filter((_, i) => i !== index);
        setOtherImages(filteredImages);
    };

    return (
        <DndProvider backend={HTML5Backend}>
            <Box sx={{ padding: 2, textAlign: 'left' }}>
                <Stack spacing={2}>
                    <label>
                        이미지 업로드(다수 등록 가능, 최대 {MAX_IMAGES}개)
                        <input
                            type="file"
                            id="formFileMultiple"
                            multiple
                            onChange={handleOtherImagesChange}
                            disabled={otherImages.length >= MAX_IMAGES} // 최대 갯수 초과 시 업로드 비활성화
                        />
                        <p>
                            <h7>{`현재 등록된 이미지: ${otherImages.length}/${MAX_IMAGES}`}</h7>
                        </p>
                    </label>
                </Stack>
            </Box>

            <Box sx={{padding: 1}}>
                <div>
                    <ImageGallerySlider
                        otherImages={otherImages}
                        moveImage={moveImage}
                        removeImage={removeImage}
                    />
                </div>
            </Box>
        </DndProvider>
    );
};

export default ImageUpload;