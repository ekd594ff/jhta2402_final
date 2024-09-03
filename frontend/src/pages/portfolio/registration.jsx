import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import { Button, TextField, Avatar, Box, Chip, Stack,
    FormControl,
    getImageListItemUtilityClass,
    ImageList,
    ImageListItem,
    Input,
} from "@mui/material";
import ImageUpload from '../../components/portfolio/imageUpload.jsx'
import SolutionForm from "../../components/portfolio/solutionForm.jsx";
import style from "../../styles/portfolio-registration.module.scss";

function Registration() {

    const navigate = useNavigate();

    // useState 받은 데이터 적용하기
    const [companyInfo, setCompanyInfo] = useState({
        address: "",
        companyName: "",
        description: "",
        phone: "",
        url: "",
    });

    // 데이터 보내는 방법
    const [portfolioInfo, setPortfolioInfo] = useState({
        title: "",
        description: "",
    })


    // useEffect(페이지 처음 불러올 때 실행하기) => axios 데이터 불러오기 / 전송하기
    useEffect(() => {
        axios.get("/api/company/info", {withCredentials: true})  // withCredentials: true =>  요청에 로그인 쿠키를 담아서 전송
            .then(res => setCompanyInfo(res.data));
    }, []);

    // 로컬에서 임시 저장한 내용을 불러오기
    useEffect(() => {
        const savedDraft = localStorage.getItem('portfolioDraft');
        if (savedDraft) {
            setPortfolioInfo(JSON.parse(savedDraft));
        }
    }, []);


    // 포트폴리오 등록
    const submitPortfolio = () => {
        axios.post("/api/portfolio",
            portfolioInfo,
            {withCredentials: true})
            .then((res) => {
                alert("포트폴리오가 등록되었습니다.");
                navigate("/");
            })
            .catch(err => alert("오류가 발생했습니다. 다시 시도해주세요."));
    }

    // 작성중인 내용 서버에 임시저장(미사용)
    const saveDraftPortfolio = () => {
        axios.post("/api/portfolio/draft",
            portfolioInfo,
            { withCredentials: true })
            .then((res) => {
                alert("포트폴리오가 임시 저장되었습니다.");
            })
            .catch(err => alert("오류가 발생했습니다. 다시 시도해주세요."));
    }

    // 작성중인 내용 로컬에 임시저장
    const saveDraftToLocal = () => {
        localStorage.setItem('portfolioDraft', JSON.stringify(portfolioInfo));
        alert("현재 기기에 임시 저장되었습니다.");
    }

    return (
        <div>
            <Box className={style["btn1"]} sx={{padding: 4}}>

                <Box sx={{ padding: 2, textAlign: 'center' }}>
                    <h1>새 포트폴리오 등록</h1>
                </Box>

                <Box sx={{padding: 2}}>
                    <TextField
                        value={portfolioInfo.title} // value로 상태를 설정
                        onChange={(e) => setPortfolioInfo({...portfolioInfo, title: e.target.value})}
                        type="email"
                        id="title"
                        label="포트폴리오 제목"
                        variant="outlined"
                        placeholder="(예) [삼강] 목공 디자인 전문업체"
                        fullWidth={true}
                    />
                    <TextField
                        value={portfolioInfo.description}
                        onChange={(e) => setPortfolioInfo({...portfolioInfo, description: e.target.value})}
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
                    <Button variant="outlined" color="secondary" onClick={saveDraftToLocal}>임시저장</Button>
                    <Button variant="outlined" color="success" href="#text-buttons" onClick={() => {/*previewPortfolio*/}}>포트폴리오
                        미리보기</Button>
                </Box>
            </Box>
        </div>
    );
}

export default Registration;