import {useState} from 'react';
import {BrowserRouter as Router, Link} from 'react-router-dom';
import {Alert, Breadcrumb} from 'react-bootstrap';
import axios from 'axios';

import './App.css';

import Header from './components/Header';
import Footer from './components/Footer';
import MyRoutes from './utilities/MyRoutes';

const App = () => {

    const [loggedInState, setLoggedInState] = useState(null);
    const [alertVisible, setAlertVisible] = useState(false);
    const [variant, setVariant] = useState(null);
    const [message, setMessage] = useState(null);
    const [breadcrumbItems, setBreadcrumbItems] = useState([]);
    const [breadcrumbTitle, setBreadcrumbTitle] = useState(null);

    const handleError = (error) => {
        showMessage("warning", error.response.data.message);
        return Promise.reject(error);
    }

    axios.interceptors.response.use((response) => response, handleError);

    const showMessage = (variant, message) => {
        setVariant(variant);
        setMessage(message);
        setAlertVisible(true);
        setTimeout(() => {
            setAlertVisible(false);
        }, 3000);
    }

    const setBreadcrumb = (title, items) => {
        setBreadcrumbTitle(title);
        setBreadcrumbItems(items);
    }

    const changeLoggedInState = () => {
        if (loggedInState === null) {
            setLoggedInState(false);
            return;
        }
        setLoggedInState(!loggedInState);
    }

    return (
        <div className="app">
            <Router>
                <Header loggedInState={loggedInState}/>
                <Breadcrumb style={breadcrumbTitle === null ? {display: 'none'} : null}>
                    <div className="breadcrumb-title">
                        {breadcrumbTitle}
                    </div>
                    {breadcrumbItems.map((item, i, {length}) => (
                        <Breadcrumb.Item active key={item.text}>
                            {length - 1 === i ? (
                                <div style={{marginLeft: '19px'}}>
                                    {item.text}
                                </div>
                            ) : (
                                <Link className="black-nav-link" to={item.href}>
                                    {item.text}
                                </Link>
                            )}
                        </Breadcrumb.Item>
                    ))}
                </Breadcrumb>
                <div style={alertVisible && breadcrumbTitle === null ? {marginTop: 40, marginBottom: '-1rem'} : null}>
                    <Alert dismissible onClose={() => setAlertVisible(false)} transition={false} show={alertVisible}
                           variant={variant}>
                        {message}
                    </Alert>
                </div>
                <div className="route-container">
                    <MyRoutes changeLoggedInState={changeLoggedInState} setBreadcrumb={setBreadcrumb}
                              showMessage={showMessage}/>
                </div>
                <Footer/>
            </Router>
        </div>
    );
}

export default App;
