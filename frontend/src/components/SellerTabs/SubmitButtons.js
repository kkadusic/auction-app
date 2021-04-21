import React from 'react';
import {Button} from 'react-bootstrap';
import {IoIosArrowBack, IoIosArrowForward} from 'react-icons/io';

const SubmitButtons = ({onBack, lastTab, loading}) => {
    return (
        <div style={{display: 'flex', justifyContent: 'space-between'}}>
            <Button
                style={{width: 183}}
                size="xxl"
                variant={lastTab ? "fill-purple-shadow" : "transparent-black-shadow"}
                onClick={onBack}
            >
                <IoIosArrowBack style={{fontSize: 24, marginLeft: -5, marginRight: 5}}/>
                BACK
            </Button>
            <Button
                style={{width: 183}}
                type="submit"
                size="xxl"
                variant={lastTab ? "fill-purple-shadow" : "transparent-black-shadow"}
                disabled={loading}
            >
                {lastTab ? "DONE" :
                    <>
                        NEXT
                        <IoIosArrowForward style={{fontSize: 24, marginRight: -5, marginLeft: 5}}/>
                    </>
                }
            </Button>
        </div>
    );
}

export default SubmitButtons;
