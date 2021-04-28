import React from 'react';
import {Route, Switch} from 'react-router-dom';
import PrivateRoute from './PrivateRoute';
import LandingPage from '../pages/LandingPage';
import Shop from '../pages/Shop';
import Login from '../pages/Login';
import Register from '../pages/Register';
import MyAccount from '../pages/MyAccount';
import PageNotFound from "../pages/PageNotFound";
import AboutUs from "../pages/AboutUs";
import TermsConditions from "../pages/TermsConditions";
import PrivacyPolicy from "../pages/PrivacyPolicy";
import ItemPage from "../pages/ItemPage";
import AllCategories from "../pages/AllCategories";
import ForgotPassword from "../pages/ForgotPassword";
import ResetPassword from "../pages/ResetPassword";
import Sell from "../pages/Sell";
import Payment from "../pages/Payment";

const UserRoutes = () => {
    return (
        <Switch>
            <Route exact path="/" component={LandingPage}/>
            <Route path="/all" component={AllCategories}/>
            <Route path="/shop/*/*/:id" component={ItemPage}/>
            <Route path="/shop*" component={Shop}/>
            <Route path="/about" component={AboutUs}/>
            <Route path="/privacy" component={PrivacyPolicy}/>
            <Route path="/terms" component={TermsConditions}/>
            <Route path="/login" component={Login}/>
            <Route path="/register" component={Register}/>
            <Route path="/forgot_password" component={ForgotPassword}/>
            <Route path="/reset_password" component={ResetPassword}/>
            <PrivateRoute path="/my-account/bids/payment" component={Payment}/>
            <PrivateRoute path="/my-account/seller/sell" component={Sell}/>
            <PrivateRoute path="/my-account" component={MyAccount}/>
            <Route component={PageNotFound}/>
        </Switch>
    );
}

export default UserRoutes;
