import React, {useState} from 'react';
import {Button} from 'react-bootstrap';
import DeactivateAccount from "../Modals/DeactivateAccount";

import './myAccountTabs.css';

const Settings = () => {

    const [showModal, setShowModal] = useState(false);

    return (
        <div className="settings-container">
            <div className="settings-tab">
                <div className="settings-tab-title">
                    Account
                </div>
                <div className="settings-tab-content">
                    Do you want to deactivate your account?
                    <Button
                        size="xxl"
                        style={{width: 243, marginTop: 38, border: "3px solid #E3E3E3", boxShadow: "3px 3px 0 0 #ECECEC"}}
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
