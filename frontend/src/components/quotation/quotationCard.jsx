import React, {useState} from 'react';
import style from "../../styles/quotationRequest-detail.module.scss";
import Button from "@mui/material/Button";
import {Card, Modal} from "@mui/material";
import {Swiper, SwiperSlide} from "swiper/react";
import {Navigation, Pagination} from "swiper/modules";
import {dateFormatter} from "../../utils/dateUtil.jsx";

function QuotationCard({quotation, cancelQuotation, requestProgress}) {

    const [modal, setModal] = useState({
        open: false,
        url: "",
    });

    return (
        <Card className={style['quotation-card']}>
            <div className={style['quotation-sub-title']}>
                <div className={style['updated-at']}>
                    {dateFormatter(quotation.updatedAt)}
                    <div className={style['total']}>
                        <p className={style['price']}>₩{quotation.totalTransactionAmount}</p>
                    </div>
                </div>
                {quotation.progress === "PENDING" && !requestProgress.endsWith("CANCELLED") &&
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
            <div className={style['quotation-info']} key={quotation.id}>
                {quotation.imageUrls.length > 1 ?
                    <Swiper modules={[Navigation, Pagination]}
                            navigation
                            spaceBetween={50}
                            slidesPerView={3}
                            scrollbar={{draggable: true}}
                            sx={{margin: "auto"}}>
                        {quotation.imageUrls.map(url =>
                            <SwiperSlide key={url} sx={{height: "64px", width: "64px", margin: "auto"}}>
                                <img style={{height: "auto", width: "100%", objectFit: "cover", cursor: "zoom-in"}}
                                     src={url}
                                     alt="quotation image"
                                     onClick={() => setModal({open: true, url: url})}/>
                            </SwiperSlide>
                        )}
                    </Swiper>
                    :
                    <img style={{height: "auto", width: "30%", objectFit: "cover", cursor: "zoom-in"}}
                         src={quotation.imageUrls[0]}
                         alt="quotation image"
                         onClick={() => setModal({open: true, url: quotation.imageUrls[0]})}/>
                }
            </div>
            <Modal open={modal.open} onClose={() => setModal({open: false, url: ""})}>
                <img style={{
                    position: "absolute", top: "50%", left: "50%",
                    transform: 'translate(-50%, -50%)',
                    zIndex: "150",
                    width: "90%",
                    height: "90%",
                    objectFit: "contain",
                    backgroundColor: "#989898",
                    cursor: "zoom-out"
                }} src={modal.url}
                     onClick={() => setModal({open: false, url: ""})}/>
            </Modal>
        </Card>
    );
}

export default QuotationCard;