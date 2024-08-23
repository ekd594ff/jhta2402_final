import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function Login() {

    const navigate = useNavigate()

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const login = () => {
        axios.post('/api/login', {
                email: email,
                password: password
            },
            {
                withCredentials: true
            }
        ).then(res => {
            alert("login Success")
            navigate("/")
        })
            .catch(err => alert(err));
    }

    return <>
        <input value={email} onChange={(e) => setEmail(e.target.value)}
               type="email" name="email" placeholder="Email"/>
        <input value={password} onChange={(e) => setPassword(e.target.value)}
               type="password" name="password" placeholder="Password"/>
        <button onClick={login}>Login</button>
    </>
}

export default Login;