import {Button} from "@mui/material";
import {Link, useNavigate} from "react-router-dom";
import * as style from "../../styles/header.module.scss";
import {useEffect, useState} from "react";

function Header() {

    const navigate = useNavigate();

    const [isLogined, setIsLogined] = useState(null);

    useEffect(() => {
        const cookie = document.cookie;
        const result = cookie.split("=");
        if (result[0] !== 'login') {
            setIsLogined(false);
        } else {
            setIsLogined(true);
        }
    }, []);

    return <header className={style["header"]}>
        <div className={style['container']}>
            <Link to={"/"} className={style["logoSample"]}>
                <img src="/website.svg" alt="logo"/>
            </Link>
            <div className={style["buttons"]}>
                {
                    isLogined === null ? <></> : isLogined ? <>
                            <Button variant="outlined" onClick={() => {
                                setIsLogined(false);
                                document.cookie = 'login=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
                            }}>Logout</Button>
                        </> :
                        <>
                            <Button variant="outlined" onClick={() => navigate("/login")}>Login</Button>
                            <Button variant="contained" onClick={() => navigate("/signup")}>SignUp</Button>
                        </>
                }
            </div>
        </div>
    </header>
}

export default Header;