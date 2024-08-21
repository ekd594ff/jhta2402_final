import React, {useEffect, useState} from 'react';
import axios from "axios";
import {useParams} from "react-router-dom";

function ExampleDetail(props) {

    const params = useParams();
    const id = params.id;

    const [trigger, setTrigger] = useState(false);

    const [example, setExample] = useState(
        {name: "", description: ""});
    const [commentList, setCommentList] = useState([]);

    const [description, setDescription] = useState("");

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
                alert("created");
            })
            .catch(() => alert("error"));
    }

    return (
        <>
            <div>
                <p>
                    id : {example.id}
                </p>
                <p>
                    name : {example.name}
                </p>
                <p>
                    description : {example.description}
                </p>
                <p>
                    createdAt : {example.createdAt}
                </p>
                <p>
                    updatedAt : {example.updatedAt}
                </p>
            </div>

            <br/>
            <label htmlFor="description">Description</label>
            <input value={description} onChange={(e) => setDescription(e.target.value)}/>
            <button onClick={createComment}>코멘트 생성</button>
            <br/>

            {commentList.map((comment) => <div key={comment.id}>
                <p>
                    {comment.id}
                </p>
                <p>
                    {comment.description}
                </p>
            </div>)}
        </>
    );
}

export default ExampleDetail;