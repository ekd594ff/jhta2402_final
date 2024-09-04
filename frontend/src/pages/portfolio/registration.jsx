import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {
    Button,
    TextField,
    Box,
    Step,
    StepLabel,
    Stepper,
    StepConnector,
    stepConnectorClasses,
    styled, Alert, Snackbar, Card, Grid2
} from "@mui/material";
import style from "../../styles/portfolio-registration.module.scss";
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import {Check} from "@mui/icons-material";
import {HTML5Backend} from "react-dnd-html5-backend";
import Stack from "@mui/material/Stack";
import ImageItem from "../../components/portfolio/ImageItem.jsx";
import {DndProvider} from "react-dnd";
import ImageUpload from "../../components/portfolio/imageUpload.jsx";


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
    });

    const [images, setImages] = useState([]);

    const [solutions, setSolutions] = useState([]);

    const [otherImages, setOtherImages] = useState([]);


    // useEffect(페이지 처음 불러올 때 실행하기) => axios 데이터 불러오기 / 전송하기
    useEffect(() => {
        axios.get("/api/company/info", {withCredentials: true})  // withCredentials: true =>  요청에 로그인 쿠키를 담아서 전송
            .then(res => setCompanyInfo(res.data));
    }, []);

    // 로컬에서 임시 저장한 내용을 불러오기
    // useEffect(() => {
    //     const savedDraft = localStorage.getItem('portfolioDraft');
    //     if (savedDraft) {
    //         setPortfolioInfo(JSON.parse(savedDraft));
    //     }
    // }, []);


    // 포트폴리오 등록
    const submitPortfolio = () => {
        const formData = new FormData();

        formData.append("title", portfolioInfo.title);
        formData.append("description", portfolioInfo.description);
        images.map(image => {
            console.log(image)
            formData.append("images", image)
        });
        formData.append("solutionStrings", JSON.stringify(solutions));


        axios.post("/api/portfolio",
            formData,
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
            {withCredentials: true})
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

    const QontoConnector = styled(StepConnector)(({theme}) => ({
        [`&.${stepConnectorClasses.active}`]: {
            [`& .${stepConnectorClasses.line}`]: {
                borderColor: '#FA4D56',
            },
        },
        [`&.${stepConnectorClasses.completed}`]: {
            [`& .${stepConnectorClasses.line}`]: {
                borderColor: '#FA4D56',
            },
        },
    }));

    const QontoStepIconRoot = styled('div')(({theme}) => ({
        color: '#eaeaf0',
        display: 'flex',
        height: 22,
        alignItems: 'center',
        '& .QontoStepIcon-completedIcon': {
            color: '#FA4D56',
            zIndex: 1,
            fontSize: 24,
        },
        '& .QontoStepIcon-circle': {
            width: 16,
            height: 16,
            borderRadius: '50%',
            backgroundColor: 'currentColor',
        },
        ...theme.applyStyles('dark', {
            color: theme.palette.grey[700],
        }),
        variants: [
            {
                props: ({ownerState}) => ownerState.active,
                style: {
                    color: '#FA4D56',
                },
            },
        ],
    }));

    const QontoStepIcon = (props) => {
        const {active, completed, className} = props;

        return (
            <QontoStepIconRoot ownerState={{active}} className={className}>
                {completed ? (
                    <Check className="QontoStepIcon-completedIcon"/>
                ) : (
                    <div className="QontoStepIcon-circle"/>
                )}
            </QontoStepIconRoot>
        );
    }


    // solution ----------------------------------------------

    const addSolution = () => {

        setSolutions([...solutions, {title: '', description: '', price: ''}]);
    };

    const moveSolution = (dragIndex, hoverIndex) => {
        const newSolutions = [...solutions];
        const [draggedSolution] = newSolutions.splice(dragIndex, 1);
        newSolutions.splice(hoverIndex, 0, draggedSolution);
        setSolutions(newSolutions);
    };

    const handleInputChange = (index, event) => {
        const newSolutions = [...solutions];
        newSolutions[index][event.target.name] = event.target.value;
        setSolutions(newSolutions);
    };

    const handleCurrencyChange = (index, event) => {
        const newSolutions = [...solutions];
        newSolutions[index].currency = event.target.value;
        setSolutions(newSolutions);
    };

    const handleRemoveSolution = (index) => {
        const newSolutions = solutions.filter((_, i) => i !== index);
        setSolutions(newSolutions);
    };

    // solution Drag&Drop

    return (
        <>
            <Header/>
            <main className={style["main"]}>

                <div className={style["container"]}>
                    <h2 className={style["title"]}>새 포트폴리오 등록</h2>

                    <Stepper activeStep={activeStep} alternativeLabel
                             connector={<QontoConnector/>}>
                        {steps.map((label) => (
                            <Step className={style["step"]} key={label}>
                                <StepLabel StepIconComponent={QontoStepIcon}>{label}</StepLabel>
                            </Step>
                        ))}
                    </Stepper>

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
                        <ImageUpload images={images} setImages={setImages} otherImages={otherImages}
                                     setOtherImages={setOtherImages}/>

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
                        <Box sx={{margin: 4, padding: 1}}>
                            {/*<DndProvider backend={HTML5Backend}>*/}
                            {/*    <Box sx={{padding: 0, textAlign: 'left'}}>*/}
                            {/*        <Button id="addSolution" onClick={addSolution}*/}
                            {/*                variant="outlined"*/}
                            {/*                style={{*/}
                            {/*                    borderColor: '#FA4D56',*/}
                            {/*                    color: '#FA4D56',*/}
                            {/*                    margin: '0 0 16px 0',*/}
                            {/*                }}*/}
                            {/*        >솔루션 추가</Button>*/}
                            {/*        <div>*/}
                            {/*            {solutions.map((solution, index) => (*/}
                            {/*                <SolutionItem*/}
                            {/*                    className={style["solution-item"]}*/}
                            {/*                    key={index}*/}
                            {/*                    index={index}*/}
                            {/*                    solution={solution}*/}
                            {/*                    moveSolution={moveSolution}*/}
                            {/*                    handleInputChange={handleInputChange}*/}
                            {/*                    handleCurrencyChange={handleCurrencyChange}*/}
                            {/*                    handleRemoveSolution={handleRemoveSolution}*/}
                            {/*                />*/}
                            {/*            ))}*/}
                            {/*        </div>*/}
                            {/*    </Box>*/}
                            {/*</DndProvider>*/}
                        </Box>

                        <div className={style["button-div"]}>
                            <Button className={style["prev-button"]} variant="outlined"
                                    onClick={() => setActiveStep(1)}>
                                이전
                            </Button>
                            <Button className={style["register-button"]} variant="contained"
                                    onClick={submitPortfolio}>등록</Button>
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