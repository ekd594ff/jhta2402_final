import {Link, Route, Routes, useLocation, useNavigate} from "react-router-dom";

import AdminIndex from "./pages/index.jsx";

import style from "../../styles/admin-index.module.scss"
import * as React from 'react';
import ListSubheader from '@mui/material/ListSubheader';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import {useEffect, useLayoutEffect, useState} from "react";
import axios from "axios";
import Paper from '@mui/material/Paper';
import {DataGrid} from "@mui/x-data-grid";
import {TablePagination} from "@mui/material";


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
// const columns = [
//     { field: 'id', headerName: 'ID'},
//     { field: 'firstName', headerName: 'First name' },
//     { field: 'lastName', headerName: 'Last name'},
//     {
//         field: 'age',
//         headerName: 'Age',
//         type: 'number',
//         width: 90,
//     },
//     {
//         field: 'fullName',
//         headerName: 'Full name',
//         description: 'This column has a value getter and is not sortable.',
//         sortable: false,
//         valueGetter: (value, row) => `${row.firstName || ''} ${row.lastName || ''}`,
//     },
// ];

// const rows = [
//     { id: 1, lastName: 'Snow', firstName: 'Jon', age: 35 },
//     { id: 2, lastName: 'Lannister', firstName: 'Cersei', age: 42 },
//     { id: 3, lastName: 'Lannister', firstName: 'Jaime', age: 45 },
//     { id: 4, lastName: 'Stark', firstName: 'Arya', age: 16 },
//     { id: 5, lastName: 'Targaryen', firstName: 'Daenerys', age: null },
//     { id: 6, lastName: 'Melisandre', firstName: null, age: 150 },
//     { id: 7, lastName: 'Clifford', firstName: 'Ferrara', age: 44 },
//     { id: 8, lastName: 'Frances', firstName: 'Rossini', age: 36 },
//     { id: 9, lastName: 'Roxie', firstName: 'Harvey', age: 65 },
// ];

const paginationModel = { page: 0, pageSize: 5 };

function DataTable01() {
    const [data, setData] = useState([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [totalCount, setTotalCount] = useState(0);
    const path = useLocation()
    const pathname = path.pathname.split("/admin/")[1];
    const [columns, setColumns] = useState([]);

    const fetchData = async (pathname,page, size) => {
        try {
            const response =
                await axios.get(`/api/${pathname}/admin/list`, {
                    params: {page, size}
                });

            // 동적으로 컬럼 정의 생성
            if (response.data.content.length > 0) {
                const cols = Object.keys(response.data.content[0]).map(key => ({
                    field: key,
                    headerName: key.charAt(0).toUpperCase() + key.slice(1), // 첫 글자 대문자
                    width: 200, // 기본 너비 설정
                }));
                setColumns(cols);
            }

            setData(response.data.content); // 실제 데이터 구조에 맞게 수정
            setTotalCount(response.data.page.totalElements); // 전체 데이터 수
        } catch (error) {
            console.error('데이터를 가져오는 데 오류가 발생했습니다:', error);
        }
    };

    useEffect(() => {
        fetchData(pathname,page, rowsPerPage); // 페이지는 0부터 시작하므로 +1
        console.log("page=" + page);
        console.log("rowsPerPage=" + rowsPerPage);
        console.log(pathname);
        console.log(data);
    }, [pathname,page, rowsPerPage,totalCount]);


    return (
        <Paper sx={{ height: 400, width: '100%' }}>
            <DataGrid
                rows={data}
                columns={columns}
                checkboxSelection
                filterMode="server" // 클라이언트 측 필터링 또는 서버 측 필터링 설정 (server / client)
                totalcount={totalCount}
            />
        </Paper>
    );
}

function Admin01() {
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
                    <Route path={"company"} element={<DataTable01/>}/>
                    <Route path={"portfolio"} element={<DataTable01/>}/>
                    <Route path={"review"} element={<DataTable01/>} />
                    <Route path={"member"} element={<DataTable01/>}/>
                    <Route path={"/"} element={<DataTable01/>}/>
                </Routes>
            </div>
        </main>
    </>
}

export default Admin01;