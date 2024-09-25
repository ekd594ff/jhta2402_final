import React, { useEffect, useState } from "react";
import { List, ListItem, ListItemText, Avatar } from "@mui/material";
import { useLocation, useNavigate } from "react-router-dom";

import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import AssignmentIcon from "@mui/icons-material/Assignment";
import StarIcon from "@mui/icons-material/Star";
import NotificationsIcon from "@mui/icons-material/Notifications";

import style from "../../styles/mypage-sidebar.module.scss";

const Sidebar = ({
  onSelectProfile,
  onSelectQuotationRequests,
  onSelectReportUserList,
  onSelectReviewUserList,
}) => {
  const navigate = useNavigate();
  const [memberId, setMemberId] = useState("");
  const [userData, setUserData] = useState({
    username: "",
    profileImage: "default-profile-image-url.jpg",
  });

  const path = useLocation().pathname.split("/")[2];

  useEffect(() => {
    const fetchMemberId = async () => {
      try {
        const response = await fetch(`/api/member/email`);
        const data = await response.json();
        setMemberId(data.id);
        setUserData({
          username: data.username,
          profileImage:
            data.images && data.images.length > 0
              ? data.images[data.images.length - 1].url
              : "default-profile-image-url.jpg",
        });
      } catch (error) {
        console.error("회원 정보를 불러오는데 실패했습니다.", error);
      }
    };
    fetchMemberId();
  }, []);

  return (
    <div className={style["mypage-sidebar"]}>
      <div className={style["info"]}>
        <Avatar
          className={style["avatar"]}
          alt={userData.username}
          src={userData.profileImage}
        />
        <div className={style["username"]}>{userData.username}</div>
      </div>
      <List className={style["list"]}>
        <ListItem
          onClick={onSelectProfile}
          className={path === "profile" ? style["selected"] : ""}
        >
          <AccountCircleIcon />
          <span>내 프로필</span>
        </ListItem>
        <ListItem
          onClick={onSelectQuotationRequests}
          className={path === "quotationRequest" ? style["selected"] : ""}
        >
          <AssignmentIcon />
          <span>내 신청서</span>
        </ListItem>
        <ListItem
          onClick={onSelectReportUserList}
          className={path === "reportUserList" ? style["selected"] : ""}
        >
          <NotificationsIcon />
          <span>내 신고</span>
        </ListItem>
        <ListItem
          onClick={onSelectReviewUserList}
          className={path === "reviewList" ? style["selected"] : ""}
        >
          <StarIcon />
          <span>내 리뷰</span>
        </ListItem>
      </List>
    </div>
  );
};

export default Sidebar;
