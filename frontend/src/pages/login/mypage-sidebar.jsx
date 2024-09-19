import React, { useEffect, useState } from "react";
import { List, ListItem, ListItemText, Divider, Avatar, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Sidebar = ({ onSelectProfile, onSelectQuotationRequests }) => {
    const navigate = useNavigate();
    const [memberId, setMemberId] = useState("");
    const [userData, setUserData] = useState({ username: "", profileImage: "default-profile-image-url.jpg" });

    useEffect(() => {
        const fetchMemberId = async () => {
            try {
                const response = await fetch(`/api/member/email`);
                const data = await response.json();
                setMemberId(data.id);
                setUserData({
                    username: data.username, 
                    profileImage: data.images && data.images.length > 0 ? data.images[data.images.length - 1].url : "default-profile-image-url.jpg"
                });
            } catch(error) {
                console.error("회원 정보를 불러오는데 실패했습니다.", error);
            }
        };
        fetchMemberId();
    }, []);

    

    return (
        <div
            style={{
                width: "250px",
                height: "100vh",
                backgroundColor: "#f0f0f0",
                padding: "20px",
            }}
        >
            <Avatar
                alt={userData.username}
                src={userData.profileImage}
                style={{ width: 80, height: 80, marginLeft: 85, marginTop: 70, marginBottom: 16 }}
            />
            <div style={{
                marginTop: "10px", 
                marginLeft: "20px",
                marginRight: "20px",
                padding: "5px",
                border: "2px solid #ccc",
                borderRadius: "8px",
                backgroundColor: "#fff",
                display: "flex",
                justifyContent: "center",
                boxShadow: "12px 12px 7px rgba(0, 0, 0, 0.4)",
                
            }}>
                <Typography variant="h6" style={{flexGrow: 1, textAlign: "center" }}>{userData.username}</Typography>
            </div>
            <List style={{marginTop: "40px" }}>
                <ListItem button onClick={onSelectProfile}>
                    <ListItemText primary="내 프로필" />
                </ListItem>
                <Divider />
                <ListItem button style={{marginTop: "10px"}} onClick={onSelectQuotationRequests}>
                    <ListItemText primary="내 신청서" />
                </ListItem>            
            </List>
            <Divider />
        </div>
    );
};

export default Sidebar;
