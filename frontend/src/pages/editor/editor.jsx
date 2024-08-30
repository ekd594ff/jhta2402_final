import {Button, TextField, Typography} from "@mui/material";
import React, {useState} from "react";
import axios from "axios";

import style from "../../styles/editor.module.scss";
import {useNavigate} from "react-router-dom";

function Editor() {
    const [exampleForm, setExampleForm] = useState({name: "", description: ""});
    const [trigger, setTrigger] = useState(false);
    const navigator = useNavigate();
    const createExample = () => {
        axios.post("/api/example", exampleForm)
            .then(() => {
                setTrigger(prev => !prev);
                setExampleForm({name: "", description: ""});
                alert("글쓰기가 완료되었습니다");
                navigator("/example");
            })
            .catch(() => alert("error"))
    }

    return <main className={style['editor']}>
        <div className={style['container']}>
            <Typography variant="h4" style={{fontWeight: 'bold'}}>
                Editor
            </Typography>
            <TextField className={style['title']}
                       label="제목"
                       variant="standard"
                       value={exampleForm.name}
                       InputLabelProps={{shrink: true}}
                       onChange={(e) => setExampleForm({...exampleForm, name: e.target.value})}/>
            <TextField multiline
                       rows={10}
                       InputLabelProps={{shrink: true}}
                       className={style['content']} label="내용" variant="outlined"
                       value={exampleForm.description}
                       onChange={(e) => setExampleForm({...exampleForm, description: e.target.value})}/>
            <div className={style['btn-box']}>
                <Button variant="contained" onClick={() => {
                    navigator(-1);
                }} color="success">목록</Button>
                <Button variant="contained" onClick={createExample}>글쓰기</Button>
            </div>
        </div>
    </main>;
}

export default Editor;