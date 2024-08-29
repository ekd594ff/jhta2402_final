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
    const [username, setUsername] = React.useState("");

    const handleLogout = async () => {
        try {
            await axios.get('/api/member/logout'); // 로그아웃 API 호출
            setIsLoggedIn(false); // 로그인 상태 업데이트
            navigate("/"); // 인덱스 페이지로 리다이렉트
        } catch (err) {
            console.error("Logout failed", err);
        }
    }

    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    useEffect(() => {
        async function a() {
            try {
                const value = await axios.get(`/api/member/email`);
                setIsLoggedIn(true);
            } catch (err) {
                setIsLoggedIn(false);
            }
        }
        a();
    }, []);

    return (
        <header className={style["header"]}>
            <div className={style['container']}>
                <Link to="/" className={style["logoSample"]}>
                    IntArea
                </Link>
                <div className={style["buttons"]}>
                    {isLoggedIn ? (
                        <>
                            <Avatar
                                alt={username}
                                src=""
                                id="basic-button"
                                aria-controls={open ? 'basic-menu' : undefined}
                                aria-haspopup="true"
                                aria-expanded={open ? 'true' : undefined}
                                onClick={handleClick}
                            />
                            <Menu
                                id="basic-menu"
                                anchorEl={anchorEl}
                                open={open}
                                onClose={handleClose}
                                MenuListProps={{
                                    'aria-labelledby': 'basic-button',
                                }}
                            >
                                <MenuItem onClick={() => navigate("/mypage")}>My Page</MenuItem>
                                <MenuItem onClick={handleLogout}>Logout</MenuItem>
                            </Menu>
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
