import React from "react";
import { useAppDispatch, useAppSelector } from "../hooks";
import { AuthState, selectAuth } from "../authSlice";
import { Outlet, useNavigate } from "react-router-dom";
import { UseToastOptions, useToast } from "@chakra-ui/react";
import { UserRoles } from "../../../assets/models";
import { API_URL } from "../../../assets/url";

interface ProtectedRouteProps {
  children: React.ReactNode;
  redirectIfAuthFails?: string;
  toastOptions: UseToastOptions;
  roles?: UserRoles[];
}

function ProtectedRoute(props: ProtectedRouteProps) {
  const { children, redirectIfAuthFails = "/login", toastOptions } = props;
  const navigate = useNavigate();
  const toast = useToast();
  // check if user is logged in
  const authState = useAppSelector(selectAuth);
  console.log("I am getting the user: ", authState);
  // retrieve user
  // if user not found, redirect to login with a toast
  // that describes the situation

  if (shouldProceedToPage(authState)) return children;
  React.useEffect(() => {
    navigate(redirectIfAuthFails);
    toast(toastOptions);
  }, []);

  return <></>;
}

function shouldProceedToPage(state: AuthState) {
  return state.accessToken && state.refreshToken;
}

export default ProtectedRoute;
