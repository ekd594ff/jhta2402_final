import React, { useState } from "react";
import style from "../../styles/quotationRequest-detail.module.scss";
import Button from "@mui/material/Button";
import { Card, Modal } from "@mui/material";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination } from "swiper/modules";
import { dateFormatter } from "../../utils/dateUtil.jsx";
import axios from "axios";
import { TransformComponent, TransformWrapper } from "react-zoom-pan-pinch";

function QuotationCard({
  quotation,
  cancelQuotation,
  requestProgress,
  isMember,
  navigate,
}) {
  const [modal, setModal] = useState({
    open: false,
    url: "",
  });

  const approveQuotation = (id) => {
    if (!confirm("해당 견적서를 승인처리하시겠습니까?")) return;

    axios
      .patch(`/api/quotation/approve/${id}`, {}, { withCredentials: true })
      .then(() => {
        alert("승인되었습니다.");
        navigate(0);
      })
      .catch(() => alert("문제가 발생했습니다."));
  };

  return (
    <Card className={style["quotation-card"]}>
      <div className={style["quotation-sub-title"]}>
        <div className={style["card-sub-title"]}>
          <div className={style["updated-at"]}>
            {dateFormatter(quotation.updatedAt)}
          </div>
          <div className={style["total"]}>
            <p className={style["price"]}>
              ₩ {Number(quotation.totalTransactionAmount).toLocaleString()}
            </p>
          </div>
        </div>
        <div className={style["button-container"]}>
          {quotation.progress === "PENDING" &&
            !requestProgress.endsWith("CANCELLED") && (
              <div className={style["button-div"]}>
                <Button
                  variant="outlined"
                  className={style["cancel-button"]}
                  onClick={() => cancelQuotation(quotation.id)}
                >
                  견적서 취소
                </Button>
                {/*<Button variant="outlined" className={style['edit-button']}*/}
                {/*        onClick={() => navigate("/quotation")}>*/}
                {/*    견적서 수정*/}
                {/*</Button>*/}
              </div>
            )}
          {isMember && quotation.progress === "PENDING" && (
            <div className={style["button-div"]}>
              <Button
                variant="outlined"
                className={style["edit-button"]}
                onClick={() => approveQuotation(quotation.id)}
              >
                견적서 승인
              </Button>
            </div>
          )}
        </div>
      </div>
      <div className={style["quotation-info"]} key={quotation.id}>
        {quotation.imageUrls.length > 1 ? (
          <Swiper
            modules={[Navigation, Pagination]}
            navigation
            spaceBetween={50}
            slidesPerView={quotation.imageUrls.length === 2 ? 2 : 3}
            scrollbar={{ draggable: true }}
            sx={{ margin: "auto" }}
          >
            {quotation.imageUrls.map((url) => (
              <SwiperSlide key={url}>
                <img
                  style={{
                    height: "auto",
                    width: "100%",
                    objectFit: "cover",
                    cursor: "pointer",
                    border: "0.5px solid",
                    borderColor: "#989898",
                  }}
                  src={url}
                  alt="quotation image"
                  onClick={() => setModal({ open: true, url: url })}
                />
              </SwiperSlide>
            ))}
          </Swiper>
        ) : (
          <img
            style={{
              height: "auto",
              width: "30%",
              objectFit: "cover",
              cursor: "pointer",
              margin: "16px",
              border: "0.5px solid",
              borderColor: "#989898",
            }}
            src={quotation.imageUrls[0]}
            alt="quotation image"
            onClick={() =>
              setModal({ open: true, url: quotation.imageUrls[0] })
            }
          />
        )}
      </div>
      <Modal
        open={modal.open}
        onClose={() => setModal({ open: false, url: "" })}
        style={{ zIndex: "99999" }}
      >
        <div
          style={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            zIndex: "9999",
            width: "80vw",
            height: "80vh",
            cursor: "zoom-in",
          }}
        >
          <TransformWrapper>
            <TransformComponent>
              <img
                style={{
                  width: "80vw",
                  height: "80vh",
                  objectFit: "contain",
                  backgroundColor: "#989898",
                }}
                src={modal.url}
                onClick={() => setModal({ open: false, url: "" })}
              />
            </TransformComponent>
          </TransformWrapper>
        </div>
      </Modal>
    </Card>
  );
}

export default QuotationCard;
