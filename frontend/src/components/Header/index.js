import {useEffect, useState} from 'react';
import {SiFacebook, SiTwitter, SiInstagram} from 'react-icons/si';
import {FaGooglePlus} from 'react-icons/fa';
import {GrFormSearch} from 'react-icons/gr';
import {RiAuctionFill} from 'react-icons/ri';
import {FormControl, Image, Nav, Navbar, ListGroup} from 'react-bootstrap';
import {Link, NavLink, useHistory} from 'react-router-dom';
import {removeSession, getUser} from '../../utilities/Common';
import {
    loginUrl, registerUrl, forgotPasswordUrl, resetPasswordUrl, myAccountUrl, myAccountSellerUrl, myAccountBidsUrl,
    myAccountSettingsUrl, myAccountWishlistUrl, homeUrl, productUrl
} from '../../utilities/AppUrl';
import {useUserContext} from "../../AppContext";
import * as qs from 'query-string';

import './header.css';

const Header = () => {

    const user = getUser();
    const history = useHistory();
    const [searchInput, setSearchInput] = useState("");
    const {loggedIn, setLoggedIn} = useUserContext();
    const [accountListVisible, setAccountListVisible] = useState(false);

    const handleLogout = () => {
        setLoggedIn(false);
        removeSession();
    };

    useEffect(() => {
        const urlParams = qs.parse(history.location.search);
        if (searchInput !== urlParams.query)
            setSearchInput(urlParams.query);
        // eslint-disable-next-line
    }, [history.location.search])

    const handleSearch = async () => {
        const urlParams = {
            query: searchInput,
            sort: "default"
        };
        history.push({
            pathname: '/shop',
            search: qs.stringify(urlParams)
        });
    }

    return (
        <>
            <div className="topbar-container">
                <div className="social-media-container">
                    <a className="social-media-link" href="https://www.facebook.com/AtlantBH" target="_blank"
                       rel="noopener noreferrer">
                        <SiFacebook/>
                    </a>
                    <a className="social-media-link" href="https://www.instagram.com/atlantbh" target="_blank"
                       rel="noopener noreferrer">
                        <SiInstagram/>
                    </a>
                    <a className="social-media-link" href="https://twitter.com/atlantbh" target="_blank"
                       rel="noopener noreferrer">
                        <SiTwitter/>
                    </a>
                    <a className="social-media-link" href="https://accounts.google.com/" target="_blank"
                       rel="noopener noreferrer">
                        <FaGooglePlus/>
                    </a>
                </div>
                <Nav className="topbar-nav-links">
                    {loggedIn && user !== null ?
                        (
                            <>
                                <Image style={{marginRight: '0.5rem'}} roundedCircle className="avatar-image-tiny"
                                       src={user.imageUrl}
                                       onClick={() => history.push(myAccountUrl)}/>
                                <div className="topbar-username"
                                     onClick={() => history.push(myAccountUrl)}>
                                    {user.firstName + ' ' + user.lastName}
                                </div>
                                |
                                <Link style={{paddingRight: 0, paddingLeft: 5}} className="white-nav-link nav-link"
                                      onClick={handleLogout} to={homeUrl}>
                                    Log out
                                </Link>
                            </>
                        ) :
                        (
                            <>
                                <Link className="white-nav-link nav-link" to="/login">
                                    Login
                                </Link>
                                <Navbar.Text style={{color: '#9B9B9B'}}>
                                    or
                                </Navbar.Text>
                                <Link style={{paddingRight: 0}} className="white-nav-link nav-link" to="/register">
                                    Create an Account
                                </Link>
                            </>
                        )}
                </Nav>
            </div>

            <div className="navbar-container">
                <Link className="navbar-brand" to="/">
                    <RiAuctionFill className="navbar-auction-icon"/>
                    AUCTION
                </Link>
                <div className="navbar-search">
                    <FormControl
                        value={searchInput || ""}
                        onChange={(e) => setSearchInput(e.target.value)}
                        size="xl-18"
                        type="text"
                        placeholder="Try enter: Shoes"
                        maxLength="255"
                        onKeyUp={(e) => e.key === 'Enter' ? handleSearch() : null}
                    />
                    <GrFormSearch className="navbar-search-icon" onClick={handleSearch}/>
                </div>
                <Nav style={{position: 'relative'}}>
                    <NavLink
                        isActive={(match, location) => (location.pathname === loginUrl || location.pathname === registerUrl ||
                            location.pathname === forgotPasswordUrl || location.pathname === resetPasswordUrl)} exact
                        className="black-nav-link nav-link" activeClassName="black-active-nav-link" to="/">
                        HOME
                    </NavLink>
                    <NavLink className="black-nav-link nav-link" activeClassName="black-active-nav-link" to="/shop">
                        SHOP
                    </NavLink>
                    <NavLink
                        style={{paddingTop: 28, paddingBottom: 28, paddingRight: loggedIn ? '1rem' : 0}}
                        className={"black-nav-link nav-link"}
                        activeClassName="black-active-nav-link"
                        to={myAccountUrl}
                        onMouseEnter={() => setAccountListVisible(true)}
                        onMouseLeave={() => setAccountListVisible(false)}
                    >
                        MY ACCOUNT
                    </NavLink>
                    {accountListVisible ?
                        <ListGroup
                            className="account-list"
                            variant="filter"
                            onMouseEnter={() => setAccountListVisible(true)}
                            onMouseLeave={() => setAccountListVisible(false)}
                        >
                            <ListGroup.Item onClick={() => history.push(myAccountUrl)}>Profile</ListGroup.Item>
                            <ListGroup.Item onClick={() => history.push(myAccountSellerUrl)}>Become
                                Seller</ListGroup.Item>
                            <ListGroup.Item onClick={() => history.push(myAccountBidsUrl)}>Your Bids</ListGroup.Item>
                            <ListGroup.Item onClick={() => history.push(myAccountWishlistUrl)}>Wishlist</ListGroup.Item>
                            <ListGroup.Item onClick={() => history.push(myAccountSettingsUrl)}>Settings</ListGroup.Item>
                        </ListGroup> : null}
                </Nav>
            </div>
        </>
    );
}

export default Header;
