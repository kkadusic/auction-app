import {useEffect, useState} from 'react';
import {SiFacebook, SiTwitter, SiInstagram} from 'react-icons/si';
import {FaGooglePlus} from 'react-icons/fa';
import {GrFormSearch} from 'react-icons/gr';
import {RiAuctionFill} from 'react-icons/ri';
import {FormControl, Image, Nav, Navbar} from 'react-bootstrap';
import {Link, NavLink, useHistory} from 'react-router-dom';
import {removeSession, getUser} from '../../utilities/Common';
import {loginUrl, registerUrl, forgotPasswordUrl, resetPasswordUrl} from '../../utilities/AppUrl';
import {useUserContext} from "../../AppContext";
import * as qs from 'query-string';

import './header.css';

const Header = () => {

    const user = getUser();
    const history = useHistory();
    const [searchInput, setSearchInput] = useState("");
    const {loggedIn, setLoggedIn} = useUserContext();

    const handleLogout = () => {
        setLoggedIn(false);
        removeSession();
    };

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
                <Nav>
                    {loggedIn ?
                        (
                            <>
                                <Image style={{marginRight: '0.5rem'}} roundedCircle className="avatar-image-tiny"
                                       src={user.imageUrl}/>
                                {user.firstName + ' ' + user.lastName + ' |'}
                                <Link style={{paddingRight: 0, paddingLeft: 5}} className="white-nav-link nav-link"
                                      onClick={handleLogout} to="/">
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
                        value={searchInput}
                        onChange={(e) => setSearchInput(e.target.value)}
                        size="xl-18"
                        type="text"
                        placeholder="Try enter: Shoes"
                        onKeyUp={(e) => e.key === 'Enter' ? handleSearch() : null}
                    />
                    <GrFormSearch className="navbar-search-icon" onClick={handleSearch}/>
                </div>
                <Nav>
                    <NavLink
                        isActive={(match, location) => (location.pathname === loginUrl || location.pathname === registerUrl ||
                            location.pathname === forgotPasswordUrl || location.pathname === resetPasswordUrl)} exact
                        className="black-nav-link nav-link" activeClassName="black-active-nav-link" to="/">
                        HOME
                    </NavLink>
                    <NavLink className="black-nav-link nav-link" activeClassName="black-active-nav-link" to="/shop">
                        SHOP
                    </NavLink>
                    <NavLink style={{paddingRight: 0}} className="black-nav-link nav-link"
                             activeClassName="black-active-nav-link" to="/my-account">
                        MY ACCOUNT
                    </NavLink>
                </Nav>
            </div>
        </>
    );
}

export default Header;
