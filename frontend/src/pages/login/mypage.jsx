import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie'; // js-cookie 라이브러리 추가

const Mypage = () => {
    const [userData, setUserData] = useState({
        username: '',
        password: '',
    });
    const [message, setMessage] = useState('');

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get('/api/member/email'); // 이메일로 사용자 정보 가져오기
                setUserData({
                    email: response.data.email,
                    username: response.data.username,
                    password: '', // 비밀번호는 초기화
                });
            } catch (error) {
                console.error('사용자 정보를 가져오는 중 오류 발생:', error);
                setMessage('정보를 가져오는 데 실패했습니다.');
            }
        };

        fetchUserData();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserData({
            ...userData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.patch('/api/member', userData); // 사용자 정보 수정 요청
            setMessage('정보가 성공적으로 수정되었습니다.');
        } catch (error) {
            console.error('수정 중 오류 발생:', error);
            setMessage('수정에 실패했습니다.');
        }
    };

    return (
        <div>
            <h1>내 프로필 수정</h1>
            {message && <p>{message}</p>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="username">이름:</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={userData.username}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="email">이메일:</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={userData.email}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button type="submit">수정하기</button>
            </form>
        </div>
    );
};

export default Mypage;
