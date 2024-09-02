import Avatar from "@mui/material/Avatar";

import style from "../../styles/solution-list-item.module.scss";

function SolutionListItem(props) {
    const {value} = props;
    return <div className={style['solution-list-item']}>
        <div className={style['container']}>
            <div className={style['left']}><Avatar className={style['avatar']}
                                                   src={`https://picsum.photos/seed/${Date.now() + value}/600/400`}/>
            </div>
            <div className={style['right']}>
                <div className={style['title']}>{`솔루션 이름_${value}`}</div>
                <div className={style['description']}>솔루션 설명</div>
            </div>
        </div>
    </div>;
}

export default SolutionListItem;