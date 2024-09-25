import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Chip,
  CircularProgress,
  Snackbar,
  Divider,
  Collapse,
  Badge,
} from "@mui/material";

import style from "../styles/reportUserList.module.scss";
import { useNavigate } from "react-router-dom";

import SupportAgentIcon from "@mui/icons-material/SupportAgent";

const ReportUserList = ({ memberId }) => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [page, setPage] = useState(0);
  const [isEndOfData, setIsEndOfData] = useState(false);

  const fetchReports = async () => {
    try {
      const response = await axios.get(
        `/api/report/memberList/${memberId}?page=${page}&pageSize=5`
      );

      if (!response.data.content.length) {
        setIsEndOfData(true);
        return;
      }

      setReports((prev) => [...prev, ...response.data.content]);
    } catch (error) {
      console.error("신고 리스트를 가져오는 중 오류 발생:", error);
      setMessage("신고 리스트를 가져오는데 실패 했습니다.");
      setOpenSnackbar(true);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!memberId) {
      console.error("memberId가 undefined입니다.");
      setMessage("잘못된 요청입니다.");
      setOpenSnackbar(true);
      setLoading(false);
      return;
    }

    fetchReports();
  }, [memberId]);

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  const navigator = useNavigate();

  useEffect(() => {
    if (page) {
      fetchReports();
    }
  }, [page]);

  function ProgressChip(progress, comment, open, setOpen) {
    const css = {
      borderColor: "red",
      color: "red",
    };
    switch (progress) {
      case "COMPLETED":
        {
          css.borderColor = "green";
          css.color = "green";
        }
        break;
      case "PENDING":
        {
          css.borderColor = "orange";
          css.color = "orange";
        }
        break;
      case "IN_PROGRESS": {
        css.borderColor = "blue";
        css.color = "blue";
      }
      default:
        break;
    }

    return comment ? (
      <Badge color="success" variant="dot" className={style["badge"]}>
        <Chip
          className={style["chip"]}
          label={progress}
          variant="outlined"
          size="small"
          style={css}
          onClick={() => {
            setOpen((prev) => !prev);
          }}
        />
      </Badge>
    ) : (
      <Chip
        className={style["chip"]}
        label={progress}
        variant="outlined"
        size="small"
        style={css}
      />
    );
  }

  function Item(props) {
    const {
      id,
      title,
      sort,
      refId,
      description,
      createdAt,
      progress,
      comment,
    } = props;

    const [open, setOpen] = useState(false);

    return (
      <li key={id} className={style["item"]}>
        <div className={style["date"]}>
          {createdAt.split("T")[0].replace(/-/g, ".")}
          <Chip
            className={style["sort"]}
            size="small"
            variant="filled"
            label={sort}
            data-sort={sort}
            onClick={
              sort === "PORTFOLIO"
                ? () => {
                    navigator(`/portfolio/${refId}`);
                  }
                : () => {}
            }
          />
        </div>
        <div className={style["info"]}>
          <span className={style["title"]}>{title}</span>
        </div>
        <div className={style["description"]}>{description}</div>
        <div className={style["progress"]}>
          {ProgressChip(progress, comment, open, setOpen)}
        </div>
        <Collapse in={open} className={style["comment"]}>
          <div className={style["content"]}>
            <SupportAgentIcon />
            <div>{comment}</div>
          </div>
        </Collapse>
      </li>
    );
  }

  return (
    <div className={style["reportUserList"]}>
      <div className={style["title"]}>내 신고 리스트</div>
      {loading ? (
        <CircularProgress />
      ) : reports.length ? (
        <ul className={style["list"]}>
          {reports.map((report, index) => {
            return <Item {...report} key={`${index}_${report.id}`} />;
          })}
        </ul>
      ) : (
        <div className={style["empty"]}>신고 내역이 존재하지 않습니다</div>
      )}
      {isEndOfData ? (
        <></>
      ) : (
        <Divider variant="middle" className={style["divider"]}>
          <Chip
            label="더보기"
            variant="outlined"
            size="small"
            onClick={() => {
              setPage((prev) => prev + 1);
            }}
          />
        </Divider>
      )}
      <Snackbar
        open={openSnackbar}
        onClose={handleCloseSnackbar}
        message={message}
        autoHideDuration={6000}
      />
    </div>
  );
};

export default ReportUserList;
