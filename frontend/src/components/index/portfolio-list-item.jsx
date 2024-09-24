import { useNavigate } from "react-router-dom";

import Rating from "@mui/material/Rating";

import style from "../../styles/portfolio-list-item.module.scss";

function PortfolioListItem(props) {
  const { avgrate, title, thumbnail, companyname, description, portfolioid } =
    props;
  const navigator = useNavigate();
  return (
    <li
      className={style["portfolio-list-item"]}
      onClick={() => {
        navigator(`/portfolio/${portfolioid}`);
      }}
    >
      <div className={style["top"]}>
        <img
          className={style["thumbnail"]}
          src={thumbnail || `https://picsum.photos/seed/${Date.now()}/1200/800`}
          alt="portfolio thumbnail"
        />
      </div>
      <div className={style["bottom"]}>
        <div className={style["name"]}>{companyname || ""}</div>
        <div className={style["info"]}>
          <div className={style["rating"]}>
            <Rating readOnly defaultValue={1} max={1} size="small" />
            {avgrate ? avgrate.toFixed(1) : ""}
          </div>
        </div>
        <div className={style["title"]}>{title || ""}</div>
        <div className={style["description"]}>{description || ""}</div>
      </div>
    </li>
  );
}

export default PortfolioListItem;
