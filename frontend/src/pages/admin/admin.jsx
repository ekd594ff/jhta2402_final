import {Link, Route, Routes, useLocation, useNavigate} from "react-router-dom";

import style from "../../styles/admin-index.module.scss"
import * as React from 'react';
import ListSubheader from '@mui/material/ListSubheader';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import {useEffect, useLayoutEffect, useState} from "react";
import axios from "axios";
import Paper from '@mui/material/Paper';
import {
    DataGrid,
    GridToolbarContainer,
    GridToolbarFilterButton,
    GridToolbarExport,
    getGridStringOperators
} from "@mui/x-data-grid";
import {Button, TextField} from "@mui/material";
import {Modal, Box, Typography} from '@mui/material';
import MemberModalContent from "./component/memberModalContent.jsx";
import CompanyModalContent from "./component/companyModalContent.jsx";
import PortfolioModalContent from "./component/portfolioModalContent.jsx";
import QuotationModalContent from "./component/quotationModalContent.jsx";
import ReportModalContent from "./component/reportModalContent.jsx";
import QuotationRequestModalContent from "./component/quotationRequestModalContent.jsx";
import ReviewModalContent from "./component/reviewModalContent.jsx";

const linkNameArray = ["회원 정보", "업체 정보", "포트폴리오", "리뷰", "신고 내역", "견적서", "견적 신청서"];
const IconArray = [];

function route(params, inputValue, navigator) {
    if (!params) {
        navigator("/admin/member");
    }
    switch (params) {
        case "member" :
            return <MemberModalContent {...inputValue}/>;
        case "company" :
            return <CompanyModalContent {...inputValue}/>;
        case "portfolio" :
            return <PortfolioModalContent {...inputValue}/>;
        case "review" :
            return <ReviewModalContent {...inputValue}/>;
        case "report" :
            return <ReportModalContent {...inputValue}/>;
        case "quotation" :
            return <QuotationModalContent {...inputValue}/>;
        case "quotationRequest" :
            return <QuotationRequestModalContent {...inputValue}/>;
        default :
            return <></>;
    }
}

function NestedList() {
    const [open, setOpen] = React.useState(true);

    const handleClick = () => {
        setOpen(!open);
    };

    return (
        <List
            component="nav"
            aria-labelledby="nested-list-subheader"
            subheader={
                <ListSubheader
                    component="div"
                    id="nested-list-subheader"
                >
                    Admin
                </ListSubheader>
            }
        >
            {['Member', 'Company', 'Portfolio', 'Review', 'Report', 'Quotation', 'QuotationRequest'].map((text, index) => {
                return (
                    <Link to={text.charAt(0).toLowerCase() + text.slice(1)}
                          style={{textDecoration: 'none', color: 'inherit'}} key={text}>
                        <ListItemButton className={style["list-item"]}>
                            <ListItemText primary={linkNameArray[index]}/>
                        </ListItemButton>
                    </Link>
                );
            })}
        </List>
    );
}


const handleApiRequest = async () => {
    try {
        const ids = selectedRows;
        const response = await axios.delete(`/api/${pathname}/admin/soft/${ids}`,
        );

        console.log('selectedRows', selectedRows);
        console.log('API Response:', response.data);
    } catch (error) {
        console.error('Error sending API request:', error);
    } finally {
        console.log('selectedRows', selectedRows);
    }
};

function CustomToolbar(props) {
    return (
        <GridToolbarContainer>
            <GridToolbarFilterButton/>
            <GridToolbarExport/>
        </GridToolbarContainer>
    );
}


const filterOperators = getGridStringOperators()
    .filter(
        (operator) =>
            operator.value === "contains"
    )
    .map((operator) => {
        return {
            ...operator
        };
    });
const handleUpdateClick = async (id) => {
    console.log('id', id);
    // try {
    //     const response = await axios.delete(`/api/${pathname}/admin/soft/${id}`,
    //     );
    // } catch (error) {
    //     console.error('Error sending API request:', error);
    // }
};


function DataTable() {
    const [data, setData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const path = useLocation()
    const pathname = path.pathname.split("/admin/")[1];
    const [columns, setColumns] = useState([]);
    const [filterModel, setFilterModel] = useState({field: "", value: ""});
    const [paginationModel, setPaginationModel] = useState({page: 0, pageSize: 5});
    const [sortModel, setSortModel] = useState({field: "", sort: ""})
    const [selectedRows, setSelectedRows] = useState([]);
    const [open, setOpen] = useState(false);
    const [inputValue, setInputValue] = useState({});
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const navigator = useNavigate();

    const buttonColumns = {
        field: 'delete',
        headerName: '수정',
        width: 150,
        renderCell: (params) => (
            <Button
                variant="contained"
                color="default"
                onClick={(event) => {
                    console.log("params.row", params.row);
                    event.stopPropagation();
                    setInputValue(params.row);
                    handleUpdateClick(params.row.id)
                    handleOpen();
                }} // ID를 전달
            >
                수정
            </Button>
        ),
    };

    const fetchData = async (pathname, paginationModel) => {
        try {
            const {page, pageSize} = paginationModel;
            const response =
                await axios.get(`/api/${pathname}/admin/list`, {
                    params: {page, pageSize}
                });

            // 동적으로 컬럼 정의 생성
            if (response.data.content.length > 0) {
                const cols = Object.keys(response.data.content[0]).map(key => ({
                    field: key,
                    headerName: key.charAt(0).toUpperCase() + key.slice(1), // 첫 글자 대문자
                    flex: 1,
                    filterOperators
                }));
                console.log(response.data.content);
                cols.push(buttonColumns);
                setColumns(cols);
            }
            setData(response.data.content);
            // setData(response.data.content.map(item => ({
            //     ...item,
            //     action: '버튼 클릭',
            // }))); // 실제 데이터 구조에 맞게 수정
            setTotalCount(response.data.page.totalElements); // 전체 데이터 수
        } catch (error) {
            console.error('데이터를 가져오는 데 오류가 발생했습니다:', error);
        } finally {
            // console.log(data);
        }
    };


    const fetchFilterdData = async (filterModel, sortModel, pathname, paginationModel) => {
        try {
            const {page, pageSize} = paginationModel;
            const param = {};
            param.page = page;
            param.pageSize = pageSize;
            if (sortModel) {
                if (sortModel.field) {
                    param.sortField = sortModel.field;
                }
                if (sortModel.sort) {
                    console.log("sortValue", sortModel.sort);
                    param.sort = sortModel.sort;
                }
            }
            if (filterModel.value) {
                param.filterValue = filterModel.value;
            }
            if (filterModel.field) {
                param.filterColumn = filterModel.field;
            }
            const response =
                await axios.get(`/api/${pathname}/admin/list/filter/contains`, {
                    params: param    //sortModel, filterModel
                });
            // 동적으로 컬럼 정의 생성
            if (response.data.content.length > 0) {
                const cols = Object.keys(response.data.content[0]).map(key => ({
                    field: key,
                    headerName: key.charAt(0).toUpperCase() + key.slice(1), // 첫 글자 대문자
                    flex: 1, // 기본 너비
                    filterOperators
                }));

                cols.push(buttonColumns);
                setColumns(cols);
            }
            setData(response.data.content); // 실제 데이터 구조에 맞게 수정
            setTotalCount(response.data.page.totalElements); // 전체 데이터 수
        } catch (error) {
            console.error('데이터를 가져오는 데 오류가 발생했습니다:', error);
        } finally {
            // console.log(data);
        }
    };

    useEffect(() => {
        // console.log("filterModel.value.length=", filterModel.value.length);
        // console.log("sortModel.field.length=", sortModel.field.length);
        let sortField;
        let filterValue;
        if (sortModel) {
            sortField = sortModel.field;
        }
        if (filterModel) {
            filterValue = filterModel.value;
        }
        if (filterValue || sortField) { // filterModel, sortModel 값 확인 후 분기
            fetchFilterdData(filterModel, sortModel, pathname, paginationModel); // 매개변수 수정
        } else {
            fetchData(pathname, paginationModel);
        }

    }, [paginationModel, sortModel, totalCount, filterModel]);

    useEffect(() => {
        setPaginationModel({page: 0, pageSize: 5})
    }, [pathname, sortModel, filterModel]);

    useEffect(() => {
        setSortModel({field: "", sort: ""});
        setFilterModel({field: "", value: ""});
        setPaginationModel({page: 0, pageSize: 5});
    }, [pathname]);
    const handleFilterModelChange = (model) => {
        console.log('filterModel', model);
        setFilterModel(model.items[0])
    };
    const handleSortModelChange = (model) => {
        console.log("sortModel[0]", model[0]);
        console.log("sortModel", model);
        setSortModel(model[0]);
    };
    const handlePaginationModelChange = (model) => {
        // console.log("paginationModel", model);
        setPaginationModel(model);
    };
    const handleRowSelection = (newSelection) => {
        setSelectedRows(newSelection.join(","));
    };


    return (
        <>
            <div className={style.buttonContainer}>
                {/*<Button*/}
                {/*    onClick={handleApiRequest}*/}
                {/*    variant="outlined"*/}
                {/*    style={{backgroundColor: 'default', color: 'default', marginRight: '10px'}} // 오른쪽 여백 추가*/}
                {/*    size="small"*/}
                {/*    className={style.customButton}*/}
                {/*>*/}
                {/*    추가*/}
                {/*</Button>*/}
                <Button
                    onClick={handleApiRequest}
                    variant="outlined"
                    style={{backgroundColor: '#f50057', color: '#fff', marginRight: '10px'}} // 오른쪽 여백 추가
                    size="small"
                    className={style.customButton}
                >
                    삭제
                </Button>
            </div>
            <div className={style["data-Grid"]}>
                <DataGrid
                    rows={data}
                    columns={columns}
                    checkboxSelection
                    components={{
                        Toolbar: CustomToolbar,
                    }}
                    pagination
                    filterMode="server" // 클라이언트 측 필터링 또는 서버 측 필터링 설정 (server / client)
                    paginationMode="server"
                    sortingMode="server"
                    rowCount={totalCount}
                    pageSizeOptions={[5, 10, 100]}
                    onFilterModelChange={handleFilterModelChange}
                    onSortModelChange={handleSortModelChange}
                    onPaginationModelChange={handlePaginationModelChange}
                    onRowSelectionModelChange={handleRowSelection}
                    autoHeight
                    paginationModel={paginationModel}
                />
            </div>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-title"
                aria-describedby="modal-description"
            >
                <Box className={style["modal-style"]}>
                    <Typography id="modal-title" variant="h6" component="h2">
                        수정
                    </Typography>
                    {route(pathname, inputValue, navigator)}
                    <Button variant="outlined" onClick={handleClose}>
                        닫기
                    </Button>
                </Box>
            </Modal>
        </>
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
                    <Route path={"report"} element={<DataTable/>}/>
                    <Route path={"quotation"} element={<DataTable/>}/>
                    <Route path={"quotationRequest"} element={<DataTable/>}/>
                    <Route path={"/"} element={<DataTable/>}/>
                </Routes>
            </div>
        </main>
    </>
}

export default Admin;