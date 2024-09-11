import React, {useEffect, useState} from 'react';
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import style from "../../styles/company-detail.module.scss";
import axios from "axios";
import {checkSeller} from "../../utils/loginUtil.jsx";
import {useNavigate} from "react-router-dom";
import Paper from "@mui/material/Paper";
import {
    Alert,
    Card,
    CardContent,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableRow, Typography
} from "@mui/material";
import Button from "@mui/material/Button";

function CompanyDetail() {

    const navigate = useNavigate();

    const [companyInfo, setCompanyInfo] = useState({
        imageUrl: "",
        companyName: "",
        description: "",
        phone: "",
        address: "",
        applied: true,
        requestPendingCount: 10,
        requestApprovedCount: 10,
    });

    const getCompanyInfo = async () => await axios.get("/api/company/info", {withCredentials: true});
    const getRequestCount = async () => await axios.get("/api/quotationRequest/company/count", {withCredentials: true});

    useEffect(() => {
        Promise.all([getCompanyInfo(), getRequestCount(), checkSeller()])
            .then(([res, countRes, _]) => {

                if (res.data.deleted) {
                    alert("삭제된 회사입니다.");
                    navigate(-1);
                }

                setCompanyInfo({
                    companyId: res.data.companyId,
                    imageUrl: res.data.url,
                    companyName: res.data.companyName,
                    description: res.data.description,
                    phone: res.data.phone,
                    address: `${res.data.address} ${res.data.detailAddress}`,
                    applied: res.data.applied,
                    requestPendingCount: countRes.data.pendingCount,
                    requestApprovedCount: countRes.data.approvedCount,
                });
            })
    }, []);

    const createData = (name, value) => {
        return {name, value};
    }

    const rows = [
        createData("회사명", companyInfo.companyName),
        createData("전화번호", companyInfo.phone),
        createData("주소", companyInfo.address),
        createData("회사 설명", companyInfo.description),
    ];

    return (
        <>
            <Header/>
            <main className={style['main']}>
                <div className={style['container']}>
                    <h2 className={style['sub-title']}>회사 정보</h2>
                    {!companyInfo.applied &&
                        <Alert severity="info">관리자가 회사 승인을 검토하고 있습니다.</Alert>
                    }
                    <div className={style['company-info-div']}>
                        <img className={style['company-img']}
                             src={companyInfo.imageUrl}/>
                        <TableContainer component={Paper} className={style['company-table']}>
                            <Table sx={{minWidth: 240}} aria-label="simple table" className={style['table']}>
                                <TableBody className={style['table-body']}>
                                    {rows.map((row) => (
                                        <TableRow
                                            key={row.name}
                                            sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                            className={style['table-row']}
                                        >
                                            <TableCell align="left"
                                                       className={style['table-name']}>{row.name}</TableCell>
                                            <TableCell align="left">{row.value}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </div>
                    <div className={style['button-div']}>
                        <Button className={style['button']}
                                onClick={() => navigate("/company/edit")}>수정하기</Button>
                    </div>

                    <h2 className={style['sub-title']}>견적신청서</h2>
                    <Card className={style['request-card']}>
                        <CardContent className={style['card-content']}>
                            <div className={style['card-div']}>
                                <div className={style['title']}>진행중인 인테리어</div>
                                <div className={style['count']}>{companyInfo.requestPendingCount}</div>
                            </div>
                            <div className={style['card-div']}>
                                <div className={style['title']}>완료된 인테리어</div>
                                <div className={style['count']}>{companyInfo.requestApprovedCount}</div>
                            </div>
                        </CardContent>
                    </Card>
                    <div className={style['button-div']}>
                        <Button className={style['button']}
                                onClick={() => navigate(`/qr/sellerList/${companyInfo.companyId}`)}>더보기</Button>
                    </div>

                </div>
            </main>
            <Footer/>
        </>
    );
}

export default CompanyDetail;