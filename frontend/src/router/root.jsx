import {lazy, Suspense} from "react";
import {createBrowserRouter} from "react-router-dom";
import SearchList from "../pages/searchlist";

const Index = lazy(() => import("../pages/index"));
const SignIn = lazy(() => import('../pages/signin'));
const SignUp = lazy(() => import('../pages/signup'));
const Mypage = lazy(() => import('../pages/mypage'));

const root = createBrowserRouter([
    {
        path: '/',
        element: <Suspense><Index/></Suspense>
    },
    {
        path: '/login',
        element: <Suspense><SignIn/></Suspense>
    },
    {
        path: '/signup',
        element: <Suspense><SignUp/></Suspense>
    },
    {
        path: '/mypage',
        element: <Suspense><Mypage/></Suspense>
    },
    {
        path: '/search',
        element: <Suspense><SearchList/></Suspense>
    },
])

export default root;