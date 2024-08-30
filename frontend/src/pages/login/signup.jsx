import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {Button, TextField, Typography} from "@mui/material";
import style from "../../styles/login.module.scss";

function Signup() {

    const navigate = useNavigate()

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const signup = () => {
        axios.post('/api/signup', {
                email: email,
                password: password
            },
            {
                withCredentials: true
            }
        ).then(res => {
            alert("signup Success")
            navigate("/login")
        })
            .catch(err => alert(err));
    }

    return <>
        <main className={style['login']}>
            <div className={style['container']}>
                <div className={style['box']}>
                    <Typography variant="h5" gutterBottom>
                        Sign up
                    </Typography>
                    <form onSubmit={() => {
                    }}>
                        <TextField variant="standard" value={email} onChange={(e) => setEmail(e.target.value)}
                                   type="email" name="email" placeholder="Email"/>
                        <TextField variant="standard" value={password} onChange={(e) => setPassword(e.target.value)}
                                   type="password" name="password" placeholder="Password"/>
                        <Button variant="contained" size="medium" onClick={signup}>Sign up</Button>
                    </form>
                </div>
            </div>
        </main>
    </>
}

export default Signup;