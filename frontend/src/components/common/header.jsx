import {Button} from "@mui/material";
import {Link, useNavigate} from "react-router-dom";
import * as style from "../../styles/header.module.scss";

function Header() {

    const navigate = useNavigate();

    return <header className={style["header"]}>
        <Link to={"/"} className={style["logoSample"]}>
            IntArea
        </Link>
        <div className={style["buttons"]}>
            <Button onClick={() => navigate("/login")}>Login</Button>
            <Button onClick={() => navigate("/signup")}>SignUp</Button>
        </div>
    </header>
}

export default Header;