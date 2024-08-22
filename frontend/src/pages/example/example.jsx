import React, {useEffect, useState} from 'react';
import axios from "axios";
import {Link} from "react-router-dom";
import {getPageList} from "../../utils/pageUtil.jsx";
import {Button} from "@mui/material";

function Example() {

    const contentSize = 2; // 페이지에 보여줄 Example 갯수
    const pageSize = 5; // Pagination 갯수 (5-> [1, 2, 3, 4, 5])

    const [pagination, setPagination] = useState(
        {page: 1, totalPages: 0, first: true, last: true, pageList: []});
    const [exampleList, setExampleList] = useState([]);

    // useEffect를 동작하게 하기 위한 상태 변경
    const [trigger, setTrigger] = useState(false);

    useEffect(() => {
        axios.get(`/api/example?page=${pagination.page - 1}&size=${contentSize}`)
            .then((res) => {
                console.log(res);

                setExampleList(res.data.content);
                setPagination({
                    ...pagination,
                    totalPages: res.data.page.totalPages,
                    first: pagination.page <= 1,
                    last: pagination.page >= res.data.page.totalPages,
                    pageList: getPageList(pagination.page, pageSize, res.data.page.totalPages)
                });
            })
    }, [pagination.page, trigger]);

    const [exampleForm, setExampleForm] = useState({name: "", description: ""});


    const createExample = () => {
        axios.post("/api/example", exampleForm)
            .then(() => {
                setTrigger(prev => !prev);
                setExampleForm({name: "", description: ""});

                alert("created");
            })
            .catch(() => alert("error"))
    }

    const deleteExample = (id) => {
        axios.delete(`/api/example/${id}`)
            .then(() => {
                setTrigger(prev => !prev);
                setExampleForm({name: "", description: ""});
                alert("complete");
            })
            .catch(() => alert("error"))
    }

    const deleteExampleAdmin = (id) => {
        axios.delete(`/api/admin/example/${id}`)
            .then(() => {
                setTrigger(prev => !prev);
                setExampleForm({name: "", description: ""});
                alert("complete");
            })
            .catch(() => alert("error"))
    }

    return (
        <>
            <div>
                <label htmlFor="name">이름</label>
                <input id="name" value={exampleForm.name}
                       onChange={(e) => setExampleForm({...exampleForm, name: e.target.value})}/>
                <label htmlFor="description">설명</label>
                <input id="description" value={exampleForm.description}
                       onChange={(e) => setExampleForm({...exampleForm, description: e.target.value})}/>
                <button onClick={createExample}>Create Example</button>
            </div>

            {exampleList.map((example) => <div key={example.id}>
                <Link to={`/example/${example.id}`}>
                    <p>
                        memberEmail : {example.memberEmail}
                    </p>
                    <p>
                        id : {example.id}
                    </p>
                    <p>
                        name : {example.name}
                    </p>
                    <p>
                        description : {example.description}
                    </p>
                </Link>
                <button onClick={() => deleteExample(example.id)}>Delete</button>
                <button onClick={() => deleteExampleAdmin(example.id)}>Admin Delete</button>
            </div>)}


            {!pagination.first ? <Button
                onClick={() => setPagination({...pagination, page: pagination.page - 1})}>prev</Button> : <></>}

            {pagination.pageList.map((pageNum) =>
                <Button key={pageNum}
                        onClick={() => setPagination({...pagination, page: pageNum})}>
                    {pageNum}
                </Button>)}

            {!pagination.last ? <Button
                onClick={() => setPagination({...pagination, page: pagination.page + 1})}>
                next
            </Button> : <></>}
        </>
    );
}

export default Example;