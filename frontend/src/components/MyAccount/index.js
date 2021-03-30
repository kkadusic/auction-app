import React, {useEffect, useState} from 'react';
import {useHistory} from 'react-router-dom';
import {Button} from 'react-bootstrap';
import {
    myAccountBidsUrl,
    myAccountSellerUrl,
    myAccountSettingsUrl,
    myAccountUrl,
    myAccountWishlistUrl
} from '../../utilities/AppUrl';
import Profile from '../MyAccountTabs/Profile';
import Seller from '../MyAccountTabs/Seller';
import Bids from '../MyAccountTabs/Bids';
import Wishlist from '../MyAccountTabs/Wishlist';
import Settings from '../MyAccountTabs/Settings';
import PageNotFound from '../PageNotFound';
import {FaGift, FaList, FaUser} from 'react-icons/fa';
import {ImHammer2} from 'react-icons/im';
import {RiSettings5Fill} from 'react-icons/ri';
import {useBreadcrumbContext} from "../../AppContext";

import './myAccount.css';

const MyAccount = () => {
    const history = useHistory();
    const [activeTab, setActiveTab] = useState(0);
    const tabs = [<></>, <Profile/>, <Seller/>, <Bids/>, <Wishlist/>, <Settings/>, <PageNotFound/>];
    const {setBreadcrumb} = useBreadcrumbContext();

    useEffect(() => {
        formBreadcrumb();
        // eslint-disable-next-line
    }, [history.location.pathname])

    const formBreadcrumb = () => {
        const urlElements = history.location.pathname.split("/").slice(1);
        if (urlElements.length === 1) {
            setBreadcrumb("MY ACCOUNT", []);
            setActiveTab(1);
        } else {
            setBreadcrumb("MY ACCOUNT", urlElements.map((el, i) => {
                return {
                    text: el.toUpperCase().split("-").join(" "),
                    href: "/" + urlElements.slice(0, i + 1).join("/")
                }
            }));
            switch (urlElements[1]) {
                case "seller":
                    setActiveTab(2);
                    break;
                case "bids":
                    setActiveTab(3);
                    break;
                case "wishlist":
                    setActiveTab(4);
                    break;
                case "settings":
                    setActiveTab(5);
                    break;
                default:
                    setActiveTab(6);
            }
        }
    }

    const inactiveButton = {
        backgroundColor: '#F0F0F0',
        border: '1px #F0F0F0'
    };

    return (
        <div>
            <div className="account-tabs-container">
                <Button onClick={() => history.push(myAccountUrl)}
                        variant={activeTab === 1 ? "fill-purple" : "fill-gray"} size="lg"
                        style={activeTab !== 1 ? inactiveButton : {boxShadow: 'none'}}>
                    <FaUser style={{marginRight: 5}}/>
                    Profile
                </Button>
                <Button onClick={() => history.push(myAccountSellerUrl)}
                        variant={activeTab === 2 ? "fill-purple" : "fill-yellow"} size="lg"
                        style={activeTab !== 2 ? inactiveButton : {boxShadow: 'none'}}>
                    <FaList style={{marginRight: 5}}/>
                    Seller
                </Button>
                <Button onClick={() => history.push(myAccountBidsUrl)}
                        variant={activeTab === 3 ? "fill-purple" : "fill-gray"} size="lg"
                        style={activeTab !== 3 ? inactiveButton : {boxShadow: 'none'}}>
                    <ImHammer2 style={{marginRight: 5}}/>
                    Bids
                </Button>
                <Button onClick={() => history.push(myAccountWishlistUrl)}
                        variant={activeTab === 4 ? "fill-purple" : "fill-gray"} size="lg"
                        style={activeTab !== 4 ? inactiveButton : {boxShadow: 'none'}}>
                    <FaGift style={{marginRight: 5}}/>
                    Wishlist
                </Button>
                <Button onClick={() => history.push(myAccountSettingsUrl)}
                        variant={activeTab === 5 ? "fill-purple" : "fill-gray"} size="lg"
                        style={activeTab !== 5 ? inactiveButton : {boxShadow: 'none'}}>
                    <RiSettings5Fill style={{fontSize: 20, marginRight: 5}}/>
                    Settings
                </Button>
            </div>
            {tabs[activeTab]}
        </div>
    );
}

export default MyAccount;
