import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useDropzone} from "react-dropzone";

import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";

import axios from "axios";
import {
    ListItem,
    List,
    Collapse,
    Chip,
    Button,
    Backdrop, Alert
} from "@mui/material";
import {ExpandLess, ExpandMore} from "@mui/icons-material";
import Divider from "@mui/material/Divider";

import style from "../../styles/quotation-form.module.scss";
import IconButton from "@mui/material/IconButton";
import * as PropTypes from "prop-types";

function QuotationContent(props) {
    const {
        description,
        memberId,
        portfolioId,
        progress,
        solutions,
        title,
        username,
    } = props;

    const navigator = useNavigate();

    return <>
        <div className={style['top']}>
            <div className={style['info']}>
                <span className={style['username']}>{username}</span>
                <Chip label={"포트폴리오"} className={style['portfolio']} size="small" variant="filled" onClick={() => {
                    navigator(`/portfolio/${portfolioId}`)
                }}/>
            </div>
            <div className={style['title']}>{title}</div>
            <pre className={style['description']}>{description}</pre>
        </div>
        <div className={style['middle']}>
            <List className={style['list']}>
                {
                    solutions.map((solution, index) => {
                        const {title, description, price} = solution;
                        const [open, setOpen] = useState(false);
                        const render = [<ListItem className={style['solution']} key={index}>
                            <div className={style['info']} onClick={() => {
                                setOpen(prev => !prev)
                            }}>
                                <div className={style['detail']}>
                                    <span className={style['title']}>{title}</span>
                                    <span className={style['price']}>{price.toLocaleString()} 원</span>
                                </div>
                                {open ? <ExpandLess/> : <ExpandMore/>}
                            </div>
                            <Collapse className={style['collapse']} in={open} timeout="auto" unmountOnExit>
                                <pre className={style['description']}>{description}</pre>
                            </Collapse>
                        </ListItem>];
                        if (index !== solutions.length - 1) {
                            render.push(<Divider className={style['divider']} variant="middle"
                                                 key={`sol_div_${index}`}/>)
                        }
                        return render;
                    })
                }
            </List>
        </div>
        <div className={style['bottom']}>
            <span className={style['total-price']}>
                결제금액
                <span>
                    {solutions.reduce((acc, cur) => {
                        acc += cur.price;
                        return acc;
                    }, 0).toLocaleString()}
                    원
                </span>
            </span>
        </div>
    </>
}

function CloseIcon(props) {
    return null;
}

CloseIcon.propTypes = {fontSize: PropTypes.string};

function QuotationForm() {
    const {requestId} = useParams();

    const [quotationRequest, setQuotationRequest] = useState(null);
    const [total, setTotal] = useState(0);
    const [files, setFiles] = useState([]);
    const [modal, setModal] = useState({
        open: false, src: ""
    });
    const [alert, setAlert] = useState({
        open: true,
        message: "",
        severity: ""
    });

    const {getRootProps, getInputProps} = useDropzone({
        accept: {
            'image/*': []
        },
        onDrop: acceptedFiles => {
            setFiles(acceptedFiles.map(file => Object.assign(file, {
                preview: URL.createObjectURL(file)
            })));
        }
    });

    const onSubmit = () => {
        const formData = new FormData();
        formData.append("quotationRequestId", requestId);
        formData.append("totalTransactionAmount", total);
        files.forEach(file => {
            formData.append("imageFiles", file)
        });
        axios.post("/api/quotation/company/create", formData, {
            headers: {
                "Content-Type": 'multipart/form-data'
            }
        }).then(result => {
            setAlert(prev => ({open: true, severity: "success", message: "견적서 제출이 완료되었습니다"}));
        }).catch(err => {
            setAlert(prev => ({open: false, severity: "", message: "에러가 발생했습니다"}));
        }).finally(() => {
            setTimeout(() => {
                setAlert({open: false, severity: "warning", message: ""});
            }, 2500);
        })
    }

    useEffect(() => {
        if (requestId) {
            axios.get(`/api/quotationRequest/${requestId}`)
                .then((result) => {
                    const {data} = result;
                    setQuotationRequest(data);
                    const {solutions} = data;
                    setTotal(solutions.reduce((acc, cur) => {
                        acc += cur.price
                        return acc;
                    }, 0));
                })
        }
    }, []);

    useEffect(() => {
        if (modal.open) {
            document.body.classList.add("modal");
        } else {
            document.body.classList.remove("modal");
        }
    }, [modal.open]);


    return <>
        <Header/>
        <main className={style['quotation-form']}>
            <div className={style['container']}>
                <Collapse className={style['collapse']} in={alert.open}>
                    <Alert
                        severity={alert.severity}
                    >
                        {alert.message}
                    </Alert>
                </Collapse>
                <div className={style['title']}>견적서 작성</div>
                <div className={style['request']}>
                    <div className={style['container']}>
                        {quotationRequest ? <QuotationContent {...quotationRequest}/> : <></>}
                    </div>
                </div>
                <div className={style['title']}>이미지 첨부</div>
                <div {...getRootProps({className: style['upload-image']})}>
                    <input {...getInputProps()} />
                    <p>이미지 업로드</p>
                </div>
                <div className={style['preview']}>
                    {
                        files.map((file, index) => {
                            return <div className={style['preview-item']} key={`f_p_${index}`}
                                        onClick={
                                            () => {
                                                setModal({src: file.preview, open: true});
                                            }
                                        }>
                                <img src={file.preview} alt='image'/>
                            </div>
                        })
                    }
                </div>
                <div className={style['btn-group']}>
                    <Button className={style['list']} disableRipple>목록으로</Button>
                    <Button className={style['submit']} disableRipple onClick={onSubmit}>견적서 제출</Button>
                </div>
            </div>
            <Backdrop
                open={modal.open}
                onClick={() => {
                    setModal(prev => ({...prev, open: false}));
                }}
                className={style['modal']}
            >
                <img src={modal.src} alt="preview"/>
            </Backdrop>
        </main>
        <Footer/>
    </>
}

export default QuotationForm;