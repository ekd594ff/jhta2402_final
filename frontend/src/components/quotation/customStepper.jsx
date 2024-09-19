import React from 'react';
import {Step, StepConnector, stepConnectorClasses, StepLabel, Stepper, styled} from "@mui/material";
import style from "../../styles/portfolio-registration.module.scss";
import {Check} from "@mui/icons-material";

function CustomStepper({activeStep, steps}) {
    const QontoConnector = styled(StepConnector)(({theme}) => ({
        [`&.${stepConnectorClasses.active}`]: {
            [`& .${stepConnectorClasses.line}`]: {
                borderColor: '#FA4D56',
            },
        },
        [`&.${stepConnectorClasses.completed}`]: {
            [`& .${stepConnectorClasses.line}`]: {
                borderColor: '#FA4D56',
            },
        },
    }));

    const QontoStepIconRoot = styled('div')(({theme}) => ({
        color: '#eaeaf0',
        display: 'flex',
        height: 22,
        alignItems: 'center',
        '& .QontoStepIcon-completedIcon': {
            color: '#FA4D56',
            zIndex: 1,
            fontSize: 24,
        },
        '& .QontoStepIcon-circle': {
            width: 16,
            height: 16,
            borderRadius: '50%',
            backgroundColor: 'currentColor',
        },
        ...theme.applyStyles('dark', {
            color: theme.palette.grey[700],
        }),
        variants: [
            {
                props: ({ownerState}) => ownerState.active,
                style: {
                    color: '#FA4D56',
                },
            },
        ],
    }));

    const QontoStepIcon = (props) => {
        const {active, completed, className} = props;

        return (
            <QontoStepIconRoot ownerState={{active}} className={className}>
                {completed ? (
                    <Check className="QontoStepIcon-completedIcon"/>
                ) : (
                    <div className="QontoStepIcon-circle"/>
                )}
            </QontoStepIconRoot>
        );
    }

    return (
        <Stepper activeStep={activeStep} alternativeLabel
                 connector={<QontoConnector/>}>
            {steps.map((label) => (
                <Step className={style["step"]} key={label}>
                    <StepLabel StepIconComponent={QontoStepIcon}>{label}</StepLabel>
                </Step>
            ))}
        </Stepper>
    );
}

export default CustomStepper;