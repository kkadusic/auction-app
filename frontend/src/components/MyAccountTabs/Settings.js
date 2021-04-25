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
