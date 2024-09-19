import style from '../../styles/company-list-item.module.scss';

function CompanyListItem(props) {
    const {companyName, description, id, url, rank} = props;
    return <li className={style['company-list-item']}>
        <div className={style['rank']}>{rank + 1}</div>
        <div className={style['info']}>
            <span className={style['name']}>{companyName}</span>
        </div>
        {
            url ? <img alt={'company'} src={url}/> : <></>
        }
    </li>;
}

export default CompanyListItem;