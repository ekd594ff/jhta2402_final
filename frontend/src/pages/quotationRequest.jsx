import React, {useEffect, useState} from 'react';
import {Button, Divider, Snackbar, TextField} from '@mui/material';
import axios from 'axios';
import Header from "../components/common/header.jsx";
import {useLocation, useNavigate} from "react-router-dom";

import Checkbox from "@mui/material/Checkbox";

import style from "../styles/quotationRequest.module.scss";
import Footer from "../components/common/footer.jsx";


function SolutionItemList(props) {
    const {list, setter, index, title, price, description} = props;

    function handleToggle() {
        const newSelectedList = [...list];
        newSelectedList[index] = !newSelectedList[index];
        setter([...newSelectedList]);
    }

    return (<li className={style['solution-list-item']} onClick={handleToggle}>
        <div className={style['left']}>
            <Checkbox
                disableTouchRipple
                edge="start"
                checked={list[index]}
                tabIndex={-1}
                disableRipple
                size="medium"
            />
        </div>
        <div className={style['right']}>
            <div className={style['left']}>
                <div className={style['title']}>{title}</div>
                <div className={style['description']}>{description}</div>
            </div>
            <div className={style['right']}>
                <div className={style['price']}>{price.toLocaleString()}</div>
            </div>
        </div>
    </li>);
}

const QuotationRequest = () => {

    const location = useLocation();

    const {state: {portfolioInfo}} = location;

    const navigator = useNavigate();

    const [selectedSolutions, setSelectedSolutions] = useState(location?.state?.selectedList ? [...location?.state?.selectedList] : []);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [Count, setCount] = useState(1);
    const [customDescription, setCustomDescription] = useState('');
    const [totalPrice, setTotalPrice] = useState(0);
    const [customTitle, setCustomTitle] = useState("");

    // 솔루션 추가
    const addSolution = (solution) => {
        if (selectedSolutions.find(s => s.title === solution.title)) {
            setSnackbarMessage('이미 신청한 솔루션입니다.');
            setOpenSnackbar(true);
        } else {
            setSelectedSolutions([...selectedSolutions, solution]);
        }
    };

    // 신청 완료 핸들러
    const handleRequestSubmit = async () => {
        try {
            const data = {
                memberId: "6f3f2761-0fa0-43f3-bd22-e658e2a5d7df",
                portfolioId: "a84a9e98-3eb0-4542-9bd0-94b54de5276f",
                title: `title${Count}`,
                description: customDescription,
                solutions: selectedSolutions,
            };
            const response = await axios.post('/api/quotationRequest/create', data);
            if (response.status === 200) {
                setSnackbarMessage('신청 완료');
                setOpenSnackbar(true);
                setSelectedSolutions([]);
                setCustomDescription('');
                setCount(Count + 1);
            }

        } catch (error) {
            setSnackbarMessage('신청 실패: ' + error.message);
            setOpenSnackbar(true);
        }
    };

    // Snackbar 닫기 핸들러
    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    useEffect(() => {
        const {state: {list}} = location;
        const newTotal = selectedSolutions.reduce((acc, cur, index) => {
            if (cur) {
                acc += list[index].price;
            }
            return acc;
        }, 0);
        setTotalPrice(newTotal);
    }, [selectedSolutions, setTotalPrice, location.state]);

    return (<>
        <Header/>
        <main className={style['quotation-request']}>
            <div className={style['container']}>
                <div className={style['title']}>신청한 솔루션</div>
                <div className={style['portfolio-info']}>
                    <div className={style['company']}>{portfolioInfo.companyName}</div>
                    <div className={style['portfolio']}>{portfolioInfo.title}</div>
                    <div className={style['description']}>{portfolioInfo.description}</div>
                </div>
                <ul className={style['solution-list']}>
                    {location.state.list.map((solution, index) => <SolutionItemList key={index}
                                                                                    {...solution}
                                                                                    list={selectedSolutions}
                                                                                    index={index}
                                                                                    setter={setSelectedSolutions}/>)}
                </ul>
                <Divider/>
                <div className={style['total-price']}>
                    <div className={style['total']}>총 금액<span>KRW {totalPrice.toLocaleString()}</span></div>
                </div>
                <TextField
                    label="기타 요청사항"
                    variant="outlined"
                    fullWidth
                    multiline
                    rows={4}
                    value={customDescription}
                    onChange={(e) => setCustomDescription(e.target.value)}
                    InputLabelProps={{shrink: true}}
                    className={style['other-description']}
                    sx={{
                        '& .MuiOutlinedInput-root': {
                            '&:hover fieldset': {
                                borderColor: 'rgba(0, 0, 0, 0.6);', // hover 시에도 기본값
                            }
                        },
                    }}
                />
                <div className={style['btn-group']}>
                    <Button variant="contained" className={style['back']} onClick={() => {
                        navigator(`/portfolio/${portfolioInfo.portfolioId}`);
                    }}>
                        이전으로
                    </Button>
                    <Button variant="contained" className={style['submit']} onClick={handleRequestSubmit}>
                        신청 완료
                    </Button>
                </div>
            </div>
        </main>
        <Footer/>
    </>);
};

export default QuotationRequest;
