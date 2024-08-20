import {Button} from "@mui/material";
import {useNavigate} from "react-router-dom";
import * as style from "../../styles/header.module.scss";

function Header() {

    const navigate = useNavigate();

    return <header className={style["header"]}>
        <div className={style["logoSample"]}>
            IntArea
        </div>
        <div className={style["buttons"]}>
            <Button onClick={() => navigate("/login")}>Login</Button>
            <Button onClick={() => navigate("/signup")}>SignUp</Button>
        </div>
    </header>
}

export default Header;