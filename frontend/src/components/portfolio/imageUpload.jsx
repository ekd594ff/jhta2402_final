import React from 'react';
import {HTML5Backend} from "react-dnd-html5-backend";
import style from "../../styles/portfolio-registration.module.scss";
import Stack from "@mui/material/Stack";
import {Box, Button, Grid2} from "@mui/material";
import ImageItem from "./ImageItem.jsx";
import {DndProvider} from "react-dnd";

const MAX_IMAGES = 32;

function ImageUpload({images, setImages, otherImages, setOtherImages}) {

    // Image -------------------------------------------

    const handleOtherImagesChange = (event) => {
        const files = Array.from(event.target.files);
        const remainingSlots = MAX_IMAGES - otherImages.length;

        // 새로운 이미지가 최대 갯수를 초과하지 않도록 제한
        // if (files.length > remainingSlots) {
        //     alert(`최대 ${MAX_IMAGES}개의 이미지만 등록할 수 있습니다.`);
        //     return;
        // }

        const imageUrls = files.map(file => ({
            id: URL.createObjectURL(file),
            url: URL.createObjectURL(file),
            file: file
        }));
        console.log("test");

        setOtherImages([...otherImages, ...imageUrls]);
        console.log(otherImages);
        setImages(files);


    };

    const moveImage = (dragIndex, hoverIndex) => {
        const draggedOtherImages = [...otherImages];
        const [draggedOtherImage] = draggedOtherImages.splice(dragIndex, 1);
        draggedOtherImages.splice(hoverIndex, 0, draggedOtherImage);
        setOtherImages(draggedOtherImages);

        const draggedImages = [...otherImages];
        const [draggedImage] = draggedImages.splice(dragIndex, 1);
        draggedImages.splice(hoverIndex, 0, draggedImage);
        setImages(draggedImages);
    };

    const removeImage = (index) => {
        setOtherImages(otherImages.filter((_, i) => i !== index));
        setImages(images.filter((_, i) => i !== index));
    };

    return (
        <DndProvider backend={HTML5Backend} className={style["dnd-provider"]}>
            <Stack direction="row" sx={{margin: "16px", gap: "16px"}}>
                {/*<p>{`현재 등록된 이미지: ${otherImages.length}/${MAX_IMAGES}`}</p>*/}
                <Button style={{borderColor: '#FA4D56', color: '#FA4D56', margin: '8px'}}
                        variant="outlined" component="label">
                    <input
                        type="file"
                        id="formFileMultiple"
                        multiple
                        onChange={handleOtherImagesChange}
                        disabled={otherImages.length >= MAX_IMAGES} // 최대 갯수 초과 시 업로드 비활성화
                        hidden
                    />이미지 등록
                </Button>
            </Stack>

            <Box sx={{flexGrow: 1}}>
                <Grid2 container spacing={{xs: 2, md: 3}} columns={{xs: 4, sm: 8, md: 12}}>

                    {otherImages.map((image, index) => (
                        <ImageItem
                            key={image.id}
                            index={index}
                            image={image}
                            moveImage={moveImage}
                            removeImage={removeImage} // 이미지 삭제 함수 전달
                        />
                    ))}
                </Grid2>
            </Box>

        </DndProvider>
    );
}

export default ImageUpload;