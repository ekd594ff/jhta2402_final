import * as React from 'react';
import {Button, TextField} from "@mui/material";
import Avatar from '@mui/material/Avatar';
import {Link, useNavigate} from "react-router-dom";
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import style from "../../styles/header.module.scss";
import {useEffect} from "react";
import axios from "axios";
import IconButton from "@mui/material/IconButton";
import SearchIcon from '@mui/icons-material/Search';

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
                    <img src="/logo.svg" alt="home"/>
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
                    <IconButton aria-label="search" className={style['search']}>
                        <SearchIcon/>
                    </IconButton>
                    {isLoading ? <></> : isLoggedIn ? (
                        <>
                            <Avatar
                                alt={username}
                                src=""
                                onClick={handleClickAvatar}
                                className={style['avatar']}
                            />
                            <div className={`${style['menu']} ${open ? style['open'] : ""}`}
                            >
                                <MenuItem onClick={() => navigate("/mypage")}>My Page</MenuItem>
                                <MenuItem onClick={handleLogout}>Logout</MenuItem>
                            </div>
                        </>
                    ) : (
                        <>
                            <Button className={style['login-btn']} variant="outlined"
                                    disableRipple
                                    onClick={() => navigate("/login")}>로그인</Button>
                            <Button className={style['signup-btn']}
                                    disableRipple
                                    onClick={() => navigate("/signup")}>회원가입</Button>
                        </>
                    )}
                </div>
            </div>
        </header>
    );
}

export default Header;
