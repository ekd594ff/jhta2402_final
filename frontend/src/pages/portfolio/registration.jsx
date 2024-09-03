import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {
    Button,
    Chip,
    FormControl,
    getImageListItemUtilityClass,
    ImageList,
    ImageListItem,
    Input,
    Stack
} from "@mui/material";
import TextField from "@mui/material/TextField";
import Avatar from "@mui/material/Avatar";
import Box from "@mui/material/Box";

import ImageUpload from '../../components/portfolio/imageUpload.jsx'
import SolutionForm from "../../components/portfolio/solutionForm.jsx";

import style from "../../styles/portfolio-registration.module.scss";

function Registration() {

    const navigate = useNavigate();

    // 1. axios 데이터 불러오기 / 전송하기
    axios.get("/api/company/info", {withCredentials: true})  // withCredentials: true =>  요청에 로그인 쿠키를 담아서 전송
        .then(res => {
            console.log(res);
        });

    // 2. useState 받은 데이터 적용하기
    const [companyInfo, setCompanyInfo] = useState({
        address: "",
        companyName: "",
        description: "",
        phone: "",
        url: "",
    });

    // 3. useEffect 페이지 처음 불러올 때 실행하기
    useEffect(() => {
        axios.get("/api/company/info", {withCredentials: true})  // withCredentials: true =>  요청에 로그인 쿠키를 담아서 전송
            .then(res => {
                setCompanyInfo(res.data);
            });
    }, []);

    // 4. 데이터 보내는 방법
    const [portfolioInfo, setPortfolioInfo] = useState({
        title: "",
        description: "",
    })

    const submitPortfolio = () => {
        axios.post("/api/portfolio",
            portfolioInfo,
            {withCredentials: true})
            .then((res) => {
                alert("생성되었습니다.");
                navigate("/");
            })
            .catch(err => alert("문제가 발생했습니다."));
    }

    return (
        <div>
            <Box className={style["btn1"]} sx={{padding: 4}}>

                <Box sx={{ padding: 2, textAlign: 'center' }}>
                    <h1>새 포트폴리오 등록</h1>
                </Box>

                <Box sx={{padding: 2}}>
                    <TextField onChange={(e) => setPortfolioInfo({...portfolioInfo, title: e.target.value})}
                               type="email"
                               id="title"
                               label="포트폴리오 제목"
                               variant="outlined"
                               placeholder="(예) [삼강] 목공 디자인 전문업체"
                               fullWidth={true}
                    />
                    <TextField onChange={(e) => setPortfolioInfo({...portfolioInfo, description: e.target.value})}
                               type="email"
                               id="description"
                               label="포트폴리오 내용"
                               variant="outlined"
                               placeholder=""
                               multiline maxRows={10}
                               fullWidth={true}
                    />
                </Box>

                <Box sx={{padding: 1}}>
                    <label><h3>포트폴리오 이미지 등록</h3></label>
                    <ImageUpload />
                </Box>

                <Box sx={{padding: 1}}>
                    <label><h3>솔루션 등록</h3></label>
                    <SolutionForm />
                </Box>

                <Box sx={{padding: 1, display: 'flex', flexDirection: 'column', gap: 2}}>
                    <label><h3>업체정보</h3></label>
                    <Box sx={{padding: 1, display: 'flex', flexDirection: 'column', gap: 1}}>
                        <Stack direction="row" spacing={1}>
                            <label htmlFor="">업체명</label>
                            <Chip
                                avatar={<Avatar alt="" src={companyInfo.companyName}/>}
                                label={companyInfo.companyName}
                                color="primary"
                                variant="outlined"
                            />
                        </Stack>
                        <Stack direction="row" spacing={1}>
                            <label htmlFor="">연락처</label>
                            <Chip label={companyInfo.phone} color="primary" variant="outlined"/>
                        </Stack>
                    </Box>
                </Box>

                <Box className={style["btn2"]} sx={{padding: 1, display: 'flex', flexDirection: 'row', gap: 2}}>
                    <Button className={style["register-btn"]} variant="contained" onClick={submitPortfolio}>등록</Button>
                    <Button variant="outlined" color="secondary" onClick={() => {/*save form*/}}>임시저장</Button>
                    <Button variant="outlined" color="success" href="#text-buttons" onClick={() => {/*previewPortfolio*/}}>포트폴리오
                        미리보기</Button>
                </Box>
            </Box>
        </div>
    );
}

export default Registration;