import {lazy, Suspense} from "react";
import {createBrowserRouter} from "react-router-dom";

const Index = lazy(() => import("../pages/index"));
const SignIn = lazy(() => import('../pages/login/signin.jsx'));

const root = createBrowserRouter([
    {
        path: '/',
        element: <Suspense><Index/></Suspense>
    },
    {
        path: '/login',
        element: <Suspense><SignIn/></Suspense>
    },
])

export default root;