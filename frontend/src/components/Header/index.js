import {SiFacebook, SiTwitter, SiInstagram} from 'react-icons/si';
import {FaGooglePlus} from 'react-icons/fa';
import {GrFormSearch} from 'react-icons/gr';
import {RiAuctionFill} from 'react-icons/ri';
import {FormControl, Nav, Navbar} from 'react-bootstrap';
import {Link, NavLink} from 'react-router-dom';

import './header.css';

const Header = () => {
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
                    <Link className="white-nav-link nav-link" to="/login">
                        Login
                    </Link>
                    <Navbar.Text style={{color: '#9B9B9B'}}>
                        or
                    </Navbar.Text>
                    <Link className="white-nav-link nav-link" to="/register">
                        Create an Account
                    </Link>
                </Nav>
            </div>

            <div className="navbar-container">
                <Link className="navbar-brand" to="/">
                    <RiAuctionFill className="navbar-auction-icon"/>
                    AUCTION
                </Link>
                <div className="navbar-search">
                    <FormControl size="xl-18" type="text" placeholder="Try enter: Shoes"/>
                    <GrFormSearch className="navbar-search-icon"/>
                </div>
                <Nav>
                    <NavLink className="black-nav-link nav-link" activeClassName="black-active-nav-link" to="/">
                        HOME
                    </NavLink>
                    <NavLink className="black-nav-link nav-link" activeClassName="black-active-nav-link" to="/shop">
                        SHOP
                    </NavLink>
                    <NavLink className="black-nav-link nav-link" activeClassName="black-active-nav-link"
                             to="/my-account">
                        MY ACCOUNT
                    </NavLink>
                </Nav>
            </div>
        </>
    );
}

export default Header;