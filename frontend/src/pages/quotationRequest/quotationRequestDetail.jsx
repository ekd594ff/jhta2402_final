import React, {useEffect, useState} from 'react';
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import style from "../../styles/quotationRequest-detail.module.scss";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import {Box, Card, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography} from "@mui/material";
import Button from "@mui/material/Button";
import QuotationCard from "../../components/quotation/quotationCard.jsx";
import CustomStepper from "../../components/quotation/customStepper.jsx";
import Paper from "@mui/material/Paper";

function QuotationRequestDetail() {

    const {id} = useParams();
    const navigate = useNavigate();

    const [quotationRequest, setQuotationRequest] = useState({
        id: "",
        title: "",
        description: "",
        progress: "PENDING",
        solutions: [],
        quotations: [],
    });
    const [trigger, setTrigger] = useState(false);
    const [isMember, setIsMember] = useState(true);

    const steps = ['견적신청서 등록', '견적서 등록', '고객 승인 대기', '승인 완료'];
    const [activeStep, setActiveStep] = useState(-1);

    const getQuotationDetail = async () => await axios.get(`/api/quotationRequest/detail/${id}`, {withCredentials: true});

    useEffect(() => {
        getQuotationDetail()
            .then((res) => {
                setQuotationRequest(res.data);

                if (res.data.progress.endsWith("CANCELLED")) {
                    setActiveStep(-1);
                } else if (res.data.progress === "APPROVED") {
                    setActiveStep(4);
                } else if (res.data.quotations.some(quotation => quotation.progress === "PENDING")) {
                    setActiveStep(2);
                } else if (res.data.quotations.length > 0) {
                    setActiveStep(1);
                } else {
                    setActiveStep(0);
                }

                if (res.data.loginEmail !== res.data.member.email) {
                    setIsMember(false);
                }
            })
            .catch(() => {
                alert("문제가 발생했습니다.");
                navigate(-1);
            })
    }, [trigger]);

    const cancelQuotation = (id) => {
        if (!confirm("해당 견적서를 취소처리 하시겠습니까?")) return;

        const url = (isMember)
            ? `/api/quotation/cancel/${id}`
            : `/api/quotation/company/cancel/${id}`

        axios.patch(url)
            .then(() => {
                alert("취소처리 되었습니다.");
                setTrigger(prev => !prev);
            })
            .catch(() => alert("문제가 발생했습니다."));
    }

    return (
        <>
            <Header/>
            <main className={style['main']}>
                <div className={style['container']}>
                    <Box sx={{height: "48px"}}/>
                    <CustomStepper className={style['custom-stepper']} activeStep={activeStep} steps={steps}/>
                    <Box sx={{height: "48px"}}/>
                    <h4 className={style['sub-title']}>견적신청서</h4>
                    <Card className={style['quotationRequest-card']}
                          sx={{margin: "16px 0", padding: "16px", gap: "16px"}}>
                        <Typography variant="h6" sx={{margin: "16px", textAlign: "center"}}>
                            {quotationRequest.title}
                        </Typography>
                        <Typography>
                            {quotationRequest.description}
                        </Typography>
                        <TableContainer variant="outlined" component={Paper}>
                            <Table aria-label="solution table">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>솔루션 제목</TableCell>
                                        <TableCell>내용</TableCell>
                                        <TableCell>가격</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {quotationRequest.solutions.map(solution =>
                                        <TableRow key={solution.id}>
                                            <TableCell>{solution.title}</TableCell>
                                            <TableCell>{solution.description}</TableCell>
                                            <TableCell sx={{opacity: 1}}>
                                                ₩ {Number(solution.price).toLocaleString()}
                                            </TableCell>
                                        </TableRow>
                                    )}
                                    <TableRow>
                                        <TableCell></TableCell>
                                        <TableCell></TableCell>
                                        <TableCell sx={{fontWeight: 500, opacity: 1}}>
                                            ₩ {Number(quotationRequest.solutions.reduce((result, solution) => result + solution.price, 0)).toLocaleString()}
                                        </TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Card>
                    <div className={style['create-button-div']}>
                        <h4 className={style['sub-title']}>완료된 견적서</h4>
                        {quotationRequest.progress === "PENDING" &&
                            !isMember &&
                            <Button variant="contained" className={style['create-button']}
                                    onClick={() => navigate(`/quotation/form/${quotationRequest.id}`)}>
                                새 견적서 작성
                            </Button>
                        }
                    </div>
                    {quotationRequest.quotations
                        .filter((quotation) => quotation.progress === "APPROVED")
                        .map(quotation => <QuotationCard key={quotation.id} quotation={quotation}
                                                         cancelQuotation={cancelQuotation}
                                                         requestProgress={quotationRequest.progress}
                                                         isMember={isMember}
                                                         navigate={navigate}/>
                        )}
                    {quotationRequest.quotations
                            .filter((quotation) => quotation.progress === "APPROVED")
                            .length === 0 &&
                        <div className={style['no-content']}>해당 조건의 견적서가 없습니다.</div>
                    }
                    <div className={style['create-button-div']}>
                        <h4 className={style['sub-title']}>진행중인 견적서</h4>
                        <></>
                    </div>
                    {quotationRequest.quotations
                        .filter((quotation) => quotation.progress === "PENDING")
                        .map(quotation => <QuotationCard key={quotation.id} quotation={quotation}
                                                         cancelQuotation={cancelQuotation}
                                                         requestProgress={quotationRequest.progress}
                                                         isMember={isMember}
                                                         navigate={navigate}/>
                        )}
                    {quotationRequest.quotations
                            .filter((quotation) => quotation.progress === "PENDING")
                            .length === 0 &&
                        <div className={style['no-content']}>해당 조건의 견적서가 없습니다.</div>
                    }
                    <div className={style['create-button-div']}>
                        <h4 className={style['sub-title']}>취소된 견적서</h4>
                        <></>
                    </div>
                    {quotationRequest.quotations
                        .filter((quotation) =>
                            quotation.progress === "USER_CANCELLED" ||
                            quotation.progress === "SELLER_CANCELLED" ||
                            quotation.progress === "ADMIN_CANCELLED")
                        .map(quotation => <QuotationCard key={quotation.id} quotation={quotation}
                                                         cancelQuotation={cancelQuotation}
                                                         requestProgress={quotationRequest.progress}
                                                         isMember={isMember}
                                                         navigate={navigate}/>
                        )}
                    {quotationRequest.quotations
                            .filter((quotation) =>
                                quotation.progress === "USER_CANCELLED" ||
                                quotation.progress === "SELLER_CANCELLED" ||
                                quotation.progress === "ADMIN_CANCELLED")
                            .length === 0 &&
                        <div className={style['no-content']}>해당 조건의 견적서가 없습니다.</div>
                    }
                    <Box sx={{height: "32px"}}/>
                </div>
            </main>
            <Footer/>
        </>
    );
}

export default QuotationRequestDetail;