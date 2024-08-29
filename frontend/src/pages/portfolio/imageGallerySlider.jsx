import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Slider from '@mui/material/Slider';

const ImageGallerySlider = ({ otherImages }) => {
    const [sliderValue, setSliderValue] = useState(0);

    const handleSliderChange = (event, newValue) => {
        setSliderValue(newValue);
    };

    const displayedImages = otherImages.slice(sliderValue, sliderValue + 3); // 한 번에 보여질 이미지 수 설정

    return (
        <Box sx={{ width: '100%', overflow: 'hidden' }}>
            <Slider
                value={sliderValue}
                onChange={handleSliderChange}
                min={0}
                max={otherImages.length - 3} // 이미지 개수에 따른 최대 값 설정
                step={1}
                marks
                sx={{ marginBottom: 2 }}
            />
            <Box sx={{ display: 'flex', gap: 2 }}>
                {displayedImages.map((image, index) => (
                    <img
                        key={index}
                        src={image}
                        alt={`Preview ${index + 1}`}
                        style={{
                            width: '100px',
                            height: 'auto',
                            border: '1px solid black',
                        }}
                    />
                ))}
            </Box>
        </Box>
    );
};

export default ImageGallerySlider;