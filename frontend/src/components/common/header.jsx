import * as React from 'react';
import {Button} from "@mui/material";
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
                <div className={style["buttons"]}>
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
