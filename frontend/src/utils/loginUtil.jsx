import {getCookie, isEmpty} from "./cookieUtil.jsx";
import axios from "axios";
import {useNavigate} from "react-router-dom";

// 로그인 쿠키 검사, return true -> 로그인 되어있는 상태
export const checkLogin = () => {
    return !isEmpty(getCookie("login"));
}


// Admin 권한 검사
/* 아래 코드와 같이 사용
    const test = async () => await axios.get("/api/company/admin/unapply?page=0&size=2");

    Promise.all([checkAdmin(), test()])
        .then(([_, testResult]) => {
            console.log(testResult.data);
        });
 */
export const checkAdmin = async () => {
    const navigate = useNavigate();

    return await axios.get("/api/member/admin/role", {withCredentials: true})
        .catch(() => {
            alert("관리자 권한이 필요합니다.");
            navigate(-1);
        });
}

// Seller 검사
export const checkSeller = async () => {
    const navigate = useNavigate();

    return await axios.get("/api/member/seller/role", {withCredentials: true})
        .catch(() => {
            alert("판매자 권한이 필요합니다.");
            navigate(-1);
        });
}

// 로그인 검사
export const checkRoleTest = async () => {
    const navigate = useNavigate();

    return await axios.get("/api/member/role", {withCredentials: true})
        .catch(() => {
            alert("로그인이 필요합니다.");
            navigate("/login");
        });
}