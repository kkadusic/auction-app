import {Route, Redirect} from 'react-router-dom';
import {getToken} from './Common';

const PrivateRoute = ({component: Component, ...rest}) => {
    return (
        // Show the component only when the user is logged in
        // Otherwise, redirect the user to /login page
        <Route {...rest} render={props => (
            getToken() ?
                <Component {...props} />
                : <Redirect to={{pathname: '/login', state: {from: props.location}}}/>
        )}
        />
    )
}

export default PrivateRoute;
