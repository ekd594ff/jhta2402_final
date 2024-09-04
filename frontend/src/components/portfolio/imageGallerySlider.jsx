import React, { useState } from 'react';
import Box from '@mui/material/Box';
import { useDrag, useDrop } from 'react-dnd';

const ItemType = {
    IMAGE: 'image',
};

const ImageGallerySlider = ({ otherImages, moveImage, removeImage }) => {
    return (
        <Box
            sx={{
                display: 'flex',
                flexWrap: 'wrap',
                gap: 1, // 이미지 사이의 간격 설정
                justifyContent: 'flex-start', // 왼쪽 정렬
            }}
        >
            {otherImages.map((image, index) => (
                <ImageItem
                    key={image.id}
                    index={index}
                    image={image}
                    moveImage={moveImage}
                    removeImage={removeImage} // 이미지 삭제 함수 전달
                />
            ))}
        </Box>
    );
};

const ImageItem = ({ image, index, moveImage, removeImage }) => {
    const ref = React.useRef(null);

    const [imageHeight, setImageHeight] = useState(0);

    const [, drop] = useDrop({
        accept: ItemType.IMAGE,
        hover(item) {
            if (!ref.current) return;

            const dragIndex = item.index;
            const hoverIndex = index;

            if (dragIndex === hoverIndex) return;

            moveImage(dragIndex, hoverIndex);
            item.index = hoverIndex;
        },
    });

    const [{ isDragging }, drag] = useDrag({
        type: ItemType.IMAGE,
        item: { index },
        collect: (monitor) => ({
            isDragging: monitor.isDragging(),
        }),
    });

    drag(drop(ref));

    return (
        <div
            ref={ref}
            style={{
                position: 'relative', // 순서 태그를 배치하기 위해 position 설정
                opacity: isDragging ? 0.5 : 1,
                cursor: 'move',
                flex: '1 1 180px', // 최소 너비를 설정하여 브라우저 크기에 따라 유동적으로 조정
                marginBottom: '8px',
                boxSizing: 'border-box',
                maxWidth: 'calc(25% + 20px)', // 최대 너비를 설정하여 큰 화면에서도 적절히 배치
            }}
        >
            {/* 순서 태그 */}
            <div
                style={{
                    position: 'absolute',
                    top: '4px',
                    left: '4px',
                    backgroundColor: 'rgba(0, 0, 0, 0.7)',
                    color: 'white',
                    borderRadius: '50%',
                    width: '20px',
                    height: '20px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontSize: '12px',
                    zIndex: 1,
                }}
            >
                {index + 1}
            </div>

            {/* 삭제 버튼 */}
            <div
                onClick={() => removeImage(index)} // 삭제 함수 호출
                style={{
                    position: 'absolute',
                    top: `${imageHeight + 24}px`, // 이미지 높이에 따라 버튼 위치 설정
                    left: '4px',
                    backgroundColor: 'yellow',
                    color: 'red',
                    borderRadius: '50%',
                    width: '20px',
                    height: '20px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontSize: '12px',
                    cursor: 'pointer',
                    zIndex: 1,
                }}
            >
                X
            </div>

            <img
                src={image.url}
                alt={`Preview ${index + 1}`}
                style={{
                    width: '100px',
                    height: 'auto',
                    border: '1px solid black',
                }}
            />
        </div>
    );
};

export default ImageGallerySlider;