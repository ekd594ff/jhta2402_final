import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import ImageGallerySlider from "./imageGallerySlider.jsx";
import ImageRepresentative from "./imageRepresentative.jsx";

const ImageUpload = () => {
    const [representativeImage, setRepresentativeImage] = useState(null);
    const [otherImages, setOtherImages] = useState([]);

    const handleRepresentativeImageChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            setRepresentativeImage(URL.createObjectURL(file));
        }
    };

    const handleOtherImagesChange = (event) => {
        const files = Array.from(event.target.files);
        const imageUrls = files.map(file => URL.createObjectURL(file));
        setOtherImages(imageUrls);
    };

    return (
        <div>
            <Box sx={{ padding: 2, textAlign: 'left' }}>
                <Stack spacing={2}>
                    <label>
                        대표 이미지(1개)
                        <input
                            type="file"
                            id="representativeImage"
                            onChange={handleRepresentativeImageChange}
                            placeholder="대표이미지"
                        />
                    </label>
                    <label>
                        그 외 이미지(10개 이하)
                        <input
                            type="file"
                            id="formFileMultiple"
                            multiple
                            onChange={handleOtherImagesChange}
                        />
                    </label>
                </Stack>
            </Box>

            <Box sx={{padding: 1}}>
                <div>
                    <ImageRepresentative representativeImage={representativeImage}/>
                </div>
                <div>
                    <ImageGallerySlider otherImages={otherImages}/>
                </div>
            </Box>
        </div>
    );
};

export default ImageUpload;