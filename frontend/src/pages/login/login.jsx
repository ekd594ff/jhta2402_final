import {useState} from "react";
import axios from "axios";
import {useNavigate, Link} from "react-router-dom";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

import style from "../../styles/login.module.scss";

function Login() {

    const navigate = useNavigate()

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const login = () => {
        axios.post('/api/member/login', {
                email: email,
                password: password
            },
            {
                withCredentials: true
            }
        ).then(res => {
                alert("login Success")
                navigate("/")
            }
        ).catch(err => alert(err));
    }

    return <>
        <main className={style['login']}>
            <div className={style['container']}>
                <form method="post">
                    <TextField
                        required
                        onChange={(e) => setEmail(e.target.value)}
                        name="email"
                        id="outlined-required"
                        label="Email"
                        variant="standard"
                        value={email}
                    />
                    <TextField
                        required
                        onChange={(e) => setPassword(e.target.value)}
                        name="password"
                        id="outlined-required"
                        label="Password"
                        variant="standard"
                        value={password}
                    />
                    <div className={style['btn-group']}>
                        <Button variant="contained" className={style['login-btn']} size="large" onClick={login}>
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