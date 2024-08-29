import {Link, Route, Routes, useNavigate} from "react-router-dom";

import AdminIndex from "./pages/index.jsx";

import style from "../../styles/admin-index.module.scss"
import * as React from 'react';
import ListSubheader from '@mui/material/ListSubheader';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import PropTypes from 'prop-types';
import {alpha} from '@mui/material/styles';
import Box from '@mui/material/Box';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import TableSortLabel from '@mui/material/TableSortLabel';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import Checkbox from '@mui/material/Checkbox';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import FormControlLabel from '@mui/material/FormControlLabel';
import Switch from '@mui/material/Switch';
import DeleteIcon from '@mui/icons-material/Delete';
import FilterListIcon from '@mui/icons-material/FilterList';
import {visuallyHidden} from '@mui/utils';
import {useEffect, useLayoutEffect, useState} from "react";
import axios from "axios";



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
                <Link to={"/login"}><ListItemText primary="Company"/></Link>
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

// function createData(id, name, calories, fat, carbs, protein) {
//     return {
//         id,
//         name,
//         calories,
//         fat,
//         carbs,
//         protein,
//     };
// }

// const rows = [
//     createData(1, 'Cupcake', 305, 3.7, 67, 4.3),
//     createData(2, 'Donut', 452, 25.0, 51, 4.9),
//     createData(3, 'Eclair', 262, 16.0, 24, 6.0),
//     createData(4, 'Frozen yoghurt', 159, 6.0, 24, 4.0),
//     createData(5, 'Gingerbread', 356, 16.0, 49, 3.9),
//     createData(6, 'Honeycomb', 408, 3.2, 87, 6.5),
//     createData(7, 'Ice cream sandwich', 237, 9.0, 37, 4.3),
//     createData(8, 'Jelly Bean', 375, 0.0, 94, 0.0),
//     createData(9, 'KitKat', 518, 26.0, 65, 7.0),
//     createData(10, 'Lollipop', 392, 0.2, 98, 0.0),
//     createData(11, 'Marshmallow', 318, 0, 81, 2.0),
//     createData(12, 'Nougat', 360, 19.0, 9, 37.0),
//     createData(13, 'Oreo', 437, 18.0, 63, 4.0),
// ];

function descendingComparator(a, b, orderBy) {
    if (b[orderBy] < a[orderBy]) {
        return -1;
    }
    if (b[orderBy] > a[orderBy]) {
        return 1;
    }
    return 0;
}

function getComparator(order, orderBy) {
    return order === 'desc'
        ? (a, b) => descendingComparator(a, b, orderBy)
        : (a, b) => -descendingComparator(a, b, orderBy);
}

const headCells = [
    {
        id: 'id',
        numeric: false,
        disablePadding: true,
        label: 'id',
    },
    {
        id: 'email',
        numeric: true,
        disablePadding: false,
        label: 'email',
    },
    {
        id: 'role',
        numeric: true,
        disablePadding: false,
        label: 'role',
    },
    {
        id: 'username',
        numeric: true,
        disablePadding: false,
        label: 'username',
    },
    {
        id: 'platform',
        numeric: true,
        disablePadding: false,
        label: 'platform',
    },
    {
        id: 'createdAt',
        numeric: true,
        disablePadding: false,
        label: 'createdAt',
    },
    {
        id: 'updatedAt',
        numeric: true,
        disablePadding: false,
        label: 'updatedAt',
    },
    {
        id: 'deleted',
        numeric: true,
        disablePadding: false,
        label: 'deleted',
    },
];

function EnhancedTableHead(props) {
    const {onSelectAllClick, order, orderBy, numSelected, rowCount, onRequestSort} =
        props;
    const createSortHandler = (property) => (event) => {
        onRequestSort(event, property);
    };

    return (
        <TableHead>
            <TableRow>
                <TableCell padding="checkbox">
                    <Checkbox
                        color="primary"
                        indeterminate={numSelected > 0 && numSelected < rowCount}
                        checked={rowCount > 0 && numSelected === rowCount}
                        onChange={onSelectAllClick}
                        inputProps={{
                            'aria-label': 'select all desserts',
                        }}
                    />
                </TableCell>
                {headCells.map((headCell) => (
                    <TableCell
                        key={headCell.id}
                        align={headCell.numeric ? 'right' : 'left'}
                        padding={headCell.disablePadding ? 'none' : 'normal'}
                        sortDirection={orderBy === headCell.id ? order : false}
                    >
                        <TableSortLabel
                            active={orderBy === headCell.id}
                            direction={orderBy === headCell.id ? order : 'asc'}
                            onClick={createSortHandler(headCell.id)}
                        >
                            {headCell.label}
                            {orderBy === headCell.id ? (
                                <Box component="span" sx={visuallyHidden}>
                                    {order === 'desc' ? 'sorted descending' : 'sorted ascending'}
                                </Box>
                            ) : null}
                        </TableSortLabel>
                    </TableCell>
                ))}
            </TableRow>
        </TableHead>
    );
}

EnhancedTableHead.propTypes = {
    numSelected: PropTypes.number.isRequired,
    onRequestSort: PropTypes.func.isRequired,
    onSelectAllClick: PropTypes.func.isRequired,
    order: PropTypes.oneOf(['asc', 'desc']).isRequired,
    orderBy: PropTypes.string.isRequired,
    rowCount: PropTypes.number.isRequired,
};

function EnhancedTableToolbar(props) {
    const {numSelected} = props;
    return (
        <Toolbar
            sx={[
                {
                    pl: {sm: 2},
                    pr: {xs: 1, sm: 1},
                },
                numSelected > 0 && {
                    bgcolor: (theme) =>
                        alpha(theme.palette.primary.main, theme.palette.action.activatedOpacity),
                },
            ]}
        >
            {numSelected > 0 ? (
                <Typography
                    sx={{flex: '1 1 100%'}}
                    color="inherit"
                    variant="subtitle1"
                    component="div"
                >
                    {numSelected} selected
                </Typography>
            ) : (
                <Typography
                    sx={{flex: '1 1 100%'}}
                    variant="h6"
                    id="tableTitle"
                    component="div"
                >
                    Nutrition
                </Typography>
            )}

            {numSelected > 0 ? (
                <Tooltip title="Delete">
                    <IconButton>
                        <DeleteIcon/>
                    </IconButton>
                </Tooltip>
            ) : (
                <Tooltip title="Filter list">
                    <IconButton>
                        <FilterListIcon/>
                    </IconButton>
                </Tooltip>
            )}
        </Toolbar>
    );
}

EnhancedTableToolbar.propTypes = {
    numSelected: PropTypes.number.isRequired,
};

function EnhancedTable() {
    const [memberData, setMemberData] = useState([]);
    // const [pageData, setPageData] = useState([]);
    const [loading, setLoading] = useState(true);
    // const { size, number, totalElements, totalPages } = pageData;
    const [order, setOrder] = React.useState('asc');
    const [orderBy, setOrderBy] = React.useState('createdAt');
    const [selected, setSelected] = React.useState([]);
    const [totalCount, setTotalCount] = useState(0);
    // const [page, setPage] = React.useState(0); //페이지
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [dense, setDense] = React.useState(false);
    const [error, setError] = useState(null);
    // const [memberDataPerPage, setMemberDataPerPage] = React.useState(5); // 한페이지당 갯수


    const fetchData = async (page, size) => {
        // setLoading(true);
        try {
            const response = await axios.get("/api/member/admin/list", {
                params: {page, size}
            });
            setMemberData(response.data.content);
            setTotalCount(response.data.page.totalElements);
        } catch (err) {
            setError(err);
        } finally {
        }
        setLoading(false); // 이것만 마지막에 실행
    };

    useEffect(() => {
        fetchData(page, size);
    }, [page, size]); // 페이지나 페이지당 데이터 수가 변경될 때마다 호출

    // useEffect(() => {
    //     try {
    //         axios.get("/api/member/admin/list", {
    //             params: {page, size}
    //         })
    //             .then(result => {
    //                 setMemberData(result.data.content);
    //                 // setPageData(result.data.page);
    //                 setTotalCount(result.data.page.totalElements);
    //             });
    //     } catch (err){
    //         setError(err);
    //     } finally {
    //         console.log(memberData);
    //         setLoading(false);
    //     }
    // }, [page, size]);
    //
    // if (error) return <div>Error: {error.message}</div>;

    const handleRequestSort = (event, property) => {
        const isAsc = orderBy === property && order === 'asc';
        setOrder(isAsc ? 'desc' : 'asc');
        setOrderBy(property);
    };

    const handleSelectAllClick = (event) => {
        if (event.target.checked) {
            const newSelected = memberData.map((n) => n.id);
            setSelected(newSelected);
            return;
        }
        setSelected([]);
    };

    const handleClick = (event, id) => {
        const selectedIndex = selected.indexOf(id);
        let newSelected = [];

        if (selectedIndex === -1) {
            newSelected = newSelected.concat(selected, id);
        } else if (selectedIndex === 0) {
            newSelected = newSelected.concat(selected.slice(1));
        } else if (selectedIndex === selected.length - 1) {
            newSelected = newSelected.concat(selected.slice(0, -1));
        } else if (selectedIndex > 0) {
            newSelected = newSelected.concat(
                selected.slice(0, selectedIndex),
                selected.slice(selectedIndex + 1),
            );
        }
        setSelected(newSelected);
    };
    // const handleChangeMemberDataPerPage = (event) => {
    //     setMemberDataPerPage(parseInt(event.target.value, 10));
    //     setPage(0);
    // };

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeSize = (event) => {
        setSize(parseInt(event.target.value, 10));
        setPage(0); // 페이지를 0으로 리셋
    };

    const handleChangeDense = (event) => {
        setDense(event.target.checked);
    };

    const isSelected = (id) => selected.indexOf(id) !== -1;

    // Avoid a layout jump when reaching the last page with empty rows.
    const emptyRows =
        page > 0 ? Math.max(0, (1 + page) * size - memberData.length) : 0;

    const visibleRows = React.useMemo(
        () =>
            [...memberData]
                .sort(getComparator(order, orderBy)),
                // .slice(pageData.number * pageData.size, pageData.number * pageData.size + pageData.size),
        [order, orderBy, page, size],
    );
    if (loading) {
        return <div>Loading...</div>;
    }
    return (
        <Box sx={{width: '100%'}}>
            <Paper sx={{width: '100%', mb: 2}}>
                <EnhancedTableToolbar numSelected={selected.length}/>
                <TableContainer>
                    <Table
                        sx={{minWidth: 750}}
                        aria-labelledby="tableTitle"
                        size={dense ? 'small' : 'medium'}
                    >
                        <EnhancedTableHead
                            numSelected={selected.length}
                            order={order}
                            orderBy={orderBy}
                            onSelectAllClick={handleSelectAllClick}
                            onRequestSort={handleRequestSort}
                            rowCount={memberData.length}
                        />
                        <TableBody>
                            {visibleRows.map((row, index) => {
                                const isItemSelected = isSelected(row.id);
                                const labelId = `enhanced-table-checkbox-${index}`;

                                return (
                                    <TableRow
                                        hover
                                        onClick={(event) => handleClick(event, row.id)}
                                        role="checkbox"
                                        aria-checked={isItemSelected}
                                        tabIndex={-1}
                                        key={row.id}
                                        selected={isItemSelected}
                                        sx={{cursor: 'pointer'}}
                                    >
                                        <TableCell padding="checkbox">
                                            <Checkbox
                                                color="primary"
                                                checked={isItemSelected}
                                                inputProps={{
                                                    'aria-labelledby': labelId,
                                                }}
                                            />
                                        </TableCell>
                                        <TableCell
                                            component="th"
                                            id={labelId}
                                            scope="row"
                                            padding="none"
                                        >
                                            {row.name}
                                        </TableCell>
                                        <TableCell align="right">{row.email}</TableCell>
                                        <TableCell align="right">{row.role}</TableCell>
                                        <TableCell align="right">{row.username}</TableCell>
                                        <TableCell align="right">{row.platform}</TableCell>
                                        <TableCell align="right">{row.createdAt}</TableCell>
                                        <TableCell align="right">{row.updatedAt}</TableCell>
                                        <TableCell align="right">{row.deleted ? "true":"false"}</TableCell>
                                    </TableRow>
                                );
                            })}
                            {emptyRows > 0 && (
                                <TableRow
                                    style={{
                                        height: (dense ? 33 : 53) * emptyRows,
                                    }}
                                >
                                    <TableCell colSpan={6}/>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
                <TablePagination
                    rowsPerPageOptions={[5, 10, 25]}
                    component="div"
                    count={totalCount} // 총 데이터 수
                    rowsPerPage={size}
                    page={page}
                    onPageChange={handleChangePage}
                    // onRowsPerPageChange={handleChangeRowsPerPage}
                />
                {/*<TablePagination*/}
                {/*    rowsPerPageOptions={[5, 10, 25]}*/}
                {/*    component="div"*/}
                {/*    count={-1}                 //행의 총개수 서버측 페이지 매김 -1*/}
                {/*    rowsPerPage={-1}           //페이지의 행 수 모든 행 -1*/}
                {/*    page={pageData.number}                         // 현제 페이지의 0부터 시작하는 인덱스*/}
                {/*    onPageChange={handleChangePage}     //페이지가 변경되면 콜백 시작*/}
                {/*    // onRowsPerPageChange={handleChangeMemberDataPerPage}*/}
                {/*    //ActionsComponent={}              //액션을 표시하는 데 사용되는 구성 요소입니다. HTML 요소를 사용하는 문자열 또는 구성 요소.*/}
                {/*/>*/}
            </Paper>
            <FormControlLabel
                control={<Switch checked={dense} onChange={handleChangeDense}/>}
                label="Dense padding"
            />
        </Box>
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
                    <Route path={"member"} element={<EnhancedTable />}/>
                    <Route path={"/"} element={<EnhancedTable />}/>
                </Routes>
            </div>
        </main>
    </>
}

export default Admin;