import React, {useEffect, useState} from 'react';
import {useHistory} from 'react-router-dom';
import {Button, Form} from 'react-bootstrap';
import {Formik} from 'formik';
import {loginUrl} from '../../utilities/AppUrl';
import {forgotPassword} from "../../utilities/ServerCall";
import * as yup from 'yup';

import './forgotPassword.css';

const ForgotPassword = ({setBreadcrumb}) => {

    const history = useHistory();
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setBreadcrumb("FORGOT PASSWORD", []);
        // eslint-disable-next-line
    }, [])

    const schema = yup.object().shape({
        email: yup.string()
            .email("*Email must be valid")
            .max(100, "*Email can't be longer than 100 characters")
            .required("*Email is required")
    });

    const handleSubmit = async (data) => {
        setLoading(true);
        try {
            const message = await forgotPassword(data.email);
            setLoading(false);
            history.push({
                pathname: loginUrl,
                state: {message}
            });
        } catch (e) {
            setLoading(false);
        }
    }

    return (
        <>
            <div className="forgot-container">
                <div className="forgot-title">
                    FORGOT PASSWORD
                </div>
                <div className="forgot-content">
                    <Formik
                        validationSchema={schema}
                        initialValues={{email: ""}}
                        onSubmit={handleSubmit}
                    >
                        {({
                              handleSubmit,
                              handleChange,
                              touched,
                              errors,
                          }) => (
                            <Form noValidate className="forgot-form" onSubmit={handleSubmit}>
                                <Form.Text style={{textAlign: 'left', color: 'var(--text-secondary)', margin: '20px 0'}}
                                           className="font-18">
                                    Lost your password? Please enter your email address. You will receive a link to
                                    create a new password via email.
                                </Form.Text>

                                <Form.Group style={{marginTop: 40}}>
                                    <Form.Label>Enter Email</Form.Label>
                                    <Form.Control
                                        className="form-control-gray"
                                        size="xl-18"
                                        type="email"
                                        name="email"
                                        maxLength={100}
                                        defaultValue=""
                                        onChange={handleChange}
                                        isInvalid={touched.email && errors.email}
                                    />
                                    <Form.Control.Feedback type="invalid">
                                        {errors.email}
                                    </Form.Control.Feedback>
                                </Form.Group>

                                <Button
                                    size="xxl"
                                    style={{margin: '60px 0'}}
                                    block
                                    disabled={loading}
                                    type="submit"
                                    variant="transparent-black-shadow"
                                >
                                    SUBMIT
                                </Button>
                            </Form>
                        )}
                    </Formik>
                </div>
            </div>
        </>
    );
}

export default ForgotPassword;
