import React from 'react';
import style from "../../styles/quotationRequest-detail.module.scss";
import Button from "@mui/material/Button";
import {Card} from "@mui/material";

function QuotationCard({quotation}) {
    return (
        <Card className={style['quotation-card']}>
            <div className={style['quotation-info']} key={quotation.id}>
                {quotation.imageUrls.map(url =>
                    <img className={style['image']} src={url} alt="quotation image" key={url}/>
                )}
                {quotation.totalTransactionAmount}
                {quotation.progress}
                {quotation.progress === "PENDING" &&
                    <div className={style['button-div']}>
                        <Button variant="outlined" className={style['cancel-button']}
                                onClick={() => cancelQuotation(quotation.id)}>
                            견적서 취소
                        </Button>
                        <Button variant="outlined" className={style['edit-button']}
                                onClick={() => navigate("/quotation")}>
                            견적서 수정
                        </Button>
                    </div>
                }
            </div>
        </Card>
    );
}

export default QuotationCard;