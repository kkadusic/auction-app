import React from 'react';
import {Route, Switch} from 'react-router-dom';
import PrivateRoute from './PrivateRoute';

import Login from '../components/Login';
import Register from '../components/Register';
import Home from '../components/Home';
import Shop from '../components/Shop';
import MyAccount from '../components/MyAccount';

const UserRoutes = () => {
    return (
        <Switch>
            <Route exact path="/" component={Home}/>
            <Route path="/login" component={Login}/>
            <Route path="/register" component={Register}/>
            <Route path="/shop" component={Shop}/>
            <PrivateRoute path="/my-account" component={MyAccount}/>
        </Switch>
    );
}

export default UserRoutes;
