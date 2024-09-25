import React, { useEffect, useState } from "react";
import axios from "axios";
import { TextField, Button, Box, Snackbar, Avatar } from "@mui/material";
import { useNavigate, Route, Routes, Navigate } from "react-router-dom";
import Header from "../../components/common/header";
import Footer from "../../components/common/footer";
import Sidebar from "../login/mypage-sidebar";
import ReportUserList from "../reportUserList";
import QuotationRequestListComponent from "../quotationRequest/quotationRequestListComponent.jsx";
import ReviewUserList from "../reviewUserList";
import { useDropzone } from "react-dropzone";
import style from "../../styles/mypage.module.scss";

const MyPage = () => {
  const [selectedComponent, setSelectedComponent] = useState("profile");
  const [userData, setUserData] = useState(null);
  const navigator = useNavigate();

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await axios.get("/api/member/email");
        setUserData(response.data);
      } catch (error) {
        console.error("사용자 정보를 가져오는 중 오류 발생:", error);
      }
    };

    fetchUserData();
  }, []);

  const handleSelectProfile = () => {
    //setSelectedComponent("profile");
    navigator("profile");
  };

  const handleSelectQuotationRequests = () => {
    //setSelectedComponent("quotationRequest/member");
    navigator("quotationRequest/member");
  };

  const handleSelectReportUserList = () => {
    //setSelectedComponent("reportUserList");
    if (userData && userData.id) {
      navigator(`reportUserList/${userData.id}`);
    } else {
      console.error("userData가 로드되지 않았습니다.");
    }
  };

  const handleSelectReviewUserList = () => {
    navigator("reviewList");
  };

  return (
    <>
      <Header />
      <main className={style["mypage"]}>
        <div className={style["container"]}>
          <Sidebar
            onSelectProfile={handleSelectProfile}
            onSelectQuotationRequests={handleSelectQuotationRequests}
            onSelectReportUserList={handleSelectReportUserList}
            onSelectReviewUserList={handleSelectReviewUserList}
          />
          <Routes>
            <Route path="/profile" element={<MyProfile />} />
            <Route
              path="/quotationRequest/member"
              element={<QuotationRequestListComponent />}
            />
            <Route
              path="/reportUserList/:memberId"
              element={
                userData ? (
                  <ReportUserList memberId={userData.id} />
                ) : (
                  <div>Loading...</div>
                )
              }
            />
            <Route path="/reviewList" element={<ReviewUserList />} />
            <Route path="/" element={<Navigate to="profile" replace />} />
          </Routes>
        </div>
      </main>
      <Footer />
    </>
  );
};

export function MyProfile() {
  const [userData, setUserData] = useState({
    username: "",
    email: "",
    password: "",
    profileImage: "",
  });
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadFile, setUploadFile] = useState(null);
  const [message, setMessage] = useState("");
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const navigate = useNavigate();
  const [userImage, setUserImage] = useState("");
  const [username, setUsername] = useState("");

  useEffect(() => {
    fetch(`/api/member/email`)
      .then((result) => result.json())
      .then((data) => {
        const { images } = data;
        if (images.length) {
          const { url } = images[images.length - 1];
          setUserImage(url);
        } else {
          setUserImage("");
        }
      });
    const fetchUserData = async () => {
      try {
        const response = await axios.get("/api/member/email");
        setUserData({
          email: response.data.email,
          username: response.data.username,
          password: "",
          profileImage: response.data.profileImage || "",
        });
      } catch (error) {
        console.error("사용자 정보를 가져오는 중 오류 발생:", error);
        setMessage("정보를 가져오는 데 실패했습니다.");
        setOpenSnackbar(true);
      }
    };

    fetchUserData();
  }, []);

  const { getRootProps, getInputProps } = useDropzone({
    accept: {
      "image/*": [],
    },
    onDrop: (acceptedFiles) => {
      if (acceptedFiles.length) {
        setUploadFile(acceptedFiles[0]);
      }
      setSelectedFile(
        acceptedFiles.map((file) => {
          return Object.assign(file, {
            preview: URL.createObjectURL(file),
          });
        })[0]
      );
    },
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData({
      ...userData,
      [name]: value,
    });
  };

  const handleFileChange = (e) => {
    setSelectedFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData();
      formData.append("username", userData.username);
      formData.append("password", userData.password);

      // 이미지 업로드 요청
      if (uploadFile) {
        formData.append("file", uploadFile);
      }

      // 프로필 수정 요청
      await axios.patch("/api/member/profile", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      // 사용자 이미지와 사용자 이름 업데이트
      if (uploadFile) {
        const imageUrl = URL.createObjectURL(uploadFile); // 임시 URL 생성
        setUserImage(imageUrl);
      }
      setUsername(userData.username);

      setMessage("정보가 성공적으로 수정되었습니다.");
      setOpenSnackbar(true);
      navigate(0);
    } catch (error) {
      console.error("수정 중 오류 발생:", error);
      setMessage("수정에 실패했습니다.");
      setOpenSnackbar(true);
    }
  };

  const handleCloseSnackbar = () => {
    setOpenSnackbar(false);
  };

  return (
    <div className={style["profile-content"]}>
      <div className={style["title"]}>내 프로필 수정</div>
      <div className={style["content"]}>
        <div {...getRootProps({ className: style["dropzone"] })}>
          {selectedFile ? (
            <img src={selectedFile.preview} className={style["preview"]} />
          ) : (
            <Avatar className={style["avatar"]} src={userImage}></Avatar>
          )}
          <input {...getInputProps()} />
        </div>
        <form onSubmit={handleSubmit}>
          <TextField
            fullWidth
            label="새로운 이름은 2 ~ 17자 내로 작성"
            name="username"
            value={userData.username}
            onChange={handleChange}
            required
            variant="standard"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            fullWidth
            label="비밀번호는 영문 숫자조합 8 ~ 25자 내로 작성"
            name="password"
            type="password"
            value={userData.password}
            onChange={handleChange}
            required
            variant="standard"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <div className={style["btn-group"]}>
            <Button variant="contained" color="primary" type="submit">
              수정하기
            </Button>
          </div>
        </form>
      </div>
      <Snackbar
        open={openSnackbar}
        onClose={handleCloseSnackbar}
        message={message}
        autoHideDuration={6000}
      />
    </div>
  );
}

export default MyPage;
