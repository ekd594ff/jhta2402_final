import React from "react";
import Header from "../../components/common/header.jsx";
import Footer from "../../components/common/footer.jsx";
import QuotationRequestListComponent from "./quotationRequestListComponent.jsx";

import style from "../../styles/quotationRequestList.module.scss";

function QuotationRequestList() {
  return (
    <>
      <Header />
      <main className={style["quotationRequestList"]}>
        <div className={style["container"]}>
          <QuotationRequestListComponent expand />
        </div>
      </main>
      <Footer />
    </>
  );
}

export default QuotationRequestList;
