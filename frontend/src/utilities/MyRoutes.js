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

const UserRoutes = (props) => {
    return (
        <Switch>
            <Route exact path="/" render={() => <LandingPage {...props} />}/>
            <Route path="/all" render={() => <AllCategories {...props} />} />
            <Route path="/shop/*/*/:id" render={() => <ItemPage {...props} />}/>
            <Route path="/shop*" render={() => <Shop {...props} />}/>
            <Route path="/about" render={() => <AboutUs {...props} />}/>
            <Route path="/privacy" render={() => <PrivacyPolicy {...props} />}/>
            <Route path="/terms" render={() => <TermsConditions {...props} />}/>
            <Route path="/login" render={() => <Login {...props} />}/>
            <Route path="/register" render={() => <Register {...props} />}/>
            <Route path="/forgot_password" render={() => <ForgotPassword {...props} />} />
            <Route path="/reset_password" render={() => <ResetPassword {...props} />} />
            <PrivateRoute path="/my-account" component={MyAccount}/>
            <Route component={PageNotFound}/>
        </Switch>
    );
}

export default UserRoutes;
