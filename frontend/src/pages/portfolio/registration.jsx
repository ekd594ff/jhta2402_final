import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import {
    Button,
    TextField,
    Alert,
    Snackbar, Card, Grid2
} from "@mui/material";
import style from "../../styles/portfolio-registration.module.scss";
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import ImageUpload from "../../components/portfolio/imageUpload.jsx";
import SolutionForm from "../../components/portfolio/solutionForm.jsx";
import {checkSeller} from "../../utils/loginUtil.jsx";
import CustomStepper from "../../components/quotation/customStepper.jsx";


function Registration() {

    const navigate = useNavigate();

    const location = useLocation();
    const isEdit = location.pathname.startsWith("/portfolio/edit");
    const {id} = (isEdit) ? useParams() : "";

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
    });

    const [solutions, setSolutions] = useState([]);

    const [images, setImages] = useState([]);

    const getCompanyInfo = async () => await axios.get("/api/company/info", {withCredentials: true});

    Promise.all([checkSeller(), getCompanyInfo()])
        .then(([_, res]) => setCompanyInfo(res.data));


    if (isEdit) {

        useEffect(() => {
            axios.get(`/api/portfolio/my/${id}`)
                .then((res) => {
                    setPortfolioInfo({
                        title: res.data.title,
                        description: res.data.description,
                    });

                    const imageUrls = res.data.images.map(image => ({
                        id: image.id,
                        url: image.url,
                        file: new File([""], image.id)
                    }));
                    setImages([...imageUrls]);

                    const responseSolutions = res.data.solution.map(solution => ({
                        id: solution.id,
                        title: solution.title,
                        description: solution.description,
                        price: solution.price,
                    }));
                    setSolutions([...responseSolutions]);

                })
                .catch(() => {
                    alert("에러가 발생했습니다.");
                    // navigate(-1);
                })
        }, []);
    }

    // 로컬에서 임시 저장한 내용을 불러오기
    // useEffect(() => {
    //     const savedDraft = localStorage.getItem('portfolioDraft');
    //     if (savedDraft) {
    //         setPortfolioInfo(JSON.parse(savedDraft));
    //     }
    // }, []);


    // 포트폴리오 등록, 수정
    const submitPortfolio = () => {
        const isEmpty = solutions.some(solution => {
            if (solution.title === "" || solution.description === "" || solution.price === "") {
                return true;
            }
        });

        if (isEmpty) {
            setSnackbarState({open: true, message: "솔루션 빈칸을 채워주세요."})
            return;
        }
        if (!confirm(`포트폴리오를 ${(isEdit ? "수정" : "등록")}하시겠습니까?`)) return;

        const formData = new FormData();

        if (isEdit) formData.append("id", id);
        formData.append("title", portfolioInfo.title);
        formData.append("description", portfolioInfo.description);
        images.map(image => formData.append("images", image.file));
        formData.append("solutionStrings", JSON.stringify(solutions));


        if (!isEdit) {
            axios.post("/api/portfolio",
                formData,
                {withCredentials: true})
                .then((res) => {
                    alert(`포트폴리오가 등록되었습니다.`);
                    navigate("/");
                })
                .catch(err => alert(err.response.data + "\r\n오류가 발생했습니다. 다시 시도해주세요."));
        } else {
            axios.patch(`/api/portfolio/${id}`,
                formData,
                {withCredentials: true})
                .then((res) => {
                    alert(`포트폴리오가 수정되었습니다.`);
                    navigate("/");
                })
                .catch(err => alert(err + "오류가 발생했습니다. 다시 시도해주세요."));
        }

    }

    // 작성중인 내용 서버에 임시저장(미사용)
    // const saveDraftPortfolio = () => {
    //     axios.post("/api/portfolio/draft",
    //         portfolioInfo,
    //         {withCredentials: true})
    //         .then((res) => {
    //             alert("포트폴리오가 임시 저장되었습니다.");
    //         })
    //         .catch(err => alert("오류가 발생했습니다. 다시 시도해주세요."));
    // }

    // 작성중인 내용 로컬에 임시저장
    // const saveDraftToLocal = () => {
    //     localStorage.setItem('portfolioDraft', JSON.stringify(portfolioInfo));
    //     alert("현재 기기에 임시 저장되었습니다.");
    // }

    const steps = ['포트폴리오 정보 입력', '이미지 등록', '솔루션 입력'];
    const [activeStep, setActiveStep] = useState(0);

    const [snackbarState, setSnackbarState] = useState({
        open: false,
        message: "",
    });

    const checkInputAndNext = () => {
        if (portfolioInfo.title === "") {
            setSnackbarState({
                open: true,
                message: "포트폴리오 제목을 작성해주세요."
            });
        } else if (portfolioInfo.description === "") {
            setSnackbarState({
                open: true,
                message: "포트폴리오 설명을 작성해주세요."
            });
        } else {
            setActiveStep(1);
        }
    }

    return (
        <>
            <Header/>
            <main className={style["main"]}>

                <div className={style["container"]}>
                    <h2 className={style["title"]}>
                        {(isEdit) ? "포트폴리오 수정" : "새 포트폴리오 등록"}
                    </h2>

                    <CustomStepper activeStep={activeStep} steps={steps}/>

                    {activeStep === 0 &&
                        <div className={style["form1"]}>
                            <div className={style["textfield-div"]}>
                                <TextField
                                    className={style["default-textfield"]}
                                    value={portfolioInfo.title} // value로 상태를 설정
                                    onChange={(e) => setPortfolioInfo({...portfolioInfo, title: e.target.value})}
                                    type="email"
                                    id="title"
                                    label="포트폴리오 제목"
                                    variant="standard"
                                    placeholder="(예) [삼강] 목공 디자인 전문업체"
                                    required={true}
                                    slotProps={{
                                        inputLabel: {
                                            shrink: true,
                                        }
                                    }}/>
                                <TextField
                                    className={style["outlined-textfield"]}
                                    value={portfolioInfo.description}
                                    onChange={(e) => setPortfolioInfo({...portfolioInfo, description: e.target.value})}
                                    type="email"
                                    id="description"
                                    label="포트폴리오 내용"
                                    variant="outlined"
                                    placeholder=""
                                    multiline rows={10}
                                    required={true}
                                    slotProps={{
                                        inputLabel: {
                                            shrink: true,
                                        }
                                    }}/>
                            </div>

                            <Card className={style["company-div"]} variant="outlined">
                                <Grid2 className={style["info-container"]} container spacing={2}>
                                    <Grid2 className={style["info-grid"]} size={6}>
                                        <p className={style["info-title"]}>업체명</p>
                                        <p>{companyInfo.companyName}</p>
                                    </Grid2>
                                    <Grid2 className={style["info-grid"]} size={6}>
                                        <p className={style["info-title"]}>연락처</p>
                                        <p>{companyInfo.phone}</p>
                                    </Grid2>
                                </Grid2>
                            </Card>

                            <div className={style["button-div"]}>
                                <Button className={style["next-button"]} variant="outlined"
                                        onClick={checkInputAndNext}>
                                    다음
                                </Button>
                            </div>
                        </div>}

                    {activeStep === 1 && <div className={style["form2"]}>
                        <ImageUpload images={images}
                                     setImages={setImages}/>

                        <div className={style["button-div"]}>
                            <Button className={style["prev-button"]} variant="outlined"
                                    onClick={() => setActiveStep(0)}>
                                이전
                            </Button>
                            <Button className={style["next-button"]} variant="outlined"
                                    onClick={() => setActiveStep(2)}>
                                다음
                            </Button>
                        </div>
                    </div>}

                    {activeStep === 2 && <div className={style["form3"]}>
                        <SolutionForm solutions={solutions} setSolutions={setSolutions}/>

                        <div className={style["button-div"]}>
                            <Button className={style["prev-button"]} variant="outlined"
                                    onClick={() => setActiveStep(1)}>
                                이전
                            </Button>
                            <Button className={style["register-button"]} variant="contained"
                                    onClick={submitPortfolio}>
                                {(isEdit) ? "수정" : "등록"}
                            </Button>
                        </div>
                    </div>
                    }
                </div>

                <Snackbar
                    className={style["snackbar"]}
                    anchorOrigin={{vertical: "top", horizontal: "center"}}
                    open={snackbarState.open}
                    onClose={() => setSnackbarState({...snackbarState, open: false})}
                    message={snackbarState.message}>
                    <Alert
                        onClose={() => setSnackbarState({...snackbarState, open: false})}
                        severity="error"
                        variant="outlined"
                        sx={{
                            width: '100%',
                            bgcolor: 'background.paper',
                        }}
                    >
                        {snackbarState.message}
                    </Alert>
                </Snackbar>
            </main>
            <Footer/>
        </>
    );
}

export default Registration;