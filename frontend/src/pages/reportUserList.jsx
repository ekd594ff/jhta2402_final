import React, { useEffect, useState } from "react";
import axios from "axios";
import { Chip, CircularProgress, Divider, Snackbar } from "@mui/material";

import style from "../styles/reportUserList.module.scss";
import { useNavigate } from "react-router-dom";

const ReportUserList = ({ memberId }) => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [openSnackbar, setOpenSnackbar] = useState(false);

  useEffect(() => {
    if (!memberId) {
      console.error("memberId가 undefined입니다.");
      setMessage("잘못된 요청입니다.");
      setOpenSnackbar(true);
      setLoading(false);
      return;
    }

    const fetchReports = async () => {
      try {
        const response = await axios.get(
          `/api/report/memberList/${memberId}?page=0&pageSize=10`
        );
        setReports(response.data.content);
      } catch (error) {
        console.error("신고 리스트를 가져오는 중 오류 발생:", error);
        setMessage("신고 리스트를 가져오는데 실패 했습니다.");
        setOpenSnackbar(true);
      } finally {
        setLoading(false);
      }
    };

    fetchReports();
  }, [memberId]);

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  const navigator = useNavigate();

  return (
    <div className={style["reportUserList"]}>
      <div className={style["title"]}>내 신고 리스트</div>
      {loading ? (
        <CircularProgress />
      ) : (
        <ul className={style["list"]}>
          {reports.map((report, index) => {
            const { id, title, sort, refId, description } = report;
            const render = [
              <li key={id} className={style["item"]}>
                <div className={style["info"]}>
                  <span className={style["title"]}>{title}</span>
                  <Chip
                    className={style["sort"]}
                    size="small"
                    variant="filled"
                    label={sort}
                    data-sort={sort}
                    onClick={
                      report.sort === "PORTFOLIO"
                        ? () => {
                            navigator(`/portfolio/${refId}`);
                          }
                        : () => {}
                    }
                  />
                </div>
                <div className={style["description"]}>{description}</div>
              </li>,
            ];
            if (index !== reports.length - 1) {
              render.push(<Divider key={`div_${id}`} variant="middle" />);
            }
            return render;
          })}
        </ul>
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
