import style from '../../styles/company-list-item.module.scss';

function CompanyListItem(props) {
    const {value} = props;
    return <li className={style['company-list-item']}>
        <div className={style['rank']}>{value}</div>
        <img alt={'company'} src={`https://picsum.photos/seed/${Date.now() + value}/1200/800`}/>
    </li>;
}

export default CompanyListItem;