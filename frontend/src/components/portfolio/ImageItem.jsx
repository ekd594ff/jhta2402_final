import React, {useRef} from 'react';
import {useDrag, useDrop} from 'react-dnd';
import IconButton from "@mui/material/IconButton";
import DeleteIcon from "@mui/icons-material/Delete";
import {Card, Grid2} from "@mui/material";

const ItemType = {
    IMAGE: 'image',
};

function ImageItem({image, index, moveImage, removeImage}) {

    const ref = useRef(null);

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

    const [{isDragging}, drag] = useDrag({
        type: ItemType.IMAGE,
        item: {index},
        collect: (monitor) => ({
            isDragging: monitor.isDragging(),
        }),
    });

    drag(drop(ref));

    return (
        <Grid2 size={{xs: 2, sm: 4, md: 4}} key={index}
               ref={ref}
               style={{
                   position: 'relative', // 순서 태그를 배치하기 위해 position 설정
                   height: 'auto',
                   width: 'auto',
                   opacity: isDragging ? 0.5 : 1,
                   cursor: 'move',
                   margin: 'auto',
                   flex: '1 1 180px', // 최소 너비를 설정하여 브라우저 크기에 따라 유동적으로 조정
                   marginBottom: '8px',
                   boxSizing: 'border-box',
                   maxWidth: 'calc(25% + 20px)', // 최대 너비를 설정하여 큰 화면에서도 적절히 배치
               }}
        >
            {/* 순서 태그 */}
            <Card sx={{textAlign: 'center'}}>
                <div
                    style={{
                        position: 'absolute',
                        top: '4px',
                        left: '4px',
                        backgroundColor: 'rgba(0, 0, 0, 0.4)',
                        color: 'white',
                        borderRadius: '50%',
                        width: '20px',
                        height: '20px',
                        margin: 'auto',
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
                <IconButton aria-label="delete" color="primary"
                            onClick={() => removeImage(index)} // 삭제 함수 호출
                            style={{
                                position: 'absolute',
                                top: '0px',
                                right: '0px',
                                color: 'red',
                                width: '32px',
                                height: '32px',
                            }}
                >
                    <DeleteIcon/>
                </IconButton>

                <img
                    src={image.url}
                    alt={`Preview ${index + 1}`}
                    style={{
                        width: '200px',
                        height: '200px',
                        objectFit: "cover",
                        margin: "auto",
                    }}
                />
            </Card>
        </Grid2>
    );
}

export default ImageItem;

