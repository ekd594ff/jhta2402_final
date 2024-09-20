import {useNavigate} from "react-router-dom";
import Avatar from "@mui/material/Avatar";

import style from "../../styles/solution-list-item.module.scss";


function SolutionListItem(props) {
    const {title, id, portfolioId, description, url} = props;
    const navigator = useNavigate();
    return <div className={style['solution-list-item']} onClick={() => {
        navigator(`/portfolio/${portfolioId}`);
    }}>
        <div className={style['container']}>
            <div className={style['left']}><Avatar className={style['avatar']}
                                                   src={url}/>
            </div>
            <div className={style['right']}>
                <div className={style['title']}>{title}</div>
                <div className={style['description']}>{description}</div>
            </div>
        </div>
    </div>;
}

export default SolutionListItem;