import React from 'react';

function Register(props) {
    return (
        <main>
            <div className="container">
                <h1 className="text-center">포트폴리오 등록</h1>
                <p className="lead">아래의 양식을 작성해주세요.</p>

                <div className="mb-3">
                    <label htmlFor="exampleFormControlInput1" className="form-label">포트폴리오 제목</label>
                    <input type="email" className="form-control" id="exampleFormControlInput1"
                           placeholder="(예) [삼강] 목공예품 디자인 전문업체"/>
                </div>
                <div className="mb-3">
                    <label htmlFor="exampleFormControlTextarea1" className="form-label">포트폴리오 내용</label>
                    <textarea className="form-control" id="exampleFormControlTextarea1" rows="3"></textarea>
                </div>
                <div className="mb-3">
                    <label htmlFor="formFileMultiple" className="form-label">포트폴리오 이미지 삽입</label>
                    <input className="form-control" type="file" id="formFileMultiple" multiple/>
                </div>

                <label htmlFor="formFileMultiple" className="form-label">등록사진 확인</label>
                <div className="input-group mb-3">
                    <button className="btn btn-outline-secondary" type="button">사진</button>
                    <button className="btn btn-outline-secondary" type="button">사진</button>
                    ...(등록시 버튼 계속 생성)
                    <input type="text" className="form-control" placeholder=""
                           aria-label="Example text with two button addons"/>
                </div>


                <div className="mb-3">
                    <label htmlFor="exampleFormControlInput1" className="form-label">회사정보</label>
                    <div className="row mb-3">
                        <label htmlFor="" className="col-sm-2 col-form-label col-form-label-sm">업체명</label>
                        <div className="col-sm-10">
                            <input type="text" className="form-control form-control-sm" id="" placeholder="연락처"/>
                        </div>
                    </div>
                    <div className="row mb-3">
                        <label htmlFor="" className="col-sm-2 col-form-label col-form-label-sm">이메일</label>
                        <div className="col-sm-10">
                            <input type="text" className="form-control form-control-sm" id="" placeholder="이메일"/>
                        </div>
                    </div>
                </div>


                <button type="button" className="btn btn-primary">등록</button>
                <button type="button" className="btn btn-secondary">임시저장</button>
                <button type="button" className="btn btn-warning">포트폴리오 미리보기</button>
            </div>
        </main>
    );
}

export default Register;