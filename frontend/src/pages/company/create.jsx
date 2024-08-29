import React, {useState} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";

function CreateCompany() {

    const navigate = useNavigate();

    const [companyInfo, setCompanyInfo] = useState({
        companyName: "",
        description: "",
        phone: "",
        address: "",
        image: new File([""], ""),
    });

    const createCompany = () => {
        const formData = new FormData();
        formData.append("companyName", companyInfo.companyName);
        formData.append("description", companyInfo.description);
        formData.append("phone", companyInfo.phone);
        formData.append("address", companyInfo.address);
        formData.append("image", companyInfo.image);

        axios.post("/api/company",
            formData,
            {withCredentials: true})
            .then((res) => {
                alert("생성되었습니다.");
                navigate("/");
            }).catch(() => {
            alert("문제가 발생했습니다.");
        })
    }

    return (
        <main>
            <label htmlFor="title">제목</label>
            <input id="title" name="title" onChange={(e) =>
                setCompanyInfo({...companyInfo, title: e.target.value})}/>
            <label htmlFor="description">설명</label>
            <input id="description" name="description" onChange={(e) =>
                setCompanyInfo({...companyInfo, description: e.target.value})}/>
            <label htmlFor="phone">전화번호</label>
            <input id="phone" name="phone" onChange={(e) =>
                setCompanyInfo({...companyInfo, phone: e.target.value})}/>
            <label htmlFor="phone">주소</label>
            <input id="address" name="address" onChange={(e) =>
                setCompanyInfo({...companyInfo, address: e.target.value})}/>
            <label htmlFor="image">이미지</label>
            <input id="image" name="image" type="file" onChange={(e) =>
                setCompanyInfo({...companyInfo, image: e.target.files[0]})}/>

            <button onClick={createCompany}>생성</button>
        </main>
    );
}

export default CreateCompany;