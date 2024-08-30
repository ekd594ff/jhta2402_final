import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Slider from '@mui/material/Slider';

const ImageGallerySlider = ({ otherImages }) => {
    const [sliderValue, setSliderValue] = useState(0);

    const handleSliderChange = (event, newValue) => {
        setSliderValue(newValue);
    };

    const displayedImages = otherImages.slice(sliderValue, sliderValue + 5); // 한 번에 보여질 이미지 수 설정

    return (
        <div>
            {(otherImages.length >= 1) && (
                <Box sx={{ width: '100%', overflow: 'hidden' }}>
                    {(otherImages.length >= 3) && (
                        <Slider
                            value={sliderValue}
                            onChange={handleSliderChange}
                            min={0}
                            max={Math.max(otherImages.length - 3, 1)} // 이미지 개수에 따른 최대 값 설정
                            step={1}
                            marks
                            sx={{ marginBottom: 2 }}
                        />
                    )}
                    <Box sx={{ display: 'flex', gap: 0 }}>
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
            )}
        </div>
    );
};

export default ImageGallerySlider;