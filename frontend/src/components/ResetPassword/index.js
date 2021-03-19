import React, {useEffect, useState} from 'react';
import {useHistory} from 'react-router-dom';
import {Button, Form} from 'react-bootstrap';
import {Formik} from 'formik';
import {loginUrl, forgotPasswordUrl} from '../../utilities/AppUrl';
import {resetPassword, validResetToken} from "../../utilities/ServerCall";
import * as qs from 'query-string';
import * as yup from 'yup';

import '../ForgotPassword/forgotPassword.css';

const ResetPassword = ({setBreadcrumb, showMessage}) => {
    const history = useHistory();
    const urlParams = qs.parse(history.location.search);
    const [validToken, setValidToken] = useState(false);

    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setBreadcrumb("RESET PASSWORD", []);
        const fetchData = async () => {
            try {
                setValidToken(await validResetToken(urlParams.token));
            } catch (e) {
                showMessage("warning", "Unable to reset password. The reset link is invalid or expired.");
            }
        }
        fetchData();
        // eslint-disable-next-line
    }, []);

    const validationSchema = yup.object().shape({
        password: yup.string()
            .required("*Password is required")
            .matches(
                /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/,
                "Must Contain 8 Characters, One Uppercase, One Lowercase, One Number and one special case Character"
            ),
        confirmPassword: yup.string()
            .required("*Confirm your password")
            .oneOf([yup.ref("password")], "*Passwords must match")
            .matches(
                /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/,
                "Must Contain 8 Characters, One Uppercase, One Lowercase, One Number and one special case Character"
            )
    });

    const handleSubmit = async (data) => {
        setLoading(true);
        try {
            const message = await resetPassword(urlParams.token, data.password);
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
                    RESET PASSWORD
                </div>
                <div className="forgot-content">
                    {validToken ?
                        <Formik
                            validationSchema={validationSchema}
                            initialValues={{password: "", confirmPassword: ""}}
                            onSubmit={handleSubmit}
                        >
                            {({
                                  handleSubmit,
                                  handleChange,
                                  touched,
                                  errors,
                              }) => (
                                <Form noValidate className="forgot-form" onSubmit={handleSubmit}>
                                    <Form.Text
                                        style={{textAlign: 'left', color: 'var(--text-secondary)', margin: '20px 0'}}
                                        className="font-18">
                                        Enter a new password below.
                                    </Form.Text>

                                    <Form.Group style={{marginTop: 40}}>
                                        <Form.Label>Password</Form.Label>
                                        <Form.Control
                                            className="form-control-gray"
                                            size="xl-18"
                                            type="password"
                                            name="password"
                                            maxLength={255}
                                            defaultValue=""
                                            onChange={handleChange}
                                            isInvalid={touched.password && errors.password}
                                        />
                                        <Form.Control.Feedback type="invalid">
                                            {errors.password}
                                        </Form.Control.Feedback>
                                    </Form.Group>

                                    <Form.Group style={{marginTop: 20}}>
                                        <Form.Label>Confirm Password</Form.Label>
                                        <Form.Control
                                            className="form-control-gray"
                                            size="xl-18"
                                            type="password"
                                            name="confirmPassword"
                                            maxLength={255}
                                            defaultValue=""
                                            onChange={handleChange}
                                            isInvalid={touched.confirmPassword && errors.confirmPassword}
                                        />
                                        <Form.Control.Feedback type="invalid">
                                            {errors.confirmPassword}
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
                        </Formik> :
                        <>
                            <Form.Group className="forgot-form">
                                <Form.Label>
                                    Unable to reset password. The reset link is invalid or expired.
                                    Check your email or try requesting a new one.
                                </Form.Label>
                                <Button
                                    size="xxl"
                                    style={{marginTop: 10, marginBottom: 5}}
                                    block
                                    variant="transparent-black-shadow"
                                    onClick={() => history.push(forgotPasswordUrl)}
                                >
                                    RESET PASSWORD
                                </Button>
                            </Form.Group>
                        </>
                    }
                </div>
            </div>
        </>
    );
}

export default ResetPassword;
