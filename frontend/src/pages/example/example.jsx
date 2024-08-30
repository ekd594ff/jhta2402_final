import React, {useEffect, useState} from 'react';
import axios from "axios";
import {Link, useNavigate} from "react-router-dom";
import {getPageList} from "../../utils/pageUtil.jsx";
import {
    Button,
    IconButton,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    TextField,
    Tooltip,
    Typography
} from "@mui/material";

import style from "../../styles/board.module.scss";

function Example() {

    const contentSize = 4; // 페이지에 보여줄 Example 갯수
    const pageSize = 5; // Pagination 갯수 (5-> [1, 2, 3, 4, 5])

    const [pagination, setPagination] = useState(
        {page: 1, totalPages: 0, first: true, last: true, pageList: []});
    const [exampleList, setExampleList] = useState([]);

    // useEffect를 동작하게 하기 위한 상태 변경
    const [trigger, setTrigger] = useState(false);

    const navigator = useNavigate();

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


    const deleteExample = (id) => {
        axios.delete(`/api/example/${id}`)
            .then(() => {
                setTrigger(prev => !prev);
                alert("complete");
            })
            .catch(() => alert("error"))
    }

    const deleteExampleAdmin = (id) => {
        axios.delete(`/api/admin/example/${id}`)
            .then(() => {
                setTrigger(prev => !prev);
                alert("complete");
            })
            .catch(() => alert("error"))
    }

    return (
        <main className={style['board']}>
            <div className={style['container']}>
                <div className={style['btn-box']}>
                    <Typography variant="h4" style={{fontWeight: 'bold'}}>
                        Board
                    </Typography>
                    <Button variant="contained" onClick={() => {
                        navigator("/editor")
                    }}>글쓰기</Button>
                </div>
                <Table sx={{minWidth: 700}} aria-label="customized table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Title</TableCell>
                            <TableCell>Content</TableCell>
                            <TableCell>Email</TableCell>
                            <TableCell>ID</TableCell>
                            <TableCell> </TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {
                            exampleList.map((item, index) => {
                                return (<TableRow className={style['row']} key={item.id} onClick={() => {
                                    navigator(`/example/${item.id}`)
                                }}>
                                    <TableCell>{item.name}</TableCell>
                                    <TableCell>{item.description}</TableCell>
                                    <TableCell component="th" scope="row">
                                        {item.memberEmail}
                                    </TableCell>
                                    <TableCell>{item.id}</TableCell>
                                    <TableCell>
                                        <Tooltip title="Delete">
                                            <IconButton size="small"
                                                        onClick={() => deleteExample(item.id)}>🗑️</IconButton>
                                        </Tooltip>
                                        <Tooltip title="Admin Delete">
                                            <IconButton size="small"
                                                        onClick={() => deleteExampleAdmin(item.id)}>❌️</IconButton>
                                        </Tooltip>
                                    </TableCell>
                                </TableRow>);
                            })
                        }
                    </TableBody>
                </Table>
                <div className={style['pagination-box']}>
                    {!pagination.first ? <Button
                        onClick={() => setPagination({...pagination, page: pagination.page - 1})}>prev</Button> : <></>}

                    {pagination.pageList.map((pageNum) =>
                        <button
                            className={`${style['pagination-btn']} ${pageNum === pagination.page ? style['selected'] : ""}`}
                            key={pageNum}
                            onClick={() => setPagination({...pagination, page: pageNum})}>
                            {pageNum}
                        </button>)}

                    {!pagination.last ? <Button
                        onClick={() => setPagination({...pagination, page: pagination.page + 1})}>
                        next
                    </Button> : <></>}
                </div>
            </div>
        </main>
    );
}

export default Example;