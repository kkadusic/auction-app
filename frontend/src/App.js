import {BrowserRouter as Router} from 'react-router-dom';
import Header from './shared/Header';
import Footer from './shared/Footer';
import MyRoutes from './utilities/MyRoutes';
import MyBreadcrumb from './shared/MyBreadcrumb';
import MyAlert from "./shared/MyAlert";

import './App.css';

const App = () => {

    return (
        <div className="app">
            <Router>
                <Header/>
                <MyBreadcrumb/>
                <MyAlert/>
                <div className="route-container">
                    <MyRoutes/>
                </div>
                <Footer/>
            </Router>
        </div>
    );
}

export default App;
