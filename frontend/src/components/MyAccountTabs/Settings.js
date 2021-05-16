import React, {useState} from 'react';
import {Button, Form} from 'react-bootstrap';
import DeactivateAccount from "../Modals/DeactivateAccount";
import {getUser, setUser} from "../../utilities/Common";
import {updateNotifications} from "../../utilities/ServerCall";

import './myAccountTabs.css';

const Settings = () => {

    const user = getUser();
    const [showModal, setShowModal] = useState(false);
    const [emailLoading, setEmailLoading] = useState(false);
    const [pushLoading, setPushLoading] = useState(false);

    const handleEmailClick = async (emailNotify) => {
        setEmailLoading(true);
        try {
            await updateNotifications(emailNotify, user.pushNotify);
            user.emailNotify = emailNotify;
        } catch (e) {
            setEmailLoading(false);
            return;
        }
        setUser(user);
        setEmailLoading(false);
    }

    const handlePushClick = async (pushNotify) => {
        setPushLoading(true);
        try {
            await updateNotifications(user.emailNotify, pushNotify);
            user.pushNotify = pushNotify;
        } catch (e) {
            setPushLoading(false);
            return;
        }
        setUser(user);
        setPushLoading(false);
    }

    return (
        <div className="settings-container">
            <div className="settings-tab">
                <div className="settings-tab-title">
                    Policy and Community
                </div>
                <div className="settings-tab-content">
                    Receive updates on bids and seller's offers. <br/>
                    Stay informed through:
                    <Form.Check
                        custom
                        type="checkbox"
                        id="custom-email-checkbox"
                        label="Email"
                        style={{marginTop: 22}}
                        defaultChecked={user.emailNotify}
                        disabled={emailLoading}
                        onChange={e => handleEmailClick(e.target.checked)}
                    />
                    <Form.Check
                        custom
                        type="checkbox"
                        id="custom-push-checkbox"
                        label="Push Notifications"
                        style={{marginTop: 22}}
                        defaultChecked={user.pushNotify}
                        disabled={pushLoading}
                        onChange={e => handlePushClick(e.target.checked)}
                    />
                </div>
            </div>

            <div className="settings-tab">
                <div className="settings-tab-title">
                    Contact Information
                </div>
                <div className="settings-tab-content">
                    This information can be edited on your profile.
                    <div style={{marginTop: 30}}>
                        Email:
                        <span style={{color: '#8367D8', marginLeft: 23, wordWrap: 'anywhere'}}>
                            <a className="purple-nav-link" target="_blank" rel="noopener noreferrer"
                               href={"mailto:" + user.email}>{user.email}</a>
                        </span>
                    </div>
                    <div style={{marginTop: 30}}>
                        Phone:
                        <span style={{color: '#8367D8', marginLeft: 16, wordWrap: 'anywhere'}}>
                            <a className="purple-nav-link"
                               href={"tel:" + user.phoneNumber}>
                                {user.phoneNumber}
                            </a>
                        </span>
                    </div>
                </div>
            </div>

            <div className="settings-tab">
                <div className="settings-tab-title">
                    Account
                </div>
                <div className="settings-tab-content">
                    Do you want to deactivate your account?
                    <Button
                        size="xxl"
                        id="btn-deactivate-account"
                        variant="transparent-black-shadow-disabled"
                        onClick={() => setShowModal(true)}
                    >
                        DEACTIVATE
                    </Button>
                    <DeactivateAccount showModal={showModal} setShowModal={setShowModal}/>
                </div>
            </div>
        </div>
    );
}

export default Settings;
