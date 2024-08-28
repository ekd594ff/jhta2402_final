import {useState} from "react";
import axios from "axios";
import {useNavigate, Link} from "react-router-dom";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

import style from "../../styles/login.module.scss";

function Login() {

    const navigate = useNavigate()

    // const login = () => {
    //     axios.post('/api/member/login', {
    //             email: email,
    //             password: password
    //         },
    //         {
    //             withCredentials: true
    //         }
    //     ).then(res => {
    //             alert("login Success")
    //             navigate("/")
    //         }
    //     ).catch(err => alert(err));
    // }

    const [login, setLogin] = useState({email : "", password : ""});

    return <>
        <main className={style['login']}>
            <div className={style['container']}>
                <form method="post">
                    <TextField
                        required
                        onChange={(e) => setLogin({...login, email: e.target.value})}
                        name="email"
                        label="Email"
                        variant="standard"
                        value={login.email}
                        InputLabelProps={{ shrink: true }}
                    />
                    <TextField
                        required
                        onChange={(e) => setLogin({...login, password: e.target.value})}
                        name="password"
                        label="Password"
                        variant="standard"
                        value={login.password}
                        InputLabelProps={{ shrink: true }}
                    />
                    <div className={style['btn-group']}>
                        <Button variant="contained" className={style['login-btn']} size="large">
                            로그인
                        </Button>
                        <div className={style['others']}>
                            <Link to="/signup">회원가입</Link>
                        </div>
                    </div>
                </form>
            </div>
        </main>
    </>
}

export default Login;