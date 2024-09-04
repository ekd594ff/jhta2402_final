import * as React from 'react';
import {Button, TextField} from "@mui/material";
import Avatar from '@mui/material/Avatar';
import {Link, useNavigate} from "react-router-dom";
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import style from "../../styles/header.module.scss";
import {useEffect} from "react";
import axios from "axios";

function Header() {
    const navigate = useNavigate();

    const [isLoggedIn, setIsLoggedIn] = React.useState(false);
    const [isLoading, setIsLoading] = React.useState(true);
    const [username, setUsername] = React.useState("");
    const [open, setOpen] = React.useState(false);
    const [searchTerm, setSearchTerm] = React.useState("");


    const handleClickAvatar = (event) => {
        event.stopPropagation();
        setOpen(prev => !prev);
    }

    const handleLogout = async () => {
        try {
            await axios.get('/api/member/logout'); // 로그아웃 API 호출
            setIsLoggedIn(false); // 로그인 상태 업데이트
            navigate("/"); // 인덱스 페이지로 리다이렉트
        } catch (err) {
            console.error("Logout failed", err);
        }
    }

    const handleSearch = () => {
        if (searchTerm) {
            navigate(`/search/detailed?query=${searchTerm}`);
        }
    }

    const handleCompanyClick = async () => {
        try {
            const response = await axios.get('/api/member/seller/role');
            const hasSellerRole = response.status;
    
            if (hasSellerRole === 200) {
                navigate("/company/edit");
            }
        } catch (error) { // 오류 객체를 인자로 받음
            alert("회사가 없습니다. 생성 페이지로 이동합니다.");
            navigate("/company/create");
        }
    }
    

    useEffect(() => {
        axios.get(`/api/member/email`)
            .then((res) => {
                setIsLoggedIn(res.data.id !== null);
            }).finally(() => {
            setIsLoading(false);
        });

        document.addEventListener("click", function (event) {
           setOpen(false);
        });
    }, []);

    return (
        <header className={style["header"]}>
            <div className={style['container']}>
                <Link to="/" className={style["logoSample"]}>
                    <img src="/logo.svg"/>
                </Link>
                {/* 검색 기능 추가 */}
                <div className={style["search"]}>
                    <TextField
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        placeholder="검색어 입력"
                        variant="outlined"
                        size="small"
                    />
                    <Button onClick={handleSearch} variant="contained">검색</Button>
                </div>
                <div className={style["buttons"]}>
                    {isLoading ? <></> : isLoggedIn ? (
                        <>
                            <Avatar
                                alt={username}
                                src=""
                                onClick={handleClickAvatar}
                                className={style['avatar']}
                            />
                            <div className={`${style['menu']} ${open ? style['open'] : ""}`}>
                                <MenuItem onClick={() => navigate("/mypage")}>My Page</MenuItem>
                                <MenuItem onClick={handleCompanyClick}>Company</MenuItem> {/* 수정된 부분 */}
                                <MenuItem onClick={handleLogout}>Logout</MenuItem>
                            </div>
                        </>
                    ) : (
                        <>
                            <Button onClick={() => navigate("/login")}>Login</Button>
                            <Button onClick={() => navigate("/signup")}>Sign Up</Button>
                        </>
                    )}
                </div>
            </div>
        </header>
    );
}

export default Header;
