import { lazy, Suspense } from "react";
import { createBrowserRouter } from "react-router-dom";
import SearchList from "../pages/searchlist";
import QuotationRequest from "../pages/quotationRequest";
import QuotationRequestUserList from "../pages/quotationRequestUserList";
import QuotationRequestSellerList from "../pages/quotationRequest/quotationRequestList.jsx";

const Index = lazy(() => import("../pages/index"));
const SignIn = lazy(() => import("../pages/signin"));
const SignUp = lazy(() => import("../pages/signup"));
const Mypage = lazy(() => import("../pages/mypage"));

const root = createBrowserRouter([
  {
    path: "/",
    element: (
      <Suspense>
        <Index />
      </Suspense>
    ),
  },
  {
    path: "/login",
    element: (
      <Suspense>
        <SignIn />
      </Suspense>
    ),
  },
  {
    path: "/signup",
    element: (
      <Suspense>
        <SignUp />
      </Suspense>
    ),
  },
  {
    path: "/mypage",
    element: (
      <Suspense>
        <Mypage />
      </Suspense>
    ),
  },
  {
    path: "/search",
    element: (
      <Suspense>
        <SearchList />
      </Suspense>
    ),
  },
  {
    path: "/quotationRequest",
    element: (
      <Suspense>
        <QuotationRequest />
      </Suspense>
    ),
  },
  {
    path: "/qr/userList",
    element: (
      <Suspense>
        <QuotationRequestUserList />
      </Suspense>
    ),
  },
  {
    path: "/qr/sellerList",
    element: (
      <Suspense>
        <QuotationRequestSellerList />
      </Suspense>
    ),
  },
]);

export default root;
