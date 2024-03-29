import {Link} from 'react-router-dom';
import {SiFacebook, SiTwitter, SiInstagram} from "react-icons/si";
import {FaGooglePlus} from 'react-icons/fa';
import {IoIosArrowForward} from "react-icons/io";
import {Button, Form} from 'react-bootstrap';

import './footer.css';

const Footer = () => {
    return (
        <div className="footer-container">
            <div className="footer-content">
                <div className="footer-content-title">
                    AUCTION
                </div>
                <Link className="white-nav-link" to="/about">
                    About Us
                </Link>
                <Link className="white-nav-link" to="/terms">
                    Terms and Conditions
                </Link>
                <Link className="white-nav-link" to="/privacy">
                    Privacy and Policy
                </Link>
            </div>

            <div className="footer-content">
                <div className="footer-content-title">
                    GET IN TOUCH
                </div>
                Call Us at +123 797-567-2535
                <a className="white-nav-link" href="mailto:support@auction.com" target="_blank"
                   rel="noopener noreferrer">
                    auction.abh@gmail.com
                </a>
                <div className="social-media-container">
                    <a className="social-media-link" rel="noopener noreferrer" href="https://www.facebook.com/AtlantBH"
                       target="_blank">
                        <SiFacebook/>
                    </a>
                    <a className="social-media-link" rel="noopener noreferrer" href="https://www.instagram.com/atlantbh"
                       target="_blank">
                        <SiInstagram/>
                    </a>
                    <a className="social-media-link" rel="noopener noreferrer" href="https://twitter.com/atlantbh"
                       target="_blank">
                        <SiTwitter/>
                    </a>
                    <a className="social-media-link" rel="noopener noreferrer" href="https://accounts.google.com"
                       target="_blank">
                        <FaGooglePlus/>
                    </a>
                </div>
            </div>

            <div className="footer-content">
                <div className="footer-content-title">
                    NEWSLETTER
                </div>
                <p>
                    Enter your email address and get notified
                    <br/>
                    about new products. We hate spam!
                </p>
                <div className="footer-newsletter-bottom">
                    <Form.Control className="footer-email-input" size="xl-16" type="text"
                                  style={{backgroundColor: "#3B3B3B", border: "none"}}
                                  placeholder="Your Email address"/>
                    <Button style={{width: 116}} size="xl" variant="transparent-white">
                        GO
                        <IoIosArrowForward className="footer-go-button-arrow"/>
                    </Button>
                </div>
            </div>
        </div>
    );
}

export default Footer;
