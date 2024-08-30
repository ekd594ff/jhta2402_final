import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

import style from "../../styles/login.module.scss"
import {Button, Snackbar, TextField, Typography} from "@mui/material";

function Login() {

    const navigate = useNavigate()

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');


    const login = (event) => {
        event.stopPropagation();
        event.preventDefault();
        axios.post('/api/login', {
                email: email,
                password: password
            },
            {
                withCredentials: true
            }
        ).then(res => {
            alert("로그인 되었습니다");
            document.location.href = "/";
        })
            .catch(err => alert(err));
    }

    return <>
        <main className={style['login']}>
            <div className={style['container']}>
                <div className={style['box']}>
                    <Typography variant="h5" gutterBottom>
                        Login
                    </Typography>
                    <form onSubmit={() => {
                    }}>
                        <TextField variant="standard" value={email} onChange={(e) => setEmail(e.target.value)}
                                   type="email" name="email" placeholder="Email"/>
                        <TextField variant="standard" value={password} onChange={(e) => setPassword(e.target.value)}
                                   type="password" name="password" placeholder="Password"/>
                        <Button variant="outlined" size="medium" onClick={login}>Login</Button>
                    </form>
                </div>
            </div>
        </main>
    </>
}

export default Login;