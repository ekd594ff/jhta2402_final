import {Link, Route, Routes, useNavigate} from "react-router-dom";

import AdminIndex from "./pages/index.jsx";

import style from "../../styles/admin-index.module.scss"
import * as React from 'react';
import ListSubheader from '@mui/material/ListSubheader';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
// import PropTypes from 'prop-types';
// import {alpha} from '@mui/material/styles';
// import Box from '@mui/material/Box';
// import Table from '@mui/material/Table';
// import TableBody from '@mui/material/TableBody';
// import TableCell from '@mui/material/TableCell';
// import TableContainer from '@mui/material/TableContainer';
// import TableHead from '@mui/material/TableHead';
// import TablePagination from '@mui/material/TablePagination';
// import TableRow from '@mui/material/TableRow';
// import TableSortLabel from '@mui/material/TableSortLabel';
// import Toolbar from '@mui/material/Toolbar';
// import Typography from '@mui/material/Typography';
// import Paper from '@mui/material/Paper';
// import Checkbox from '@mui/material/Checkbox';
// import IconButton from '@mui/material/IconButton';
// import Tooltip from '@mui/material/Tooltip';
// import FormControlLabel from '@mui/material/FormControlLabel';
// import Switch from '@mui/material/Switch';
// import DeleteIcon from '@mui/icons-material/Delete';
// import FilterListIcon from '@mui/icons-material/FilterList';
// import {visuallyHidden} from '@mui/utils';
import {useEffect, useLayoutEffect, useState} from "react";
import axios from "axios";
// import * as React from 'react';
import {DataGrid} from '@mui/x-data-grid';
import Paper from '@mui/material/Paper';
import {Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow} from "@mui/material";
import {number} from "prop-types";


function NestedList() {
    const [open, setOpen] = React.useState(true);

    const handleClick = () => {
        setOpen(!open);
    };

    return (
        <List
            sx={{width: '100%', maxWidth: 360, bgcolor: 'background.paper'}}
            component="nav"
            aria-labelledby="nested-list-subheader"
            subheader={
                <ListSubheader component="div" id="nested-list-subheader">
                    Nested List Items
                </ListSubheader>
            }
        >
            <ListItemButton>
                <Link to={"member"}><ListItemText primary="Member"/></Link>
            </ListItemButton>
            <ListItemButton>
                <Link to={"company"}><ListItemText primary="Company"/></Link>
            </ListItemButton>
            <ListItemButton>
                <Link to={"portfolio"}><ListItemText primary="Portfolio"/></Link>
            </ListItemButton>
            <ListItemButton>
                <Link to={"review"}><ListItemText primary="Review"/></Link>
            </ListItemButton>
        </List>
    );
}

const columns = [
    {field: 'id', headerName: 'id', width: 150},
    {field: 'email', headerName: 'email', width: 130},
    {field: 'deleted', headerName: 'deleted', width: 130},
    {field: 'username', headerName: 'username', width: 130},
    {field: 'platform', headerName: 'platform', width: 130},
    {field: 'role', headerName: 'role', width: 130},
    {field: 'updatedAt', headerName: 'updatedAt', width: 130},
    {field: 'createdAt', headerName: 'createdAt', width: 130},
];

function DataTable() {
    const [data, setData] = useState([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [totalCount, setTotalCount] = useState(0);

    // API에서 데이터 가져오기
    const fetchData = async (page, rowsPerPage) => {
        try {
            const response =
                await axios.get(`/api/member/admin/list?page=${page}&size=${rowsPerPage}`, {
                    params: {page, rowsPerPage}
                });
            setData(response.data.content); // 실제 데이터 구조에 맞게 수정
            setTotalCount(response.data.page.totalElements); // 전체 데이터 수
        } catch (error) {
            console.error('데이터를 가져오는 데 오류가 발생했습니다:', error);
        }
    };

    useEffect(() => {
        fetchData(page, rowsPerPage); // 페이지는 0부터 시작하므로 +1
        console.log("page=" + page);
        console.log("rowsPerPage=" + rowsPerPage);
    }, [page, rowsPerPage]);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));

        setPage(0); // 페이지를 0으로 리셋
    };

    return (
        <Paper>
            <TableContainer>
                <Table>
                    <TableHead>
                        <TableRow>
                            {data.length ? Object.keys(data[0]).map((column) => (
                                <TableCell key={column}>{column}</TableCell>
                            )) : <></>}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.map((item) => (
                            <TableRow key={item.id}>
                                {Object.keys(item).map((column, index) => (
                                    <TableCell key={`${item.id}_${index}`}>{`${item[column]}`}</TableCell>
                                ))}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <TablePagination
                rowsPerPageOptions={[5, 10, 25]}
                component="div"
                count={totalCount}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />
        </Paper>
    );
}

function Admin() {
    const navigate = useNavigate();
    const [isLoading1, setIsLoading1] = useState(true);
    // const [isLoading2, setIsLoading2] = useState(true);
    useEffect(() => {
        axios.get("/api/member/admin/role")
            .then(result => {
                //console.log(result);
                setIsLoading1(false);
            })
            .catch(err => {
                console.error(err);
                navigate("/");
            });
    }, []);
    if (isLoading1) {
        return <></>; // 또는 로딩 스피너 같은 것을 표시할 수
    }

    return <>
        <main className={style['index']}>
            <aside className={style['aside']}>
                <NestedList></NestedList>
            </aside>
            <div className={style['container']}>
                <Routes>
                    <Route path={"company"} element={<DataTable/>}/>
                    <Route path={"portfolio"} element={<DataTable/>}/>
                    <Route path={"review"} element={<DataTable/>}/>
                    <Route path={"member"} element={<DataTable/>}/>
                    <Route path={"/"} element={<DataTable/>}/>
                </Routes>
            </div>
        </main>
    </>
}

export default Admin;