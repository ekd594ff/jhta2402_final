import Stack from "@mui/material/Stack";
import React from "react";


const ImageRepresentative = ({ representativeImage }) => {
    return (
        <div>
            <Stack spacing={1} direction="column" flexWrap="wrap">
                <h6>대표이미지</h6>
                {representativeImage && (
                    <img
                        src={representativeImage}
                        alt="Representative"
                        style={{
                            width: '250px',
                            height: 'auto',
                            marginRight: '10px',
                            border: '2px solid blue' // 파란색 테두리 추가
                        }}
                    />
                )}
            </Stack>
        </div>
    );
};

export default ImageRepresentative;

