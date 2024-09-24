import {
  Link,
  Route,
  Routes,
  useLocation,
  useNavigate,
  Navigate,
} from "react-router-dom";

import style from "../../styles/admin-index.module.scss";
import * as React from "react";
import ListSubheader from "@mui/material/ListSubheader";
import List from "@mui/material/List";
import ListItemButton from "@mui/material/ListItemButton";
import { useEffect, useState } from "react";
import axios from "axios";
import {
  DataGrid,
  GridToolbarContainer,
  GridToolbarFilterButton,
  GridToolbarExport,
  getGridStringOperators,
  GridFooterContainer,
} from "@mui/x-data-grid";
import { Button } from "@mui/material";
import { Modal, Box, Typography, Chip, Rating } from "@mui/material";
import MemberModalContent from "./component/memberModalContent.jsx";
import CompanyModalContent from "./component/companyModalContent.jsx";
import PortfolioModalContent from "./component/portfolioModalContent.jsx";
import QuotationModalContent from "./component/quotationModalContent.jsx";
import ReportModalContent from "./component/reportModalContent.jsx";
import QuotationRequestModalContent from "./component/quotationRequestModalContent.jsx";
import ReviewModalContent from "./component/reviewModalContent.jsx";

import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import BusinessIcon from "@mui/icons-material/Business";
import HouseIcon from "@mui/icons-material/House";
import StarIcon from "@mui/icons-material/Star";
import NotificationsIcon from "@mui/icons-material/Notifications";
import HandshakeIcon from "@mui/icons-material/Handshake";
import AssignmentIcon from "@mui/icons-material/Assignment";
import PendingIcon from "@mui/icons-material/Pending";
import TaskAltIcon from "@mui/icons-material/TaskAlt";
import DisplaySettingsIcon from "@mui/icons-material/DisplaySettings";
import DeleteIcon from "@mui/icons-material/Delete";

function formatTimestamp(timestamp) {
  return new Date(timestamp).toISOString().split("T")[0];
}

const linkNameArray = [
  "회원 정보",
  "업체 정보",
  "포트폴리오",
  "리뷰",
  "신고 내역",
  "견적서",
  "견적 신청서",
];

const pathNameArray = [
  "Member",
  "Company",
  "Portfolio",
  "Review",
  "Report",
  "Quotation",
  "QuotationRequest",
];

const IconArray = [
  <AccountCircleIcon />,
  <BusinessIcon />,
  <HouseIcon />,
  <StarIcon />,
  <NotificationsIcon />,
  <HandshakeIcon />,
  <AssignmentIcon />,
];

const customColumns = {
  member: [
    {
      name: "ID",
      width: 25,
    },
    { name: "이메일", width: 180 },
    {
      name: "권한",
      width: 150,
      renderCell: (params) => {
        const {
          row: { role },
        } = params;
        const r = role.split("_")[1];

        return (
          <Chip
            size="small"
            label={r}
            variant="outlined"
            color={(() => {
              switch (r) {
                case "ADMIN":
                  return "success";
                case "USER":
                  return "secondary";
                default:
                  return "primary";
              }
            })()}
          ></Chip>
        );
      },
    },
    { name: "닉네임", width: 100 },
    {
      name: "플랫폼",
      renderCell: (param) => {
        const {
          row: { platform },
        } = param;
        const style = {
          backgroundColor: "#176bef",
          color: "#fff",
        };
        switch (platform) {
          case "KAKAO":
            style.backgroundColor = "#ffe812";
            style.color = "#010101";
            break;
          case "NAVER":
            style.backgroundColor = "#03c75a";
            style.color = "#ffffff";
          case "GOOGLE":
            style.backgroundColor = "#4285f4";
            style.color = "#ffffff";
          default:
            break;
        }
        return <Chip size="small" label={platform} style={style} />;
      },
      width: 100,
    },
    {
      name: "생성일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { createdAt },
        } = param;
        return formatTimestamp(createdAt);
      },
    },
    {
      name: "수정일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { createdAt },
        } = param;
        return formatTimestamp(createdAt);
      },
    },
    {
      name: "삭제",
      width: 60,
      renderCell: (param) => {
        const {
          row: { deleted },
        } = param;
        return !deleted ? (
          <></>
        ) : (
          <div
            style={{
              backgroundColor: "red",
              display: "inline",
              padding: "4px 4px",
              fontWeight: "bold",
              fontSize: "10px",
              color: "#fff",
              borderRadius: "3px",
            }}
          >
            삭제
          </div>
        );
      },
    },
  ],
  company: [
    {
      name: "업체",
      width: 120,
      renderCell: (param) => {
        const {
          row: { id },
        } = param;
        return (
          <Link to={`/company/${id}`}>
            <Chip
              size="small"
              label="업체 페이지"
              variant="outlined"
              color="primary"
            ></Chip>
          </Link>
        );
      },
    },
    { name: "업체명", width: 160 },
    { name: "세부사항", width: 120 },
    { name: "연락처", width: 120 },
    { name: "주소", width: 100 },
    { name: "세부 주소", width: 100 },
    {
      name: "생성일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { createdAt },
        } = param;
        return formatTimestamp(createdAt);
      },
    },
    {
      name: "승인",
      width: 60,
      renderCell: (param) => {
        const {
          row: { applied },
        } = param;
        return (
          <div
            style={{
              display: "flex",
              alignItems: "center",
              height: "100%",
              justifyContent: "center",
            }}
          >
            {applied ? (
              <TaskAltIcon style={{ color: "#22bb33", fontSize: "18px" }} />
            ) : (
              <PendingIcon style={{ color: "#bb2124", fontSize: "18px" }} />
            )}
          </div>
        );
      },
    },
  ],
  portfolio: [
    {
      name: "포트폴리오",
      width: 120,
      renderCell: (param) => {
        const {
          row: { id },
        } = param;
        return (
          <Link to={`/portfolio/${id}`}>
            <Chip
              size="small"
              label="포트폴리오"
              variant="outlined"
              color="primary"
            ></Chip>
          </Link>
        );
      },
    },
    { name: "제목", width: 160 },
    { name: "세부사항", width: 160 },
    { name: "업체명", width: 120 },
    {
      name: "생성일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { createdAt },
        } = param;
        return formatTimestamp(createdAt);
      },
    },
    {
      name: "수정일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { updatedAt },
        } = param;
        return formatTimestamp(updatedAt);
      },
    },
    {
      name: "삭제",
      width: 60,
      renderCell: (param) => {
        const {
          row: { deleted },
        } = param;
        return !deleted ? (
          <></>
        ) : (
          <div
            style={{
              backgroundColor: "red",
              display: "inline",
              padding: "4px 4px",
              fontWeight: "bold",
              fontSize: "10px",
              color: "#fff",
              borderRadius: "3px",
            }}
          >
            삭제
          </div>
        );
      },
    },
    {
      name: "활성화",
      width: 60,
      renderCell: (param) => {
        const {
          row: { activated },
        } = param;
        return (
          <div
            style={{
              display: "flex",
              alignItems: "center",
              height: "100%",
              justifyContent: "center",
            }}
          >
            {activated ? (
              <TaskAltIcon style={{ color: "#22bb33", fontSize: "18px" }} />
            ) : (
              <PendingIcon style={{ color: "#bb2124", fontSize: "18px" }} />
            )}
          </div>
        );
      },
    },
  ],
  review: [
    {
      name: "ID",
      width: 25,
      sortable: false,
    },
    {
      name: "포트폴리오",
      width: 120,
      sortable: false,
      renderCell: (param) => {
        const {
          row: { portfolioId },
        } = param;
        return (
          <Link to={`/portfolio/${portfolioId}`}>
            <Chip
              size="small"
              label="포트폴리오"
              variant="outlined"
              color="primary"
            ></Chip>
          </Link>
        );
      },
    },
    { name: "닉네임", width: 100 },
    { name: "리뷰 제목", width: 140 },
    { name: "리뷰 상세", width: 140 },
    {
      name: "평점",
      width: 140,
      renderCell: (param) => {
        const {
          row: { rate },
        } = param;
        return (
          <div
            style={{
              height: "100%",
              display: "flex",
              alignItems: "center",
              gap: 8,
            }}
          >
            <Rating
              sx={{
                // 채워진 별 색깔
                "& .MuiRating-iconFilled": {
                  color: "#ff5722", // 채워진 별 색깔 지정
                },
              }}
              value={rate}
              size="small"
              readOnly
            />
            <span>{rate.toFixed(1)}</span>
          </div>
        );
      },
    },
    {
      name: "생성일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { createdAt },
        } = param;
        return formatTimestamp(createdAt);
      },
    },
    {
      name: "수정일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { createdAt },
        } = param;
        return formatTimestamp(createdAt);
      },
    },
  ],
  report: [
    {
      name: "ID",
      width: 25,
    },
    {
      name: "참조",
      link: true,
    },
    { name: "닉네임", width: 80 },
    {
      name: "신고 분류",
      width: 120,
      renderCell: (param) => {
        const {
          row: { sort },
        } = param;
        return (
          <Chip
            size="small"
            label={sort}
            variant="outlined"
            color={sort === "REVIEW" ? "primary" : "success"}
          ></Chip>
        );
      },
    },
    { name: "제목", width: 160 },
    { name: "상세", width: 160 },
    { name: "비고" },
    {
      name: "현황",
      width: 140,
      renderCell: (param) => {
        const {
          row: { progress },
        } = param;
        return (
          <Chip
            size="small"
            label={progress}
            variant="filled"
            color={(() => {
              switch (progress) {
                case "PENDING":
                  return "success";
                case "COMPLETED":
                  return "default";
                default:
                  return "primary";
              }
            })()}
          ></Chip>
        );
      },
    },
    {
      name: "생성일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { createdAt },
        } = param;
        return formatTimestamp(createdAt);
      },
    },
    {
      name: "수정일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { updatedAt },
        } = param;
        return formatTimestamp(updatedAt);
      },
    },
  ],
  quotation: [
    {
      name: "ID",
      width: 25,
    },
    {
      name: "금액",
      integer: true,
      width: 100,
      renderCell: (param) => {
        const {
          row: { totalTransactionAmount },
        } = param;
        return (
          <span style={{ fontWeight: "bold" }}>
            {totalTransactionAmount.toLocaleString()}
          </span>
        );
      },
    },
    {
      name: "현황",
      width: 180,
      renderCell: (param) => {
        const {
          row: { progress },
        } = param;
        return (
          <Chip
            size="small"
            label={progress}
            variant="filled"
            color={(() => {
              if (progress.indexOf("CANCELLED") >= 0) {
                return "default";
              }
              switch (progress) {
                case "APPROVED":
                  return "success";
                default:
                  return "primary";
              }
            })()}
          ></Chip>
        );
      },
    },
    {
      name: "생성일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { createdAt },
        } = param;
        return formatTimestamp(createdAt);
      },
    },
    {
      name: "수정일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { updatedAt },
        } = param;
        return formatTimestamp(updatedAt);
      },
    },
  ],
  quotationrequest: [
    {
      name: "ID",
      width: 25,
    },
    { name: "닉네임", width: 100 },
    {
      name: "포트폴리오",
      width: 120,
      renderCell: (param) => {
        const {
          row: { portfolioId },
        } = param;
        return (
          <Link to={`/portfolio/${portfolioId}`}>
            <Chip
              size="small"
              label="포트폴리오"
              variant="outlined"
              color="primary"
            ></Chip>
          </Link>
        );
      },
    },
    { name: "제목", width: 160 },
    { name: "상세", width: 200 },
    {
      name: "현황",
      width: 180,
      renderCell: (param) => {
        const {
          row: { progress },
        } = param;
        return (
          <Chip
            size="small"
            label={progress}
            variant="filled"
            color={(() => {
              if (progress.indexOf("CANCELLED") >= 0) {
                return "default";
              }
              switch (progress) {
                case "APPROVED":
                  return "success";
                default:
                  return "primary";
              }
            })()}
          ></Chip>
        );
      },
    },
    {
      name: "생성일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { createdAt },
        } = param;
        return formatTimestamp(createdAt);
      },
    },
    {
      name: "수정일",
      date: true,
      width: 120,
      renderCell: (param) => {
        const {
          row: { updatedAt },
        } = param;
        return formatTimestamp(updatedAt);
      },
    },
  ],
};

function route(params, inputValue, closer) {
  switch (params) {
    case "":
      return <MemberModalContent {...inputValue} closer={closer} />;
    case "member":
      return <MemberModalContent {...inputValue} closer={closer} />;
    case "company":
      return <CompanyModalContent {...inputValue} closer={closer} />;
    case "portfolio":
      return <PortfolioModalContent {...inputValue} closer={closer} />;
    case "review":
      return <ReviewModalContent {...inputValue} closer={closer} />;
    case "report":
      return <ReportModalContent {...inputValue} closer={closer} />;
    case "quotation":
      return <QuotationModalContent {...inputValue} closer={closer} />;
    case "quotationRequest":
      return <QuotationRequestModalContent {...inputValue} closer={closer} />;
    default:
      return <></>;
  }
}

function NestedList() {
  const [open, setOpen] = React.useState(true);

  const path = (() => {
    const value = useLocation().pathname.split("/");
    return value.length > 2 ? value[2] : "/";
  })();

  const handleClick = () => {
    setOpen(!open);
  };

  return (
    <List
      className={style["nav-list"]}
      subheader={
        <ListSubheader className={style["home-link-wrapper"]}>
          <Link to="/" className={style["home"]}>
            <img src="/logo.svg" alt="home" />
          </Link>
        </ListSubheader>
      }
    >
      {pathNameArray.map((text, index) => {
        return (
          <Link to={text.charAt(0).toLowerCase() + text.slice(1)} key={text}>
            <ListItemButton
              className={`${style["list-item"]} ${
                path.toLowerCase() === text.toLowerCase()
                  ? style["selected"]
                  : ""
              }`}
            >
              {IconArray[index]}
              <span>{linkNameArray[index]}</span>
            </ListItemButton>
          </Link>
        );
      })}
    </List>
  );
}

const handleApiRequest = async () => {
  const check = window.confirm("삭제 하시겠습니까?");
  if (!check) {
    return;
  }
  try {
    const ids = selectedRows;
    const response = await axios.delete(`/api/${pathname}/admin/soft/${ids}`);
  } catch (error) {
  } finally {
  }
};

function CustomToolbar(props) {
  return (
    <GridToolbarContainer>
      <GridToolbarFilterButton />
      <GridToolbarExport />
    </GridToolbarContainer>
  );
}

const filterOperators = getGridStringOperators()
  .filter((operator) => operator.value === "contains")
  .map((operator) => {
    return {
      ...operator,
    };
  });

const handleUpdateClick = async (id) => {};

function DataTable() {
  const [data, setData] = useState([]);
  const [totalCount, setTotalCount] = useState(0);
  const path = useLocation();
  const pathname = path.pathname.split("/admin/")[1];
  const [columns, setColumns] = useState([]);
  const [filterModel, setFilterModel] = useState({ field: "", value: "" });
  const [paginationModel, setPaginationModel] = useState({
    page: 0,
    pageSize: 5,
  });
  const [sortModel, setSortModel] = useState({ field: "", sort: "" });
  const [selectedRows, setSelectedRows] = useState([]);
  const [open, setOpen] = useState(false);
  const [inputValue, setInputValue] = useState({});
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const navigator = useNavigate();

  const buttonColumns = {
    field: "delete",
    headerName: "수정",
    width: 150,
    sortable: false,
    renderCell: (params) => (
      <div style={{ height: "100%", display: "flex", alignItems: "center" }}>
        <DisplaySettingsIcon
          style={{ fill: "#adb5bd", cursor: "pointer" }}
          onClick={(event) => {
            event.stopPropagation();
            setInputValue(params.row);
            handleUpdateClick(params.row.id);
            handleOpen();
          }} // ID를 전달
        />
      </div>
    ),
  };

  const fetchData = async (pathname, paginationModel) => {
    try {
      const { page, pageSize } = paginationModel;
      const response = await axios.get(`/api/${pathname}/admin/list`, {
        params: { page, pageSize },
      });

      // 동적으로 컬럼 정의 생성
      if (response.data.content.length > 0) {
        const cols = Object.keys(response.data.content[0]).map((key, idx) => {
          const columArray = customColumns[pathname.toLowerCase()];
          const { name, width, renderCell, sortable } = columArray[idx];

          const col = {
            field: key,
            headerName: name, // 첫 글자 대문자
            filterOperators,
          };

          if (width) {
            col["width"] = width;
          } else {
            col["flex"] = 1;
          }

          if (renderCell) {
            col["renderCell"] = renderCell;
          }

          if (sortable === false) {
            col["sortable"] = false;
          }

          return col;
        });
        cols.push(buttonColumns);
        setColumns(cols);
      }
      setData(response.data.content);
      setTotalCount(response.data.page.totalElements); // 전체 데이터 수
    } catch (error) {
      console.error("데이터를 가져오는 데 오류가 발생했습니다:", error);
    }
  };

  const fetchFilterdData = async (
    filterModel,
    sortModel,
    pathname,
    paginationModel
  ) => {
    try {
      const { page, pageSize } = paginationModel;
      const param = {};
      param.page = page;
      param.pageSize = pageSize;
      if (sortModel) {
        if (sortModel.field) {
          param.sortField = sortModel.field;
        }
        if (sortModel.sort) {
          param.sort = sortModel.sort;
        }
      }
      if (filterModel.value) {
        param.filterValue = filterModel.value;
      }
      if (filterModel.field) {
        param.filterColumn = filterModel.field;
      }
      const response = await axios.get(
        `/api/${pathname}/admin/list/filter/contains`,
        {
          params: param, //sortModel, filterModel
        }
      );
      // 동적으로 컬럼 정의 생성
      if (response.data.content.length > 0) {
        const cols = Object.keys(response.data.content[0]).map((key, idx) => {
          const columArray = customColumns[pathname.toLowerCase()];
          const { name, width, renderCell, sortable } = columArray[idx];
          const col = {
            field: key,
            headerName: name, // 첫 글자 대문자
            filterOperators,
          };
          if (width) {
            col["width"] = width;
          } else {
            col["flex"] = 1;
          }

          if (renderCell) {
            col["renderCell"] = renderCell;
          }

          if (sortable === false) {
            col["sortable"] = false;
          }

          return col;
        });
        cols.push(buttonColumns);
        setColumns(cols);
      }
      setData(response.data.content); // 실제 데이터 구조에 맞게 수정
      setTotalCount(response.data.page.totalElements); // 전체 데이터 수
    } catch (error) {
      console.error("데이터를 가져오는 데 오류가 발생했습니다:", error);
    } finally {
      // console.log(data);
    }
  };

  useEffect(() => {
    let sortField;
    let filterValue;
    if (sortModel) {
      sortField = sortModel.field;
    }
    if (filterModel) {
      filterValue = filterModel.value;
    }
    if (filterValue || sortField) {
      // filterModel, sortModel 값 확인 후 분기
      fetchFilterdData(filterModel, sortModel, pathname, paginationModel); // 매개변수 수정
    } else {
      fetchData(pathname, paginationModel);
    }
  }, [paginationModel, sortModel, totalCount, filterModel]);

  useEffect(() => {
    setPaginationModel({ page: 0, pageSize: 5 });
  }, [pathname, sortModel, filterModel]);

  useEffect(() => {
    setSortModel({ field: "", sort: "" });
    setFilterModel({ field: "", value: "" });
    setPaginationModel({ page: 0, pageSize: 5 });
  }, [pathname]);
  const handleFilterModelChange = (model) => {
    setFilterModel(model.items[0]);
  };
  const handleSortModelChange = (model) => {
    setSortModel(model[0]);
  };
  const handlePaginationModelChange = (model) => {
    setPaginationModel(model);
  };
  const handleRowSelection = (newSelection) => {
    setSelectedRows(newSelection.join(","));
  };

  return (
    <>
      <div className={style["title"]}>
        {(() => {
          const index = pathNameArray
            .map((item) => item.toLowerCase())
            .indexOf(pathname.toLowerCase());
          return [
            IconArray[index],
            <span key={linkNameArray[index]}>{linkNameArray[index]}</span>,
          ];
        })()}
      </div>
      <div className={style.buttonContainer}>
        <Button
          onClick={handleApiRequest}
          variant="contained"
          size="normal"
          startIcon={<DeleteIcon />}
          disableRipple
          className={style.customButton}
        >
          삭제
        </Button>
      </div>
      <div className={style["data-grid-wrapper"]}>
        <DataGrid
          className={style["data-grid"]}
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
          pageSizeOptions={[5, 10]}
          onFilterModelChange={handleFilterModelChange}
          onSortModelChange={handleSortModelChange}
          onPaginationModelChange={handlePaginationModelChange}
          onRowSelectionModelChange={handleRowSelection}
          autoHeight
          paginationModel={paginationModel}
        />
      </div>
      <Modal open={open} onClose={handleClose}>
        <Box className={style["modal-style"]}>
          <span className={style["title"]}>상세정보</span>
          {route(pathname, inputValue, handleClose)}
        </Box>
      </Modal>
    </>
  );
}

function Admin() {
  const navigate = useNavigate();
  const [isLoading1, setIsLoading1] = useState(true);

  useEffect(() => {
    axios
      .get("/api/member/admin/role")
      .then((result) => {
        //console.log(result);
        setIsLoading1(false);
      })
      .catch((err) => {
        navigate("/");
      });
  }, []);

  if (isLoading1) {
    return <></>; // 또는 로딩 스피너 같은 것을 표시할 수
  }

  return (
    <>
      <main className={style["index"]}>
        <aside className={style["aside"]}>
          <NestedList></NestedList>
        </aside>
        <div className={style["container"]}>
          <Routes>
            <Route path={"company"} element={<DataTable />} />
            <Route path={"portfolio"} element={<DataTable />} />
            <Route path={"review"} element={<DataTable />} />
            <Route path={"member"} element={<DataTable />} />
            <Route path={"report"} element={<DataTable />} />
            <Route path={"quotation"} element={<DataTable />} />
            <Route path={"quotationRequest"} element={<DataTable />} />
            <Route path="" element={<Navigate to="member" replace />} />
          </Routes>
        </div>
      </main>
    </>
  );
}

export default Admin;
