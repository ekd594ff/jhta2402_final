import React, {useEffect, useState} from 'react';
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import style from "../../styles/quotationRequest-detail.module.scss";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import {Card, Typography} from "@mui/material";
import Button from "@mui/material/Button";

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
                    <h2>견적신청서</h2>
                    <Card className={style['quotationRequest-card']}>
                        <Typography>
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
                    <h2>견적서</h2>
                    <div>
                        <Button onClick={() => navigate("/quotation")}>
                            견적서 작성
                        </Button>
                    </div>
                    <Card className={style['quotation-card']}>
                        {quotationRequest.quotations.map(quotation =>
                            <div className={style['quotation-info']} key={quotation.id}>
                                {quotation.imageUrls.map(url =>
                                    <img className={style['image']} src={url} alt="quotation image" key={url}/>
                                )}
                                {quotation.totalTransactionAmount}
                                {quotation.progress}
                                {quotation.progress === "PENDING" &&
                                    <div>
                                        <Button onClick={() => navigate("/quotation")}>
                                            견적서 수정
                                        </Button>
                                        <Button onClick={() => cancelQuotation(quotation.id)}>
                                            견적서 취소
                                        </Button>
                                    </div>
                                }
                            </div>
                        )}
                    </Card>
                </div>
            </main>
            <Footer/>
        </>
    );
}

export default QuotationRequestDetail;