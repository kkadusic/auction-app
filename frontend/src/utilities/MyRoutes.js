import React from 'react';
import {Route, Switch} from 'react-router-dom';
import PrivateRoute from './PrivateRoute';
import LandingPage from '../components/LandingPage';
import Shop from '../components/Shop';
import Login from '../components/Login';
import Register from '../components/Register';
import MyAccount from '../components/MyAccount';
import PageNotFound from "../components/PageNotFound";
import AboutUs from "../components/AboutUs";
import TermsConditions from "../components/TermsConditions";
import PrivacyPolicy from "../components/PrivacyPolicy";
import ItemPage from "../components/ItemPage";
import AllCategories from "../components/AllCategories";
import ForgotPassword from "../components/ForgotPassword";
import ResetPassword from "../components/ResetPassword";

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
            <PrivateRoute path="/my-account" component={MyAccount}/>
            <Route component={PageNotFound}/>
        </Switch>
    );
}

export default UserRoutes;
