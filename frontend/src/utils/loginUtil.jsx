import axios from "axios";


// Admin 권한 검사
/* 아래 코드와 같이 사용
    const test = async () =>
        await axios.get("/api/company/admin/unapply?page=0&size=2")
            .catch(() => alert("오류가 발생했습니다."));

    useEffect(() => {
        Promise.all([checkAdmin(), test()])
        .then(([_, testResult]) => {
            console.log(testResult.data);
        })
        .catch(() => navigate(-1));
    }, []);
 */
export const checkAdmin = async () => {

    return await axios.get("/api/member/admin/role", {withCredentials: true})
        .catch(() => alert("관리자 권한이 필요합니다."));
}

// Seller 검사
export const checkSeller = async () => {

    return await axios.get("/api/member/seller/role", {withCredentials: true})
        .catch(() => alert("판매자 권한이 필요합니다."));
}

// 로그인 검사
export const checkMember = async () => {

    return await axios.get("/api/member/role", {withCredentials: true})
        .catch(() => alert("로그인이 필요합니다."));
}