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
import Profile from '../../components/MyAccountTabs/Profile';
import Seller from '../../components/MyAccountTabs/Seller';
import Bids from '../../components/MyAccountTabs/Bids';
import Wishlist from '../../components/MyAccountTabs/Wishlist';
import Settings from '../../components/MyAccountTabs/Settings';
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

    const createTab = (number, name, url, icon) => {
        return (
            <Button onClick={() => history.push(url)}
                    variant={activeTab === number ? "fill-purple" : "fill-gray"} size="lg"
                    style={activeTab !== number ? inactiveButton : {boxShadow: 'none'}}>
                {icon} &nbsp;
                {name}
            </Button>
        );
    }

    return (
        <div>
            <div className="account-tabs-container">
                {createTab(1, "Profile", myAccountUrl, <FaUser/>)}
                {createTab(2, "Seller", myAccountSellerUrl, <FaList/>)}
                {createTab(3, "Bids", myAccountBidsUrl, <ImHammer2/>)}
                {createTab(4, "Wishlist", myAccountWishlistUrl, <FaGift/>)}
                {createTab(5, "Settings", myAccountSettingsUrl, <RiSettings5Fill/>)}
            </div>
            {tabs[activeTab]}
        </div>
    );
}

export default MyAccount;
