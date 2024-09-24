import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useLocation, useNavigate} from "react-router-dom";
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import style from "../../styles/company-create.module.scss";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import {useDaumPostcodePopup} from "react-daum-postcode";
import {checkMember, checkSeller} from "../../utils/loginUtil.jsx";
import {formatPhoneNumber} from "../../utils/phoneUtil.jsx";

function CreateEditCompany() {

    const location = useLocation();
    const navigate = useNavigate();
    const isEdit = location.pathname.startsWith("/company/edit");

    const [companyInfo, setCompanyInfo] = useState({
        companyName: "",
        description: "",
        phone: "",
        address: "",
        detailAddress: "",
        image: new File([""], ""),
        imageUrl: "",
    });

    const getCompanyInfo = async () => await axios.get("/api/company/info", {withCredentials: true});

    useEffect(() => {

        if (isEdit) {

            Promise.all([getCompanyInfo(), checkSeller()])
                .then(([res, _]) => {
                    if (res.data.deleted) {
                        alert("삭제된 회사입니다.");
                        navigate(-1);
                    }

                    setCompanyInfo({
                        ...companyInfo,
                        companyName: res.data.companyName,
                        description: res.data.description || "",
                        phone: res.data.phone,
                        address: res.data.address,
                        detailAddress: res.data.detailAddress,
                        imageUrl: res.data.url
                    });
                })
                .catch(() => navigate(-1));
        } else {
            checkMember()
                .catch(() => navigate("/login"));
        }
    }, []);


    const checkInput = () => {
        if (companyInfo.companyName === "") {
            alert("회사 이름을 입력해주세요.");
        } else if (companyInfo.phone.length < 8) {
            alert("회사 전화번호를 입력해주세요.");
        } else if (companyInfo.address === "") {
            alert("회사 주소를 입력해주세요.");
        } else if (companyInfo.detailAddress === "") {
            alert("회사 상세 주소를 입력해주세요.");
        } else {
            return false;
        }

        return true;
    }

    const createEditCompany = () => {

        const confirmMessage = (isEdit) ? "변경사항을 등록하시겠습니까?" : "등록하시겠습니까?";

        const apiUrl = (isEdit) ? "/api/company/edit" : "/api/company";

        if (checkInput()) return;

        if (!confirm(confirmMessage)) return;

        const formData = new FormData();
        formData.append("companyName", companyInfo.companyName);
        formData.append("description", companyInfo.description);
        formData.append("phone", companyInfo.phone);
        formData.append("address", companyInfo.address);
        formData.append("detailAddress", companyInfo.detailAddress);
        formData.append("image", companyInfo.image);

        axios.post(apiUrl,
            formData,
            {withCredentials: true})
            .then((res) => {
                alert((isEdit) ? "수정되었습니다." : "생성되었습니다.");
                navigate("/company/info");
            })
            .catch((err) => {
                alert(err.response.data);
            })
    }

    // 이미지 미리보기
    const [image, setImage] = useState();

    const previewImage = (file) => {
        setImage(URL.createObjectURL(file));
    }

    // 다음 주소 api
    const handleComplete = (data) => {
        let fullAddress = data.address;
        let extraAddress = '';
        let localAddress = data.sido + ' ' + data.sigungu;

        if (data.addressType === 'R') {
            if (data.bname !== '') {
                extraAddress += data.bname;
            }
            if (data.buildingName !== '') {
                extraAddress += (extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName);
            }
            fullAddress = fullAddress.replace(localAddress, '');
            fullAddress += (extraAddress !== '' ? ` (${extraAddress})` : '');
        }

        setCompanyInfo({
            ...companyInfo,
            address: localAddress + fullAddress,
        });
    };

    const postcodeScriptUrl = 'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    const open = useDaumPostcodePopup(postcodeScriptUrl);

    const findAddress = () => {
        open({onComplete: handleComplete});
    };

    return (<>
        <Header/>
        <main className={style['main']}>
            <div className={style['container']}>
                <h2 className={style['page-title']}>
                    {isEdit ? "회사 정보 수정" : "회사 등록"}
                </h2>
                {!isEdit ?
                    <p className={style['page-subtitle']}>회사 등록 후, 승인을 받으면 포트폴리오를 작성할 수 있습니다.</p>
                    : <div className={style["padding"]}></div>
                }
                <div className={style['input-div']}>
                    <TextField className={style['text-field']}
                               value={companyInfo.companyName}
                               onChange={(e) => setCompanyInfo(
                                   {...companyInfo, companyName: e.target.value})}
                               onKeyDown={(event) => {
                                   if (event.key === 'Enter') {

                                       event.preventDefault();
                                   }
                               }}
                               type="text"
                               name="companyName"
                               placeholder=""
                               variant="standard"
                               label="회사 이름"
                               required={true}
                               slotProps={{
                                   inputLabel: {
                                       shrink: true,
                                   }
                               }}/>
                    <TextField className={style['text-field']}
                               value={companyInfo.phone}
                               onChange={(e) => setCompanyInfo(
                                   {...companyInfo, phone: formatPhoneNumber(e.target.value)})}
                               type="text"
                               name="phone"
                               placeholder=""
                               variant="standard"
                               label="회사 전화번호"
                               required={true}
                               slotProps={{
                                   inputLabel: {
                                       shrink: true,
                                   }
                               }}/>
                </div>
                <div className={style['input-div']}>
                    <TextField
                        className={style['text-field']}
                        value={companyInfo.description}
                        onChange={(e) => setCompanyInfo(
                            {...companyInfo, description: e.target.value})}
                        type="text"
                        multiline
                        rows={5}
                        name="description"
                        placeholder=""
                        variant="outlined"
                        label="회사 설명"
                        slotProps={{
                            inputLabel: {
                                shrink: true,
                            },
                            htmlInput: {
                                maxLength: 250,
                            }
                        }}/>
                </div>
                <div className={style['input-div-address']}>
                    <div className={style['address-div']}>
                        <TextField
                            className={style['text-field']}
                            value={companyInfo.address}
                            type="text"
                            name="address"
                            placeholder=""
                            variant="standard"
                            label="회사 주소"
                            required={true}
                            slotProps={{
                                htmlInput: {readOnly: true},

                                inputLabel: {
                                    shrink: true,
                                }
                            }}/>
                        <div className={style['padding']}/>
                        <TextField className={style['text-field-detail']}
                                   value={companyInfo.detailAddress}
                                   onChange={(e) => {
                                       setCompanyInfo({
                                           ...companyInfo,
                                           fullAddress: `${companyInfo.address} ${e.target.value}`,
                                           detailAddress: e.target.value
                                       });
                                   }}
                                   type="text"
                                   name="detailAddress"
                                   placeholder=""
                                   variant="standard"
                                   label="상세 주소"
                                   required={true}
                                   slotProps={{
                                       inputLabel: {
                                           shrink: true,
                                       }
                                   }}/>
                    </div>
                    <Button className={style['sub-button']} variant="outlined"
                            onClick={findAddress}>주소 찾기</Button>
                </div>
                <div className={style['image-div']}>
                    {(!isEdit || isEdit && image != null || isEdit && companyInfo.imageUrl == null)
                        ? <img src={image} className={style['image-blob']}/>
                        : <img src={companyInfo.imageUrl} className={style['image-blob']}/>}
                    <Button className={style['button']}
                            variant="outlined" component="label">
                        <input id="image" name="image" type="file" onChange={(e) => {
                            setCompanyInfo({...companyInfo, image: e.target.files[0]});
                            previewImage(e.target.files[0]);
                        }}
                               hidden/>
                        {isEdit ? "회사 이미지 수정" : "회사 이미지 등록"}
                    </Button>
                </div>

                <div className={style['button-div']}>
                    <Button variant="contained" color="success" onClick={createEditCompany}>
                        {isEdit ? "정보 수정" : "회사 등록"}
                    </Button>
                </div>
            </div>
        </main>
        <Footer/>
    </>);
}

export default CreateEditCompany;