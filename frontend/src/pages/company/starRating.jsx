import React from 'react';
import Rating from '@mui/material/Rating';

const StarRating = ({ rate }) => {
    return (
        <Rating
            name="read-only"
            value={rate}
            precision={0.5}
            readOnly
            // size="small"
            sx={{ fontSize: '14px' }}  // 크기를 더 작게 설정
        />
    );
};

export default StarRating;