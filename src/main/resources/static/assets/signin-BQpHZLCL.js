import{u as p,r as o,j as e}from"./index-PHBf0-X0.js";import{a as u}from"./axios-B4uVmeYG.js";function g(){const n=p(),[t,r]=o.useState(""),[s,i]=o.useState(""),l=()=>{u.post("http://localhost:8080/api/login",{email:t,password:s},{withCredentials:!0}).then(a=>{alert("login Success"),n("/")}).catch(a=>alert(a))};return e.jsxs(e.Fragment,{children:[e.jsx("input",{value:t,onChange:a=>r(a.target.value),type:"email",name:"email",placeholder:"Email"}),e.jsx("input",{value:s,onChange:a=>i(a.target.value),type:"password",name:"password",placeholder:"Password"}),e.jsx("button",{onClick:l,children:"Login"})]})}export{g as default};
