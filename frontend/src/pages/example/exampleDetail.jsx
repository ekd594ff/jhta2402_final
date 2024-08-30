import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";

import style from "../../styles/example-detail.module.scss"
import {Button, TextField, Typography} from "@mui/material";

function ExampleDetail(props) {

    // url PathVariable 불러오기 (app.jsx 에 적은 :id)
    const params = useParams();
    const id = params.id;

    const [trigger, setTrigger] = useState(false);

    const [example, setExample] = useState(
        {name: "", description: ""});
    const [commentList, setCommentList] = useState([]);

    const [description, setDescription] = useState("");

    const navigator = useNavigate();

    useEffect(() => {
        axios.get(`/api/example/${id}`)
            .then((res) => {
                setExample(res.data);
            })
            .catch(() => alert("error example"))
    }, []);

    useEffect(() => {
        axios.get(`/api/example/${id}/comment`)
            .then((res) => {
                setCommentList(res.data)
            })
            .catch(() => alert("error exampleComment"));
    }, [trigger]);

    const createComment = () => {
        axios.post(`/api/example/${id}/comment`,
            {description: description})
            .then(() => {
                setTrigger(prev => !prev);
                setDescription("");
                alert("created");
            })
            .catch(() => alert("error"));
    }

    return (
        <main className={style['example-detail']}>
            <div className={style['container']}>
                <Typography variant="h4" style={{fontWeight: 'bold'}}>
                    {example.name}
                </Typography>
                <p className={style['description']}>{example.description}</p>
                <div className={style['btn-box']}>
                    <Button variant="contained" onClick={() => {
                        navigator("/example")
                    }} color="success">목록</Button>
                    <Button variant="contained" onClick={() => {
                    }} color="error">삭제</Button>
                </div>
                <div className={style['comment-box']}>
                    <TextField className={style['comment-input']} InputLabelProps={{shrink: true}} label="댓글쓰기"
                               variant="standard" value={description}
                               onChange={(e) => setDescription(e.target.value)}/>
                    <Button variant="contained" onClick={createComment}>댓글 쓰기</Button>
                </div>
                {commentList.map((comment) => <div className={style['comment-item']} key={comment.id}>
                    <div className={style['top']}>
                        <div className={style['id']}>🇮🇩: {`${comment.id}`}</div>
                        <div className={style['email']}>✉: {comment.memberEmail}</div>
                    </div>
                    <div className={style['content']}>
                        {comment.description}
                    </div>
                </div>)}
            </div>

        </main>
    );
}

export default ExampleDetail;