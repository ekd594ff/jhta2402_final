import React, {useEffect, useState} from 'react';
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import style from "../../styles/quotationRequest-detail.module.scss";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import {Card, Typography} from "@mui/material";
import Button from "@mui/material/Button";
import QuotationCard from "../../components/quotation/quotationCard.jsx";

function QuotationRequestDetail() {

    const {id} = useParams();
    const navigate = useNavigate();

    const [quotationRequest, setQuotationRequest] = useState({
        id: "",
        title: "",
        description: "",
        progress: "",
        solutions: [],
        quotations: [],
    });
    const [trigger, setTrigger] = useState(false);

    useEffect(() => {
        axios.get(`/api/quotationRequest/detail/${id}`)
            .then((res) => {
                console.log(res.data)
                setQuotationRequest(res.data);
            })
            .catch(() => {
                alert("문제가 발생했습니다.");
                // navigate(-1);
            })
    }, [trigger]);

    const cancelQuotation = (id) => {
        if (!confirm("해당 견적서를 취소처리 하시겠습니까?")) return;

        axios.patch(`/api/quotation/company/cancel/${id}`)
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
                    <Card className={style['quotationRequest-card']}
                          sx={{margin: "16px 0", padding: "16px", gap: "16px"}}>
                        <Typography variant="h6" sx={{margin: "16px", textAlign: "center"}}>
                            {quotationRequest.title}
                        </Typography>
                        <Typography>
                            {quotationRequest.description}
                        </Typography>
                        <Typography>
                            {quotationRequest.progress}
                        </Typography>
                        {quotationRequest.solutions.map(solution =>
                            <div key={solution.id}>
                                <Typography>
                                    {solution.title}
                                </Typography>
                                <Typography>
                                    {solution.description}
                                </Typography>
                                <Typography>
                                    {solution.price}
                                </Typography>
                            </div>
                        )}
                    </Card>
                    <div className={style['create-button-div']}>
                        <h4>완료된 견적서</h4>
                        <></>
                    </div>
                    {quotationRequest.quotations
                        .filter((quotation) => quotation.progress === "APPROVED")
                        .map(quotation => <QuotationCard quotation={quotation}/>
                        )}
                    <div className={style['create-button-div']}>
                        <h4>진행중인 견적서</h4>
                        <Button variant="contained" className={style['create-button']}
                                onClick={() => navigate("/quotation")}>
                            새 견적서 작성
                        </Button>
                    </div>
                    {quotationRequest.quotations
                        .filter((quotation) => quotation.progress === "PENDING")
                        .map(quotation => <QuotationCard quotation={quotation}/>
                        )}
                    <div className={style['create-button-div']}>
                        <h4>취소된 견적서</h4>
                        <></>
                    </div>
                    {quotationRequest.quotations
                        .filter((quotation) =>
                            quotation.progress === "USER_CANCELLED" ||
                            quotation.progress === "SELLER_CANCELLED" ||
                            quotation.progress === "ADMIN_CANCELLED")
                        .map(quotation => <QuotationCard quotation={quotation}/>
                        )}
                </div>
            </main>
            <Footer/>
        </>
    );
}

export default QuotationRequestDetail;